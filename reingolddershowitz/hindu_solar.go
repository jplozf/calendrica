package reingolddershowitz

import (
	"fmt"
	"math"
)

const (
	HinduSiderealYear    = 365 + 279457.0/1080000
	HinduAnomalisticYear = 1577917828000.0 / (4320000000.0 - 387)
	HinduSolarEra        = 3179
	hinduCreation        = float64(OldHinduSolarEpoch) - 1955880000*HinduSiderealYear
)

var Ujjain = NewLocation("Ujjain, India", Angle(23, 9, 0), Angle(75, 46, 0), Mt(0), 5+8.0/75)

// HinduSolar represents a date in the modern Hindu solar calendar (Saka era).
type HinduSolar struct {
	StandardDate
}

func NewHinduSolar(year int64, month, day int) HinduSolar {
	return HinduSolar{StandardDate: NewStandardDate(year, month, day)}
}

func NewHinduSolarFromFixed(rd int64) HinduSolar { return HinduSolarFromFixed(rd) }

func HinduSolarToFixed(year int64, month, day int) int64 {
	return NewHinduSolar(year, month, day).ToFixed()
}

func (d HinduSolar) ToFixed() int64 {
	approx := int64(math.Floor((float64(d.Year+HinduSolarEra)+float64(d.Month-1)/12)*HinduSiderealYear)) +
		OldHinduSolarEpoch + int64(d.Day-1)
	rate := 360 / HinduSiderealYear
	phi := float64(d.Month-1)*30 + float64(d.Day-1)*rate
	capDelta := hinduMod(HinduSolarLongitude(float64(approx)+0.25)-phi+180, 360) - 180
	tau := approx - int64(math.Ceil(capDelta/rate))
	date := tau - 1
	for !HinduSolarOnOrBefore(d, HinduSolarFromFixed(date)) {
		date++
	}
	return date
}

func HinduSolarFromFixed(rd int64) HinduSolar {
	critical := HinduSunrise(rd + 1)
	month := HinduZodiac(critical)
	year := HinduCalendarYear(critical) - HinduSolarEra
	approx := rd - 3 - int64(hinduMod(math.Floor(HinduSolarLongitude(critical)), 30))
	begin := approx
	for HinduZodiac(HinduSunrise(begin+1)) != month {
		begin++
	}
	return NewHinduSolar(year, month, int(rd-begin+1))
}

func (d *HinduSolar) FromFixed(rd int64) { *d = HinduSolarFromFixed(rd) }

func HinduSineTable(entry int) float64 {
	exact := 3438 * math.Sin(float64(entry)*225/60*math.Pi/180)
	errorTerm := 0.215 * signum(exact) * signum(math.Abs(exact)-1716)
	return math.Floor(exact+errorTerm+0.5) / 3438
}

func HinduSine(theta float64) float64 {
	entry := theta * 60 / 225
	fraction := hinduMod(entry, 1)
	return fraction*HinduSineTable(int(math.Ceil(entry))) +
		(1-fraction)*HinduSineTable(int(math.Floor(entry)))
}

func HinduArcsin(amplitude float64) float64 {
	negative := amplitude < 0
	amplitude = math.Abs(amplitude)
	position := 0
	for amplitude > HinduSineTable(position) {
		position++
	}
	below := HinduSineTable(position - 1)
	result := 225.0 / 60 * (float64(position-1) + (amplitude-below)/(HinduSineTable(position)-below))
	if negative {
		return -result
	}
	return result
}

func HinduMeanPosition(moment, period float64) float64 {
	return 360 * hinduMod((moment-hinduCreation)/period, 1)
}

func HinduTruePosition(moment, period, size, anomalistic, change float64) float64 {
	mean := HinduMeanPosition(moment, period)
	offset := HinduSine(HinduMeanPosition(moment, anomalistic))
	contraction := math.Abs(offset) * change * size
	equation := HinduArcsin(offset * (size - contraction))
	return hinduMod(mean-equation, 360)
}

func HinduSolarLongitude(moment float64) float64 {
	return HinduTruePosition(moment, HinduSiderealYear, 14.0/360, HinduAnomalisticYear, 1.0/42)
}

func HinduZodiac(moment float64) int { return 1 + int(math.Floor(HinduSolarLongitude(moment)/30)) }

func HinduSolarOnOrBefore(a, b HinduSolar) bool {
	return a.Year < b.Year || a.Year == b.Year && (a.Month < b.Month || a.Month == b.Month && a.Day <= b.Day)
}

func HinduCalendarYear(moment float64) int64 {
	return int64(math.Floor((moment-float64(OldHinduSolarEpoch))/HinduSiderealYear -
		HinduSolarLongitude(moment)/360 + 0.5))
}

func HinduEquationOfTime(date int64) float64 {
	offset := HinduSine(HinduMeanPosition(float64(date), HinduAnomalisticYear))
	equationSun := offset * 3438.0 / 60 * (math.Abs(offset)/1080 - 14.0/360)
	return HinduDailyMotion(date) / 360 * equationSun / 360 * HinduSiderealYear
}

func HinduAscensionalDifference(date int64, locale Location) float64 {
	sinDeclination := 1397.0 / 3438 * HinduSine(HinduTropicalLongitude(date))
	diurnalRadius := HinduSine(90 + HinduArcsin(sinDeclination))
	tanLatitude := HinduSine(locale.Latitude) / HinduSine(90+locale.Latitude)
	earthSine := sinDeclination * tanLatitude
	return HinduArcsin(-earthSine / diurnalRadius)
}

func HinduTropicalLongitude(date int64) float64 {
	days := math.Floor(float64(date - OldHinduSolarEpoch))
	precession := 27 - math.Abs(54-hinduMod(27+108*(600.0/1577917828)*days, 108))
	return hinduMod(HinduSolarLongitude(float64(date))-precession, 360)
}

func HinduRisingSign(date int64) float64 {
	risingSigns := [...]float64{1670.0 / 1800, 1795.0 / 1800, 1935.0 / 1800, 1935.0 / 1800, 1795.0 / 1800, 1670.0 / 1800}
	index := int(hinduMod(math.Floor(HinduTropicalLongitude(date)/30), 6))
	return risingSigns[index]
}

func HinduDailyMotion(date int64) float64 {
	meanMotion := 360 / HinduSiderealYear
	anomaly := HinduMeanPosition(float64(date), HinduAnomalisticYear)
	epicycle := 14.0/360 - math.Abs(HinduSine(anomaly))/1080
	entry := int(math.Floor(anomaly / (225.0 / 60)))
	sineTableStep := HinduSineTable(entry+1) - HinduSineTable(entry)
	factor := sineTableStep * -15 * epicycle
	return meanMotion * (factor + 1)
}

func HinduSolarSiderealDifference(date int64) float64 {
	return HinduDailyMotion(date) * HinduRisingSign(date)
}

func HinduSunrise(date int64) float64 {
	return float64(date) + 0.25 + (Ujjain.Longitude-HinduLocale.Longitude)/360 +
		HinduEquationOfTime(date) + (1577917828.0/1582237828/360)*
		(HinduAscensionalDifference(date, HinduLocale)+0.25*HinduSolarSiderealDifference(date))
}

var HinduLocale = Ujjain

func HinduSolarLongitudeAfter(moment, longitude float64) float64 {
	const epsilon = 1.0 / 1_000_000
	tau := moment + HinduSiderealYear/360*hinduMod(longitude-HinduSolarLongitude(moment), 360)
	lo := math.Max(moment, tau-5)
	hi := tau + 5
	x := (lo + hi) / 2
	for hi-lo >= epsilon {
		if hinduMod(HinduSolarLongitude(x)-longitude, 360) < 180 {
			hi = x
		} else {
			lo = x
		}
		x = (lo + hi) / 2
	}
	return x
}

func HinduMeshaSamkranti(gregorianYear int64) float64 {
	return HinduSolarLongitudeAfter(float64(GregorianToFixed(gregorianYear, January, 1)), 0)
}

func (d HinduSolar) Format() string {
	month := ""
	if d.Month >= 1 && d.Month <= len(OldHinduLunarMonthNames) {
		month = OldHinduLunarMonthNames[d.Month-1]
	}
	return fmt.Sprintf("%s, %d %s %d S.E.", OldHinduSolarDayOfWeekNames[DayOfWeekFromFixed(d.ToFixed())], d.Day, month, d.Year)
}

func (d HinduSolar) Equals(other HinduSolar) bool { return d.StandardDate.Equals(other.StandardDate) }

func hinduMod(value, modulus float64) float64 { return value - modulus*math.Floor(value/modulus) }

func signum(value float64) float64 {
	if value < 0 {
		return -1
	}
	if value > 0 {
		return 1
	}
	return 0
}

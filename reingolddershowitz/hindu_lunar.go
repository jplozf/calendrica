package reingolddershowitz

import (
	"fmt"
	"math"
)

const (
	HinduLunarEra         = 3044
	HinduSynodicMonth     = 29 + 7087771.0/13358334
	HinduSiderealMonth    = 27 + 4644439.0/14438334
	HinduAnomalisticMonth = 1577917828.0 / (57753336 - 488199)
)

// HinduLunar is a modern Hindu lunisolar calendar date.
type HinduLunar struct {
	Year      int64
	Month     int
	LeapMonth bool
	Day       int
	LeapDay   bool
}

func NewHinduLunar(year int64, month int, leapMonth bool, day int, leapDay bool) HinduLunar {
	return HinduLunar{Year: year, Month: month, LeapMonth: leapMonth, Day: day, LeapDay: leapDay}
}

func NewHinduLunarFromFixed(rd int64) HinduLunar { return HinduLunarFromFixed(rd) }

func HinduLunarToFixed(year int64, month int, leapMonth bool, day int, leapDay bool) int64 {
	return NewHinduLunar(year, month, leapMonth, day, leapDay).ToFixed()
}

func (d HinduLunar) ToFixed() int64 {
	approx := float64(OldHinduSolarEpoch) + HinduSiderealYear*
		(float64(d.Year+HinduLunarEra)+float64(d.Month-1)/12)
	s := int64(math.Floor(approx - (1.0/360)*
		(hinduMod(HinduSolarLongitude(approx)-float64(d.Month-1)*30+180, 360)-180)))
	k := HinduLunarDay(float64(s) + 0.25)
	var x int
	if 3 < k && k < 27 {
		x = k
	} else {
		mid := HinduLunarFromFixed(s - 15)
		if mid.Month < d.Month || mid.LeapMonth && !d.LeapMonth {
			x = hinduIntMod(k+15, 30) - 15
		} else {
			x = hinduIntMod(k-15, 30) + 15
		}
	}
	est := s + int64(d.Day-x)
	tau := est - int64(hinduIntMod(HinduLunarDay(float64(est)+0.25)-d.Day+15, 30)) + 15
	date := tau - 1
	for !HinduLunarOnOrBefore(d, HinduLunarFromFixed(date)) {
		date++
	}
	return date
}

func HinduLunarFromFixed(rd int64) HinduLunar {
	critical := HinduSunrise(rd)
	day := HinduLunarDay(critical)
	leapDay := day == HinduLunarDay(HinduSunrise(rd-1))
	lastNewMoon := HinduLunarNewMoonBefore(critical)
	nextNewMoon := HinduLunarNewMoonBefore(math.Floor(lastNewMoon) + 35)
	solarMonth := HinduZodiac(lastNewMoon)
	leapMonth := solarMonth == HinduZodiac(nextNewMoon)
	month := int(AdjustedMod(int64(solarMonth+1), 12))
	year := HinduCalendarYear(nextNewMoon) - HinduLunarEra
	if leapMonth && month == 1 {
		year++
	}
	return NewHinduLunar(year, month, leapMonth, day, leapDay)
}

func (d *HinduLunar) FromFixed(rd int64) { *d = HinduLunarFromFixed(rd) }

func HinduLunarNewMoonBefore(moment float64) float64 {
	const epsilon = 1 / 17179869184.0
	tau := moment - (1.0/360)*HinduLunarPhase(moment)*HinduSynodicMonth
	lo := tau - 1
	hi := math.Min(moment, tau+1)
	x := (lo + hi) / 2
	for HinduZodiac(lo) != HinduZodiac(hi) && hi-lo >= epsilon {
		if HinduLunarPhase(x) < 180 {
			hi = x
		} else {
			lo = x
		}
		x = (lo + hi) / 2
	}
	return x
}

func HinduLunarOnOrBefore(a, b HinduLunar) bool {
	return a.Year < b.Year || a.Year == b.Year && (a.Month < b.Month ||
		a.Month == b.Month && (a.LeapMonth && !b.LeapMonth ||
			a.LeapMonth == b.LeapMonth && (a.Day < b.Day || a.Day == b.Day && (!a.LeapDay || b.LeapDay))))
}

func HinduLunarDayAfter(moment, day float64) float64 {
	const epsilon = 1 / 131072.0
	phase := (day - 1) * 12
	tau := moment + (1.0/360)*hinduMod(phase-HinduLunarPhase(moment), 360)*HinduSynodicMonth
	lo := math.Max(moment, tau-2)
	hi := tau + 2
	x := (lo + hi) / 2
	for hi-lo >= epsilon {
		if hinduMod(HinduLunarPhase(x)-phase, 360) < 180 {
			hi = x
		} else {
			lo = x
		}
		x = (lo + hi) / 2
	}
	return x
}

func HinduLunarLongitude(moment float64) float64 {
	return HinduTruePosition(moment, HinduSiderealMonth, 32.0/360, HinduAnomalisticMonth, 1.0/96)
}

func HinduLunarPhase(moment float64) float64 {
	return hinduMod(HinduLunarLongitude(moment)-HinduSolarLongitude(moment), 360)
}

func HinduLunarDay(moment float64) int { return int(math.Floor(HinduLunarPhase(moment)/12)) + 1 }

func HinduLunarStation(date int64) int {
	return int(math.Floor(HinduLunarLongitude(HinduSunrise(date))/(800.0/60))) + 1
}

func HinduLunarNewYear(gregorianYear int64) int64 {
	jan1 := GregorianToFixed(gregorianYear, January, 1)
	mina := HinduSolarLongitudeAfter(float64(jan1), 330)
	newMoon := HinduLunarDayAfter(mina, 1)
	day := int64(math.Floor(newMoon))
	critical := HinduSunrise(day)
	if newMoon < critical || HinduLunarDay(HinduSunrise(day+1)) == 2 {
		return day
	}
	return day + 1
}

func HinduKarana(n int) int {
	if n == 1 {
		return 0
	}
	if n > 57 {
		return n - 50
	}
	return int(AdjustedMod(int64(n-1), 7))
}

func HinduYoga(date int64) int {
	longitude := (HinduSolarLongitude(float64(date)) + HinduLunarLongitude(float64(date))) * 60 / 800
	return int(math.Floor(hinduMod(longitude, 27))) + 1
}

func HinduSacredWednesdays(start, end int64) []int64 {
	wednesday := KDayOnOrAfter(Wednesday, start)
	var dates []int64
	for ; wednesday <= end; wednesday += 7 {
		if HinduLunarFromFixed(wednesday).Day == 8 {
			dates = append(dates, wednesday)
		}
	}
	return dates
}

func HinduSacredWednesdaysInGregorian(gregorianYear int64) []int64 {
	return HinduSacredWednesdays(GregorianToFixed(gregorianYear, January, 1), GregorianToFixed(gregorianYear, December, 31))
}

func (d HinduLunar) Format() string {
	month := ""
	if d.Month >= 1 && d.Month <= len(OldHinduLunarMonthNames) {
		month = OldHinduLunarMonthNames[d.Month-1]
	}
	if d.LeapMonth {
		month += " II"
	}
	dayName := OldHinduLunarDayOfWeekNames[DayOfWeekFromFixed(d.ToFixed())]
	leapDay := ""
	if d.LeapDay {
		leapDay = " II"
	}
	return fmt.Sprintf("%s, %d%s %s %d V.E.", dayName, d.Day, leapDay, month, d.Year)
}

func (d HinduLunar) Equals(other HinduLunar) bool {
	return d.Year == other.Year && d.Month == other.Month && d.LeapMonth == other.LeapMonth &&
		d.Day == other.Day && d.LeapDay == other.LeapDay
}

func hinduIntMod(value, modulus int) int {
	return int(Mod(int64(value), int64(modulus)))
}

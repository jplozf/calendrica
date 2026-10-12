package reingolddershowitz

import "math"

const futureBahaiMeanTropicalYear = 365.242189

var HAIFA = NewLocation("Haifa, Israel", 32.82, 35, Mt(0), 2)

// FutureBahai is the astronomical Bahá'í calendar used from 2015 onward.
type FutureBahai struct {
	Bahai
}

func NewFutureBahai(major int64, cycle, year, month, day int) FutureBahai {
	return FutureBahai{Bahai: NewBahai(major, cycle, year, month, day)}
}

func NewFutureBahaiFromFixed(rd int64) FutureBahai {
	return FutureBahaiFromFixedDate(rd)
}

func FutureBahaiToFixed(major int64, cycle, year, month, day int) int64 {
	years := 361*(major-1) + 19*int64(cycle-1) + int64(year)
	if month == 19 {
		approx := BahaiEpoch + int64(math.Floor(futureBahaiMeanTropicalYear*(float64(years)+0.5)))
		return FutureBahaiNewYearOnOrBefore(approx) - 19 + int64(day-1)
	}
	approx := BahaiEpoch + int64(math.Floor(futureBahaiMeanTropicalYear*(float64(years)-0.5)))
	if month == BahaiAyyamIHa {
		return FutureBahaiNewYearOnOrBefore(approx) + 342 + int64(day-1)
	}
	return FutureBahaiNewYearOnOrBefore(approx) + int64((month-1)*19+day-1)
}

func (f FutureBahai) ToFixed() int64 {
	return FutureBahaiToFixed(f.Major, f.Cycle, int(f.Year), f.Month, f.Day)
}

func FutureBahaiFromFixedDate(rd int64) FutureBahai {
	newYear := FutureBahaiNewYearOnOrBefore(rd)
	years := int64(math.Round(float64(newYear-BahaiEpoch) / futureBahaiMeanTropicalYear))
	major := Quotient(years, 361) + 1
	cycle := int(Quotient(Mod(years, 361), 19)) + 1
	year := int(Mod(years, 19)) + 1

	month := int(Quotient(rd-newYear, 19)) + 1
	if rd >= FutureBahaiToFixed(major, cycle, year, 19, 1) {
		month = 19
	} else if rd >= FutureBahaiToFixed(major, cycle, year, BahaiAyyamIHa, 1) {
		month = BahaiAyyamIHa
	}
	day := int(rd + 1 - FutureBahaiToFixed(major, cycle, year, month, 1))
	return FutureBahai{Bahai: NewBahai(major, cycle, year, month, day)}
}

func (f *FutureBahai) FromFixed(rd int64) { *f = FutureBahaiFromFixedDate(rd) }

func (f FutureBahai) Format() string { return f.Bahai.format(f.ToFixed()) }

func (f FutureBahai) Equals(other FutureBahai) bool { return f.Bahai.Equals(other.Bahai) }

func FutureBahaiSunsetInHaifa(rd int64) float64 {
	return Sunset(rd-1, HAIFA) - HAIFA.Zone/24
}

func FutureBahaiNewYearOnOrBefore(rd int64) int64 {
	moment := FutureBahaiSunsetInHaifa(rd)
	approx := SolarLongitudeAfter(moment-futureBahaiMeanTropicalYear, 0)
	candidate := int64(math.Floor(approx)) - 1
	for SolarLongitude(FutureBahaiSunsetInHaifa(candidate)) > 2 {
		candidate++
	}
	return candidate
}

func FutureBahaiFeastOfRidvan(gYear int64) int64 {
	years := gYear - GregorianYearFromFixed(BahaiEpoch)
	major := 1 + Quotient(years, 361)
	cycle := 1 + int(Quotient(Mod(years, 361), 19))
	year := 1 + int(Mod(years, 19))
	return FutureBahaiToFixed(major, cycle, year, 2, 13)
}

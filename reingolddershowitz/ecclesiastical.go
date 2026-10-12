package reingolddershowitz

import "math"

// OrthodoxEaster returns the fixed date of Orthodox Easter in a Gregorian year.
func OrthodoxEaster(gregorianYear int64) int64 {
	shiftedEpact := Mod(14+11*Mod(gregorianYear, 19), 30)
	julianYear := gregorianYear
	if julianYear <= 0 {
		julianYear--
	}
	paschalMoon := JulianToFixed(julianYear, April, 19) - shiftedEpact
	return kDayAfter(paschalMoon, Sunday)
}

func AltOrthodoxEaster(gregorianYear int64) int64 {
	paschalMoon := 354*gregorianYear +
		30*Quotient(7*gregorianYear-8, 19) +
		Quotient(gregorianYear, 4) -
		Quotient(gregorianYear, 19) - 272
	return kDayAfter(paschalMoon, Sunday)
}

// Easter returns the fixed date of Western Easter in the Gregorian year.
func Easter(gregorianYear int64) int64 {
	century := 1 + Quotient(gregorianYear, 100)
	shiftedEpact := Mod(14+11*Mod(gregorianYear, 19)-Quotient(3*century, 4)+Quotient(5+8*century, 25), 30)
	adjustedEpact := shiftedEpact
	if shiftedEpact == 0 || (shiftedEpact == 1 && 10 < Mod(gregorianYear, 19)) {
		adjustedEpact++
	}
	paschalMoon := GregorianToFixed(gregorianYear, April, 19) - adjustedEpact
	return kDayAfter(paschalMoon, Sunday)
}

func Pentecost(gregorianYear int64) int64 {
	return Easter(gregorianYear) + 49
}

// AstronomicalEaster returns the proposed astronomical Easter date in a Gregorian year.
func AstronomicalEaster(gregorianYear int64) int64 {
	januaryFirst := GregorianToFixed(gregorianYear, January, 1)
	equinox := SolarLongitudeAfter(float64(januaryFirst), Spring)
	fullMoon := LunarPhaseAfter(equinox, Full)
	paschalMoon := int64(math.Floor(ApparentFromLocal(LocalFromUniversal(fullMoon, JERUSALEM))))
	return kDayAfter(paschalMoon, Sunday)
}

func kDayAfter(rd int64, weekday int) int64 {
	return KDayOnOrAfter(weekday, rd+1)
}

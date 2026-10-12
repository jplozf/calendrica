package reingolddershowitz

import (
	"fmt"
	"math"
)

const (
	PersianEpoch            int64 = 226896
	PersianMeanTropicalYear       = 365.242189
	FrenchEpoch             int64 = 654415
)

var (
	Tehran = NewLocation("Tehran, Iran", 35.68, 51.42, Mt(1100), 3.5)
	Paris  = NewLocation("Paris, France", 48.51, Angle(2, 20, 15), Mt(27), 1)
)

var persianMonthNames = [...]string{
	"Farvardin", "Ordibehesht", "Xordad", "Tir", "Mordad", "Shahrivar",
	"Mehr", "Aban", "Azar", "Dey", "Bahman", "Esfand",
}

var frenchMonthNames = [...]string{
	"Vendemiaire", "Brumaire", "Frimaire", "Nivose", "Pluviose", "Ventose",
	"Germinal", "Floreal", "Prairial", "Messidor", "Thermidor", "Fructidor",
	"Sansculottides",
}

type Persian struct {
	StandardDate
}

func NewPersian(year int64, month, day int) Persian {
	return Persian{StandardDate: NewStandardDate(year, month, day)}
}

func PersianToFixed(year int64, month, day int) int64 {
	yearOffset := year - 1
	if year <= 0 {
		yearOffset = year
	}
	approx := PersianEpoch + 180 + int64(math.Floor(PersianMeanTropicalYear*float64(yearOffset)))
	newYear := PersianNewYearOnOrBefore(approx)
	monthDays := int64(31 * (month - 1))
	if month > 7 {
		monthDays = int64(30*(month-1) + 6)
	}
	return newYear - 1 + monthDays + int64(day)
}

func (p Persian) ToFixed() int64 {
	return PersianToFixed(p.Year, p.Month, p.Day)
}

func PersianFromFixed(rd int64) Persian {
	newYear := PersianNewYearOnOrBefore(rd)
	year := 1 + int64(math.Floor(float64(newYear-PersianEpoch)/PersianMeanTropicalYear+0.5))
	if year <= 0 {
		year--
	}
	dayOfYear := 1 + rd - PersianToFixed(year, 1, 1)
	month := 0
	if dayOfYear < 186 {
		month = int(math.Ceil(float64(dayOfYear) / 31))
	} else {
		month = int(math.Ceil(float64(dayOfYear-6) / 30))
	}
	day := int(rd - (PersianToFixed(year, month, 1) - 1))
	return NewPersian(year, month, day)
}

func PersianMiddayInTehran(rd int64) float64 {
	return UniversalFromStandard(Midday(rd, Tehran), Tehran)
}

func PersianNewYearOnOrBefore(rd int64) int64 {
	approx := EstimatePriorSolarLongitude(PersianMiddayInTehran(rd), 0)
	for date := int64(math.Floor(approx)) - 1; ; date++ {
		if SolarLongitude(PersianMiddayInTehran(date)) <= 2 {
			return date
		}
	}
}

func PersianNawRuz(gregorianYear int64) int64 {
	persianYear := 1 + gregorianYear - GregorianYearFromFixed(PersianEpoch)
	if persianYear <= 0 {
		persianYear--
	}
	return PersianToFixed(persianYear, 1, 1)
}

func (p Persian) Format() string {
	weekday := []string{"Yek-shanbeh", "Do-shanbeh", "Se-shanbeh", "Char-shanbeh", "Panj-shanbeh", "Jom`eh", "Shanbeh"}
	month := ""
	if p.Month >= 1 && p.Month <= len(persianMonthNames) {
		month = persianMonthNames[p.Month-1]
	}
	return fmt.Sprintf("%s, %d %s %d A.P.", weekday[DayOfWeekFromFixed(p.ToFixed())], p.Day, month, p.Year)
}

func (p Persian) Equals(other Persian) bool {
	return p.StandardDate.Equals(other.StandardDate)
}

type French struct {
	StandardDate
}

func NewFrench(year int64, month, day int) French {
	return French{StandardDate: NewStandardDate(year, month, day)}
}

func FrenchToFixed(year int64, month, day int) int64 {
	approx := int64(math.Floor(float64(FrenchEpoch) + 180 + PersianMeanTropicalYear*float64(year-1)))
	newYear := FrenchNewYearOnOrBefore(approx)
	return newYear - 1 + int64(30*(month-1)+day)
}

func (f French) ToFixed() int64 {
	return FrenchToFixed(f.Year, f.Month, f.Day)
}

func FrenchFromFixed(rd int64) French {
	newYear := FrenchNewYearOnOrBefore(rd)
	year := int64(math.Floor(float64(newYear-FrenchEpoch)/PersianMeanTropicalYear+0.5)) + 1
	days := rd - newYear
	return NewFrench(year, 1+int(Quotient(days, 30)), 1+int(Mod(days, 30)))
}

func FrenchMidnightInParis(rd int64) float64 {
	return UniversalFromStandard(Midnight(rd+1, Paris), Paris)
}

func FrenchNewYearOnOrBefore(rd int64) int64 {
	approx := EstimatePriorSolarLongitude(FrenchMidnightInParis(rd), 180)
	for date := int64(math.Floor(approx)) - 1; ; date++ {
		if SolarLongitude(FrenchMidnightInParis(date)) >= 180 {
			return date
		}
	}
}

func (f French) Format() string {
	if f.Month == 13 && f.Day >= 1 && f.Day <= 6 {
		specialDays := [...]string{"Jour de la Vertu", "Jour du Genie", "Jour du Labour", "Jour de la Raison", "Jour de la Recompense", "Jour de la Revolution"}
		return fmt.Sprintf("%s de l'Annee %d de la Revolution", specialDays[f.Day-1], f.Year)
	}
	month := ""
	if f.Month >= 1 && f.Month <= len(frenchMonthNames) {
		month = frenchMonthNames[f.Month-1]
	}
	decadeNames := [...]string{"I", "II", "III"}
	weekdayNames := [...]string{"Primidi", "Duodi", "Tridi", "Quartidi", "Quintidi", "Sextidi", "Septidi", "Octidi", "Nonidi", "Decadi"}
	decade := (f.Day - 1) / 10
	weekday := (f.Day - 1) % 10
	if decade < 0 || decade >= len(decadeNames) || weekday < 0 || weekday >= len(weekdayNames) {
		return fmt.Sprintf("%d %s %d", f.Day, month, f.Year)
	}
	return fmt.Sprintf("Decade %s, %s de %s de l'Annee %d de la Revolution", decadeNames[decade], weekdayNames[weekday], month, f.Year)
}

func (f French) Equals(other French) bool {
	return f.StandardDate.Equals(other.StandardDate)
}

type ModifiedFrench struct {
	StandardDate
}

func NewModifiedFrench(year int64, month, day int) ModifiedFrench {
	return ModifiedFrench{StandardDate: NewStandardDate(year, month, day)}
}

func ModifiedFrenchToFixed(year int64, month, day int) int64 {
	return FrenchEpoch - 1 +
		365*(year-1) +
		Quotient(year-1, 4) -
		Quotient(year-1, 100) +
		Quotient(year-1, 400) -
		Quotient(year-1, 4000) +
		int64(30*(month-1)+day)
}

func (f ModifiedFrench) ToFixed() int64 {
	return ModifiedFrenchToFixed(f.Year, f.Month, f.Day)
}

func ModifiedFrenchFromFixed(rd int64) ModifiedFrench {
	approx := 1 + int64(math.Floor(float64(rd-FrenchEpoch+2)/(1460969.0/4000)))
	year := approx
	if rd < ModifiedFrenchToFixed(approx, 1, 1) {
		year--
	}
	days := rd - ModifiedFrenchToFixed(year, 1, 1)
	month := 1 + int(Quotient(days, 30))
	day := int(rd - ModifiedFrenchToFixed(year, month, 1) + 1)
	return NewModifiedFrench(year, month, day)
}

func IsModifiedFrenchLeapYear(year int64) bool {
	return Mod(year, 4) == 0 &&
		Mod(year, 400) != 100 &&
		Mod(year, 400) != 200 &&
		Mod(year, 400) != 300 &&
		Mod(year, 4000) != 0
}

func (f ModifiedFrench) Format() string {
	return NewFrench(f.Year, f.Month, f.Day).Format()
}

func (f ModifiedFrench) Equals(other ModifiedFrench) bool {
	return f.StandardDate.Equals(other.StandardDate)
}

package reingolddershowitz

import (
	"fmt"
	"math"
)

const (
	HebrewTishri = 7
	HebrewNisan  = 1
	HebrewEpoch  = int64(-1373427)
)

var (
	HebrewDayOfWeekNames = []string{
		"yom rishon", "yom sheni", "yom shelishi", "yom revi`i",
		"yom hamishi", "yom shishi", "yom shabbat",
	}
	HebrewMonthNames = []string{
		"Nisan", "Iyyar", "Sivan", "Tammuz", "Av", "Elul", "Tishri",
		"Marheshvan", "Kislev", "Tevet", "Shevat", "Adar",
	}
	HebrewLeapMonthNames = []string{
		"Nisan", "Iyyar", "Sivan", "Tammuz", "Av", "Elul", "Tishri",
		"Marheshvan", "Kislev", "Tevet", "Shevat", "Adar I", "Adar II",
	}
)

// Hebrew is a date in the Hebrew calendar.
type Hebrew struct {
	StandardDate
}

func NewHebrew(year int64, month, day int) Hebrew {
	return Hebrew{StandardDate: NewStandardDate(year, month, day)}
}

func NewHebrewFromFixed(rd int64) Hebrew { return HebrewFromFixed(rd) }

func HebrewIsLeapYear(year int64) bool { return Mod(1+7*year, 19) < 7 }

func HebrewLastMonthOfYear(year int64) int {
	if HebrewIsLeapYear(year) {
		return 13
	}
	return 12
}

func HebrewCalendarElapsedDays(year int64) int64 {
	months := Quotient(235*year-234, 19)
	parts := 12084 + 13753*months
	day := 29*months + Quotient(parts, 25920)
	if Mod(3*(day+1), 7) < 3 {
		return day + 1
	}
	return day
}

func HebrewNewYearDelay(year int64) int {
	previous, current, next := HebrewCalendarElapsedDays(year-1),
		HebrewCalendarElapsedDays(year), HebrewCalendarElapsedDays(year+1)
	if next-current == 356 {
		return 2
	}
	if current-previous == 382 {
		return 1
	}
	return 0
}

func HebrewNewYear(year int64) int64 {
	return HebrewEpoch + HebrewCalendarElapsedDays(year) + int64(HebrewNewYearDelay(year))
}

func HebrewJewishDuskChecked(date int64, locale Location) (float64, error) {
	return DuskChecked(date, locale, Angle(4, 40, 0))
}

func HebrewJewishDusk(date int64, locale Location) float64 {
	result, err := HebrewJewishDuskChecked(date, locale)
	if err != nil {
		panic(err)
	}
	return result
}

func HebrewJewishSabbathEndsChecked(date int64, locale Location) (float64, error) {
	return DuskChecked(date, locale, Angle(7, 5, 0))
}

func HebrewJewishSabbathEnds(date int64, locale Location) float64 {
	result, err := HebrewJewishSabbathEndsChecked(date, locale)
	if err != nil {
		panic(err)
	}
	return result
}

func HebrewJewishMorningEndChecked(date int64, locale Location) (float64, error) {
	return StandardFromSundialChecked(date, 10, locale)
}

func HebrewJewishMorningEnd(date int64, locale Location) float64 {
	result, err := HebrewJewishMorningEndChecked(date, locale)
	if err != nil {
		panic(err)
	}
	return result
}

func HebrewDaysInYear(year int64) int {
	return int(HebrewNewYear(year+1) - HebrewNewYear(year))
}

func HebrewHasLongMarheshvan(year int64) bool {
	days := HebrewDaysInYear(year)
	return days == 355 || days == 385
}

func HebrewHasShortKislev(year int64) bool {
	days := HebrewDaysInYear(year)
	return days == 353 || days == 383
}

func HebrewLastDayOfMonth(month int, year int64) int {
	if month == 2 || month == 4 || month == 6 || month == 10 || month == 13 ||
		(month == 12 && !HebrewIsLeapYear(year)) {
		return 29
	}
	if month == 8 && !HebrewHasLongMarheshvan(year) ||
		month == 9 && HebrewHasShortKislev(year) {
		return 29
	}
	return 30
}

func HebrewToFixed(year int64, month, day int) int64 {
	rd := HebrewNewYear(year) + int64(day-1)
	if month < HebrewTishri {
		for m := HebrewTishri; m <= HebrewLastMonthOfYear(year); m++ {
			rd += int64(HebrewLastDayOfMonth(m, year))
		}
		for m := HebrewNisan; m < month; m++ {
			rd += int64(HebrewLastDayOfMonth(m, year))
		}
	} else {
		for m := HebrewTishri; m < month; m++ {
			rd += int64(HebrewLastDayOfMonth(m, year))
		}
	}
	return rd
}

func (h Hebrew) ToFixed() int64 { return HebrewToFixed(h.Year, h.Month, h.Day) }

func HebrewFromFixed(rd int64) Hebrew {
	year := 1 + int64(math.Floor(float64(rd-HebrewEpoch)/(35975351.0/98496.0)))
	for HebrewNewYear(year) > rd {
		year--
	}
	for HebrewNewYear(year+1) <= rd {
		year++
	}

	month := HebrewTishri
	if rd >= HebrewToFixed(year, HebrewNisan, 1) {
		month = HebrewNisan
	}
	for rd > HebrewToFixed(year, month, HebrewLastDayOfMonth(month, year)) {
		month++
		if month > HebrewLastMonthOfYear(year) {
			month = HebrewNisan
		}
	}
	day := int(rd-HebrewToFixed(year, month, 1)) + 1
	return NewHebrew(year, month, day)
}

func (h *Hebrew) FromFixed(rd int64) {
	*h = HebrewFromFixed(rd)
}

func (h Hebrew) Format() string {
	names := HebrewMonthNames
	if HebrewIsLeapYear(h.Year) {
		names = HebrewLeapMonthNames
	}
	monthName := ""
	if h.Month >= 1 && h.Month <= len(names) {
		monthName = names[h.Month-1]
	}
	return fmt.Sprintf("%s, %d %s %d A.M.",
		HebrewDayOfWeekNames[DayOfWeekFromFixed(h.ToFixed())], h.Day, monthName, h.Year)
}

func (h Hebrew) Equals(other Hebrew) bool { return h.StandardDate.Equals(other.StandardDate) }

func HebrewYomKippur(gYear int64) int64 {
	year := 1 + gYear - GregorianYearFromFixed(HebrewEpoch)
	return HebrewToFixed(year, HebrewTishri, 10)
}

func HebrewPassover(gYear int64) int64 {
	year := gYear - GregorianYearFromFixed(HebrewEpoch)
	return HebrewToFixed(year, HebrewNisan, 15)
}

func HebrewClassicalPassoverEve(gYear int64) int64 {
	jan1 := GregorianToFixed(gYear, January, 1)
	equinox := SolarLongitudeAfter(float64(jan1), 0)
	newMoon := PhasisOnOrBefore(int64(math.Floor(equinox))+10, Jerusalem)
	set := Sunset(newMoon+14, Jerusalem) - Jerusalem.Zone/24
	monthStart := newMoon
	if equinox >= set {
		monthStart = PhasisOnOrBefore(newMoon+45, Jerusalem)
	}
	return monthStart + 13
}

func HebrewOmer(rd int64) (weeks, days int, ok bool) {
	count := rd - HebrewPassover(GregorianYearFromFixed(rd))
	if count < 1 || count > 49 {
		return 0, 0, false
	}
	return int(Quotient(count, 7)), int(Mod(count, 7)), true
}

func HebrewPurim(gYear int64) int64 {
	year := gYear - GregorianYearFromFixed(HebrewEpoch)
	return HebrewToFixed(year, HebrewLastMonthOfYear(year), 14)
}

func HebrewTaAnitEsther(gYear int64) int64 {
	purim := HebrewPurim(gYear)
	if DayOfWeekFromFixed(purim) == Sunday {
		return purim - 3
	}
	return purim - 1
}

func HebrewTishahBeAv(gYear int64) int64 {
	ninth := HebrewToFixed(gYear-GregorianYearFromFixed(HebrewEpoch), 5, 9)
	if DayOfWeekFromFixed(ninth) == Saturday {
		return ninth + 1
	}
	return ninth
}

func HebrewYomHaZikkaron(gYear int64) int64 {
	iyar4 := HebrewToFixed(gYear-GregorianYearFromFixed(HebrewEpoch), 2, 4)
	if DayOfWeekFromFixed(iyar4) > Wednesday {
		return KDayOnOrBefore(Wednesday, iyar4)
	}
	return iyar4
}

func HebrewBirthday(birthDate Hebrew, year int64) int64 {
	if birthDate.Month == HebrewLastMonthOfYear(birthDate.Year) {
		return HebrewToFixed(year, HebrewLastMonthOfYear(year), birthDate.Day)
	}
	return HebrewToFixed(year, birthDate.Month, birthDate.Day)
}

func HebrewBirthdayInGregorian(birthDate Hebrew, gYear int64) []int64 {
	return hebrewAnniversariesInGregorian(gYear, func(y int64) int64 { return HebrewBirthday(birthDate, y) })
}

func HebrewYahrzeit(deathDate Hebrew, year int64) int64 {
	switch {
	case deathDate.Month == 8 && deathDate.Day == 30 && !HebrewHasLongMarheshvan(deathDate.Year+1):
		return HebrewToFixed(year, 9, 1) - 1
	case deathDate.Month == 9 && deathDate.Day == 30 && HebrewHasShortKislev(deathDate.Year+1):
		return HebrewToFixed(year, 10, 1) - 1
	case deathDate.Month == 13:
		return HebrewToFixed(year, HebrewLastMonthOfYear(year), deathDate.Day)
	case deathDate.Month == 12 && deathDate.Day == 30 && !HebrewIsLeapYear(year):
		return HebrewToFixed(year, 11, 30)
	default:
		return HebrewToFixed(year, deathDate.Month, deathDate.Day)
	}
}

func HebrewYahrzeitInGregorian(deathDate Hebrew, gYear int64) []int64 {
	return hebrewAnniversariesInGregorian(gYear, func(y int64) int64 { return HebrewYahrzeit(deathDate, y) })
}

func hebrewAnniversariesInGregorian(gYear int64, anniversary func(int64) int64) []int64 {
	jan1 := GregorianToFixed(gYear, January, 1)
	dec31 := GregorianToFixed(gYear, December, 31)
	year := HebrewFromFixed(jan1).Year
	var result []int64
	for _, date := range []int64{anniversary(year), anniversary(year + 1)} {
		if jan1 <= date && date <= dec31 {
			result = append(result, date)
		}
	}
	return result
}

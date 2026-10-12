package reingolddershowitz

import "fmt"

const (
	BahaiEpoch    = int64(673222) // 21 March 1844 Gregorian
	BahaiAyyamIHa = 0
)

var (
	BahaiDayOfWeekNames = []string{
		"Jamal", "Kamal", "Fidal", "`Idal", "Istijlal", "Istiqlal", "Jalal",
	}
	BahaiDayOfMonthNames = []string{
		"Baha'", "Jalal", "Jamal", "`Azamat", "Nur", "Rahmat", "Kalimat",
		"Kamal", "Asma'", "`Izzat", "Mashiyyat", "`Ilm", "Qudrat", "Qawl",
		"Masa'il", "Sharaf", "Sultan", "Mulk", "`Ala'",
	}
	BahaiMonthNames = []string{
		"Ayyam-i-Ha", "Baha'", "Jalal", "Jamal", "`Azamat", "Nur", "Rahmat",
		"Kalimat", "Kamal", "Asma'", "`Izzat", "Mashiyyat", "`Ilm", "Qudrat",
		"Qawl", "Masa'il", "Sharaf", "Sultan", "Mulk", "`Ala'",
	}
	BahaiYearNames = []string{
		"Alif", "Ba'", "Ab", "Dal", "Bab", "Vav", "Abad", "Jad", "Baha'",
		"Hubb", "Bahhaj", "Javab", "Ahad", "Vahhab", "Vidad", "Badi'",
		"Bahi", "Abha", "Vahid",
	}
)

// Bahai is a date in the Western (fixed March 21 new year) Bahá'í calendar.
type Bahai struct {
	StandardDate
	Major int64
	Cycle int
}

func NewBahai(major int64, cycle, year, month, day int) Bahai {
	return Bahai{
		StandardDate: NewStandardDate(int64(year), month, day),
		Major:        major,
		Cycle:        cycle,
	}
}

func NewBahaiFromFixed(rd int64) Bahai { return BahaiFromFixed(rd) }

func BahaiToFixed(major int64, cycle, year, month, day int) int64 {
	gYear := 361*(major-1) + 19*int64(cycle-1) + int64(year-1) +
		GregorianYearFromFixed(BahaiEpoch)
	monthDays := int64(19 * (month - 1))
	switch month {
	case BahaiAyyamIHa:
		monthDays = 342
	case 19:
		if IsGregorianLeapYear(gYear + 1) {
			monthDays = 347
		} else {
			monthDays = 346
		}
	}
	return GregorianToFixed(gYear, March, 20) + monthDays + int64(day)
}

func (b Bahai) ToFixed() int64 {
	return BahaiToFixed(b.Major, b.Cycle, int(b.Year), b.Month, b.Day)
}

func BahaiFromFixed(rd int64) Bahai {
	gYear := GregorianYearFromFixed(rd)
	startYear := GregorianYearFromFixed(BahaiEpoch)
	years := gYear - startYear
	if rd <= GregorianToFixed(gYear, March, 20) {
		years--
	}
	major := 1 + Quotient(years, 361)
	cycle := 1 + int(Quotient(Mod(years, 361), 19))
	year := 1 + int(Mod(years, 19))

	days := rd - BahaiToFixed(major, cycle, year, 1, 1)
	month := int(Quotient(days, 19)) + 1
	if rd >= BahaiToFixed(major, cycle, year, 19, 1) {
		month = 19
	} else if rd >= BahaiToFixed(major, cycle, year, BahaiAyyamIHa, 1) {
		month = BahaiAyyamIHa
	}
	day := int(rd + 1 - BahaiToFixed(major, cycle, year, month, 1))
	return NewBahai(major, cycle, year, month, day)
}

func (b *Bahai) FromFixed(rd int64) { *b = BahaiFromFixed(rd) }

func (b *Bahai) FromArray(values []int64) {
	if len(values) < 5 {
		return
	}
	b.Major, b.Cycle = values[0], int(values[1])
	b.Year, b.Month, b.Day = values[2], int(values[3]), int(values[4])
}

func BahaiNewYear(gYear int64) int64 { return GregorianToFixed(gYear, March, 21) }

func (b Bahai) Format() string {
	return b.format(b.ToFixed())
}

func (b Bahai) format(rd int64) string {
	weekday := BahaiDayOfWeekNames[DayOfWeekFromFixed(rd)]
	dayName := ""
	if b.Day >= 1 && b.Day <= len(BahaiDayOfMonthNames) {
		dayName = BahaiDayOfMonthNames[b.Day-1]
	}
	monthName := ""
	if b.Month >= 0 && b.Month < len(BahaiMonthNames) {
		monthName = BahaiMonthNames[b.Month]
	}
	yearName := ""
	if b.Year >= 1 && b.Year <= int64(len(BahaiYearNames)) {
		yearName = BahaiYearNames[b.Year-1]
	}
	return fmt.Sprintf("%s, %s %s, %s of Vahid %d, Kull-i-Shay %d B.E.",
		weekday, dayName, monthName, yearName, b.Cycle, b.Major)
}

func (b Bahai) Equals(other Bahai) bool {
	return b.Major == other.Major && b.Cycle == other.Cycle &&
		b.StandardDate.Equals(other.StandardDate)
}

func BahaiFeastOfRidvan(gYear int64) int64 {
	years := gYear - GregorianYearFromFixed(BahaiEpoch)
	major := 1 + Quotient(years, 361)
	cycle := 1 + int(Quotient(Mod(years, 361), 19))
	year := 1 + int(Mod(years, 19))
	return BahaiToFixed(major, cycle, year, 2, 13)
}

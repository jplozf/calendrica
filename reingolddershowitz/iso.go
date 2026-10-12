package reingolddershowitz

import "fmt"

// ISODate represents a week date in the ISO 8601 calendar.
// ISO is already used by the package's CalendarType constant.
type ISODate struct {
	Year int64
	Week int
	Day  int
}

func NewISO(year int64, week, day int) ISODate {
	return ISODate{Year: year, Week: week, Day: day}
}

func ISOToFixed(year int64, week, day int) int64 {
	weekEnd := KDayOnOrBefore(Sunday, GregorianToFixed(year-1, December, 28)+7*int64(week))
	return weekEnd + int64(day)
}

func (i ISODate) ToFixed() int64 {
	return ISOToFixed(i.Year, i.Week, i.Day)
}

func ISOFromFixed(rd int64) ISODate {
	approx := GregorianYearFromFixed(rd - 3)
	year := approx
	if rd >= ISOToFixed(approx+1, 1, 1) {
		year++
	}
	week := int(Quotient(rd-ISOToFixed(year, 1, 1), 7)) + 1
	day := int(AdjustedMod(rd, 7))
	return NewISO(year, week, day)
}

func (i ISODate) Format() string {
	weekday := GregorianDayOfWeekNames[DayOfWeekFromFixed(i.ToFixed())]
	return fmt.Sprintf("%s, Week %d, %d", weekday, i.Week, i.Year)
}

func (i ISODate) Equals(other ISODate) bool {
	return i.Year == other.Year && i.Week == other.Week && i.Day == other.Day
}

package reingolddershowitz

import "fmt"

// IslamicEpoch is the fixed date of 1 Muharram, year 1.
const IslamicEpoch int64 = 227015

var IslamicDayOfWeekNames = []string{
	"yaum al-ahad", "yaum al-ithnayna", "yaum ath-thalatha'",
	"yaum al-arba`a'", "yaum al-hamis", "yaum al-jum`a", "yaum as-sabt",
}

var IslamicMonthNames = []string{
	"Muharram", "Safar", "Rabi I", "Rabi II", "Jumada I", "Jumada II",
	"Rajab", "Sha`ban", "Ramadan", "Shawwal", "Dhu al-Qa`da", "Dhu al-Hijja",
}

// Islamic represents a date in the arithmetic Islamic calendar.
type Islamic struct {
	StandardDate
}

func NewIslamic(year int64, month, day int) Islamic {
	return Islamic{StandardDate: StandardDate{Year: year, Month: month, Day: day}}
}

func IslamicToFixed(year int64, month, day int) int64 {
	return int64(day) + 29*int64(month-1) + Quotient(int64(6*month-1), 11) +
		(year-1)*354 + Quotient(3+11*year, 30) + IslamicEpoch - 1
}

func (i Islamic) ToFixed() int64 {
	return IslamicToFixed(i.Year, i.Month, i.Day)
}

func IslamicFromFixed(rd int64) Islamic {
	year := Quotient(30*(rd-IslamicEpoch)+10646, 10631)
	priorDays := rd - IslamicToFixed(year, 1, 1)
	month := int(Quotient(11*priorDays+330, 325))
	day := int(1 + rd - IslamicToFixed(year, month, 1))
	return NewIslamic(year, month, day)
}

func IsIslamicLeapYear(year int64) bool {
	return Mod(11*year+14, 30) < 11
}

func (i Islamic) Format() string {
	weekday := IslamicDayOfWeekNames[DayOfWeekFromFixed(i.ToFixed())]
	month := ""
	if i.Month >= 1 && i.Month <= len(IslamicMonthNames) {
		month = IslamicMonthNames[i.Month-1]
	}
	return fmt.Sprintf("%s, %d %s %d A.H.", weekday, i.Day, month, i.Year)
}

func (i Islamic) Equals(other Islamic) bool {
	return i.StandardDate.Equals(other.StandardDate)
}

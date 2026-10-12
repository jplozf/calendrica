package reingolddershowitz

import "fmt"

// CopticEpoch is the fixed date of the first day of Coptic year 1.
const CopticEpoch int64 = 103605

var CopticDayOfWeekNames = []string{
	"Tkyriaka", "Pesnau", "Pshoment", "Peftoou", "Ptiou", "Psoou", "Psabbaton",
}

var CopticMonthNames = []string{
	"Tut", "Babah", "Hatur", "Kiyahk", "Tubah", "Amshir",
	"Baramhat", "Baramundah", "Bashans", "Ba'unah", "Abib", "Misra", "al-Nasi",
}

// Coptic represents a date in the Coptic calendar.
type Coptic struct {
	StandardDate
}

func NewCoptic(year int64, month, day int) Coptic {
	return Coptic{StandardDate: StandardDate{Year: year, Month: month, Day: day}}
}

func CopticToFixed(year int64, month, day int) int64 {
	return CopticEpoch - 1 + 365*(year-1) + Quotient(year, 4) +
		30*int64(month-1) + int64(day)
}

func (c Coptic) ToFixed() int64 {
	return CopticToFixed(c.Year, c.Month, c.Day)
}

func CopticFromFixed(rd int64) Coptic {
	year := Quotient(4*(rd-CopticEpoch)+1463, 1461)
	month := 1 + int(Quotient(rd-CopticToFixed(year, 1, 1), 30))
	day := int(rd + 1 - CopticToFixed(year, month, 1))
	return NewCoptic(year, month, day)
}

func IsCopticLeapYear(year int64) bool {
	return Mod(year, 4) == 3
}

func (c Coptic) Format() string {
	weekday := CopticDayOfWeekNames[DayOfWeekFromFixed(c.ToFixed())]
	month := ""
	if c.Month >= 1 && c.Month <= len(CopticMonthNames) {
		month = CopticMonthNames[c.Month-1]
	}
	return fmt.Sprintf("%s, %d %s %d A.M.", weekday, c.Day, month, c.Year)
}

func (c Coptic) Equals(other Coptic) bool {
	return c.StandardDate.Equals(other.StandardDate)
}

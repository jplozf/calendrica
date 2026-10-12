package reingolddershowitz

import "fmt"

// ArithmeticPersianEpoch is the fixed date of Persian year 1, month 1, day 1.
const ArithmeticPersianEpoch int64 = 226896

var ArithmeticPersianMonthNames = []string{
	"Farvardin", "Ordibehesht", "Xordad", "Tir", "Mordad", "Shahrivar",
	"Mehr", "Aban", "Azar", "Dey", "Bahman", "Esfand",
}

// ArithmeticPersian represents a date in the 2820-year arithmetic Persian cycle.
type ArithmeticPersian struct {
	StandardDate
}

func NewArithmeticPersian(year int64, month, day int) ArithmeticPersian {
	return ArithmeticPersian{StandardDate: StandardDate{Year: year, Month: month, Day: day}}
}

func ArithmeticPersianToFixed(pYear int64, month, day int) int64 {
	y := pYear - 474
	if pYear <= 0 {
		y = pYear - 473
	}
	year := Mod(y, 2820) + 474
	monthOffset := int64(30*(month-1) + 6)
	if month <= 7 {
		monthOffset = int64(31 * (month - 1))
	}
	return ArithmeticPersianEpoch - 1 +
		1029983*Quotient(y, 2820) +
		365*(year-1) +
		Quotient(682*year-110, 2816) +
		monthOffset + int64(day)
}

func (p ArithmeticPersian) ToFixed() int64 {
	return ArithmeticPersianToFixed(p.Year, p.Month, p.Day)
}

func ArithmeticPersianYearFromFixed(rd int64) int64 {
	l0 := rd - ArithmeticPersianToFixed(475, 1, 1)
	n2820 := Quotient(l0, 1029983)
	d1 := Mod(l0, 1029983)
	var y2820 int64
	if d1 == 1029982 {
		y2820 = 2820
	} else {
		y2820 = Quotient(2816*d1+1031337, 1028522)
	}
	year := 474 + 2820*n2820 + y2820
	if year <= 0 {
		return year - 1
	}
	return year
}

func ArithmeticPersianFromFixed(rd int64) ArithmeticPersian {
	year := ArithmeticPersianYearFromFixed(rd)
	dayOfYear := 1 + rd - ArithmeticPersianToFixed(year, 1, 1)
	var month int
	if dayOfYear < 186 {
		month = int(Quotient(dayOfYear+30, 31))
	} else {
		month = int(Quotient(dayOfYear-6+29, 30))
	}
	day := int(rd - (ArithmeticPersianToFixed(year, month, 1) - 1))
	return NewArithmeticPersian(year, month, day)
}

func IsArithmeticPersianLeapYear(pYear int64) bool {
	y := pYear - 474
	if pYear <= 0 {
		y = pYear - 473
	}
	year := Mod(y, 2820) + 474
	return Mod((year+38)*682, 2816) < 682
}

func (p ArithmeticPersian) Format() string {
	month := ""
	if p.Month >= 1 && p.Month <= len(ArithmeticPersianMonthNames) {
		month = ArithmeticPersianMonthNames[p.Month-1]
	}
	return fmt.Sprintf("%d %s %d A.P.", p.Day, month, p.Year)
}

func (p ArithmeticPersian) Equals(other ArithmeticPersian) bool {
	return p.StandardDate.Equals(other.StandardDate)
}

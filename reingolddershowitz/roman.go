package reingolddershowitz

import "fmt"

const (
	RomanKalends = 1
	RomanNones   = 2
	RomanIdes    = 3
)

var RomanCountNames = []string{
	"", "pridie ", "ante diem iii ", "ante diem iv ", "ante diem v ",
	"ante diem vi ", "ante diem vii ", "ante diem viii ", "ante diem ix ",
	"ante diem x ", "ante diem xi ", "ante diem xii ", "ante diem xiii ",
	"ante diem xiv ", "ante diem xv ", "ante diem xvi ", "ante diem xvii ",
	"ante diem xviii ", "ante diem xix ",
}

var RomanEventNames = []string{"Kalends", "Nones", "Ides"}

type Roman struct {
	Year    int64
	Month   int
	Event   int
	Count   int
	LeapDay bool
}

func NewRoman(year int64, month, event, count int, leapDay bool) Roman {
	return Roman{Year: year, Month: month, Event: event, Count: count, LeapDay: leapDay}
}

func RomanIdesOfMonth(month int) int {
	switch month {
	case March, May, July, October:
		return 15
	default:
		return 13
	}
}

func RomanNonesOfMonth(month int) int {
	return RomanIdesOfMonth(month) - 8
}

func RomanToFixed(year int64, month, event, count int, leapDay bool) int64 {
	var anchor int
	switch event {
	case RomanKalends:
		anchor = 1
	case RomanNones:
		anchor = RomanNonesOfMonth(month)
	case RomanIdes:
		anchor = RomanIdesOfMonth(month)
	default:
		return 0
	}
	adjustment := int64(1)
	if IsJulianLeapYear(year) && month == March && event == RomanKalends && count >= 6 && count <= 16 {
		adjustment = 0
	}
	if leapDay {
		adjustment++
	}
	return JulianToFixed(year, month, anchor) - int64(count) + adjustment
}

func (d Roman) ToFixed() int64 {
	return RomanToFixed(d.Year, d.Month, d.Event, d.Count, d.LeapDay)
}

func RomanFromFixed(rd int64) Roman {
	j := JulianFromFixed(rd)
	year, month, day := j.Year, j.Month, j.Day
	monthPrime := int(AdjustedMod(int64(month+1), 12))
	yearPrime := year
	if monthPrime == January {
		yearPrime++
	}
	kalends1 := RomanToFixed(yearPrime, monthPrime, RomanKalends, 1, false)

	switch {
	case day == 1:
		return NewRoman(year, month, RomanKalends, 1, false)
	case day <= RomanNonesOfMonth(month):
		return NewRoman(year, month, RomanNones, RomanNonesOfMonth(month)-day+1, false)
	case day <= RomanIdesOfMonth(month):
		return NewRoman(year, month, RomanIdes, RomanIdesOfMonth(month)-day+1, false)
	case month != February || !IsJulianLeapYear(year):
		return NewRoman(yearPrime, monthPrime, RomanKalends, int(kalends1-rd+1), false)
	case day < 25:
		return NewRoman(year, March, RomanKalends, 30-day, false)
	default:
		return NewRoman(year, March, RomanKalends, 31-day, day == 25)
	}
}

func (d *Roman) FromFixed(rd int64) {
	*d = RomanFromFixed(rd)
}

func (d *Roman) FromArray(values []int64) {
	if len(values) >= 5 {
		d.Year = values[0]
		d.Month = int(values[1])
		d.Event = int(values[2])
		d.Count = int(values[3])
		d.LeapDay = values[4] != 0
	}
}

func (d Roman) Format() string {
	count := ""
	if d.LeapDay {
		count = "ante diem bis vi "
	} else if d.Count >= 0 && d.Count < len(RomanCountNames) {
		count = RomanCountNames[d.Count]
	}
	event := ""
	if d.Event >= RomanKalends && d.Event <= RomanIdes {
		event = RomanEventNames[d.Event-1]
	}
	month := ""
	if d.Month >= 1 && d.Month <= len(GregorianMonthNames) {
		month = GregorianMonthNames[d.Month-1]
	}
	year, era := d.Year, "C.E."
	if year < 0 {
		year = -year
		era = "B.C.E."
	}
	return fmt.Sprintf("%s%s %s, %d %s", count, event, month, year, era)
}

func (d Roman) Equals(other Roman) bool {
	return d.Year == other.Year && d.Month == other.Month && d.Event == other.Event &&
		d.Count == other.Count && d.LeapDay == other.LeapDay
}

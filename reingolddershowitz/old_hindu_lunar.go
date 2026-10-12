package reingolddershowitz

import (
	"fmt"
	"math"
)

const (
	OldHinduAryaLunarMonth = 1577917500.0 / 53433336
	OldHinduAryaLunarDay   = OldHinduAryaLunarMonth / 30
)

var OldHinduLunarDayOfWeekNames = OldHinduSolarDayOfWeekNames

var OldHinduLunarMonthNames = []string{
	"Chaitra", "Vaisakha", "Jyaishtha", "Ashadha", "Sravana", "Bhadrapada",
	"Asvina", "Kartika", "Margasirsha", "Pausha", "Magha", "Phalguna",
}

type OldHinduLunar struct {
	Year      int64
	Month     int
	LeapMonth bool
	Day       int
}

func NewOldHinduLunar(year int64, month int, leapMonth bool, day int) OldHinduLunar {
	return OldHinduLunar{Year: year, Month: month, LeapMonth: leapMonth, Day: day}
}

func OldHinduLunarToFixed(year int64, month int, leapMonth bool, day int) int64 {
	mina := float64(12*year-1) * OldHinduAryaSolarMonth
	lunarNewYear := OldHinduAryaLunarMonth * (math.Floor(mina/OldHinduAryaLunarMonth) + 1)
	monthOffset := month - 1
	if !leapMonth &&
		math.Ceil((lunarNewYear-mina)/(OldHinduAryaSolarMonth-OldHinduAryaLunarMonth)) <= float64(month) {
		monthOffset = month
	}
	return int64(math.Floor(float64(OldHinduSolarEpoch) +
		lunarNewYear +
		OldHinduAryaLunarMonth*float64(monthOffset) +
		float64(day-1)*OldHinduAryaLunarDay +
		0.75))
}

func (d OldHinduLunar) ToFixed() int64 {
	return OldHinduLunarToFixed(d.Year, d.Month, d.LeapMonth, d.Day)
}

func OldHinduLunarFromFixed(rd int64) OldHinduLunar {
	sun := float64(OldHinduSolarDayCount(rd)) + 0.25
	newMoon := sun - oldHinduMod(sun, OldHinduAryaLunarMonth)
	solarMonthPosition := oldHinduMod(newMoon, OldHinduAryaSolarMonth)
	leapMonth := OldHinduAryaSolarMonth-OldHinduAryaLunarMonth >= solarMonthPosition &&
		solarMonthPosition > 0
	month := 1 + int(math.Mod(math.Ceil(newMoon/OldHinduAryaSolarMonth), 12))
	day := 1 + int(oldHinduMod(math.Floor(sun/OldHinduAryaLunarDay), 30))
	year := int64(math.Ceil((newMoon+OldHinduAryaSolarMonth)/OldHinduAryaSolarYear)) - 1
	return NewOldHinduLunar(year, month, leapMonth, day)
}

func (d *OldHinduLunar) FromFixed(rd int64) {
	*d = OldHinduLunarFromFixed(rd)
}

func OldHinduLunarIsLeapYear(year int64) bool {
	position := oldHinduMod(float64(year)*OldHinduAryaSolarYear-OldHinduAryaSolarMonth, OldHinduAryaLunarMonth)
	return position >= 23902504679.0/1282400064
}

func oldHinduMod(value, modulus float64) float64 {
	return value - modulus*math.Floor(value/modulus)
}

func (d OldHinduLunar) Format() string {
	dayName := OldHinduLunarDayOfWeekNames[DayOfWeekFromFixed(d.ToFixed())]
	monthName := ""
	if d.Month >= 1 && d.Month <= len(OldHinduLunarMonthNames) {
		monthName = OldHinduLunarMonthNames[d.Month-1]
	}
	if d.LeapMonth {
		monthName += " II"
	}
	return fmt.Sprintf("%s, %d %s %d K.Y.", dayName, d.Day, monthName, d.Year)
}

func (d OldHinduLunar) Equals(other OldHinduLunar) bool {
	return d.Year == other.Year && d.Month == other.Month &&
		d.LeapMonth == other.LeapMonth && d.Day == other.Day
}

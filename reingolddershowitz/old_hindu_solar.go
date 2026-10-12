package reingolddershowitz

import (
	"fmt"
	"math"
)

const (
	OldHinduSolarEpoch       int64 = -1132959
	OldHinduAryaSolarYear          = 1577917500.0 / 4320000
	OldHinduAryaSolarMonth         = OldHinduAryaSolarYear / 12
	OldHinduAryaJovianPeriod       = 1577917500.0 / 364224
)

var OldHinduSolarDayOfWeekNames = []string{
	"Ravivara", "Chandravara", "Mangalavara", "Buddhavara",
	"Brihaspatvara", "Sukravara", "Sanivara",
}

var OldHinduSolarMonthNames = []string{
	"Mesha", "Vrishabha", "Mithuna", "Karka", "Simha", "Kanya",
	"Tula", "Vrischika", "Dhanu", "Makara", "Kumbha", "Mina",
}

type OldHinduSolar struct {
	StandardDate
}

func NewOldHinduSolar(year int64, month, day int) OldHinduSolar {
	return OldHinduSolar{StandardDate{Year: year, Month: month, Day: day}}
}

func OldHinduSolarToFixed(year int64, month, day int) int64 {
	return int64(math.Floor(float64(OldHinduSolarEpoch) +
		float64(year)*OldHinduAryaSolarYear +
		float64(month-1)*OldHinduAryaSolarMonth +
		float64(day) - 0.25))
}

func (d OldHinduSolar) ToFixed() int64 {
	return OldHinduSolarToFixed(d.Year, d.Month, d.Day)
}

func OldHinduSolarFromFixed(rd int64) OldHinduSolar {
	sun := float64(rd-OldHinduSolarEpoch) + 0.25
	year := int64(math.Floor(sun / OldHinduAryaSolarYear))
	month := 1 + int(Mod(int64(math.Floor(sun/OldHinduAryaSolarMonth)), 12))
	day := 1 + int(math.Floor(sun-math.Floor(sun/OldHinduAryaSolarMonth)*OldHinduAryaSolarMonth))
	return NewOldHinduSolar(year, month, day)
}

func (d *OldHinduSolar) FromFixed(rd int64) {
	*d = OldHinduSolarFromFixed(rd)
}

func OldHinduSolarDayCount(rd int64) int64 {
	return rd - OldHinduSolarEpoch
}

func OldHinduSolarJovianYear(rd int64) int {
	cycle := math.Floor(float64(OldHinduSolarDayCount(rd)) / (OldHinduAryaJovianPeriod / 12))
	return 1 + int(Mod(int64(cycle), 60))
}

func (d OldHinduSolar) Format() string {
	rd := d.ToFixed()
	dayName := OldHinduSolarDayOfWeekNames[DayOfWeekFromFixed(rd)]
	monthName := ""
	if d.Month >= 1 && d.Month <= len(OldHinduSolarMonthNames) {
		monthName = OldHinduSolarMonthNames[d.Month-1]
	}
	return fmt.Sprintf("%s, %d %s %d K.Y.", dayName, d.Day, monthName, d.Year)
}

func (d OldHinduSolar) Equals(other OldHinduSolar) bool {
	return d.StandardDate.Equals(other.StandardDate)
}

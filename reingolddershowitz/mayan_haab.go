package reingolddershowitz

import "fmt"

var MayanHaabMonthNames = []string{
	"Pop", "Uo", "Zip", "Zotz", "Tzec", "Xul", "Yaxkin", "Mol", "Chen",
	"Yax", "Zac", "Ceh", "Mac", "Kankin", "Muan", "Pax", "Kayab", "Cumku", "Uayeb",
}

const MayanHaabEpoch int64 = MayanLongCountEpoch - 348

// MayanHaab represents a date in the 365-day Mayan Haab cycle.
type MayanHaab struct {
	Month int
	Day   int
}

func NewMayanHaab(month, day int) MayanHaab {
	return MayanHaab{Month: month, Day: day}
}

func MayanHaabFromFixed(rd int64) MayanHaab {
	count := Mod(rd-MayanHaabEpoch, 365)
	return NewMayanHaab(1+int(Quotient(count, 20)), int(Mod(count, 20)))
}

func (h MayanHaab) Ordinal() int {
	return (h.Month-1)*20 + h.Day
}

func MayanHaabOnOrBefore(haab MayanHaab, rd int64) int64 {
	return rd - Mod(rd-MayanHaabEpoch-int64(haab.Ordinal()), 365)
}

func (h MayanHaab) Format() string {
	month := ""
	if h.Month >= 1 && h.Month <= len(MayanHaabMonthNames) {
		month = MayanHaabMonthNames[h.Month-1]
	}
	return fmt.Sprintf("%d %s", h.Day, month)
}

func (h MayanHaab) Equals(other MayanHaab) bool {
	return h == other
}

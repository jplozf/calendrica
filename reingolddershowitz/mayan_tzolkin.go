package reingolddershowitz

import "fmt"

var MayanTzolkinNames = []string{
	"Imix", "Ik", "Akbal", "Kan", "Chicchan", "Cimi", "Manik", "Lamat", "Muluc",
	"Oc", "Chuen", "Eb", "Ben", "Ix", "Men", "Cib", "Caban", "Etznab", "Cauac", "Ahau",
}

const MayanTzolkinEpoch int64 = MayanLongCountEpoch - 159

// MayanTzolkin represents a date in the 260-day Mayan Tzolkin cycle.
type MayanTzolkin struct {
	Number int
	Name   int
}

func NewMayanTzolkin(number, name int) MayanTzolkin {
	return MayanTzolkin{Number: number, Name: name}
}

func MayanTzolkinFromFixed(rd int64) MayanTzolkin {
	count := rd - MayanTzolkinEpoch + 1
	return NewMayanTzolkin(int(AdjustedMod(count, 13)), int(AdjustedMod(count, 20)))
}

func (t MayanTzolkin) Ordinal() int {
	return int(Mod(int64(t.Number-1+39*(t.Number-t.Name)), 260))
}

func MayanTzolkinOnOrBefore(tzolkin MayanTzolkin, rd int64) int64 {
	return rd - Mod(rd-MayanTzolkinEpoch-int64(tzolkin.Ordinal()), 260)
}

// MayanCalendarRoundOnOrBefore returns the most recent fixed date with the
// supplied Haab and Tzolkin dates, or an error if the cycles cannot coincide.
func MayanCalendarRoundOnOrBefore(haab MayanHaab, tzolkin MayanTzolkin, rd int64) (int64, error) {
	haabCount := int64(haab.Ordinal()) + MayanHaabEpoch
	tzolkinCount := int64(tzolkin.Ordinal()) + MayanTzolkinEpoch
	diff := tzolkinCount - haabCount
	if Mod(diff, 5) != 0 {
		return 0, fmt.Errorf("Mayan Haab and Tzolkin dates do not coincide")
	}
	return rd - Mod(rd-haabCount-365*diff, 18980), nil
}

func (t MayanTzolkin) Format() string {
	name := ""
	if t.Name >= 1 && t.Name <= len(MayanTzolkinNames) {
		name = MayanTzolkinNames[t.Name-1]
	}
	return fmt.Sprintf("%d %s", t.Number, name)
}

func (t MayanTzolkin) Equals(other MayanTzolkin) bool {
	return t == other
}

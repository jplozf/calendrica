package reingolddershowitz

import "fmt"

// MayanLongCountEpoch is the fixed date corresponding to Julian day 584283.
const MayanLongCountEpoch int64 = -1137142

// MayanLongCount represents a date in the Mayan Long Count.
type MayanLongCount struct {
	Baktun int64
	Katun  int
	Tun    int
	Uinal  int
	Kin    int
}

func NewMayanLongCount(baktun int64, katun, tun, uinal, kin int) MayanLongCount {
	return MayanLongCount{Baktun: baktun, Katun: katun, Tun: tun, Uinal: uinal, Kin: kin}
}

func MayanLongCountToFixed(baktun int64, katun, tun, uinal, kin int) int64 {
	return MayanLongCountEpoch + baktun*144000 + int64(katun)*7200 +
		int64(tun)*360 + int64(uinal)*20 + int64(kin)
}

func (m MayanLongCount) ToFixed() int64 {
	return MayanLongCountToFixed(m.Baktun, m.Katun, m.Tun, m.Uinal, m.Kin)
}

func MayanLongCountFromFixed(rd int64) MayanLongCount {
	count := rd - MayanLongCountEpoch
	baktun := Quotient(count, 144000)
	dayOfBaktun := Mod(count, 144000)
	katun := Quotient(dayOfBaktun, 7200)
	dayOfKatun := Mod(dayOfBaktun, 7200)
	tun := Quotient(dayOfKatun, 360)
	dayOfTun := Mod(dayOfKatun, 360)
	uinal := Quotient(dayOfTun, 20)
	kin := Mod(dayOfTun, 20)
	return NewMayanLongCount(baktun, int(katun), int(tun), int(uinal), int(kin))
}

func (m MayanLongCount) Format() string {
	return fmt.Sprintf("%d.%d.%d.%d.%d", m.Baktun, m.Katun, m.Tun, m.Uinal, m.Kin)
}

func (m MayanLongCount) Equals(other MayanLongCount) bool {
	return m == other
}

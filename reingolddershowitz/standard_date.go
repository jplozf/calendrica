package reingolddershowitz

import (
	"fmt"
)

// StandardDate regroupe les champs communs à la majorité des calendriers
// (Année, Mois, Jour). Elle peut être incorporée (embedded) dans d'autres structures.
type StandardDate struct {
	Year  int64
	Month int
	Day   int
}

// NewStandardDate crée une instance de StandardDate à partir d'un triplet (année, mois, jour).
func NewStandardDate(year int64, month int, day int) StandardDate {
	return StandardDate{
		Year:  year,
		Month: month,
		Day:   day,
	}
}

// FromArray initialise les champs depuis un slice d'entiers [année, mois, jour].
func (s *StandardDate) FromArray(a []int64) {
	if len(a) >= 3 {
		s.Year = a[0]
		s.Month = int(a[1])
		s.Day = int(a[2])
	}
}

// String retourne une représentation sous forme de chaîne (équivalent de toStringFields).
func (s StandardDate) String() string {
	return fmt.Sprintf("year=%d,month=%d,day=%d", s.Year, s.Month, s.Day)
}

// Equals vérifie l'égalité des champs entre deux StandardDate.
func (s StandardDate) Equals(other StandardDate) bool {
	return s.Year == other.Year && s.Month == other.Month && s.Day == other.Day
}

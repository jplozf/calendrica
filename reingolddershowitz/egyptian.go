package reingolddershowitz

import (
	"fmt"
)

// EgyptianEpoch est la date fixe (R.D.) du début du calendrier égyptien (Ère de Nabonassar).
// JD 1448638 = 26 février 747 av. J.-C. (Julien) = R.D. -272787.
const EgyptianEpoch int64 = -272787

// Noms des mois du calendrier égyptien (12 mois de 30 jours + le mois épagomène Epagomenai)
var EgyptianMonthNames = []string{
	"Thoth",
	"Phaophi",
	"Athyr",
	"Choiak",
	"Tybi",
	"Mechir",
	"Phamenoth",
	"Pharmuthi",
	"Pachon",
	"Payni",
	"Epiphi",
	"Mesori",
	"Epagomenai",
}

// Egyptian intègre directement StandardDate
type Egyptian struct {
	StandardDate
}

func NewEgyptian(year int64, month int, day int) Egyptian {
	return Egyptian{
		StandardDate: StandardDate{Year: year, Month: month, Day: day},
	}
}

// Quotient calcule la division entière avec arrondi vers $-\infty$ (Floor Division).
func Quotient(n, k int64) int64 {
	if (n >= 0 && k > 0) || (n < 0 && k < 0) {
		return n / k
	}
	// Ajustement si le reste n'est pas nul
	if n%k != 0 {
		return (n / k) - 1
	}
	return n / k
}

// Mod (Floor Modulo) pour la gestion correcte des nombres négatifs.
/*
func Mod(n, k int64) int64 {
	return n - k*Quotient(n, k)
}
*/

// EgyptianToFixed convertit une date égyptienne (année, mois, jour) en R.D. (Fixed Date).
func EgyptianToFixed(year int64, month int, day int) int64 {
	return EgyptianEpoch + 365*(year-1) + 30*int64(month-1) + int64(day) - 1
}

// ToFixed convertit l'instance courante d'Egyptian en R.D. (Fixed Date).
func (e Egyptian) ToFixed() int64 {
	return EgyptianToFixed(e.Year, e.Month, e.Day)
}

// EgyptianFromFixed convertit un R.D. (Fixed Date) en date égyptienne.
func EgyptianFromFixed(rd int64) Egyptian {
	days := rd - EgyptianEpoch
	year := 1 + Quotient(days, 365)
	month := int(1 + Quotient(Mod(days, 365), 30))
	day := int(Mod(days-365*(year-1)-30*int64(month-1), 30) + 1)

	return Egyptian{
		StandardDate: StandardDate{Year: year, Month: month, Day: day},
	}
}

// Format retourne la date égyptienne sous forme de chaîne formatée.
// Exemple : "1 Thoth 2773"
func (e Egyptian) Format() string {
	monthName := ""
	if e.Month >= 1 && e.Month <= len(EgyptianMonthNames) {
		monthName = EgyptianMonthNames[e.Month-1]
	}

	return fmt.Sprintf("%d %s %d", e.Day, monthName, e.Year)
}

// Equals vérifie l'égalité entre deux dates égyptiennes.
func (e Egyptian) Equals(other Egyptian) bool {
	return e.Year == other.Year && e.Month == other.Month && e.Day == other.Day
}

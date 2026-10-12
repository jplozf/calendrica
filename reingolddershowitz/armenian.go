package reingolddershowitz

import (
	"fmt"
)

// ArmenianEpoch est la date fixe (R.D.) du début du calendrier arménien.
// Correspond au 11 juillet 552 CE (Julien).
const ArmenianEpoch int64 = 201443

// Armenian représente une date dans le calendrier arménien.
type Armenian struct {
	StandardDate
}

// Noms des jours de la semaine
var ArmenianDayOfWeekNames = []string{
	"Miashabathi",
	"Erkoushabathi",
	"Erekhshabathi",
	"Chorekhshabathi",
	"Hingshabathi",
	"Urbath",
	"Shabath",
}

// Noms des mois du calendrier arménien (12 mois de 30 jours + 13ème mois épagomène Aweleach)
var ArmenianMonthNames = []string{
	"Nawasardi",
	"Hori",
	"Sahmi",
	"Tre",
	"Kaloch",
	"Arach",
	"Mehekani",
	"Areg",
	"Ahekani",
	"Mareri",
	"Margach",
	"Hrotich",
	"Aweleach",
}

// NewArmenian crée une nouvelle instance de date arménienne.
func NewArmenian(year int64, month int, day int) Armenian {
	return Armenian{
		StandardDate: StandardDate{Year: year, Month: month, Day: day},
	}
}

// ArmenianToFixed convertit une date arménienne (année, mois, jour) en R.D. (Fixed Date).
func ArmenianToFixed(year int64, month int, day int) int64 {
	return ArmenianEpoch + EgyptianToFixed(year, month, day) - EgyptianEpoch
}

// ToFixed convertit l'instance courante d'Armenian en R.D. (Fixed Date).
func (a Armenian) ToFixed() int64 {
	return ArmenianToFixed(a.Year, a.Month, a.Day)
}

// ArmenianFromFixed convertit un R.D. (Fixed Date) en date arménienne.
func ArmenianFromFixed(rd int64) Armenian {
	d := EgyptianFromFixed(rd + EgyptianEpoch - ArmenianEpoch)
	return Armenian{
		StandardDate: StandardDate{Year: d.Year, Month: d.Month, Day: d.Day},
	}
}

// Format retourne la date arménienne sous forme de chaîne formatée.
// Exemple : "Miashabathi, 1 Nawasardi 1473"
func (a Armenian) Format() string {
	rd := a.ToFixed()

	// Calcul du jour de la semaine (0 = Dimanche -> Miashabathi)
	dayOfWeek := Mod(rd, 7)
	dayName := ArmenianDayOfWeekNames[dayOfWeek]

	// Récupération du nom du mois (1-indexed vers 0-indexed)
	monthName := ""
	if a.Month >= 1 && a.Month <= len(ArmenianMonthNames) {
		monthName = ArmenianMonthNames[a.Month-1]
	}

	return fmt.Sprintf("%s, %d %s %d", dayName, a.Day, monthName, a.Year)
}

// Equals vérifie l'égalité entre deux dates arméniennes.
func (a Armenian) Equals(other Armenian) bool {
	return a.Year == other.Year && a.Month == other.Month && a.Day == other.Day
}

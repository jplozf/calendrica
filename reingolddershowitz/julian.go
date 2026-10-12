package reingolddershowitz

import (
	"fmt"
)

// JulianEpoch est la date fixe (R.D.) du début du calendrier Julien.
// Correspond au 30 décembre de l'an 0 Grégorien (R.D. -1).
const JulianEpoch int64 = -1

// Constantes pour les mois (alignées sur le calendrier Grégorien)
const (
	January   = 1
	February  = 2
	March     = 3
	April     = 4
	May       = 5
	June      = 6
	July      = 7
	August    = 8
	September = 9
	October   = 10
	November  = 11
	December  = 12
)

// Julian représente une date dans le calendrier Julien.
type Julian struct {
	StandardDate
}

// NewJulian crée une nouvelle instance de date julienne.
func NewJulian(year int64, month int, day int) Julian {
	return Julian{
		StandardDate: StandardDate{Year: year, Month: month, Day: day},
	}
}

// BCE représente une année avant l'ère commune (BCE).
func BCE(n int64) int64 {
	return -n
}

// CE représente une année de l'ère commune (CE).
func CE(n int64) int64 {
	return n
}

// IsJulianLeapYear indique si une année julienne est bissextile.
func IsJulianLeapYear(jYear int64) bool {
	if jYear > 0 {
		return Mod(jYear, 4) == 0
	}
	return Mod(jYear, 4) == 3
}

// JulianToFixed convertit une date julienne (année, mois, jour) en R.D. (Fixed Date).
func JulianToFixed(year int64, month int, day int) int64 {
	y := year
	if year < 0 {
		y = year + 1
	}

	var leapCorrection int64
	if month <= 2 {
		leapCorrection = 0
	} else if IsJulianLeapYear(year) {
		leapCorrection = -1
	} else {
		leapCorrection = -2
	}

	return JulianEpoch - 1 +
		365*(y-1) +
		Quotient(y-1, 4) +
		Quotient(367*int64(month)-362, 12) +
		leapCorrection +
		int64(day)
}

// ToFixed convertit l'instance courante de Julian en R.D. (Fixed Date).
func (j Julian) ToFixed() int64 {
	return JulianToFixed(j.Year, j.Month, j.Day)
}

// JulianFromFixed convertit un R.D. (Fixed Date) en date julienne.
func JulianFromFixed(rd int64) Julian {
	approx := Quotient(4*(rd-JulianEpoch)+1464, 1461)

	var year int64
	if approx <= 0 {
		year = approx - 1
	} else {
		year = approx
	}

	priorDays := rd - JulianToFixed(year, January, 1)

	var correction int64
	march1RD := JulianToFixed(year, March, 1)
	if rd < march1RD {
		correction = 0
	} else if IsJulianLeapYear(year) {
		correction = 1
	} else {
		correction = 2
	}

	month := int(Quotient(12*(priorDays+correction)+373, 367))
	day := int(rd - JulianToFixed(year, month, 1) + 1)

	return Julian{
		StandardDate: StandardDate{Year: year, Month: month, Day: day},
	}
}

// InGregorian retourne la liste des dates fixes (R.D.) pour un jour/mois julien
// qui tombent au cours d'une année grégorienne donnée.
func InGregorian(jMonth int, jDay int, gYear int64) []int64 {
	jan1 := GregorianToFixed(gYear, January, 1)
	dec31 := GregorianToFixed(gYear, December, 31)

	y := JulianFromFixed(jan1).Year
	var yPrime int64
	if y == -1 {
		yPrime = 1
	} else {
		yPrime = y + 1
	}

	date1 := JulianToFixed(y, jMonth, jDay)
	date2 := JulianToFixed(yPrime, jMonth, jDay)

	var result []int64
	if jan1 <= date1 && date1 <= dec31 {
		result = append(result, date1)
	}
	if jan1 <= date2 && date2 <= dec31 {
		result = append(result, date2)
	}

	return result
}

// EasternOrthodoxChristmas retourne les dates fixes (R.D.) du Noël orthodoxe
// (25 décembre du calendrier Julien) au cours d'une année grégorienne donnée.
func EasternOrthodoxChristmas(gYear int64) []int64 {
	return InGregorian(December, 25, gYear)
}

// Format retourne la date julienne sous forme de chaîne formatée.
// Exemple : "25 December 2026 C.E." ou "15 March 44 B.C.E."
func (j Julian) Format() string {
	absYear := j.Year
	era := "C.E."
	if j.Year < 0 {
		absYear = -j.Year
		era = "B.C.E."
	}

	monthName := ""
	if j.Month >= 1 && j.Month <= len(GregorianMonthNames) {
		monthName = GregorianMonthNames[j.Month-1]
	}

	return fmt.Sprintf("%d %s %d %s", j.Day, monthName, absYear, era)
}

// Equals vérifie l'égalité entre deux dates juliennes.
func (j Julian) Equals(other Julian) bool {
	return j.StandardDate.Equals(other.StandardDate)
}

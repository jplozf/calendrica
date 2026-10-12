package reingolddershowitz

import (
	"fmt"
)

// GregorianEpoch est la date fixe (R.D.) du début du calendrier grégorien proleptique.
const GregorianEpoch int64 = 1

// Constantes pour les jours de la semaine
const (
	Sunday    = 0
	Monday    = 1
	Tuesday   = 2
	Wednesday = 3
	Thursday  = 4
	Friday    = 5
	Saturday  = 6
)

// Noms des jours de la semaine
var GregorianDayOfWeekNames = []string{
	"Sunday",
	"Monday",
	"Tuesday",
	"Wednesday",
	"Thursday",
	"Friday",
	"Saturday",
}

// Noms des mois du calendrier grégorien
var GregorianMonthNames = []string{
	"January",
	"February",
	"March",
	"April",
	"May",
	"June",
	"July",
	"August",
	"September",
	"October",
	"November",
	"December",
}

// Gregorian représente une date dans le calendrier grégorien.
type Gregorian struct {
	StandardDate
}

// NewGregorian crée une nouvelle instance de date grégorienne.
func NewGregorian(year int64, month int, day int) Gregorian {
	return Gregorian{
		StandardDate: StandardDate{Year: year, Month: month, Day: day},
	}
}

// --- Fonctions auxiliaires mathématiques et calendaires ---

// AdjustedMod applique un modulo 1..k (au lieu de 0..k-1).
func AdjustedMod(n, k int64) int64 {
	return Mod(n-1, k) + 1
}

// DayOfWeekFromFixed calcule le jour de la semaine pour une date fixe (0 = Dimanche).
func DayOfWeekFromFixed(rd int64) int {
	return int(Mod(rd, 7))
}

// KDayOnOrBefore retourne la date fixe du jour de la semaine k le plus proche à ou avant rd.
func KDayOnOrBefore(k int, rd int64) int64 {
	return rd - int64(DayOfWeekFromFixed(rd-int64(k)))
}

// KDayOnOrAfter retourne la date fixe du jour de la semaine k le plus proche à ou après rd.
func KDayOnOrAfter(k int, rd int64) int64 {
	return KDayOnOrBefore(k, rd+6)
}

// KDayNearest retourne la date fixe du jour de la semaine k le plus proche de rd.
func KDayNearest(rd int64, k int) int64 {
	return KDayOnOrBefore(k, rd+3)
}

// FirstKDay retourne la première occurrence du jour de la semaine k à partir de la date fixe rd.
func FirstKDay(k int, rd int64) int64 {
	return KDayOnOrAfter(k, rd)
}

// LastKDay retourne la dernière occurrence du jour de la semaine k jusqu'à la date fixe rd.
func LastKDay(k int, rd int64) int64 {
	return KDayOnOrBefore(k, rd)
}

// --- Date Conversion Methods ---

// IsGregorianLeapYear vérifie si une année grégorienne est bissextile.
func IsGregorianLeapYear(gYear int64) bool {
	if Mod(gYear, 4) == 0 {
		n := Mod(gYear, 400)
		if n != 100 && n != 200 && n != 300 {
			return true
		}
	}
	return false
}

// GregorianToFixed convertit une date grégorienne (année, mois, jour) en R.D. (Fixed Date).
func GregorianToFixed(year int64, month int, day int) int64 {
	var leapCorrection int64
	if month <= 2 {
		leapCorrection = 0
	} else if IsGregorianLeapYear(year) {
		leapCorrection = -1
	} else {
		leapCorrection = -2
	}

	return GregorianEpoch - 1 +
		365*(year-1) +
		Quotient(year-1, 4) -
		Quotient(year-1, 100) +
		Quotient(year-1, 400) +
		Quotient(367*int64(month)-362, 12) +
		leapCorrection +
		int64(day)
}

// ToFixed convertit l'instance courante de Gregorian en R.D. (Fixed Date).
func (g Gregorian) ToFixed() int64 {
	return GregorianToFixed(g.Year, g.Month, g.Day)
}

// GregorianYearFromFixed calcule l'année grégorienne correspondant à une date fixe.
func GregorianYearFromFixed(rd int64) int64 {
	l0 := rd - GregorianEpoch
	n400 := Quotient(l0, 146097)
	d1 := Mod(l0, 146097)
	n100 := Quotient(d1, 36524)
	d2 := Mod(d1, 36524)
	n4 := Quotient(d2, 1461)
	d3 := Mod(d2, 1461)
	n1 := Quotient(d3, 365)
	year := 400*n400 + 100*n100 + 4*n4 + n1

	if n100 == 4 || n1 == 4 {
		return year
	}
	return year + 1
}

// GregorianFromFixed convertit un R.D. (Fixed Date) en date grégorienne.
func GregorianFromFixed(rd int64) Gregorian {
	year := GregorianYearFromFixed(rd)
	priorDays := rd - GregorianToFixed(year, January, 1)

	var correction int64
	march1RD := GregorianToFixed(year, March, 1)
	if rd < march1RD {
		correction = 0
	} else if IsGregorianLeapYear(year) {
		correction = 1
	} else {
		correction = 2
	}

	month := int(Quotient(12*(priorDays+correction)+373, 367))
	day := int(rd - GregorianToFixed(year, month, 1) + 1)

	return Gregorian{
		StandardDate: StandardDate{Year: year, Month: month, Day: day},
	}
}

// --- Alternative Calculations ---

// AltFixedFromGregorian est un calcul alternatif de date fixe à partir d'une date grégorienne.
func AltFixedFromGregorian(year int64, month int, day int) int64 {
	m := AdjustedMod(int64(month)-2, 12)
	y := year + Quotient(int64(month)+9, 12)

	return GregorianEpoch - 1 -
		306 +
		365*(y-1) +
		Quotient(y-1, 4) -
		Quotient(y-1, 100) +
		Quotient(y-1, 400) +
		Quotient(3*m-1, 5) +
		30*(m-1) +
		int64(day)
}

// AltGregorianYearFromFixed est une estimation alternative de l'année grégorienne.
func AltGregorianYearFromFixed(rd int64) int64 {
	approx := int64(float64(rd-GregorianEpoch+2) / (146097.0 / 400.0))
	start := GregorianEpoch +
		365*approx +
		Quotient(approx, 4) -
		Quotient(approx, 100) +
		Quotient(approx, 400)

	if rd < start {
		return approx
	}
	return approx + 1
}

// AltGregorianFromFixed est une conversion alternative de date fixe en date grégorienne.
func AltGregorianFromFixed(rd int64) Gregorian {
	y := GregorianYearFromFixed((GregorianEpoch - 1) + rd + 306)
	priorDays := rd - GregorianToFixed(y-1, March, 1)
	month := int(AdjustedMod(Quotient(5*priorDays+155, 153)+2, 12))
	year := y - Quotient(int64(month)+9, 12)
	day := int(1 + rd - GregorianToFixed(year, month, 1))

	return Gregorian{
		StandardDate: StandardDate{Year: year, Month: month, Day: day},
	}
}

// --- Methods & Calculations ---

// LastDayOfMonth retourne le dernier jour du mois courant.
func (g Gregorian) LastDayOfMonth() int {
	switch g.Month {
	case February:
		if IsGregorianLeapYear(g.Year) {
			return 29
		}
		return 28
	case April, June, September, November:
		return 30
	default:
		return 31
	}
}

// DayNumber retourne le numéro du jour dans l'année (1 à 365/366).
func (g Gregorian) DayNumber() int64 {
	return g.ToFixed() - GregorianToFixed(g.Year-1, December, 31)
}

// DaysRemaining retourne le nombre de jours restants dans l'année.
func (g Gregorian) DaysRemaining() int64 {
	return GregorianToFixed(g.Year, December, 31) - g.ToFixed()
}

// --- Holidays & Key Days ---

func IndependenceDay(gYear int64) int64 {
	return GregorianToFixed(gYear, July, 4)
}

func LaborDay(gYear int64) int64 {
	return FirstKDay(Monday, GregorianToFixed(gYear, September, 1))
}

func MemorialDay(gYear int64) int64 {
	return LastKDay(Monday, GregorianToFixed(gYear, May, 31))
}

func ElectionDay(gYear int64) int64 {
	return FirstKDay(Tuesday, GregorianToFixed(gYear, November, 2))
}

func DaylightSavingStart(gYear int64) int64 {
	return FirstKDay(Sunday, GregorianToFixed(gYear, April, 1))
}

func DaylightSavingEnd(gYear int64) int64 {
	return LastKDay(Sunday, GregorianToFixed(gYear, October, 31))
}

func Christmas(gYear int64) int64 {
	return GregorianToFixed(gYear, December, 25)
}

func Advent(gYear int64) int64 {
	return KDayNearest(GregorianToFixed(gYear, November, 30), Sunday)
}

func Epiphany(gYear int64) int64 {
	return FirstKDay(Sunday, GregorianToFixed(gYear, January, 2))
}

// --- Formatting & Standard Methods ---

// Format retourne la date grégorienne sous forme de chaîne formatée.
// Exemple : "Tuesday, 29 September 2026"
func (g Gregorian) Format() string {
	rd := g.ToFixed()
	dayOfWeek := DayOfWeekFromFixed(rd)
	dayName := GregorianDayOfWeekNames[dayOfWeek]

	monthName := ""
	if g.Month >= 1 && g.Month <= len(GregorianMonthNames) {
		monthName = GregorianMonthNames[g.Month-1]
	}

	return fmt.Sprintf("%s, %d %s %d", dayName, g.Day, monthName, g.Year)
}

// Equals vérifie l'égalité entre deux dates grégoriennes.
func (g Gregorian) Equals(other Gregorian) bool {
	return g.StandardDate.Equals(other.StandardDate)
}

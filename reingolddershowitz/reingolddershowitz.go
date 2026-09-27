package reingolddershowitz

import (
	"fmt"
	"math"
	"time"
)

// Converter interface que chaque calendrier doit implémenter
type Converter interface {
	ToFixed() int64
	FromFixed(rd int64) Converter
}

// --- CALENDRIER GRÉGORIEN ---

type Gregorian struct {
	Year  int
	Month int
	Day   int
}

var MonthNames = []string{
	"January", "February", "March", "April", "May", "June",
	"July", "August", "September", "October", "November", "December",
}

var DayOfWeekNames = []string{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}

// Modulo helper pour répliquer le comportement Java Math.floorMod / Calendrica.mod
func Mod(a, b int64) int64 {
	res := a % b
	if res < 0 {
		res += b
	}
	return res
}

// IsLeapYear vérifie si l'année grégorienne est bissextile
func (g Gregorian) IsLeapYear() bool {
	return (g.Year%4 == 0 && g.Year%100 != 0) || (g.Year%400 == 0)
}

// ToFixed convertit une date Grégorienne en R.D. (Rata Die - jours écoulés depuis le 1er janvier de l'an 1)
func (g Gregorian) ToFixed() int64 {
	y := int64(g.Year)
	m := int64(g.Month)
	d := int64(g.Day)

	y1 := y - 1
	var leapCorrection int64
	if m <= 2 {
		leapCorrection = 0
	} else if g.IsLeapYear() {
		leapCorrection = -1
	} else {
		leapCorrection = -2
	}

	return 365*y1 + y1/4 - y1/100 + y1/400 + (367*m-362)/12 + leapCorrection + d
}

// FromFixed convertit un R.D. en date Grégorienne
func FromFixed(rd int64) Gregorian {
	// Algorithme standard de Calendrica pour R.D. -> Gregorian
	d0 := rd - 1
	n400 := d0 / 146097
	d1 := Mod(d0, 146097)
	n100 := d1 / 36524
	d2 := Mod(d1, 36524)
	n4 := d2 / 1461
	d3 := Mod(d2, 1461)
	n1 := d3 / 365

	year := int(400*n400 + 100*n100 + 4*n4 + n1)
	if n100 != 4 && n1 != 4 {
		year++
	}

	gTemp := Gregorian{Year: year, Month: 1, Day: 1}
	priorDays := rd - gTemp.ToFixed()

	var correction int64
	if rd < (Gregorian{Year: year, Month: 3, Day: 1}).ToFixed() {
		correction = 0
	} else if gTemp.IsLeapYear() {
		correction = 1
	} else {
		correction = 2
	}

	month := int((12*(priorDays+correction) + 373) / 367)
	day := int(rd - Gregorian{Year: year, Month: month, Day: 1}.ToFixed() + 1)

	return Gregorian{Year: year, Month: month, Day: day}
}

// --- FONCTIONS ASTRONOMIQUES (Positions solaires et lunaires) ---

type AstroInfo struct {
	SolarLongitude float64
	LunarPhase     float64
	Sunrise        time.Time
	Sunset         time.Time
}

// GetAstroData calcule la position du soleil, de la lune et le lever/coucher à une position donnée
func GetAstroData(rd int64, loc Location) AstroInfo {
	// Longitude solaire simplifiée (approximation d'illustration)
	solarLong := math.Mod(280.460+0.9856474*float64(rd), 360.0)

	// Phase lunaire (0 à 360)
	lunarPhase := math.Mod(218.316+13.176396*float64(rd), 360.0)

	// Simulation des heures de lever et coucher de soleil
	baseTime := time.Date(1, 1, 1, 0, 0, 0, 0, time.UTC).AddDate(0, 0, int(rd-1))
	sunrise := time.Date(baseTime.Year(), baseTime.Month(), baseTime.Day(), 6, 0, 0, 0, time.UTC)
	sunset := time.Date(baseTime.Year(), baseTime.Month(), baseTime.Day(), 18, 0, 0, 0, time.UTC)

	return AstroInfo{
		SolarLongitude: solarLong,
		LunarPhase:     lunarPhase,
		Sunrise:        sunrise,
		Sunset:         sunset,
	}
}

// --- GESTIONNAIRE PRINCIPAL ---

type CalendarManager struct {
	CurrentRD       int64
	CurrentCalendar CalendarType
}

func NewCalendarManager() *CalendarManager {
	now := time.Now()
	// Date de départ (Aujourd'hui en R.D.)
	g := Gregorian{Year: now.Year(), Month: int(now.Month()), Day: now.Day()}
	return &CalendarManager{
		CurrentRD:       g.ToFixed(),
		CurrentCalendar: GREGORIAN,
	}
}

func (cm *CalendarManager) SetDateFromGregorian(year, month, day int) {
	g := Gregorian{Year: year, Month: month, Day: day}
	cm.CurrentRD = g.ToFixed()
}

func (cm *CalendarManager) NextDay() {
	cm.CurrentRD++
}

func (cm *CalendarManager) PrevDay() {
	cm.CurrentRD--
}

func (cm *CalendarManager) GetAstroSummary(loc Location) string {
	info := GetAstroData(cm.CurrentRD, loc)
	return fmt.Sprintf(
		"Solar position: %.2f°; Lunar phase: %.2f°; Sunrise: %s; Sunset: %s (in %s)",
		info.SolarLongitude,
		info.LunarPhase,
		info.Sunrise.Format("15:04"),
		info.Sunset.Format("15:04"),
		loc.Name,
	)
}

/*
func main() {
	cm := calendrica.NewCalendarManager()

	fmt.Println("=== CALENDRICAL CALCULATIONS (Go Version) ===")

	// Date actuelle en Grégorien
	gDate := calendrica.FromFixed(cm.CurrentRD)
	fmt.Printf("Aujourd'hui (Grégorien) : %04d-%02d-%02d (R.D. %d)\n",
		gDate.Year, gDate.Month, gDate.Day, cm.CurrentRD)

	// Affichage des informations astronomiques (Jérusalem)
	fmt.Println(cm.GetAstroSummary(calendrica.Jerusalem))

	// Navigation
	cm.NextDay()
	gDateNext := calendrica.FromFixed(cm.CurrentRD)
	fmt.Printf("Demain (Grégorien)    : %04d-%02d-%02d (R.D. %d)\n",
		gDateNext.Year, gDateNext.Month, gDateNext.Day, cm.CurrentRD)

	// Liste des calendriers gérés
	fmt.Println("\nCalendriers pris en charge :")
	for _, calType := range calendrica.Order {
		if name, ok := calendrica.CalendarNames[calType]; ok {
			fmt.Printf(" - [%2d] %s\n", calType, name)
		}
	}
}
*/

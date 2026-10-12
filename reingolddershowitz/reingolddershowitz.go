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

var DayOfWeekNames = []string{"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"}

// Modulo helper pour répliquer le comportement Java Math.floorMod / Calendrica.mod
func Mod(a, b int64) int64 {
	res := a % b
	if res < 0 {
		res += b
	}
	return res
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
	info, err := GetAstroDataChecked(rd, loc)
	if err != nil {
		panic(err)
	}
	return info
}

func GetAstroDataChecked(rd int64, loc Location) (AstroInfo, error) {
	moment := float64(rd) + 0.5
	sunrise, err := SunriseChecked(rd, loc)
	if err != nil {
		return AstroInfo{}, err
	}
	sunset, err := SunsetChecked(rd, loc)
	if err != nil {
		return AstroInfo{}, err
	}
	return AstroInfo{
		SolarLongitude: SolarLongitude(moment),
		LunarPhase:     LunarPhase(moment),
		Sunrise:        standardMomentToTime(sunrise, loc),
		Sunset:         standardMomentToTime(sunset, loc),
	}, nil
}

func standardMomentToTime(moment float64, loc Location) time.Time {
	universal := UniversalFromStandard(moment, loc)
	unixMoment := universal - float64(GregorianToFixed(1970, January, 1))
	unixNanos := int64(math.Round(unixMoment * float64(24*time.Hour)))
	zone := time.FixedZone(loc.Name, int(math.Round(loc.Zone*60*60)))
	return time.Unix(0, unixNanos).In(zone)
}

// --- GESTIONNAIRE PRINCIPAL ---

type CalendarManager struct {
	CurrentRD       int64
	CurrentCalendar CalendarType
}

func NewCalendarManager() *CalendarManager {
	now := time.Now()
	// Date de départ (Aujourd'hui en R.D.)
	g := Gregorian{StandardDate: StandardDate{int64(now.Year()), int(now.Month()), now.Day()}}
	return &CalendarManager{
		CurrentRD:       g.ToFixed(),
		CurrentCalendar: GREGORIAN,
	}
}

func (cm *CalendarManager) SetDateFromGregorian(year, month, day int) {
	g := Gregorian{StandardDate: StandardDate{int64(year), month, day}}
	cm.CurrentRD = g.ToFixed()
}

func (cm *CalendarManager) NextDay() {
	cm.CurrentRD++
}

func (cm *CalendarManager) PrevDay() {
	cm.CurrentRD--
}

func (cm *CalendarManager) GetAstroSummary(loc Location) string {
	summary, err := cm.GetAstroSummaryChecked(loc)
	if err != nil {
		panic(err)
	}
	return summary
}

func (cm *CalendarManager) GetAstroSummaryChecked(loc Location) (string, error) {
	info, err := GetAstroDataChecked(cm.CurrentRD, loc)
	if err != nil {
		return "", err
	}
	return fmt.Sprintf(
		"Solar position: %.2f°; Lunar phase: %.2f°; Sunrise: %s; Sunset: %s (in %s)",
		info.SolarLongitude,
		info.LunarPhase,
		info.Sunrise.Format("15:04"),
		info.Sunset.Format("15:04"),
		loc.Name,
	), nil
}

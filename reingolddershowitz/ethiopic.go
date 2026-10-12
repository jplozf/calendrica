package reingolddershowitz

import "fmt"

// EthiopicEpoch is the fixed date of the first day of Ethiopic year 1.
const EthiopicEpoch int64 = 2796

var EthiopicDayOfWeekNames = []string{
	"Ihud", "Sanyo", "Maksanyo", "Rob", "Hamus", "Arb", "Kidamme",
}

var EthiopicMonthNames = []string{
	"Maskaram", "Teqemt", "Kehdar", "Takhsas", "Ter", "Yakatit",
	"Magabit", "Miyazya", "Genbot", "Sane", "Hamle", "Nahase", "Paguemen",
}

// Ethiopic represents a date in the Ethiopic calendar.
type Ethiopic struct {
	StandardDate
}

func NewEthiopic(year int64, month, day int) Ethiopic {
	return Ethiopic{StandardDate: StandardDate{Year: year, Month: month, Day: day}}
}

func EthiopicToFixed(year int64, month, day int) int64 {
	return CopticToFixed(year, month, day) - CopticEpoch + EthiopicEpoch
}

func (e Ethiopic) ToFixed() int64 {
	return EthiopicToFixed(e.Year, e.Month, e.Day)
}

func EthiopicFromFixed(rd int64) Ethiopic {
	coptic := CopticFromFixed(rd + CopticEpoch - EthiopicEpoch)
	return NewEthiopic(coptic.Year, coptic.Month, coptic.Day)
}

func (e Ethiopic) Format() string {
	weekday := EthiopicDayOfWeekNames[DayOfWeekFromFixed(e.ToFixed())]
	month := ""
	if e.Month >= 1 && e.Month <= len(EthiopicMonthNames) {
		month = EthiopicMonthNames[e.Month-1]
	}
	return fmt.Sprintf("%s, %d %s %d E.E.", weekday, e.Day, month, e.Year)
}

func (e Ethiopic) Equals(other Ethiopic) bool {
	return e.StandardDate.Equals(other.StandardDate)
}

package reingolddershowitz

import (
	"fmt"
	"math"
	"strings"
)

const (
	ChineseEpoch            int64 = -963099
	ChineseDayNameEpoch           = 15
	ChineseMonthNameEpoch         = 3
	chineseMeanTropicalYear       = 365.242189
	chineseMeanSynodicMonth       = 29.530588853
	chineseWinterSolstice         = 270.0
)

var (
	ChineseYearStemNames   = [...]string{"Jia", "Yi", "Bing", "Ding", "Wu", "Ji", "Geng", "Xin", "Ren", "Gui"}
	ChineseYearBranchNames = [...]string{"Zi", "Chou", "Yin", "Mao", "Chen", "Si", "Wu", "Wei", "Shen", "You", "Xu", "Hai"}
)

// Chinese is a date in the traditional Chinese lunisolar calendar.
type Chinese struct {
	Cycle     int64
	Year      int
	Month     int
	LeapMonth bool
	Day       int
}

// ChineseName is a sexagesimal stem-and-branch designation (both indices 1-based).
type ChineseName struct {
	Stem   int
	Branch int
}

func NewChinese(cycle int64, year, month int, leapMonth bool, day int) Chinese {
	return Chinese{Cycle: cycle, Year: year, Month: month, LeapMonth: leapMonth, Day: day}
}

func NewChineseFromFixed(rd int64) Chinese { return ChineseFromFixed(rd) }

func ChineseToFixed(cycle int64, year, month int, leapMonth bool, day int) int64 {
	midYear := int64(math.Floor(float64(ChineseEpoch) + ((float64(cycle-1)*60 + float64(year-1) + 0.5) * chineseMeanTropicalYear)))
	newYear := ChineseNewYearOnOrBefore(midYear)
	candidate := ChineseNewMoonOnOrAfter(newYear + int64(29*(month-1)))
	date := ChineseFromFixed(candidate)
	var monthStart int64
	if month == date.Month && leapMonth == date.LeapMonth {
		monthStart = candidate
	} else {
		monthStart = ChineseNewMoonOnOrAfter(candidate + 1)
	}
	return monthStart + int64(day-1)
}

func (d Chinese) ToFixed() int64 {
	return ChineseToFixed(d.Cycle, d.Year, d.Month, d.LeapMonth, d.Day)
}

func ChineseFromFixed(rd int64) Chinese {
	s1 := ChineseWinterSolsticeOnOrBefore(rd)
	s2 := ChineseWinterSolsticeOnOrBefore(s1 + 370)
	m12 := ChineseNewMoonOnOrAfter(s1 + 1)
	nextM11 := ChineseNewMoonBefore(s2 + 1)
	m := ChineseNewMoonBefore(rd + 1)
	leapYear := int64(math.Round(float64(nextM11-m12)/chineseMeanSynodicMonth)) == 12
	month := int(AdjustedMod(
		int64(math.Round(float64(m-m12)/chineseMeanSynodicMonth))-
			boolToInt64(leapYear && ChineseHasPriorLeapMonth(m12, m)),
		12))
	leapMonth := leapYear && ChineseHasNoMajorSolarTerm(m) &&
		!ChineseHasPriorLeapMonth(m12, ChineseNewMoonBefore(m))
	elapsedYears := int64(math.Floor(1.5 - float64(month)/12 + float64(rd-ChineseEpoch)/chineseMeanTropicalYear))
	cycle := Quotient(elapsedYears-1, 60) + 1
	year := int(AdjustedMod(elapsedYears, 60))
	return NewChinese(cycle, year, month, leapMonth, int(rd-m+1))
}

func (d *Chinese) FromFixed(rd int64) { *d = ChineseFromFixed(rd) }

func (d *Chinese) FromArray(a []int64) {
	if len(a) >= 5 {
		*d = NewChinese(a[0], int(a[1]), int(a[2]), a[3] != 0, int(a[4]))
	}
}

func ChineseSolarLongitudeOnOrAfter(date int64, longitude float64) float64 {
	loc := ChineseLocation(float64(date))
	tee := SolarLongitudeAfter(UniversalFromStandard(float64(date), loc), longitude)
	return tee + loc.Zone/24
}

func ChineseMidnightInChina(date int64) float64 {
	return UniversalFromStandard(float64(date), ChineseLocation(float64(date)))
}

func ChineseWinterSolsticeOnOrBefore(date int64) int64 {
	approx := SolarLongitudeAfter(ChineseMidnightInChina(date+1)-chineseMeanTropicalYear, chineseWinterSolstice)
	for i := int64(math.Floor(approx)) - 1; ; i++ {
		if SolarLongitude(ChineseMidnightInChina(i+1)) >= chineseWinterSolstice {
			return i
		}
	}
}

func ChineseNewYearInSui(date int64) int64 {
	s1 := ChineseWinterSolsticeOnOrBefore(date)
	s2 := ChineseWinterSolsticeOnOrBefore(s1 + 370)
	m12 := ChineseNewMoonOnOrAfter(s1 + 1)
	m13 := ChineseNewMoonOnOrAfter(m12 + 1)
	nextM11 := ChineseNewMoonBefore(s2 + 1)
	if int64(math.Round(float64(nextM11-m12)/chineseMeanSynodicMonth)) == 12 &&
		(ChineseHasNoMajorSolarTerm(m12) || ChineseHasNoMajorSolarTerm(m13)) {
		return ChineseNewMoonOnOrAfter(m13 + 1)
	}
	return m13
}

func ChineseNewYearOnOrBefore(date int64) int64 {
	newYear := ChineseNewYearInSui(date)
	if date >= newYear {
		return newYear
	}
	return ChineseNewYearInSui(date - 180)
}

func ChineseNameDifference(name1, name2 ChineseName) int {
	stemDifference := name2.Stem - name1.Stem
	branchDifference := name2.Branch - name1.Branch
	return 1 + int(Mod(int64(stemDifference-1+25*(branchDifference-stemDifference)), 60))
}

func ChineseDayNameOnOrBefore(name ChineseName, date int64) int64 {
	return date - int64(Mod(date+int64(ChineseNameDifference(name, ChineseSexagesimalName(ChineseDayNameEpoch))), 60))
}

func ChineseCurrentMajorSolarTerm(date int64) int {
	s := SolarLongitude(UniversalFromStandard(float64(date), ChineseLocation(float64(date))))
	return int(AdjustedMod(2+int64(math.Floor(s/30)), 12))
}

func ChineseMajorSolarTermOnOrAfter(date int64) float64 {
	longitude := chineseMod(30*math.Ceil(SolarLongitude(ChineseMidnightInChina(date))/30), 360)
	return ChineseSolarLongitudeOnOrAfter(date, longitude)
}

func ChineseCurrentMinorSolarTerm(date int64) int {
	s := SolarLongitude(ChineseMidnightInChina(date))
	return int(AdjustedMod(3+int64(math.Floor((s-15)/30)), 12))
}

func ChineseMinorSolarTermOnOrAfter(date int64) float64 {
	s := SolarLongitude(ChineseMidnightInChina(date))
	longitude := chineseMod(30*math.Ceil((s-15)/30)+15, 360)
	return ChineseSolarLongitudeOnOrAfter(date, longitude)
}

func ChineseNewMoonBefore(date int64) int64 {
	moment := NewMoonBefore(ChineseMidnightInChina(date))
	loc := ChineseLocation(moment)
	return int64(math.Floor(moment + loc.Zone/24))
}

func ChineseNewMoonOnOrAfter(date int64) int64 {
	moment := NewMoonAfter(ChineseMidnightInChina(date))
	loc := ChineseLocation(moment)
	return int64(math.Floor(moment + loc.Zone/24))
}

func ChineseHasNoMajorSolarTerm(date int64) bool {
	return ChineseCurrentMajorSolarTerm(date) == ChineseCurrentMajorSolarTerm(ChineseNewMoonOnOrAfter(date+1))
}

func ChineseHasPriorLeapMonth(first, last int64) bool {
	return last >= first && (ChineseHasNoMajorSolarTerm(last) ||
		ChineseHasPriorLeapMonth(first, ChineseNewMoonBefore(last)))
}

func ChineseSexagesimalName(n int64) ChineseName {
	return ChineseName{Stem: int(AdjustedMod(n, 10)), Branch: int(AdjustedMod(n, 12))}
}

func ChineseNameOfYear(year int) ChineseName { return ChineseSexagesimalName(int64(year)) }

func ChineseNameOfMonth(year, month int) ChineseName {
	elapsedMonths := 12*(year-1) + month - 1
	return ChineseSexagesimalName(int64(elapsedMonths + ChineseDayNameEpoch))
}

func ChineseNameOfDay(date int64) ChineseName { return ChineseSexagesimalName(date + 15) }

func ChineseLocation(moment float64) Location {
	year := GregorianYearFromFixed(int64(math.Floor(moment)))
	zone := 8.0
	if year < 1929 {
		zone = 1397.0 / 180
	}
	return NewLocation("Beijing, China", 39.55, Angle(116, 25, 0), Mt(43.5), zone)
}

func Beijing(moment float64) Location { return ChineseLocation(moment) }

func ChineseNewYear(gregorianYear int64) int64 {
	return ChineseNewYearOnOrBefore(GregorianToFixed(gregorianYear, July, 1))
}

func ChineseDragonFestival(gregorianYear int64) int64 {
	elapsedYears := gregorianYear - GregorianYearFromFixed(ChineseEpoch) + 1
	cycle := Quotient(elapsedYears-1, 60) + 1
	year := int(AdjustedMod(elapsedYears, 60))
	return ChineseToFixed(cycle, year, 5, false, 5)
}

func ChineseQingMing(gregorianYear int64) int64 {
	return int64(math.Floor(ChineseMinorSolarTermOnOrAfter(GregorianToFixed(gregorianYear, March, 30))))
}

// ChineseAge reports the traditional age and whether the birth date is not in the future.
func ChineseAge(birthdate Chinese, date int64) (int64, bool) {
	if date < birthdate.ToFixed() {
		return 0, false
	}
	today := ChineseFromFixed(date)
	return 60*(today.Cycle-birthdate.Cycle) + int64(today.Year-birthdate.Year) + 1, true
}

func (d Chinese) Format() string {
	yearName := ChineseNameOfYear(d.Year)
	stem := ChineseYearStemNames[yearName.Stem-1]
	branch := strings.ToLower(ChineseYearBranchNames[yearName.Branch-1])
	leap := ""
	if d.LeapMonth {
		leap = "leap "
	}
	return fmt.Sprintf("cycle %d, year %s-%s, %smonth %d, day %d", d.Cycle, stem, branch, leap, d.Month, d.Day)
}

func (d Chinese) Equals(other Chinese) bool {
	return d.Cycle == other.Cycle && d.Year == other.Year && d.Month == other.Month &&
		d.LeapMonth == other.LeapMonth && d.Day == other.Day
}

func (n ChineseName) Equals(other ChineseName) bool {
	return n.Stem == other.Stem && n.Branch == other.Branch
}

func boolToInt64(value bool) int64 {
	if value {
		return 1
	}
	return 0
}

func chineseMod(value, modulus float64) float64 {
	return value - modulus*math.Floor(value/modulus)
}

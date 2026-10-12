package reingolddershowitz

import "fmt"

const BalineseEpoch int64 = -1721279

type Balinese struct {
	Luang     bool
	Dwiwara   int
	Triwara   int
	Caturwara int
	Pancawara int
	Sadwara   int
	Saptawara int
	Asatawara int
	Sangawara int
	Dasawara  int
}

var (
	BalineseDwiwaraNames   = []string{"Menga", "Pepet"}
	BalineseTriwaraNames   = []string{"Pasah", "Beteng", "Kajeng"}
	BalineseCaturwaraNames = []string{"Sri", "Laba", "Jaya", "Menala"}
	BalinesePancawaraNames = []string{"Umanis", "Paing", "Pon", "Wage", "Keliwon"}
	BalineseSadwaraNames   = []string{"Tungleh", "Aryang", "Urukung", "Paniron", "Was", "Maulu"}
	BalineseSaptawaraNames = []string{"Redite", "Coma", "Anggara", "Buda", "Wraspati", "Sukra", "Saniscara"}
	BalineseAsatawaraNames = []string{"Sri", "Indra", "Guru", "Yama", "Ludra", "Brahma", "Kala", "Uma"}
	BalineseSangawaraNames = []string{"Dangu", "Jangur", "Gigis", "Nohan", "Ogan", "Erangan", "Urungan", "Tulus", "Dadi"}
	BalineseDasawaraNames  = []string{"Pandita", "Pati", "Suka", "Duka", "Sri", "Manuh", "Manusa", "Raja", "Dewa", "Raksasa"}
	BalineseWeekNames      = []string{
		"Sinta", "Landep", "Ukir", "Kulantir", "Taulu",
		"Gumbreg", "Wariga", "Warigadian", "Jukungwangi", "Sungsang",
		"Dunggulan", "Kuningan", "Langkir", "Medangsia", "Pujut",
		"Pahang", "Krulut", "Merakih", "Tambir", "Medangkungan",
		"Matal", "Uye", "Menail", "Parangbakat", "Bala",
		"Ugu", "Wayang", "Kelawu", "Dukut", "Watugunung",
	}
)

func NewBalinese(luang bool, dwiwara, triwara, caturwara, pancawara, sadwara, saptawara, asatawara, sangawara, dasawara int) Balinese {
	return Balinese{
		Luang: luang, Dwiwara: dwiwara, Triwara: triwara, Caturwara: caturwara,
		Pancawara: pancawara, Sadwara: sadwara, Saptawara: saptawara,
		Asatawara: asatawara, Sangawara: sangawara, Dasawara: dasawara,
	}
}

func BalineseDayFromFixed(rd int64) int {
	return int(Mod(rd-BalineseEpoch, 210))
}

func BalineseDasawaraFromFixed(rd int64) int {
	pancawara := BalinesePancawaraFromFixed(rd)
	saptawara := BalineseSaptawaraFromFixed(rd)
	pancawaraOffsets := [...]int{5, 9, 7, 4, 8}
	saptawaraOffsets := [...]int{5, 4, 3, 7, 8, 6, 9}
	return int(Mod(int64(pancawaraOffsets[pancawara-1]+saptawaraOffsets[saptawara-1]+1), 10))
}

func BalineseLuangFromFixed(rd int64) bool {
	return BalineseDasawaraFromFixed(rd)%2 == 0
}

func BalineseDwiwaraFromFixed(rd int64) int {
	return int(Mod(int64(BalineseDasawaraFromFixed(rd)+1), 2)) + 1
}

func BalineseTriwaraFromFixed(rd int64) int {
	return BalineseDayFromFixed(rd)%3 + 1
}

func BalineseCaturwaraFromFixed(rd int64) int {
	return int(Mod(int64(BalineseAsatawaraFromFixed(rd)-1), 4)) + 1
}

func BalinesePancawaraFromFixed(rd int64) int {
	return int(Mod(int64(BalineseDayFromFixed(rd)+1), 5)) + 1
}

func BalineseSadwaraFromFixed(rd int64) int {
	return BalineseDayFromFixed(rd)%6 + 1
}

func BalineseSaptawaraFromFixed(rd int64) int {
	return BalineseDayFromFixed(rd)%7 + 1
}

func BalineseAsatawaraFromFixed(rd int64) int {
	day := BalineseDayFromFixed(rd)
	x := 4 + int(Mod(int64(day-70), 210))
	if x < 6 {
		x = 6
	}
	return int(Mod(int64(x), 8)) + 1
}

func BalineseSangawaraFromFixed(rd int64) int {
	x := BalineseDayFromFixed(rd) - 3
	if x < 0 {
		x = 0
	}
	return x%9 + 1
}

func BalineseWeekFromFixed(rd int64) int {
	return BalineseDayFromFixed(rd)/7 + 1
}

func BalineseOnOrBefore(date Balinese, rd int64) int64 {
	a5 := date.Pancawara - 1
	a6 := date.Sadwara - 1
	b7 := date.Saptawara - 1
	b35 := int(Mod(int64(a5+14+15*(b7-a5)), 35))
	days := a6 + 36*(b35-a6)
	capDelta := BalineseDayFromFixed(0)
	return rd - Mod(rd+int64(capDelta-days), 210)
}

func BalineseDay(date Balinese) int {
	return int(BalineseOnOrBefore(date, BalineseEpoch+209) - BalineseEpoch + 1)
}

func BalineseWeek(date Balinese) int {
	return (BalineseDay(date)-1)/7 + 1
}

func BalineseFromFixed(rd int64) Balinese {
	return NewBalinese(
		BalineseLuangFromFixed(rd),
		BalineseDwiwaraFromFixed(rd),
		BalineseTriwaraFromFixed(rd),
		BalineseCaturwaraFromFixed(rd),
		BalinesePancawaraFromFixed(rd),
		BalineseSadwaraFromFixed(rd),
		BalineseSaptawaraFromFixed(rd),
		BalineseAsatawaraFromFixed(rd),
		BalineseSangawaraFromFixed(rd),
		BalineseDasawaraFromFixed(rd),
	)
}

func PositionsInInterval(n, cycle, capDelta int, start, end int64) []int64 {
	if cycle <= 0 {
		return nil
	}
	var result []int64
	pos := start
	for {
		pos += Mod(int64(n)-pos-int64(capDelta)-1, int64(cycle))
		if pos > end {
			return result
		}
		result = append(result, pos)
		pos++
	}
}

func KajengKeliwonInGregorian(year int64) []int64 {
	start := GregorianToFixed(year, January, 1)
	end := GregorianToFixed(year, December, 31)
	return PositionsInInterval(9, 15, BalineseDayFromFixed(0), start, end)
}

func TumpekInGregorian(year int64) []int64 {
	start := GregorianToFixed(year, January, 1)
	end := GregorianToFixed(year, December, 31)
	return PositionsInInterval(14, 35, BalineseDayFromFixed(0), start, end)
}

func (b Balinese) Format() string {
	dwiwara := indexedName(b.Dwiwara, BalineseDwiwaraNames)
	triwara := indexedName(b.Triwara, BalineseTriwaraNames)
	caturwara := indexedName(b.Caturwara, BalineseCaturwaraNames)
	pancawara := indexedName(b.Pancawara, BalinesePancawaraNames)
	sadwara := indexedName(b.Sadwara, BalineseSadwaraNames)
	saptawara := indexedName(b.Saptawara, BalineseSaptawaraNames)
	asatawara := indexedName(b.Asatawara, BalineseAsatawaraNames)
	sangawara := indexedName(b.Sangawara, BalineseSangawaraNames)
	dasawara := indexedName(b.Dasawara, BalineseDasawaraNames)
	week := indexedName(BalineseWeek(b), BalineseWeekNames)
	prefix := ""
	if b.Luang {
		prefix = "Luang "
	}
	return fmt.Sprintf("%s%s %s %s %s %s %s %s %s %s (%s)", prefix, dwiwara, triwara, caturwara, pancawara, sadwara, saptawara, asatawara, sangawara, dasawara, week)
}

func (b Balinese) Equals(other Balinese) bool {
	return b == other
}

func indexedName(index int, names []string) string {
	if len(names) == 0 {
		return ""
	}
	index = int(Mod(int64(index-1), int64(len(names))))
	return names[index]
}

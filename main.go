package main

import (
	"fmt"
	"os"
	"sort"
	"time"

	"calendrica/reingolddershowitz"
)

func main() {
	cm := reingolddershowitz.NewCalendarManager()

	fmt.Println("=== Reingold & Dershowitz Calendrical Calculations ===")

	rd := cm.CurrentRD
	if len(os.Args) > 2 {
		fmt.Fprintf(os.Stderr, "usage: %s [YYYY-MM-DD]\n", os.Args[0])
		os.Exit(2)
	}
	if len(os.Args) == 2 {
		date, err := time.Parse("2006-01-02", os.Args[1])
		if err != nil {
			fmt.Fprintf(os.Stderr, "invalid Gregorian date %q: expected YYYY-MM-DD: %v\n", os.Args[1], err)
			os.Exit(2)
		}
		rd = reingolddershowitz.NewGregorian(
			int64(date.Year()), int(date.Month()), date.Day(),
		).ToFixed()
	}

	gregorian := reingolddershowitz.GregorianFromFixed(rd)
	fmt.Printf("\nConversions (%s, R.D. %d) :\n", gregorian.Format(), rd)

	calendarTypes := append([]reingolddershowitz.CalendarType(nil), reingolddershowitz.Order...)
	inOrder := make(map[reingolddershowitz.CalendarType]bool, len(calendarTypes))
	for _, calType := range calendarTypes {
		inOrder[calType] = true
	}
	var additionalTypes []reingolddershowitz.CalendarType
	for calType := range reingolddershowitz.CalendarNames {
		if !inOrder[calType] {
			additionalTypes = append(additionalTypes, calType)
		}
	}
	sort.Slice(additionalTypes, func(i, j int) bool { return additionalTypes[i] < additionalTypes[j] })
	calendarTypes = append(calendarTypes, additionalTypes...)

	for _, calType := range calendarTypes {
		if calType == reingolddershowitz.GREGORIAN {
			continue
		}
		name := reingolddershowitz.CalendarNames[calType]
		date, err := formatCalendarDate(rd, calType)
		if err != nil {
			fmt.Fprintf(os.Stderr, "conversion %s failed: %v\n", name, err)
			os.Exit(1)
		}
		fmt.Printf(" - %-28s %s\n", name+":", date)
	}
}

func formatCalendarDate(rd int64, calType reingolddershowitz.CalendarType) (string, error) {
	switch calType {
	case reingolddershowitz.RD:
		return fmt.Sprintf("%d", rd), nil
	case reingolddershowitz.ASTRO:
		return fmt.Sprintf("%.1f", reingolddershowitz.JDFromFixed(rd)), nil
	case reingolddershowitz.ISO:
		return reingolddershowitz.ISOFromFixed(rd).Format(), nil
	case reingolddershowitz.JULIAN:
		return reingolddershowitz.JulianFromFixed(rd).Format(), nil
	case reingolddershowitz.COPTIC:
		return reingolddershowitz.CopticFromFixed(rd).Format(), nil
	case reingolddershowitz.ETHIOPIC:
		return reingolddershowitz.EthiopicFromFixed(rd).Format(), nil
	case reingolddershowitz.BAHAI:
		return reingolddershowitz.BahaiFromFixed(rd).Format(), nil
	case reingolddershowitz.ISLAMIC:
		return reingolddershowitz.IslamicFromFixed(rd).Format(), nil
	case reingolddershowitz.HEBREW:
		return reingolddershowitz.HebrewFromFixed(rd).Format(), nil
	case reingolddershowitz.PERSIAN:
		return reingolddershowitz.PersianFromFixed(rd).Format(), nil
	case reingolddershowitz.MAYAN_LONG_COUNT:
		return reingolddershowitz.MayanLongCountFromFixed(rd).Format(), nil
	case reingolddershowitz.MAYAN_HAAB:
		return reingolddershowitz.MayanHaabFromFixed(rd).Format(), nil
	case reingolddershowitz.MAYAN_TZOLKIN:
		return reingolddershowitz.MayanTzolkinFromFixed(rd).Format(), nil
	case reingolddershowitz.OLD_HINDU_SOLAR:
		return reingolddershowitz.OldHinduSolarFromFixed(rd).Format(), nil
	case reingolddershowitz.OLD_HINDU_LUNAR:
		return reingolddershowitz.OldHinduLunarFromFixed(rd).Format(), nil
	case reingolddershowitz.FRENCH:
		return reingolddershowitz.FrenchFromFixed(rd).Format(), nil
	case reingolddershowitz.MODIFIED_FRENCH:
		return reingolddershowitz.ModifiedFrenchFromFixed(rd).Format(), nil
	case reingolddershowitz.CHINESE:
		return reingolddershowitz.ChineseFromFixed(rd).Format(), nil
	case reingolddershowitz.HINDU_SOLAR:
		return reingolddershowitz.HinduSolarFromFixed(rd).Format(), nil
	case reingolddershowitz.HINDU_LUNAR:
		return reingolddershowitz.HinduLunarFromFixed(rd).Format(), nil
	case reingolddershowitz.MJD:
		return fmt.Sprintf("%.1f", reingolddershowitz.MJDFromFixed(rd)), nil
	case reingolddershowitz.ARITH_PERSIAN:
		return reingolddershowitz.ArithmeticPersianFromFixed(rd).Format(), nil
	case reingolddershowitz.FUTURE_BAHAI:
		return reingolddershowitz.FutureBahaiFromFixedDate(rd).Format(), nil
	case reingolddershowitz.DOY:
		return fmt.Sprintf("%d", reingolddershowitz.GregorianFromFixed(rd).DayNumber()), nil
	case reingolddershowitz.PAWUKON:
		return reingolddershowitz.BalineseFromFixed(rd).Format(), nil
	case reingolddershowitz.ASTRO_ISLAMIC:
		date, err := reingolddershowitz.ObservationalIslamicFromFixedChecked(rd)
		if err != nil {
			return "", err
		}
		return date.Format(), nil
	case reingolddershowitz.ARMENIAN:
		return reingolddershowitz.ArmenianFromFixed(rd).Format(), nil
	case reingolddershowitz.ROMAN:
		return reingolddershowitz.RomanFromFixed(rd).Format(), nil
	case reingolddershowitz.MAYAN_ROUND:
		haab := reingolddershowitz.MayanHaabFromFixed(rd)
		tzolkin := reingolddershowitz.MayanTzolkinFromFixed(rd)
		return fmt.Sprintf("%s / %s", haab.Format(), tzolkin.Format()), nil
	case reingolddershowitz.EGYPTIAN:
		return reingolddershowitz.EgyptianFromFixed(rd).Format(), nil
	default:
		return "", fmt.Errorf("no conversion is defined for calendar type %d", calType)
	}
}

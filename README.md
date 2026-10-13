# Calendrica

Calendrica is a Go implementation of calendar conversion algorithms inspired by
Edward M. Reingold and Nachum Dershowitz's work *Calendrical Calculations* and
their Calendrica programs. It converts dates through a shared integer day
number, making it possible to convert between any of the supported calendars.

## Calendars and date representations

The package covers these calendars and related day-number representations:

| Calendar / representation | Notes |
| --- | --- |
| Rata Die (R.D., fixed date) | Integer day count used as the common conversion basis. |
| Gregorian | Proleptic Gregorian calendar. |
| ISO week date | ISO year, week, and weekday. |
| Julian | Julian calendar, also called Old Style. |
| Armenian | Armenian calendar. |
| Bahá'í (Western) | Fixed-March-21 Western Bahá'í calendar. |
| Bahá'í (future) | Astronomical Bahá'í calendar, used in the implementation from 2015 onward. |
| Chinese | Traditional lunisolar calendar, including cycle, year, and leap-month information. |
| Coptic | Coptic calendar. |
| Egyptian | Ancient Egyptian calendar. |
| Ethiopic | Ethiopic calendar. |
| French Revolutionary | French Republican calendar. |
| Modified French Revolutionary | Modified French Republican calendar. |
| Hebrew | Hebrew lunisolar calendar. |
| Hindu lunar | Hindu lunar calendar, including leap-month and leap-day information. |
| Old Hindu lunar | Arya (old) Hindu lunar calendar. |
| Hindu solar | Hindu solar calendar. |
| Old Hindu solar | Arya (old) Hindu solar calendar. |
| Islamic (arithmetic) | Arithmetic/civil Islamic calendar. |
| Islamic (observational) | Crescent-visibility-based Islamic calendar, calculated for Cairo. |
| Persian (astronomical) | Astronomical Persian calendar. |
| Persian (arithmetic) | Arithmetic Persian calendar. |
| Roman | Roman calendar dates expressed using Kalends, Nones, and Ides. |
| Mayan Long Count | Mayan Long Count. |
| Mayan Haab | 365-day Mayan cycle. |
| Mayan Tzolkin | 260-day Mayan cycle. |
| Mayan Calendar Round | Combined Haab and Tzolkin cycles. |
| Balinese Pawukon | Balinese 210-day Pawukon cycle. |
| Julian Day (JD) | Julian day number representation. |
| Modified Julian Day (MJD) | Modified Julian day number representation. |
| Day of year | Ordinal day within the Gregorian year. |

The astronomical Islamic conversion depends on crescent-visibility calculations
and a location (Cairo in the provided conversion functions). Astronomical
calendar calculations are models; they should not be treated as observations
or as a substitute for locally authoritative religious calendars.

## Install and run

The module path is `calendrica`. To print conversions for the current date:

```sh
go run .
```

Or supply a Gregorian date in `YYYY-MM-DD` format:

```sh
go run . 2024-03-20
```

## Converting dates

Calendar dates convert to and from an R.D. day number. Convert the source date
to R.D. with `ToFixed()`, then pass that number to the target calendar's
`FromFixed` function. For example, convert a Gregorian date to Hebrew and back:

```go
package main

import (
	"fmt"

	cal "calendrica/reingolddershowitz"
)

func main() {
	rd := cal.NewGregorian(2024, cal.March, 20).ToFixed()
	hebrew := cal.HebrewFromFixed(rd)
	backToGregorian := cal.GregorianFromFixed(hebrew.ToFixed())

	fmt.Println("Hebrew:", hebrew.Format())
	fmt.Println("Gregorian:", backToGregorian.Format())
}
```

To convert between any two supported calendars, use the same two-step path:

```go
rd := sourceDate.ToFixed()
targetDate := cal.TargetCalendarFromFixed(rd)
```

### Convert one Gregorian date to all supported representations

This example uses March 20, 2024 as its source and shows the conversion entry
point for every calendar and representation listed above. The date types' `Format`
methods provide readable calendar dates; R.D., JD, MJD, and day-of-year values
are numeric.

```go
package main

import (
	"fmt"
	"log"

	cal "calendrica/reingolddershowitz"
)

func main() {
	rd := cal.NewGregorian(2024, cal.March, 20).ToFixed()
	gregorian := cal.GregorianFromFixed(rd)

	observationalIslamic, err := cal.ObservationalIslamicFromFixedChecked(rd)
	if err != nil {
		log.Fatal(err)
	}

	haab := cal.MayanHaabFromFixed(rd)
	tzolkin := cal.MayanTzolkinFromFixed(rd)
	dates := []struct {
		name  string
		value string
	}{
		{"Rata Die", fmt.Sprintf("%d", rd)},
		{"Gregorian", gregorian.Format()},
		{"ISO week date", cal.ISOFromFixed(rd).Format()},
		{"Julian", cal.JulianFromFixed(rd).Format()},
		{"Armenian", cal.ArmenianFromFixed(rd).Format()},
		{"Bahá'í (Western)", cal.BahaiFromFixed(rd).Format()},
		{"Bahá'í (future)", cal.FutureBahaiFromFixedDate(rd).Format()},
		{"Chinese", cal.ChineseFromFixed(rd).Format()},
		{"Coptic", cal.CopticFromFixed(rd).Format()},
		{"Egyptian", cal.EgyptianFromFixed(rd).Format()},
		{"Ethiopic", cal.EthiopicFromFixed(rd).Format()},
		{"French Revolutionary", cal.FrenchFromFixed(rd).Format()},
		{"Modified French Revolutionary", cal.ModifiedFrenchFromFixed(rd).Format()},
		{"Hebrew", cal.HebrewFromFixed(rd).Format()},
		{"Hindu lunar", cal.HinduLunarFromFixed(rd).Format()},
		{"Old Hindu lunar", cal.OldHinduLunarFromFixed(rd).Format()},
		{"Hindu solar", cal.HinduSolarFromFixed(rd).Format()},
		{"Old Hindu solar", cal.OldHinduSolarFromFixed(rd).Format()},
		{"Islamic (arithmetic)", cal.IslamicFromFixed(rd).Format()},
		{"Islamic (observational)", observationalIslamic.Format()},
		{"Persian (astronomical)", cal.PersianFromFixed(rd).Format()},
		{"Persian (arithmetic)", cal.ArithmeticPersianFromFixed(rd).Format()},
		{"Roman", cal.RomanFromFixed(rd).Format()},
		{"Mayan Long Count", cal.MayanLongCountFromFixed(rd).Format()},
		{"Mayan Haab", haab.Format()},
		{"Mayan Tzolkin", tzolkin.Format()},
		{"Mayan Calendar Round", fmt.Sprintf("%s / %s", haab.Format(), tzolkin.Format())},
		{"Balinese Pawukon", cal.BalineseFromFixed(rd).Format()},
		{"Julian Day (JD)", fmt.Sprintf("%.1f", cal.JDFromFixed(rd))},
		{"Modified Julian Day (MJD)", fmt.Sprintf("%.1f", cal.MJDFromFixed(rd))},
		{"Gregorian day of year", fmt.Sprintf("%d", gregorian.DayNumber())},
	}

	for _, date := range dates {
		fmt.Printf("%-32s %s\n", date.name, date.value)
	}
}
```

The main `FromFixed` methods return a calendar date directly. For the
observational Islamic calendar, the `...Checked` conversion variant returns an
error if the astronomical calculation cannot be completed; prefer it when the
caller needs to handle such failures.

## Package contents

Calendar types, conversions, astronomy helpers, and related utilities are in
`reingolddershowitz`. The root command is a small conversion-listing program.

Run the tests with:

```sh
go test ./...
```

## Credits and licensing

The calendar algorithms and the Calendrica body of work are due to **Edward M.
Reingold and Nachum Dershowitz**. This Go project is an implementation inspired
by their work, including *Calendrical Calculations* (Cambridge University
Press). Please consult the authors' Calendrica site for information about the
original programs and their terms of use:

- [Calendrica / calendarists.com](https://www.calendarists.com/)
- [Calendrical Calculations, 4th edition](https://www.cambridge.org/core/books/calendrical-calculations/)

This repository does not include a project-level license file. Do not assume
that the code or upstream Calendrica material is available under an
unrestricted license; check the applicable rights and terms before
redistributing or using it commercially.

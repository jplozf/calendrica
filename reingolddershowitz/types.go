package reingolddershowitz

// CalendarType identifie un type de calendrier
type CalendarType int

const (
	NONE CalendarType = iota - 1
	RD
	ASTRO
	GREGORIAN
	JULIAN
	COPTIC
	ETHIOPIC
	ISO
	BAHAI
	ISLAMIC
	HEBREW
	PERSIAN
	MAYAN_LONG_COUNT
	MAYAN_HAAB
	MAYAN_TZOLKIN
	OLD_HINDU_SOLAR
	OLD_HINDU_LUNAR
	FRENCH
	MODIFIED_FRENCH
	CHINESE
	HINDU_SOLAR
	HINDU_LUNAR
	MJD
	ARITH_PERSIAN
	FUTURE_BAHAI
	DOY
	PAWUKON
	ASTRO_ISLAMIC
	ARMENIAN
	ROMAN
	MAYAN_ROUND
	EGYPTIAN
)

// Names retourne le nom lisible de chaque calendrier
var CalendarNames = map[CalendarType]string{
	RD:               "R.D. (Fixed Date)",
	ASTRO:            "JD (Julian Day)",
	GREGORIAN:        "Gregorian",
	JULIAN:           "Julian (Old Style)",
	COPTIC:           "Coptic",
	ETHIOPIC:         "Ethiopic",
	ISO:              "ISO",
	BAHAI:            "Bahai (Western)",
	ISLAMIC:          "Islamic (Arithmetic)",
	HEBREW:           "Hebrew",
	PERSIAN:          "Persian (Astronomical)",
	MAYAN_LONG_COUNT: "Mayan Long Count",
	MAYAN_HAAB:       "Mayan Haab",
	MAYAN_TZOLKIN:    "Mayan Tzolkin",
	OLD_HINDU_SOLAR:  "Hindu Solar (Old)",
	OLD_HINDU_LUNAR:  "Hindu Lunar (Old)",
	FRENCH:           "French",
	MODIFIED_FRENCH:  "French (Modified)",
	CHINESE:          "Chinese",
	HINDU_SOLAR:      "Hindu Solar",
	HINDU_LUNAR:      "Hindu Lunar",
	MJD:              "MJD",
	ARITH_PERSIAN:    "Persian (Arithmetic)",
	FUTURE_BAHAI:     "Bahai (Future)",
	DOY:              "Day of year",
	PAWUKON:          "Balinese Pawukon",
	ASTRO_ISLAMIC:    "Islamic (Observational)",
	ARMENIAN:         "Armenian",
	ROMAN:            "Roman",
	MAYAN_ROUND:      "Mayan Calendar Round",
	EGYPTIAN:         "Egyptian",
}

// Order définit l'ordre d'affichage dans un menu ou CLI
var Order = []CalendarType{
	RD, GREGORIAN, ISO, ARMENIAN, BAHAI, FUTURE_BAHAI, CHINESE, COPTIC, EGYPTIAN,
	ETHIOPIC, FRENCH, MODIFIED_FRENCH, HEBREW, HINDU_LUNAR, OLD_HINDU_LUNAR,
	HINDU_SOLAR, OLD_HINDU_SOLAR, ISLAMIC, ASTRO_ISLAMIC, JULIAN, ASTRO, MJD,
	MAYAN_LONG_COUNT, PERSIAN, ARITH_PERSIAN,
}

// Date représente la structure générique d'une date
type Date struct {
	Year  int
	Month int
	Day   int
}

// Location représente une géolocalisation pour les calculs astronomiques
type Location struct {
	Name      string
	Latitude  float64
	Longitude float64
	Elevation float64
}

var Jerusalem = Location{
	Name:      "Jerusalem",
	Latitude:  31.778,
	Longitude: 35.235,
	Elevation: 780.0,
}

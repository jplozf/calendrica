package reingolddershowitz

import (
	"fmt"
	"math"
)

var Cairo = NewLocation("Cairo, Egypt", 30.1, 31.3, Mt(200), 2)

type ObservationalIslamic struct {
	Islamic
}

func NewObservationalIslamic(year int64, month, day int) ObservationalIslamic {
	return ObservationalIslamic{Islamic: NewIslamic(year, month, day)}
}

func ObservationalIslamicToFixed(year int64, month, day int) int64 {
	rd, err := ObservationalIslamicToFixedChecked(year, month, day)
	if err != nil {
		panic(err)
	}
	return rd
}

func ObservationalIslamicToFixedChecked(year int64, month, day int) (int64, error) {
	midmonth := float64(IslamicEpoch) +
		((float64(year-1)*12)+float64(month)-0.5)*MeanSynodicMonth
	phasis, err := PhasisOnOrBeforeChecked(int64(math.Floor(midmonth)), Cairo)
	if err != nil {
		return 0, err
	}
	return phasis + int64(day-1), nil
}

func (i ObservationalIslamic) ToFixed() int64 {
	return ObservationalIslamicToFixed(i.Year, i.Month, i.Day)
}

func ObservationalIslamicFromFixed(rd int64) ObservationalIslamic {
	date, err := ObservationalIslamicFromFixedChecked(rd)
	if err != nil {
		panic(err)
	}
	return date
}

func ObservationalIslamicFromFixedChecked(rd int64) (ObservationalIslamic, error) {
	crescent, err := PhasisOnOrBeforeChecked(rd, Cairo)
	if err != nil {
		return ObservationalIslamic{}, err
	}
	elapsedMonths := int64(math.Floor(float64(crescent-IslamicEpoch)/MeanSynodicMonth + 0.5))
	year := Quotient(elapsedMonths, 12) + 1
	month := int(Mod(elapsedMonths, 12)) + 1
	day := int(rd - crescent + 1)
	return NewObservationalIslamic(year, month, day), nil
}

func (i *ObservationalIslamic) FromFixed(rd int64) {
	date, err := ObservationalIslamicFromFixedChecked(rd)
	if err != nil {
		panic(err)
	}
	*i = date
}

func (i ObservationalIslamic) Format() string {
	weekday := IslamicDayOfWeekNames[DayOfWeekFromFixed(i.ToFixed())]
	month := ""
	if i.Month >= 1 && i.Month <= len(IslamicMonthNames) {
		month = IslamicMonthNames[i.Month-1]
	}
	return fmt.Sprintf("%s, %d %s %d A.H.", weekday, i.Day, month, i.Year)
}

func (i ObservationalIslamic) Equals(other ObservationalIslamic) bool {
	return i.StandardDate.Equals(other.StandardDate)
}

package reingolddershowitz

import "testing"

func TestHinduCalendarFixedDateConversions(t *testing.T) {
	const rd = int64(-214193)
	solar := HinduSolarFromFixed(rd)
	if want := (HinduSolar{StandardDate: NewStandardDate(-664, 5, 19)}); !solar.Equals(want) {
		t.Fatalf("HinduSolarFromFixed(%d) = %+v, want %+v", rd, solar, want)
	}
	if got := solar.ToFixed(); got != rd {
		t.Fatalf("Hindu solar round trip = %d, want %d", got, rd)
	}

	lunar := HinduLunarFromFixed(rd)
	if want := NewHinduLunar(-529, 6, false, 11, false); !lunar.Equals(want) {
		t.Fatalf("HinduLunarFromFixed(%d) = %+v, want %+v", rd, lunar, want)
	}
	if got := lunar.ToFixed(); got != rd {
		t.Fatalf("Hindu lunar round trip = %d, want %d", got, rd)
	}
}

func TestChineseFixedDateConversionPreservesLeapMonth(t *testing.T) {
	const rd = int64(-214193)
	date := ChineseFromFixed(rd)
	if want := NewChinese(35, 11, 6, false, 12); !date.Equals(want) {
		t.Fatalf("ChineseFromFixed(%d) = %+v, want %+v", rd, date, want)
	}
	if got := date.ToFixed(); got != rd {
		t.Fatalf("Chinese round trip = %d, want %d", got, rd)
	}

	const leapRD = int64(601716)
	leapDate := ChineseFromFixed(leapRD)
	if want := NewChinese(72, 25, 4, true, 20); !leapDate.Equals(want) {
		t.Fatalf("ChineseFromFixed(%d) = %+v, want %+v", leapRD, leapDate, want)
	}
	if got := leapDate.ToFixed(); got != leapRD {
		t.Fatalf("Chinese leap-month round trip = %d, want %d", got, leapRD)
	}
}

func TestHinduLunarFixedDateConversionPreservesLeapDay(t *testing.T) {
	const rd = int64(569477)
	date := HinduLunarFromFixed(rd)
	if want := NewHinduLunar(1616, 11, false, 28, true); !date.Equals(want) {
		t.Fatalf("HinduLunarFromFixed(%d) = %+v, want %+v", rd, date, want)
	}
	if got := date.ToFixed(); got != rd {
		t.Fatalf("Hindu lunar leap-day round trip = %d, want %d", got, rd)
	}
}

func TestHinduLunarOrderingDistinguishesLeapDays(t *testing.T) {
	ordinary := NewHinduLunar(2000, 1, false, 14, false)
	leapDay := NewHinduLunar(2000, 1, false, 14, true)
	if HinduLunarOnOrBefore(leapDay, ordinary) || !HinduLunarOnOrBefore(ordinary, leapDay) {
		t.Fatal("leap-day ordering should place the duplicated day after the ordinary day")
	}
}

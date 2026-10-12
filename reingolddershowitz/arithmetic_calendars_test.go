package reingolddershowitz

import "testing"

func TestCopticAndEthiopicEpochsAndRoundTrips(t *testing.T) {
	if got := CopticToFixed(1, 1, 1); got != 103605 {
		t.Fatalf("Coptic year 1 epoch = %d, want 103605", got)
	}
	if got := EthiopicToFixed(1, 1, 1); got != 2796 {
		t.Fatalf("Ethiopic year 1 epoch = %d, want 2796", got)
	}
	for _, rd := range []int64{-100000, 1, 103605, 738000} {
		coptic := CopticFromFixed(rd)
		if got := coptic.ToFixed(); got != rd {
			t.Errorf("Coptic round trip at %d returned %d (%+v)", rd, got, coptic)
		}
		ethiopic := EthiopicFromFixed(rd)
		if got := ethiopic.ToFixed(); got != rd {
			t.Errorf("Ethiopic round trip at %d returned %d (%+v)", rd, got, ethiopic)
		}
	}
}

func TestISOWeekDateConversions(t *testing.T) {
	jan4 := GregorianToFixed(2021, January, 4)
	want := NewISO(2021, 1, 1)
	if got := want.ToFixed(); got != jan4 {
		t.Fatalf("ISO 2021-W01-1 = %d, want %d", got, jan4)
	}
	for _, rd := range []int64{1, jan4, GregorianToFixed(2020, December, 31), 738000} {
		if got := ISOFromFixed(rd).ToFixed(); got != rd {
			t.Errorf("ISO round trip at %d returned %d (%+v)", rd, got, ISOFromFixed(rd))
		}
	}
}

func TestIslamicArithmeticConversions(t *testing.T) {
	if got := IslamicToFixed(1, 1, 1); got != 227015 {
		t.Fatalf("Islamic year 1 epoch = %d, want 227015", got)
	}
	for _, rd := range []int64{-100000, 1, 227015, 738000} {
		if got := IslamicFromFixed(rd).ToFixed(); got != rd {
			t.Errorf("Islamic round trip at %d returned %d (%+v)", rd, got, IslamicFromFixed(rd))
		}
	}
}

func TestArithmeticPersianConversions(t *testing.T) {
	if got := ArithmeticPersianToFixed(1, 1, 1); got != ArithmeticPersianEpoch {
		t.Fatalf("Persian year 1 epoch = %d, want %d", got, ArithmeticPersianEpoch)
	}
	for _, year := range []int64{-2821, -1, 1, 475, 2821} {
		for _, month := range []int{1, 6, 7, 8, 12} {
			rd := ArithmeticPersianToFixed(year, month, 1)
			date := ArithmeticPersianFromFixed(rd)
			if date.Year != year || date.Month != month || date.Day != 1 {
				t.Errorf("Persian round trip for %d/%d/1 = %+v", year, month, date)
			}
		}
	}
}

func TestMayanCycleConversions(t *testing.T) {
	count := NewMayanLongCount(13, 0, 0, 0, 0)
	if got := count.ToFixed(); got != MayanLongCountEpoch+13*144000 {
		t.Fatalf("Mayan long count conversion = %d", got)
	}
	for _, rd := range []int64{-1200000, MayanLongCountEpoch, 1, 738000} {
		if got := MayanLongCountFromFixed(rd).ToFixed(); got != rd {
			t.Errorf("Mayan Long Count round trip at %d returned %d", rd, got)
		}
		haab := MayanHaabFromFixed(rd)
		if MayanHaabOnOrBefore(haab, rd) != rd {
			t.Errorf("Haab date %+v was not found on or before %d", haab, rd)
		}
		tzolkin := MayanTzolkinFromFixed(rd)
		if MayanTzolkinOnOrBefore(tzolkin, rd) != rd {
			t.Errorf("Tzolkin date %+v was not found on or before %d", tzolkin, rd)
		}
		round, err := MayanCalendarRoundOnOrBefore(haab, tzolkin, rd)
		if err != nil || round != rd {
			t.Errorf("Mayan calendar round at %d = %d, %v", rd, round, err)
		}
	}
	if _, err := MayanCalendarRoundOnOrBefore(NewMayanHaab(1, 0), NewMayanTzolkin(1, 1), 0); err == nil {
		t.Error("expected incompatible Mayan cycle dates to return an error")
	}
}

package reingolddershowitz

import "testing"

func TestOldHinduSolarConversion(t *testing.T) {
	if got, want := JulianToFixed(BCE(3102), February, 18), OldHinduSolarEpoch; got != want {
		t.Fatalf("Julian-derived Hindu epoch = %d, want %d", got, want)
	}
	if got := OldHinduSolarToFixed(0, 1, 1); got != OldHinduSolarEpoch {
		t.Fatalf("epoch date = %d, want %d", got, OldHinduSolarEpoch)
	}
	for _, rd := range []int64{OldHinduSolarEpoch, 1, GregorianToFixed(2024, July, 1)} {
		if got := OldHinduSolarFromFixed(rd).ToFixed(); got != rd {
			t.Errorf("solar round trip at %d = %d", rd, got)
		}
	}
}

func TestOldHinduLunarConversion(t *testing.T) {
	for _, rd := range []int64{1, GregorianToFixed(2024, July, 1)} {
		if got := OldHinduLunarFromFixed(rd).ToFixed(); got != rd {
			t.Errorf("lunar round trip at %d = %d", rd, got)
		}
	}
}

func TestRomanConversion(t *testing.T) {
	idesOfMarch := JulianToFixed(-43, March, 15)
	roman := RomanFromFixed(idesOfMarch)
	if roman.Event != RomanIdes || roman.Count != 1 || roman.Month != March || roman.Year != -43 {
		t.Fatalf("ides of March converted to %+v", roman)
	}
	if got := roman.ToFixed(); got != idesOfMarch {
		t.Errorf("Roman round trip = %d, want %d", got, idesOfMarch)
	}
}

func TestEcclesiasticalDates(t *testing.T) {
	if got, want := Easter(2024), GregorianToFixed(2024, March, 31); got != want {
		t.Errorf("Easter(2024) = %d, want %d", got, want)
	}
	if got, want := OrthodoxEaster(2024), GregorianToFixed(2024, May, 5); got != want {
		t.Errorf("OrthodoxEaster(2024) = %d, want %d", got, want)
	}
	if got, want := AltOrthodoxEaster(2024), GregorianToFixed(2024, March, 31); got != want {
		t.Errorf("alternate Orthodox Easter = %d, want %d", got, want)
	}
	if got, want := Pentecost(2024), GregorianToFixed(2024, May, 19); got != want {
		t.Errorf("Pentecost(2024) = %d, want %d", got, want)
	}
}

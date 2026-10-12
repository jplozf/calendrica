package reingolddershowitz

import "testing"

func TestHebrewEpochAndRoundTrip(t *testing.T) {
	if got := HebrewToFixed(1, HebrewTishri, 1); got != HebrewEpoch {
		t.Fatalf("Hebrew epoch: got %d, want %d", got, HebrewEpoch)
	}
	if got := HebrewEpoch; got != JulianToFixed(BCE(3761), October, 7) {
		t.Fatalf("Hebrew epoch does not match Julian date: got %d", got)
	}
	for rd := HebrewEpoch - 800; rd <= HebrewEpoch+8000; rd++ {
		date := HebrewFromFixed(rd)
		if got := date.ToFixed(); got != rd {
			t.Fatalf("Hebrew round trip at %d returned %d (%+v)", rd, got, date)
		}
	}
	rd := GregorianToFixed(2024, April, 8)
	if got, want := HebrewFromFixed(rd), NewHebrew(5784, 13, 29); !got.Equals(want) {
		t.Fatalf("HebrewFromFixed(%d) = %+v, want %+v", rd, got, want)
	}
}

func TestBahaiEpochAndRoundTrip(t *testing.T) {
	if got := BahaiToFixed(1, 1, 1, 1, 1); got != BahaiEpoch {
		t.Fatalf("Bahá'í epoch: got %d, want %d", got, BahaiEpoch)
	}
	if got := BahaiEpoch; got != GregorianToFixed(1844, March, 21) {
		t.Fatalf("Bahá'í epoch does not match Gregorian date: got %d", got)
	}
	for rd := BahaiEpoch - 1; rd <= BahaiEpoch+15000; rd++ {
		date := BahaiFromFixed(rd)
		if got := date.ToFixed(); got != rd {
			t.Fatalf("Bahá'í round trip at %d returned %d (%+v)", rd, got, date)
		}
	}
}

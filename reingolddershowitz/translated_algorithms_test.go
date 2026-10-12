package reingolddershowitz

import "testing"

func TestBalineseCycleAndFixedDateRoundTrip(t *testing.T) {
	for rd := int64(-1721500); rd < -1721200; rd++ {
		date := BalineseFromFixed(rd)
		if got := BalineseOnOrBefore(date, rd); got != rd {
			t.Fatalf("BalineseOnOrBefore(%+v, %d) = %d", date, rd, got)
		}
		if next := BalineseFromFixed(rd + 210); next != date {
			t.Fatalf("Balinese cycle did not repeat after 210 days: at %d got %+v and %+v", rd, date, next)
		}
	}
}

func TestPositionsInInterval(t *testing.T) {
	got := PositionsInInterval(3, 7, 2, 10, 40)
	want := []int64{14, 21, 28, 35}
	if len(got) != len(want) {
		t.Fatalf("PositionsInInterval() = %v, want %v", got, want)
	}
	for i := range want {
		if got[i] != want[i] {
			t.Fatalf("PositionsInInterval() = %v, want %v", got, want)
		}
	}
}

func TestModifiedFrenchFixedDateRoundTrip(t *testing.T) {
	for _, date := range []ModifiedFrench{
		NewModifiedFrench(1, 1, 1),
		NewModifiedFrench(4, 13, 6),
		NewModifiedFrench(3996, 13, 6),
		NewModifiedFrench(-100, 7, 19),
	} {
		if got := ModifiedFrenchFromFixed(date.ToFixed()); !got.StandardDate.Equals(date.StandardDate) {
			t.Errorf("ModifiedFrenchFromFixed(%d) = %+v, want %+v", date.ToFixed(), got, date)
		}
	}
}

func TestJavaCalendarFixtureAtFixedDate(t *testing.T) {
	const rd = int64(-214193)

	if got, want := PersianFromFixed(rd), NewPersian(-1208, 5, 1); !got.Equals(want) {
		t.Errorf("PersianFromFixed(%d) = %+v, want %+v", rd, got, want)
	}
	if got, want := FrenchFromFixed(rd), NewFrench(-2378, 11, 5); !got.Equals(want) {
		t.Errorf("FrenchFromFixed(%d) = %+v, want %+v", rd, got, want)
	}
	if got, want := FutureBahaiFromFixedDate(rd), NewFutureBahai(-6, 6, 3, 7, 10); !got.Equals(want) {
		t.Errorf("FutureBahaiFromFixedDate(%d) = %+v, want %+v", rd, got, want)
	}
	if got, want := BalineseFromFixed(rd), NewBalinese(false, 1, 1, 1, 3, 1, 1, 5, 7, 3); !got.Equals(want) {
		t.Errorf("BalineseFromFixed(%d) = %+v, want %+v", rd, got, want)
	}
	observed, err := ObservationalIslamicFromFixedChecked(rd)
	if err != nil {
		t.Fatalf("ObservationalIslamicFromFixedChecked(%d): %v", rd, err)
	}
	if want := NewObservationalIslamic(-1245, 12, 12); !observed.Equals(want) {
		t.Errorf("ObservationalIslamicFromFixed(%d) = %+v, want %+v", rd, observed, want)
	}
	if got := observed.ToFixed(); got != rd {
		t.Errorf("observational Islamic round trip = %d, want %d", got, rd)
	}
}

package reingolddershowitz

import (
	"math"
	"testing"
)

const floatTol = 1e-5

func approxEqual(a, b float64) bool {
	return math.Abs(a-b) < floatTol
}

func TestAstronomicalConstants(t *testing.T) {
	if JDEpoch != -1721424.5 {
		t.Errorf("JDEpoch = %v, want -1721424.5", JDEpoch)
	}
	if MJDEpoch != 678576 {
		t.Errorf("MJDEpoch = %v, want 678576", MJDEpoch)
	}
	if J2000 != 730120.5 {
		t.Errorf("J2000 = %v, want 730120.5", J2000)
	}
	if MeanTropicalYear != 365.242189 {
		t.Errorf("MeanTropicalYear = %v, want 365.242189", MeanTropicalYear)
	}
	if MeanSynodicMonth != 29.530588853 {
		t.Errorf("MeanSynodicMonth = %v, want 29.530588853", MeanSynodicMonth)
	}
	if Spring != 0 || Summer != 90 || Autumn != 180 || Winter != 270 {
		t.Errorf("Season constants mismatch: Spring=%v, Summer=%v, Autumn=%v, Winter=%v", Spring, Summer, Autumn, Winter)
	}
	if New != 0 || FirstQuarter != 90 || Full != 180 || LastQuarter != 270 {
		t.Errorf("Lunar phase constants mismatch")
	}
}

func TestBasicHelpers(t *testing.T) {
	if got := Hr(12); got != 0.5 {
		t.Errorf("Hr(12) = %v, want 0.5", got)
	}
	if got := ModFloat(370, 360); got != 10 {
		t.Errorf("ModFloat(370, 360) = %v, want 10", got)
	}
	if got := ModFloat(-10, 360); got != 350 {
		t.Errorf("ModFloat(-10, 360) = %v, want 350", got)
	}
	if got := Signum(-5.2); got != -1 {
		t.Errorf("Signum(-5.2) = %v, want -1", got)
	}
	if got := Signum(0); got != 0 {
		t.Errorf("Signum(0) = %v, want 0", got)
	}
	if got := Signum(4.2); got != 1 {
		t.Errorf("Signum(4.2) = %v, want 1", got)
	}
	if got := Square(3.0); got != 9.0 {
		t.Errorf("Square(3) = %v, want 9", got)
	}
	coeffs := []float64{1, 2, 3} // 1 + 2*2 + 3*4 = 1 + 4 + 12 = 17
	if got := Poly(2.0, coeffs); got != 17.0 {
		t.Errorf("Poly(2, [1,2,3]) = %v, want 17", got)
	}
	if got := SinDegrees(90); !approxEqual(got, 1.0) {
		t.Errorf("SinDegrees(90) = %v, want 1.0", got)
	}
	if got := CosDegrees(180); !approxEqual(got, -1.0) {
		t.Errorf("CosDegrees(180) = %v, want -1.0", got)
	}
	if got := TanDegrees(45); !approxEqual(got, 1.0) {
		t.Errorf("TanDegrees(45) = %v, want 1.0", got)
	}
	if got := ArcSinDegrees(1.0); !approxEqual(got, 90.0) {
		t.Errorf("ArcSinDegrees(1) = %v, want 90.0", got)
	}
	if got := ArcCosDegrees(-1.0); !approxEqual(got, 180.0) {
		t.Errorf("ArcCosDegrees(-1) = %v, want 180.0", got)
	}
	if got := ArcTanDegrees(1.0, 1); !approxEqual(got, 45.0) {
		t.Errorf("ArcTanDegrees(1, 1) = %v, want 45.0", got)
	}
	if got := ArcTanDegrees(1.0, 3); !approxEqual(got, 225.0) {
		t.Errorf("ArcTanDegrees(1, 3) = %v, want 225.0", got)
	}
}

func TestJDHelpers(t *testing.T) {
	jd := JDFromMoment(J2000)
	if got := MomentFromJD(jd); got != J2000 {
		t.Errorf("MomentFromJD(JDFromMoment(J2000)) = %v, want %v", got, J2000)
	}
	rd := int64(730120)
	if got := FixedFromJD(JDFromFixed(rd)); got != rd {
		t.Errorf("FixedFromJD roundtrip = %v, want %v", got, rd)
	}
	if got := FixedFromMJD(MJDFromFixed(rd)); got != rd {
		t.Errorf("FixedFromMJD roundtrip = %v, want %v", got, rd)
	}
}

func TestTimeConversions(t *testing.T) {
	locale := URBANA
	teeU := 730120.5
	teeS := StandardFromUniversal(teeU, locale)
	if got := UniversalFromStandard(teeS, locale); !approxEqual(got, teeU) {
		t.Errorf("UniversalFromStandard roundtrip = %v, want %v", got, teeU)
	}
	teeL := LocalFromUniversal(teeU, locale)
	if got := UniversalFromLocal(teeL, locale); !approxEqual(got, teeU) {
		t.Errorf("UniversalFromLocal roundtrip = %v, want %v", got, teeU)
	}
	teeSL := StandardFromLocal(teeL, locale)
	if !approxEqual(teeSL, teeS) {
		t.Errorf("StandardFromLocal = %v, want %v", teeSL, teeS)
	}
	teeLS := LocalFromStandard(teeS, locale)
	if !approxEqual(teeLS, teeL) {
		t.Errorf("LocalFromStandard = %v, want %v", teeLS, teeL)
	}
}

func TestProtoDateEquivalents(t *testing.T) {
	rd := int64(730120) // 2000-01-01

	// Solar Longitude
	solLong := SolarLongitude(J2000)
	if !approxEqual(solLong, 280.369128) {
		t.Errorf("SolarLongitude(J2000) = %.6f, want 280.369128", solLong)
	}

	// Lunar Longitude
	lunarLong := LunarLongitude(J2000)
	if !approxEqual(lunarLong, 223.324256) {
		t.Errorf("LunarLongitude(J2000) = %.6f, want 223.324256", lunarLong)
	}

	// Lunar Phase
	phase := LunarPhase(J2000)
	if !approxEqual(phase, 302.955128) {
		t.Errorf("LunarPhase(J2000) = %.6f, want 302.955128", phase)
	}

	// Midday in Urbana
	midday := Midday(rd, URBANA)
	if !approxEqual(midday, 730120.497293) {
		t.Errorf("Midday(rd, URBANA) = %.6f, want 730120.497293", midday)
	}

	// Sunrise & Sunset in Urbana
	sunrise := Sunrise(rd, URBANA)
	if !approxEqual(sunrise, 730120.299973) {
		t.Errorf("Sunrise(rd, URBANA) = %.6f, want 730120.299973", sunrise)
	}

	sunset := Sunset(rd, URBANA)
	if !approxEqual(sunset, 730120.694706) {
		t.Errorf("Sunset(rd, URBANA) = %.6f, want 730120.694706", sunset)
	}

	// New Moon
	nmBefore := NewMoonBefore(J2000)
	if !approxEqual(nmBefore, 730095.938625) {
		t.Errorf("NewMoonBefore(J2000) = %.6f, want 730095.938625", nmBefore)
	}

	nmAfter := NewMoonAfter(J2000)
	if !approxEqual(nmAfter, 730125.759472) {
		t.Errorf("NewMoonAfter(J2000) = %.6f, want 730125.759472", nmAfter)
	}

	nthNM := NthNewMoon(24724)
	if !approxEqual(nthNM, 730125.759472) {
		t.Errorf("NthNewMoon(24724) = %.6f, want 730125.759472", nthNM)
	}

	// Visible Crescent & Phasis
	vis := VisibleCrescent(rd, JERUSALEM)
	if vis != false {
		t.Errorf("VisibleCrescent(rd, JERUSALEM) = %v, want false", vis)
	}

	phasis := PhasisOnOrBefore(rd, JERUSALEM)
	if phasis != 730097 {
		t.Errorf("PhasisOnOrBefore(rd, JERUSALEM) = %v, want 730097", phasis)
	}
}

func TestSolarAndLunarPhasesSearch(t *testing.T) {
	// Vernal equinox after J2000
	springMoment := SolarLongitudeAfter(J2000, Spring)
	if solLong := SolarLongitude(springMoment); !approxEqual(solLong, Spring) && !approxEqual(solLong, 360.0) {
		t.Errorf("SolarLongitude(springMoment) = %v, want Spring", solLong)
	}
	if springMoment <= J2000 {
		t.Errorf("springMoment = %v should be > J2000 (%v)", springMoment, J2000)
	}

	// Prior equinox
	priorSpring := EstimatePriorSolarLongitude(springMoment+10, Spring)
	if math.Abs(priorSpring-springMoment) > 1.0 {
		t.Errorf("EstimatePriorSolarLongitude close to equinox: got %v, want approx %v", priorSpring, springMoment)
	}

	// Lunar phase search
	fullMoon := LunarPhaseAfter(J2000, Full)
	if p := LunarPhase(fullMoon); math.Abs(p-Full) > 1e-3 {
		t.Errorf("LunarPhase(fullMoon) = %v, want Full (180)", p)
	}

	lastFullMoon := LunarPhaseBefore(fullMoon-0.1, Full)
	if p := LunarPhase(lastFullMoon); math.Abs(p-Full) > 1e-3 {
		t.Errorf("LunarPhase(lastFullMoon) = %v, want Full (180)", p)
	}
	if lastFullMoon >= fullMoon {
		t.Errorf("lastFullMoon = %v should be < fullMoon (%v)", lastFullMoon, fullMoon)
	}
}

func TestPolarFailureHandling(t *testing.T) {
	northPole := NewLocation("North Pole", 90.0, 0.0, 0.0, 0.0)
	winterSolsticeRD := int64(730109) // Mid-winter

	checked := []struct {
		name string
		call func() error
	}{
		{"moment from depression", func() error {
			_, err := MomentFromDepressionChecked(float64(winterSolsticeRD)+0.25, northPole, 18)
			return err
		}},
		{"dawn", func() error { _, err := DawnChecked(winterSolsticeRD, northPole, 18); return err }},
		{"dusk", func() error { _, err := DuskChecked(winterSolsticeRD, northPole, 18); return err }},
		{"sunrise", func() error { _, err := SunriseChecked(winterSolsticeRD, northPole); return err }},
		{"sunset", func() error { _, err := SunsetChecked(winterSolsticeRD, northPole); return err }},
		{"temporal hour", func() error { _, err := TemporalHourChecked(winterSolsticeRD, northPole); return err }},
		{"standard from sundial", func() error { _, err := StandardFromSundialChecked(winterSolsticeRD, 10, northPole); return err }},
	}
	for _, test := range checked {
		t.Run(test.name+" checked", func(t *testing.T) {
			if err := test.call(); err == nil {
				t.Fatal("expected a polar-time error")
			}
		})
	}

	unchecked := []struct {
		name string
		call func()
	}{
		{"moment from depression", func() { MomentFromDepression(float64(winterSolsticeRD)+0.25, northPole, 18) }},
		{"dawn", func() { Dawn(winterSolsticeRD, northPole, 18) }},
		{"dusk", func() { Dusk(winterSolsticeRD, northPole, 18) }},
		{"sunrise", func() { Sunrise(winterSolsticeRD, northPole) }},
		{"sunset", func() { Sunset(winterSolsticeRD, northPole) }},
		{"temporal hour", func() { TemporalHour(winterSolsticeRD, northPole) }},
		{"standard from sundial", func() { StandardFromSundial(winterSolsticeRD, 10, northPole) }},
		{"visible crescent", func() { VisibleCrescent(winterSolsticeRD, northPole) }},
		{"phasis", func() { PhasisOnOrBefore(winterSolsticeRD, northPole) }},
	}
	for _, test := range unchecked {
		t.Run(test.name+" panics", func(t *testing.T) {
			defer func() {
				if recover() == nil {
					t.Fatal("expected the unchecked helper to panic")
				}
			}()
			test.call()
		})
	}
}

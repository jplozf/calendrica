package reingolddershowitz

import (
	"errors"
	"math"
	"time"
)

// Constantes astronomiques et époques fondamentales.
const (
	// JDEpoch est le moment fixe du début des jours juliens (JD 0 = R.D. -1721424.5).
	JDEpoch float64 = -1721424.5

	// MJDEpoch est la date fixe de début du jour julien modifié (MJD 0 = R.D. 678576).
	MJDEpoch int64 = 678576

	// J2000 est le moment fixe du midi au 1er janvier 2000 grégorien (R.D. 730120.5).
	J2000 float64 = 730120.5

	// MeanTropicalYear est la durée moyenne de l'année tropique en jours.
	MeanTropicalYear float64 = 365.242189

	// MeanSynodicMonth est la durée moyenne du mois synodique lunaire en jours.
	MeanSynodicMonth float64 = 29.530588853

	// Phases lunaires standard (angles en degrés).
	New          float64 = 0.0
	FirstQuarter float64 = 90.0
	Full         float64 = 180.0
	LastQuarter  float64 = 270.0

	// Constantes de saisons (longitude solaire aux équinoxes et solstices).
	Spring float64 = 0.0
	Summer float64 = 90.0
	Autumn float64 = 180.0
	Winter float64 = 270.0

	// Alias compatibles avec les identifiants Java Calendrica
	SPRING = Spring
	SUMMER = Summer
	AUTUMN = Autumn
	WINTER = Winter
	NEW    = New
	FULL   = Full
)

// JERUSALEM représente l'emplacement géographique de Jérusalem selon Calendrica.
var JERUSALEM = NewLocation("Jerusalem, Israel", 31.8, 35.2, Mt(800), 2)

// ErrBogusTime indique qu'un événement solaire ne se produit pas (par exemple nuit polaire ou jour polaire).
var ErrBogusTime = errors.New("bogus time: solar depression angle not reached")

// --- Utilitaires de conversion et mathématiques ---

// Hr convertit un nombre d'heures en fraction de jour.
func Hr(x float64) float64 {
	return x / 24.0
}

// Deg est une fonction identité pour documenter les angles en degrés.
func Deg(x float64) float64 {
	return x
}

// ModFloat calcule le modulo mathématique (floor modulo) pour des flottants : x - y * floor(x / y).
func ModFloat(x, y float64) float64 {
	return x - y*math.Floor(x/y)
}

// ModReal est un alias de ModFloat.
func ModReal(x, y float64) float64 {
	return ModFloat(x, y)
}

// QuotientFloat calcule la partie entière de la division x / y arrondie vers -l'infini.
func QuotientFloat(x, y float64) int64 {
	return int64(math.Floor(x / y))
}

// QuotientReal est un alias de QuotientFloat.
func QuotientReal(x, y float64) int64 {
	return QuotientFloat(x, y)
}

// AdjustedModFloat applique un modulo ajusté pour les réels : y + mod(x, -y).
func AdjustedModFloat(x, y float64) float64 {
	return y + ModFloat(x, -y)
}

// Signum retourne le signe de x (-1, 0, 1).
func Signum(x float64) int {
	if x < 0 {
		return -1
	} else if x > 0 {
		return 1
	}
	return 0
}

// Square retourne le carré de x.
func Square(x float64) float64 {
	return x * x
}

// Poly évalue un polynôme en x avec les coefficients donnés par ordre croissant de puissance : a[0] + a[1]*x + ...
func Poly(x float64, a []float64) float64 {
	if len(a) == 0 {
		return 0
	}
	result := a[0]
	for i := 1; i < len(a); i++ {
		result += a[i] * math.Pow(x, float64(i))
	}
	return result
}

// Degrees normalise un angle en degrés dans l'intervalle [0, 360[.
func Degrees(theta float64) float64 {
	return ModFloat(theta, 360.0)
}

// RadiansToDegrees convertit un angle de radians en degrés normalisés dans [0, 360[.
func RadiansToDegrees(theta float64) float64 {
	return Degrees(theta / math.Pi * 180.0)
}

// DegreesToRadians convertit un angle de degrés en radians.
func DegreesToRadians(theta float64) float64 {
	return Degrees(theta) * math.Pi / 180.0
}

// SinDegrees calcule le sinus d'un angle en degrés.
func SinDegrees(theta float64) float64 {
	return math.Sin(DegreesToRadians(theta))
}

// CosDegrees calcule le cosinus d'un angle en degrés.
func CosDegrees(theta float64) float64 {
	return math.Cos(DegreesToRadians(theta))
}

// TanDegrees calcule la tangente d'un angle en degrés.
func TanDegrees(theta float64) float64 {
	return math.Tan(DegreesToRadians(theta))
}

// ArcTanDegrees calcule l'arc tangente de x en degrés dans le quadrant spécifié (1, 2, 3 ou 4).
func ArcTanDegrees(x float64, quad int) float64 {
	alpha := RadiansToDegrees(math.Atan(x))
	if quad == 1 || quad == 4 {
		return ModFloat(alpha, 360.0)
	}
	return ModFloat(alpha+180.0, 360.0)
}

// ArcSinDegrees calcule l'arc sinus en degrés normalisé dans [0, 360[.
func ArcSinDegrees(x float64) float64 {
	return RadiansToDegrees(math.Asin(x))
}

// ArcCosDegrees calcule l'arc cosinus en degrés normalisé dans [0, 360[.
func ArcCosDegrees(x float64) float64 {
	return RadiansToDegrees(math.Acos(x))
}

// CurrentMoment retourne le moment fixe courant (en jours depuis R.D. 0).
func CurrentMoment() float64 {
	unixMillis := float64(time.Now().UnixNano()) / 1e6
	return float64(GregorianToFixed(1970, January, 1)) + unixMillis/(1000.0*60.0*60.0*24.0)
}

// CurrentDate retourne la date fixe entière courante (R.D.).
func CurrentDate() int64 {
	return int64(math.Floor(CurrentMoment()))
}

// Difference retourne le nombre de jours séparant date1 de date2 (date2 - date1).
func Difference(date1, date2 int64) int64 {
	return date2 - date1
}

// KDayAfter retourne la date fixe du jour de semaine k strictement après rd (0 = Dimanche).
func KDayAfter(k int, rd int64) int64 {
	return KDayOnOrBefore(k, rd+7)
}

// KDayBefore retourne la date fixe du jour de semaine k strictement avant rd (0 = Dimanche).
func KDayBefore(k int, rd int64) int64 {
	return KDayOnOrBefore(k, rd-1)
}

// NthKDay retourne la n-ième occurrence du jour de semaine k après (n > 0) ou avant (n < 0) rd.
func NthKDay(n int, k int, rd int64) int64 {
	if n > 0 {
		return KDayBefore(k, rd) + int64(7*n)
	}
	return KDayAfter(k, rd) + int64(7*n)
}

// NameFromNumber retourne le nom dans nameList correspondant à number (indexation 1-based ajustée).
func NameFromNumber(number int64, nameList []string) string {
	if len(nameList) == 0 {
		return ""
	}
	return nameList[AdjustedMod(number, int64(len(nameList)))-1]
}

// NameFromDayOfWeek retourne le nom du jour de semaine dans nameList (0 = premier nom).
func NameFromDayOfWeek(dayOfWeek int64, nameList []string) string {
	return NameFromNumber(dayOfWeek+1, nameList)
}

// NameFromMonth retourne le nom du mois dans nameList (1 = premier nom).
func NameFromMonth(month int64, nameList []string) string {
	return NameFromNumber(month, nameList)
}

// --- Conversions temporelles et géographiques (Temps universel, standard, local) ---

// StandardFromUniversal convertit un moment en temps universel vers le temps standard du lieu.
func StandardFromUniversal(teeU float64, locale Location) float64 {
	return teeU + locale.Zone/24.0
}

// UniversalFromStandard convertit un moment en temps standard du lieu vers le temps universel.
func UniversalFromStandard(teeS float64, locale Location) float64 {
	return teeS - locale.Zone/24.0
}

// LocalFromUniversal convertit un moment en temps universel vers le temps local (solaire moyen).
func LocalFromUniversal(teeU float64, locale Location) float64 {
	return teeU + locale.Longitude/360.0
}

// UniversalFromLocal convertit un moment en temps local vers le temps universel.
func UniversalFromLocal(teeEll float64, locale Location) float64 {
	return teeEll - locale.Longitude/360.0
}

// StandardFromLocal convertit un moment en temps local vers le temps standard.
func StandardFromLocal(teeEll float64, locale Location) float64 {
	return StandardFromUniversal(UniversalFromLocal(teeEll, locale), locale)
}

// LocalFromStandard convertit un moment en temps standard vers le temps local.
func LocalFromStandard(teeS float64, locale Location) float64 {
	return LocalFromUniversal(UniversalFromStandard(teeS, locale), locale)
}

// Midday calcule l'heure standard du midi vrai (solaire) à une date fixe et un lieu donnés.
func Midday(date int64, locale Location) float64 {
	return StandardFromLocal(LocalFromApparent(float64(date)+Hr(12)), locale)
}

// Midnight calcule l'heure standard du minuit vrai (solaire) à une date fixe et un lieu donnés.
func Midnight(date int64, locale Location) float64 {
	return StandardFromLocal(LocalFromApparent(float64(date)), locale)
}

// --- Conversions Jour Julien (JD et MJD) ---

// MomentFromJD convertit un jour julien (JD) en moment fixe R.D.
func MomentFromJD(jd float64) float64 {
	return jd + JDEpoch
}

// JDFromMoment convertit un moment fixe R.D. en jour julien (JD).
func JDFromMoment(tee float64) float64 {
	return tee - JDEpoch
}

// FixedFromJD calcule la date fixe entière correspondant à un jour julien.
func FixedFromJD(jd float64) int64 {
	return int64(math.Floor(MomentFromJD(jd)))
}

// JDFromFixed calcule le jour julien pour une date fixe entière (à minuit R.D.).
func JDFromFixed(date int64) float64 {
	return JDFromMoment(float64(date))
}

// FixedFromMJD calcule la date fixe entière correspondant à un jour julien modifié (MJD).
func FixedFromMJD(mjd float64) int64 {
	return int64(math.Floor(mjd + float64(MJDEpoch)))
}

// MJDFromFixed calcule le jour julien modifié pour une date fixe entière.
func MJDFromFixed(date int64) float64 {
	return float64(date - MJDEpoch)
}

// Direction calcule l'azimut (en degrés dans le sens horaire depuis le Nord) pour viser focus depuis locale.
func Direction(locale, focus Location) float64 {
	phi := locale.Latitude
	phiPrime := focus.Latitude
	psi := locale.Longitude
	psiPrime := focus.Longitude
	denom := CosDegrees(phi)*TanDegrees(phiPrime) - SinDegrees(phi)*CosDegrees(psi-psiPrime)
	if denom == 0.0 {
		return 0
	}
	quad := 1
	if denom < 0 {
		quad = 2
	}
	return ModFloat(ArcTanDegrees(SinDegrees(psiPrime-psi)/denom, quad), 360.0)
}

// --- Corrections de temps dynamique et éphémérides ---

var coeffObliquity = []float64{
	0,
	Angle(0, 0, -46.8150),
	Angle(0, 0, -0.00059),
	Angle(0, 0, 0.001813),
}

// JulianCenturies calcule le nombre de siècles juliens depuis J2000 au moment tee.
func JulianCenturies(tee float64) float64 {
	return (DynamicalFromUniversal(tee) - J2000) / 36525.0
}

// Obliquity calcule l'obliquité de l'écliptique au moment tee (en degrés).
func Obliquity(tee float64) float64 {
	c := JulianCenturies(tee)
	return Angle(23, 26, 21.448) + Poly(c, coeffObliquity)
}

var (
	coeff19th = []float64{-0.00002, 0.000297, 0.025184, -0.181133, 0.553040, -0.861938, 0.677066, -0.212591}
	coeff18th = []float64{-0.000009, 0.003844, 0.083563, 0.865736, 4.867575, 15.845535, 31.332267, 38.291999, 28.316289, 11.636204, 2.043794}
	coeff17th = []float64{196.58333, -4.0675, 0.0219167}
)

// EphemerisCorrection calcule Delta T = Temps Dynamique - Temps Universel (en fraction de jour).
func EphemerisCorrection(tee float64) float64 {
	year := GregorianYearFromFixed(int64(math.Floor(tee)))
	c := float64(GregorianToFixed(year, July, 1)-GregorianToFixed(1900, January, 1)) / 36525.0
	var result float64
	if 1988 <= year && year <= 2019 {
		result = float64(year-1933) / (24.0 * 60.0 * 60.0)
	} else if 1900 <= year && year <= 1987 {
		result = Poly(c, coeff19th)
	} else if 1800 <= year && year <= 1899 {
		result = Poly(c, coeff18th)
	} else if 1620 <= year && year <= 1799 {
		result = Poly(float64(year-1600), coeff17th) / (24.0 * 60.0 * 60.0)
	} else {
		x := Hr(12) + float64(GregorianToFixed(year, January, 1)-GregorianToFixed(1810, January, 1))
		return (x*x/41048480.0 - 15.0) / (24.0 * 60.0 * 60.0)
	}
	return result
}

// UniversalFromDynamical convertit le temps dynamique en temps universel.
func UniversalFromDynamical(tee float64) float64 {
	return tee - EphemerisCorrection(tee)
}

// DynamicalFromUniversal convertit le temps universel en temps dynamique.
func DynamicalFromUniversal(tee float64) float64 {
	return tee + EphemerisCorrection(tee)
}

var (
	etCoeffLongitude    = []float64{280.46645, 36000.76983, 0.0003032}
	etCoeffAnomaly      = []float64{357.52910, 35999.05030, -0.0001559, -0.00000048}
	etCoeffEccentricity = []float64{0.016708617, -0.000042037, -0.0000001236}
)

// EquationOfTime calcule l'équation du temps au moment tee (en fraction de jour).
func EquationOfTime(tee float64) float64 {
	c := JulianCenturies(tee)
	longitude := Poly(c, etCoeffLongitude)
	anomaly := Poly(c, etCoeffAnomaly)
	eccentricity := Poly(c, etCoeffEccentricity)
	varepsilon := Obliquity(tee)
	y := Square(TanDegrees(varepsilon / 2.0))
	equation := (1.0 / 2.0 / math.Pi) * (y*SinDegrees(2.0*longitude) +
		-2.0*eccentricity*SinDegrees(anomaly) +
		4.0*eccentricity*y*SinDegrees(anomaly)*CosDegrees(2.0*longitude) +
		-0.5*y*y*SinDegrees(4.0*longitude) +
		-1.25*eccentricity*eccentricity*SinDegrees(2.0*anomaly))
	return float64(Signum(equation)) * math.Min(math.Abs(equation), Hr(12))
}

// LocalFromApparent convertit le temps solaire vrai (cadran) en temps local moyen.
func LocalFromApparent(tee float64) float64 {
	return tee - EquationOfTime(tee)
}

// ApparentFromLocal convertit le temps local moyen en temps solaire vrai (cadran).
func ApparentFromLocal(tee float64) float64 {
	return tee + EquationOfTime(tee)
}

// --- Calculs de dépression solaire, aube, crépuscule, lever et coucher ---

// MomentFromDepressionChecked calcule le moment en temps local proche de approx où l'angle de dépression du soleil est alpha.
// Retourne ErrBogusTime si l'événement ne se produit pas à cet endroit et cette date.
func MomentFromDepressionChecked(approx float64, locale Location, alpha float64) (float64, error) {
	phi := locale.Latitude
	tee := UniversalFromLocal(approx, locale)
	delta := ArcSinDegrees(SinDegrees(Obliquity(tee)) * SinDegrees(SolarLongitude(tee)))
	morning := ModFloat(approx, 1.0) < 0.5
	cosPhi := CosDegrees(phi)
	cosDelta := CosDegrees(delta)
	if cosPhi == 0 || cosDelta == 0 {
		return math.NaN(), ErrBogusTime
	}
	sineOffset := TanDegrees(phi)*TanDegrees(delta) + SinDegrees(alpha)/(cosDelta*cosPhi)
	if math.Abs(sineOffset) > 1.0 || math.IsNaN(sineOffset) {
		return math.NaN(), ErrBogusTime
	}
	offset := ModFloat(0.5+ArcSinDegrees(sineOffset)/360.0, 1.0) - 0.5
	var base float64
	if morning {
		base = 0.25 - offset
	} else {
		base = 0.75 + offset
	}
	return LocalFromApparent(math.Floor(approx) + base), nil
}

// MomentFromDepression est la variante non vérifiée retournant NaN en cas d'échec.
func MomentFromDepression(approx float64, locale Location, alpha float64) float64 {
	res, err := MomentFromDepressionChecked(approx, locale, alpha)
	if err != nil {
		panic(err)
	}
	return res
}

// DawnChecked calcule l'heure standard de l'aube pour l'angle de dépression alpha.
func DawnChecked(date int64, locale Location, alpha float64) (float64, error) {
	approx, err := MomentFromDepressionChecked(float64(date)+0.25, locale, alpha)
	if err != nil {
		approx = float64(date)
	}
	result, err := MomentFromDepressionChecked(approx, locale, alpha)
	if err != nil {
		return math.NaN(), err
	}
	return StandardFromLocal(result, locale), nil
}

// Dawn panics when the event cannot be calculated; use DawnChecked to handle errors.
func Dawn(date int64, locale Location, alpha float64) float64 {
	res, err := DawnChecked(date, locale, alpha)
	if err != nil {
		panic(err)
	}
	return res
}

// DuskChecked calcule l'heure standard du crépuscule pour l'angle de dépression alpha.
func DuskChecked(date int64, locale Location, alpha float64) (float64, error) {
	approx, err := MomentFromDepressionChecked(float64(date)+0.75, locale, alpha)
	if err != nil {
		approx = float64(date) + 0.99
	}
	result, err := MomentFromDepressionChecked(approx, locale, alpha)
	if err != nil {
		return math.NaN(), err
	}
	return StandardFromLocal(result, locale), nil
}

// Dusk panics when the event cannot be calculated; use DuskChecked to handle errors.
func Dusk(date int64, locale Location, alpha float64) float64 {
	res, err := DuskChecked(date, locale, alpha)
	if err != nil {
		panic(err)
	}
	return res
}

// SunriseChecked calcule l'heure standard du lever de soleil à une date et un lieu donnés.
func SunriseChecked(date int64, locale Location) (float64, error) {
	h := math.Max(0, locale.Elevation)
	const capR = 6.372e6
	dip := ArcCosDegrees(capR / (capR + h))
	alpha := Angle(0, 50, 0) + dip
	return DawnChecked(date, locale, alpha)
}

// Sunrise panics when the event cannot be calculated; use SunriseChecked to handle errors.
func Sunrise(date int64, locale Location) float64 {
	res, err := SunriseChecked(date, locale)
	if err != nil {
		panic(err)
	}
	return res
}

// SunsetChecked calcule l'heure standard du coucher de soleil à une date et un lieu donnés.
func SunsetChecked(date int64, locale Location) (float64, error) {
	h := math.Max(0, locale.Elevation)
	const capR = 6.372e6
	dip := ArcCosDegrees(capR / (capR + h))
	alpha := Angle(0, 50, 0) + dip
	return DuskChecked(date, locale, alpha)
}

// Sunset panics when the event cannot be calculated; use SunsetChecked to handle errors.
func Sunset(date int64, locale Location) float64 {
	res, err := SunsetChecked(date, locale)
	if err != nil {
		panic(err)
	}
	return res
}

// TemporalHourChecked calcule la durée d'une heure temporale de jour (en fraction de jour).
func TemporalHourChecked(date int64, locale Location) (float64, error) {
	rise, err := SunriseChecked(date, locale)
	if err != nil {
		return math.NaN(), err
	}
	set, err := SunsetChecked(date, locale)
	if err != nil {
		return math.NaN(), err
	}
	return (set - rise) / 12.0, nil
}

// TemporalHour panics when sunrise or sunset cannot be calculated; use TemporalHourChecked to handle errors.
func TemporalHour(date int64, locale Location) float64 {
	res, err := TemporalHourChecked(date, locale)
	if err != nil {
		panic(err)
	}
	return res
}

// StandardFromSundialChecked calcule l'heure standard pour une heure temporale de cadran donnée.
func StandardFromSundialChecked(date int64, hour float64, locale Location) (float64, error) {
	rise, err := SunriseChecked(date, locale)
	if err != nil {
		return math.NaN(), err
	}
	tee, err := TemporalHourChecked(date, locale)
	if err != nil {
		return math.NaN(), err
	}
	if 6.0 <= hour && hour <= 18.0 {
		return rise + (hour-6.0)*tee, nil
	}
	return rise + (hour-6.0)*(1.0/12.0-tee), nil
}

// StandardFromSundial panics when the temporal hour cannot be calculated; use StandardFromSundialChecked to handle errors.
func StandardFromSundial(date int64, hour float64, locale Location) float64 {
	res, err := StandardFromSundialChecked(date, hour, locale)
	if err != nil {
		panic(err)
	}
	return res
}

// --- Longitude solaire ---

var (
	slCoefficients = []int{
		403406, 195207, 119433, 112392, 3891, 2819, 1721,
		660, 350, 334, 314, 268, 242, 234, 158, 132, 129, 114,
		99, 93, 86, 78, 72, 68, 64, 46, 38, 37, 32, 29, 28, 27, 27,
		25, 24, 21, 21, 20, 18, 17, 14, 13, 13, 13, 12, 10, 10, 10,
		10,
	}
	slMultipliers = []float64{
		0.9287892, 35999.1376958, 35999.4089666,
		35998.7287385, 71998.20261, 71998.4403,
		36000.35726, 71997.4812, 32964.4678,
		-19.4410, 445267.1117, 45036.8840, 3.1008,
		22518.4434, -19.9739, 65928.9345,
		9038.0293, 3034.7684, 33718.148, 3034.448,
		-2280.773, 29929.992, 31556.493, 149.588,
		9037.750, 107997.405, -4444.176, 151.771,
		67555.316, 31556.080, -4561.540,
		107996.706, 1221.655, 62894.167,
		31437.369, 14578.298, -31931.757,
		34777.243, 1221.999, 62894.511,
		-4442.039, 107997.909, 119.066, 16859.071,
		-4.578, 26895.292, -39.127, 12297.536,
		90073.778,
	}
	slAddends = []float64{
		270.54861, 340.19128, 63.91854, 331.26220,
		317.843, 86.631, 240.052, 310.26, 247.23,
		260.87, 297.82, 343.14, 166.79, 81.53,
		3.50, 132.75, 182.95, 162.03, 29.8,
		266.4, 249.2, 157.6, 257.8, 185.1, 69.9,
		8.0, 197.1, 250.4, 65.3, 162.7, 341.5,
		291.6, 98.5, 146.7, 110.0, 5.2, 342.6,
		230.9, 256.1, 45.3, 242.9, 115.2, 151.8,
		285.3, 53.3, 126.6, 205.7, 85.9,
		146.1,
	}
)

var (
	nuCoeffA = []float64{124.90, -1934.134, 0.002063}
	nuCoeffB = []float64{201.11, 72001.5377, 0.00057}
)

// Nutation calcule la nutation en longitude au moment tee (en degrés).
func Nutation(tee float64) float64 {
	c := JulianCenturies(tee)
	capA := Poly(c, nuCoeffA)
	capB := Poly(c, nuCoeffB)
	return -0.004778*SinDegrees(capA) + -0.0003667*SinDegrees(capB)
}

// Aberration calcule l'aberration de la lumière au moment tee (en degrés).
func Aberration(tee float64) float64 {
	c := JulianCenturies(tee)
	return 0.0000974*CosDegrees(177.63+35999.01848*c) - 0.005575
}

// SolarLongitude calcule la longitude apparente du soleil au moment tee (en degrés).
func SolarLongitude(tee float64) float64 {
	c := JulianCenturies(tee)
	sigma := 0.0
	for i := 0; i < len(slCoefficients); i++ {
		sigma += float64(slCoefficients[i]) * SinDegrees(slMultipliers[i]*c+slAddends[i])
	}
	longitude := 282.7771834 +
		36000.76953744*c +
		0.000005729577951308232*sigma
	return ModFloat(longitude+Aberration(tee)+Nutation(tee), 360.0)
}

// SolarLongitudeAfter calcule le premier moment UT à ou après tee où la longitude solaire atteint phi degrés.
func SolarLongitudeAfter(tee float64, phi float64) float64 {
	const varepsilon = 0.00001
	rate := MeanTropicalYear / 360.0
	tau := tee + rate*ModFloat(phi-SolarLongitude(tee), 360.0)
	l := math.Max(tee, tau-5.0)
	u := tau + 5.0

	lo := l
	hi := u
	x := (hi + lo) / 2.0
	for hi-lo > varepsilon {
		if ModFloat(SolarLongitude(x)-phi, 360.0) < 180.0 {
			hi = x
		} else {
			lo = x
		}
		x = (hi + lo) / 2.0
	}
	return x
}

// SolarLongitudeBefore calcule le dernier moment UT à ou avant tee où la longitude solaire a atteint phi degrés.
func SolarLongitudeBefore(tee float64, phi float64) float64 {
	const varepsilon = 0.00001
	rate := MeanTropicalYear / 360.0
	tau := tee - rate*ModFloat(SolarLongitude(tee)-phi, 360.0)
	l := tau - 5.0
	u := math.Min(tee, tau+5.0)

	lo := l
	hi := u
	x := (hi + lo) / 2.0
	for hi-lo > varepsilon {
		if ModFloat(SolarLongitude(x)-phi, 360.0) < 180.0 {
			hi = x
		} else {
			lo = x
		}
		x = (hi + lo) / 2.0
	}
	return x
}

// EstimatePriorSolarLongitude fournit une approximation du moment à ou avant tee où la longitude solaire a atteint phi.
func EstimatePriorSolarLongitude(tee float64, phi float64) float64 {
	rate := MeanTropicalYear / 360.0
	tau := tee - rate*ModFloat(SolarLongitude(tee)-phi, 360.0)
	capDelta := ModFloat(SolarLongitude(tau)-phi+180.0, 360.0) - 180.0
	return math.Min(tee, tau-rate*capDelta)
}

// --- Position lunaire, phases et nouvelle lune ---

var (
	llonCoeffMeanMoon     = []float64{218.3164591, 481267.88134236, -0.0013268, 1.0 / 538841.0, -1.0 / 65194000.0}
	llonCoeffElongation   = []float64{297.8502042, 445267.1115168, -0.00163, 1.0 / 545868.0, -1.0 / 113065000.0}
	llonCoeffSolarAnomaly = []float64{357.5291092, 35999.0502909, -0.0001536, 1.0 / 24490000.0}
	llonCoeffLunarAnomaly = []float64{134.9634114, 477198.8676313, 0.008997, 1.0 / 69699.0, -1.0 / 14712000.0}
	llonCoeffMoonNode     = []float64{93.2720993, 483202.0175273, -0.0034029, -1.0 / 3526000.0, 1.0 / 863310000.0}
	llonCoeffCapE         = []float64{1, -0.002516, -0.0000074}

	llonArgsLunarElongation = []int8{
		0, 2, 2, 0, 0, 0, 2, 2, 2, 2, 0, 1, 0, 2, 0, 0, 4, 0, 4, 2, 2, 1,
		1, 2, 2, 4, 2, 0, 2, 2, 1, 2, 0, 0, 2, 2, 2, 4, 0, 3, 2, 4, 0, 2,
		2, 2, 4, 0, 4, 1, 2, 0, 1, 3, 4, 2, 0, 1, 2,
	}
	llonArgsSolarAnomaly = []int8{
		0, 0, 0, 0, 1, 0, 0, -1, 0, -1, 1, 0, 1, 0, 0, 0, 0, 0, 0, 1, 1,
		0, 1, -1, 0, 0, 0, 1, 0, -1, 0, -2, 1, 2, -2, 0, 0, -1, 0, 0, 1,
		-1, 2, 2, 1, -1, 0, 0, -1, 0, 1, 0, 1, 0, 0, -1, 2, 1, 0,
	}
	llonArgsLunarAnomaly = []int8{
		1, -1, 0, 2, 0, 0, -2, -1, 1, 0, -1, 0, 1, 0, 1, 1, -1, 3, -2,
		-1, 0, -1, 0, 1, 2, 0, -3, -2, -1, -2, 1, 0, 2, 0, -1, 1, 0,
		-1, 2, -1, 1, -2, -1, -1, -2, 0, 1, 4, 0, -2, 0, 2, 1, -2, -3,
		2, 1, -1, 3,
	}
	llonArgsMoonFromNode = []int8{
		0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, 0, -2, 2, -2, 0, 0, 0, 0, 0,
		0, 0, 0, 0, 0, 0, 0, 2, 0, 0, 0, 0, 0, 0, -2, 2, 0, 2, 0, 0, 0, 0,
		0, 0, -2, 0, 0, 0, 0, -2, -2, 0, 0, 0, 0, 0, 0, 0,
	}
	llonSineCoefficients = []int{
		6288774, 1274027, 658314, 213618, -185116, -114332,
		58793, 57066, 53322, 45758, -40923, -34720, -30383,
		15327, -12528, 10980, 10675, 10034, 8548, -7888,
		-6766, -5163, 4987, 4036, 3994, 3861, 3665, -2689,
		-2602, 2390, -2348, 2236, -2120, -2069, 2048, -1773,
		-1595, 1215, -1110, -892, -810, 759, -713, -700, 691,
		596, 549, 537, 520, -487, -399, -381, 351, -340, 330,
		327, -323, 299, 294,
	}
)

// LunarLongitude calcule la longitude géocentrique de la lune au moment tee (en degrés).
func LunarLongitude(tee float64) float64 {
	c := JulianCenturies(tee)
	meanMoon := Degrees(Poly(c, llonCoeffMeanMoon))
	elongation := Degrees(Poly(c, llonCoeffElongation))
	solarAnomaly := Degrees(Poly(c, llonCoeffSolarAnomaly))
	lunarAnomaly := Degrees(Poly(c, llonCoeffLunarAnomaly))
	moonNode := Degrees(Poly(c, llonCoeffMoonNode))
	capE := Poly(c, llonCoeffCapE)
	sigma := 0.0
	for i := 0; i < len(llonArgsLunarElongation); i++ {
		x := float64(llonArgsSolarAnomaly[i])
		sigma += float64(llonSineCoefficients[i]) *
			math.Pow(capE, math.Abs(x)) *
			SinDegrees(float64(llonArgsLunarElongation[i])*elongation+
				x*solarAnomaly+
				float64(llonArgsLunarAnomaly[i])*lunarAnomaly+
				float64(llonArgsMoonFromNode[i])*moonNode)
	}
	correction := (1.0 / 1000000.0) * sigma
	venus := (3958.0 / 1000000.0) * SinDegrees(119.75+c*131.849)
	jupiter := (318.0 / 1000000.0) * SinDegrees(53.09+c*479264.29)
	flatEarth := (1962.0 / 1000000.0) * SinDegrees(meanMoon-moonNode)
	return ModFloat(meanMoon+correction+venus+jupiter+flatEarth+Nutation(tee), 360.0)
}

var (
	nmCoeffApprox       = []float64{730125.59765, MeanSynodicMonth * 1236.85, 0.0001337, -0.000000150, 0.00000000073}
	nmCoeffCapE         = []float64{1, -0.002516, -0.0000074}
	nmCoeffSolarAnomaly = []float64{2.5534, 29.10535669 * 1236.85, -0.0000218, -0.00000011}
	nmCoeffLunarAnomaly = []float64{201.5643, 385.81693528 * 1236.85, 0.0107438, 0.00001239, -0.000000058}
	nmCoeffMoonArgument = []float64{160.7108, 390.67050274 * 1236.85, -0.0016341, -0.00000227, 0.000000011}
	nmCoeffCapOmega     = []float64{124.7746, -1.56375580 * 1236.85, 0.0020691, 0.00000215}

	nmEFactor = []int8{0, 1, 0, 0, 1, 1, 2, 0, 0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}

	nmSolarCoeff = []int8{0, 1, 0, 0, -1, 1, 2, 0, 0, 1, 0, 1, 1, -1, 2, 0, 3, 1, 0, 1, -1, -1, 1, 0}
	nmLunarCoeff = []int8{1, 0, 2, 0, 1, 1, 0, 1, 1, 2, 3, 0, 0, 2, 1, 2, 0, 1, 2, 1, 1, 1, 3, 4}
	nmMoonCoeff  = []int8{0, 0, 0, 2, 0, 0, 0, -2, 2, 0, 0, 2, -2, 0, 0, -2, 0, -2, 2, 2, 2, -2, 0, 0}

	nmSineCoeff = []float64{
		-0.40720, 0.17241, 0.01608, 0.01039, 0.00739, -0.00514, 0.00208,
		-0.00111, -0.00057, 0.00056, -0.00042, 0.00042, 0.00038, -0.00024,
		-0.00007, 0.00004, 0.00004, 0.00003, 0.00003, -0.00003, 0.00003,
		-0.00002, -0.00002, 0.00002,
	}

	nmAddConst = []float64{
		251.88, 251.83, 349.42, 84.66, 141.74, 207.14, 154.84, 34.52, 207.19,
		291.34, 161.72, 239.56, 331.55,
	}
	nmAddCoeff = []float64{
		0.016321, 26.641886, 36.412478, 18.206239, 53.303771, 2.453732,
		7.306860, 27.261239, 0.121824, 1.844379, 24.198154, 25.513099, 3.592518,
	}
	nmAddFactor = []float64{
		0.000165, 0.000164, 0.000126, 0.000110, 0.000062, 0.000060, 0.000056,
		0.000047, 0.000042, 0.000040, 0.000037, 0.000035, 0.000023,
	}
	nmExtra = []float64{299.77, 132.8475848, -0.009173}
)

// NthNewMoon calcule le moment UT de la n-ième nouvelle lune depuis celle du 11 janvier 1.
func NthNewMoon(n int64) float64 {
	k := float64(n - 24724)
	c := k / 1236.85
	approx := Poly(c, nmCoeffApprox)
	capE := Poly(c, nmCoeffCapE)
	solarAnomaly := Poly(c, nmCoeffSolarAnomaly)
	lunarAnomaly := Poly(c, nmCoeffLunarAnomaly)
	moonArgument := Poly(c, nmCoeffMoonArgument)
	capOmega := Poly(c, nmCoeffCapOmega)
	correction := -0.00017 * SinDegrees(capOmega)
	for ix := 0; ix < len(nmSineCoeff); ix++ {
		correction += nmSineCoeff[ix] * math.Pow(capE, float64(nmEFactor[ix])) *
			SinDegrees(float64(nmSolarCoeff[ix])*solarAnomaly+
				float64(nmLunarCoeff[ix])*lunarAnomaly+
				float64(nmMoonCoeff[ix])*moonArgument)
	}
	additional := 0.0
	for ix := 0; ix < len(nmAddConst); ix++ {
		additional += nmAddFactor[ix] *
			SinDegrees(nmAddConst[ix]+nmAddCoeff[ix]*k)
	}
	extra := 0.000325 * SinDegrees(Poly(c, nmExtra))
	return UniversalFromDynamical(approx + correction + extra + additional)
}

// LunarPhase calcule la phase lunaire en degrés à un moment tee donné (0 = nouvelle lune, 180 = pleine lune).
func LunarPhase(tee float64) float64 {
	return ModFloat(LunarLongitude(tee)-SolarLongitude(tee), 360.0)
}

// NewMoonBefore calcule le moment UT de la dernière nouvelle lune avant tee.
func NewMoonBefore(tee float64) float64 {
	n := int64(math.Round(tee/MeanSynodicMonth - LunarPhase(tee)/360.0))
	return NthNewMoon(n)
}

// NewMoonAfter calcule le moment UT de la première nouvelle lune à ou après tee.
func NewMoonAfter(tee float64) float64 {
	n := 1 + int64(math.Round(tee/MeanSynodicMonth-LunarPhase(tee)/360.0))
	return NthNewMoon(n)
}

// LunarPhaseBefore calcule le moment UT de la dernière fois à ou avant tee où la phase lunaire était phi degrés.
func LunarPhaseBefore(tee float64, phi float64) float64 {
	const varepsilon = 0.00001
	tau := tee - MeanSynodicMonth*(1.0/360.0)*ModFloat(LunarPhase(tee)-phi, 360.0)
	l := tau - 2.0
	u := math.Min(tee, tau+2.0)

	lo := l
	hi := u
	x := (hi + lo) / 2.0
	for hi-lo > varepsilon {
		if ModFloat(LunarPhase(x)-phi, 360.0) < 180.0 {
			hi = x
		} else {
			lo = x
		}
		x = (hi + lo) / 2.0
	}
	return x
}

// LunarPhaseAfter calcule le moment UT de la prochaine fois à ou après tee où la phase lunaire sera phi degrés.
func LunarPhaseAfter(tee float64, phi float64) float64 {
	const varepsilon = 0.00001
	tau := tee + MeanSynodicMonth*(1.0/360.0)*ModFloat(phi-LunarPhase(tee), 360.0)
	l := math.Max(tee, tau-2.0)
	u := tau + 2.0

	lo := l
	hi := u
	x := (hi + lo) / 2.0
	for hi-lo > varepsilon {
		if ModFloat(LunarPhase(x)-phi, 360.0) < 180.0 {
			hi = x
		} else {
			lo = x
		}
		x = (hi + lo) / 2.0
	}
	return x
}

var (
	llatCoeffLongitude    = []float64{218.3164591, 481267.88134236, -0.0013268, 1.0 / 538841.0, -1.0 / 65194000.0}
	llatCoeffElongation   = []float64{297.8502042, 445267.1115168, -0.00163, 1.0 / 545868.0, -1.0 / 113065000.0}
	llatCoeffSolarAnomaly = []float64{357.5291092, 35999.0502909, -0.0001536, 1.0 / 24490000.0}
	llatCoeffLunarAnomaly = []float64{134.9634114, 477198.8676313, 0.008997, 1.0 / 69699.0, -1.0 / 14712000.0}
	llatCoeffMoonNode     = []float64{93.2720993, 483202.0175273, -0.0034029, -1.0 / 3526000.0, 1.0 / 863310000.0}
	llatCoeffCapE         = []float64{1, -0.002516, -0.0000074}

	llatArgsLunarElongation = []int8{
		0, 0, 0, 2, 2, 2, 2, 0, 2, 0, 2, 2, 2, 2, 2, 2, 2, 0, 4, 0, 0, 0,
		1, 0, 0, 0, 1, 0, 4, 4, 0, 4, 2, 2, 2, 2, 0, 2, 2, 2, 2, 4, 2, 2,
		0, 2, 1, 1, 0, 2, 1, 2, 0, 4, 4, 1, 4, 1, 4, 2,
	}
	llatArgsSolarAnomaly = []int8{
		0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 1, -1, -1, -1, 1, 0, 1,
		0, 1, 0, 1, 1, 1, 0, 0, 0, 0, 0, 0, 0, 0, -1, 0, 0, 0, 0, 1, 1,
		0, -1, -2, 0, 1, 1, 1, 1, 1, 0, -1, 1, 0, -1, 0, 0, 0, -1, -2,
	}
	llatArgsLunarAnomaly = []int8{
		0, 1, 1, 0, -1, -1, 0, 2, 1, 2, 0, -2, 1, 0, -1, 0, -1, -1, -1,
		0, 0, -1, 0, 1, 1, 0, 0, 3, 0, -1, 1, -2, 0, 2, 1, -2, 3, 2, -3,
		-1, 0, 0, 1, 0, 1, 1, 0, 0, -2, -1, 1, -2, 2, -2, -1, 1, 1, -2,
		0, 0,
	}
	llatArgsMoonNode = []int8{
		1, 1, -1, -1, 1, -1, 1, 1, -1, -1, -1, -1, 1, -1, 1, 1, -1, -1,
		-1, 1, 3, 1, 1, 1, -1, -1, -1, 1, -1, 1, -3, 1, -3, -1, -1, 1,
		-1, 1, -1, 1, 1, 1, 1, -1, 3, -1, -1, 1, -1, -1, 1, -1, 1, -1,
		-1, -1, -1, -1, -1, 1,
	}
	llatSineCoefficients = []int{
		5128122, 280602, 277693, 173237, 55413, 46271, 32573,
		17198, 9266, 8822, 8216, 4324, 4200, -3359, 2463, 2211,
		2065, -1870, 1828, -1794, -1749, -1565, -1491, -1475,
		-1410, -1344, -1335, 1107, 1021, 833, 777, 671, 607,
		596, 491, -451, 439, 422, 421, -366, -351, 331, 315,
		302, -283, -229, 223, 223, -220, -220, -185, 181,
		-177, 176, 166, -164, 132, -119, 115, 107,
	}
)

// LunarLatitude calcule la latitude géocentrique de la lune au moment tee (en degrés).
func LunarLatitude(tee float64) float64 {
	c := JulianCenturies(tee)
	longitude := Degrees(Poly(c, llatCoeffLongitude))
	elongation := Degrees(Poly(c, llatCoeffElongation))
	solarAnomaly := Degrees(Poly(c, llatCoeffSolarAnomaly))
	lunarAnomaly := Degrees(Poly(c, llatCoeffLunarAnomaly))
	moonNode := Degrees(Poly(c, llatCoeffMoonNode))
	capE := Poly(c, llatCoeffCapE)
	latitude := 0.0
	for i := 0; i < len(llatArgsLunarElongation); i++ {
		x := float64(llatArgsSolarAnomaly[i])
		latitude += float64(llatSineCoefficients[i]) *
			math.Pow(capE, math.Abs(x)) *
			SinDegrees(float64(llatArgsLunarElongation[i])*elongation+
				x*solarAnomaly+
				float64(llatArgsLunarAnomaly[i])*lunarAnomaly+
				float64(llatArgsMoonNode[i])*moonNode)
	}
	latitude *= 1.0 / 1000000.0
	venus := (175.0 / 1000000.0) * (SinDegrees(119.75+c*131.849+moonNode) + SinDegrees(119.75+c*131.849-moonNode))
	flatEarth := (-2235.0/1000000.0)*SinDegrees(longitude) +
		(127.0/1000000.0)*SinDegrees(longitude-lunarAnomaly) +
		(-115.0/1000000.0)*SinDegrees(longitude+lunarAnomaly)
	extra := (382.0 / 1000000.0) * SinDegrees(313.45+c*481266.484)
	return ModFloat(latitude+venus+flatEarth+extra, 360.0)
}

var sfmSiderealCoeff = []float64{280.46061837, 36525.0 * 360.98564736629, 0.000387933, 1.0 / 38710000.0}

// SiderealFromMoment calcule le temps sidéral moyen au moment tee (exprimé en angle horaire [0, 360[).
func SiderealFromMoment(tee float64) float64 {
	c := (tee - J2000) / 36525.0
	return ModFloat(Poly(c, sfmSiderealCoeff), 360.0)
}

// LunarAltitude calcule la hauteur (altitude) de la lune au moment tee au lieu locale (en degrés).
func LunarAltitude(tee float64, locale Location) float64 {
	phi := locale.Latitude
	psi := locale.Longitude
	varepsilon := Obliquity(tee)
	lambda := LunarLongitude(tee)
	beta := LunarLatitude(tee)
	quad := int(math.Floor(lambda/90.0)) + 1
	alpha := ArcTanDegrees(
		(SinDegrees(lambda)*CosDegrees(varepsilon)-TanDegrees(beta)*SinDegrees(varepsilon))/CosDegrees(lambda),
		quad,
	)
	delta := ArcSinDegrees(SinDegrees(beta)*CosDegrees(varepsilon) +
		CosDegrees(beta)*SinDegrees(varepsilon)*SinDegrees(lambda))
	theta0 := SiderealFromMoment(tee)
	capH := ModFloat(theta0+psi-alpha, 360.0)
	altitude := ArcSinDegrees(SinDegrees(phi)*SinDegrees(delta) + CosDegrees(phi)*CosDegrees(delta)*CosDegrees(capH))
	return ModFloat(altitude+180.0, 360.0) - 180.0
}

// --- Visibilité du croissant et Phasis ---

// VisibleCrescentChecked détermine selon le critère de Shaukat si le croissant de lune est visible à date au lieu locale.
// Retourne une erreur si le crépuscule ne peut être déterminé (e.g. régions polaires).
func VisibleCrescentChecked(date int64, locale Location) (bool, error) {
	duskTime, err := DuskChecked(date, locale, 4.5)
	if err != nil {
		return false, err
	}
	tee := UniversalFromStandard(duskTime, locale)
	phase := LunarPhase(tee)
	altitude := LunarAltitude(tee, locale)
	arcOfLight := ArcCosDegrees(CosDegrees(LunarLatitude(tee)) * CosDegrees(phase))
	visible := (New < phase && phase < FirstQuarter) &&
		(10.6 <= arcOfLight && arcOfLight <= 90.0) &&
		(altitude > 4.1)
	return visible, nil
}

// VisibleCrescent panics when visibility cannot be calculated; use VisibleCrescentChecked to handle errors.
func VisibleCrescent(date int64, locale Location) bool {
	visible, err := VisibleCrescentChecked(date, locale)
	if err != nil {
		panic(err)
	}
	return visible
}

// PhasisOnOrBeforeChecked calcule la date fixe la plus proche à ou avant date où le croissant devient visible.
func PhasisOnOrBeforeChecked(date int64, locale Location) (int64, error) {
	mean := date - int64(math.Floor((LunarPhase(float64(date))/360.0)*MeanSynodicMonth))
	vis, err := VisibleCrescentChecked(date, locale)
	if err != nil {
		return 0, err
	}
	var tau int64
	if (date-mean) <= 3 && !vis {
		tau = mean - 30
	} else {
		tau = mean - 2
	}
	for d := tau; ; d++ {
		visD, err := VisibleCrescentChecked(d, locale)
		if err != nil {
			return 0, err
		}
		if visD {
			return d, nil
		}
	}
}

// PhasisOnOrBefore panics when visibility cannot be calculated; use PhasisOnOrBeforeChecked to handle errors.
func PhasisOnOrBefore(date int64, locale Location) int64 {
	res, err := PhasisOnOrBeforeChecked(date, locale)
	if err != nil {
		panic(err)
	}
	return res
}

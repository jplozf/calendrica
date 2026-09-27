package calendrica;


public class HinduSolar extends StandardDate {

	//
	// constructors
	//

	public HinduSolar() { }
	
	public HinduSolar(long date) {
		super(date);
	}
	
	public HinduSolar(Date date) {
		super(date);
	}
	
	public HinduSolar(long year, int month, int day) {
		super(year, month, day);
	}
	
	//
	// constants
	//


	/*- hindu-sidereal-year -*/

	// TYPE rational
	// Mean length of Hindu sidereal year.
	
	public static final double SIDEREAL_YEAR = 365 + 279457d/1080000;
	
	
	/*- hindu-creation -*/

	// TYPE fixed-date
	// Fixed date of Hindu creation.
	
	public static final double CREATION = OldHinduSolar.EPOCH - 1955880000 * SIDEREAL_YEAR;


	/*- hindu-anomalistic-year -*/

	// TYPE rational
	// Time from aphelion to aphelion.

	public static final double ANOMALISTIC_YEAR = 1577917828000d / (4320000000d - 387);


	/*- hindu-solar-era -*/

	// TYPE standard-year
	// Years from Kali Yuga until Saka era.
	
	public static final int SOLAR_ERA = 3179;
	
	
	/*- ujjain -*/

	// TYPE location
	// Location of Ujjain.
	
	public static final Location UJJAIN = new Location("Ujjain, India", angle(23, 9, 0), angle(75, 46, 0), mt(0), 5 + 8d/75);


	/*- hindu-locale -*/

	// TYPE location
	// Location (Ujjain) for determining Hindu calendar.

	public static final Location HINDU_LOCALE = UJJAIN;


	//
	// date conversion methods
	//
	
	
	/*- fixed-from-hindu-solar -*/

	// TYPE hindu-solar-date -> fixed-date
	// Fixed date corresponding to Hindu solar date (Saka era).
	
	public static long toFixed(long year, int month, int day)
	{
		return new HinduSolar(year, month, day).toFixed();
	}
	
	public long toFixed() {
		long approx = (long)Math.floor((year + SOLAR_ERA + (month - 1) / 12d) * SIDEREAL_YEAR) + OldHinduSolar.EPOCH + day - 1;
		double rate = deg(360) / SIDEREAL_YEAR;
		double phi = (month - 1) * deg(30) + (day - 1) * rate;
		double capDelta = mod(solarLongitude(approx + 1d/4) - phi + deg(180), 360) - deg(180);
		long tau = approx - (long)Math.ceil(capDelta / rate);
		long date = tau - 1;
		for(; !(onOrBefore(this, new HinduSolar(date))); ++date);
		return date;
	}
	
	
	/*- hindu-solar-from-fixed -*/

	// TYPE fixed-date -> hindu-solar-date
	// Old Hindu solar date equivalent to fixed $date$.
	
	public void fromFixed(long date) {
		double critical = sunrise(date + 1);
		month = zodiac(critical);
		year = calendarYear(critical) - SOLAR_ERA;
		long approx = date - 3 - (long)mod(Math.floor(solarLongitude(critical)), deg(30));
		long begin = approx;
		for(; !(zodiac(sunrise(1 + begin)) == month); ++begin);
		day = (int)(date - begin + 1);
	}

	
	//
	// support methods
	//


	/*- hindu-sine-table -*/

	// TYPE integer -> rational-amplitude
	// This simulates the Hindu sine table.
	// $entry$ is an angle given as a multiplier of 225'.
	
	public static double hinduSineTable(int entry) {
		double exact = 3438 * sinDegrees(entry * 225d/60);
		double error = 0.215 * signum(exact) * signum(Math.abs(exact) - 1716);
		return Math.round(exact + error) / 3438d;
	}
	
	
	/*- hindu-sine -*/

	// TYPE angle -> rational-amplitude
	// Linear interpolation in Hindu table is used.
	
	public static double hinduSine(double theta) {
		double entry = theta * 60d/225;
		double fraction = mod(entry, 1);
		return fraction * hinduSineTable((int)Math.ceil(entry)) +
			(1 - fraction) * hinduSineTable((int)Math.floor(entry));
	}


	/*- hindu-arcsin -*/

	// TYPE rational-amplitude -> rational-angle
	// Inverse of Hindu sine function.
	
	public static double hinduArcsin(double amp) {
		boolean neg = amp < 0;
		if(neg)
			amp = -amp;
			
		int pos = 0;
		for(; amp > hinduSineTable(pos); ++pos);
		double below = hinduSineTable(pos - 1);
		double result = 225d/60 * (pos - 1 + (amp - below) / (hinduSineTable(pos) - below));
		
		if(neg)
			result = -result;
		
		return result;
	}


	/*- mean-position -*/

	// TYPE (rational-moment rational) -> rational-angle
	// Position in degrees at moment $tee$ in uniform circular
	// orbit of $period$ days.
	
	public static double meanPosition(double tee, double period) {
		return deg(360) * mod((tee - CREATION) / period, 1);
	}


	/*- true-position -*/

	// TYPE (rational-moment rational rational rational
	// TYPE  rational) -> rational-angle
	// Longitudinal position at moment $tee$.  $period$ is
	// period of mean motion in days.  $size$ is ratio of
	// radii of epicycle and deferent.  $anomalistic$ is the
	// period of retrograde revolution about epicycle.
	// $change$ is maximum decrease in epicycle size.
	
	public static double truePosition(double tee, double period, double size, double anomalistic, double change) {
		double aLong = meanPosition(tee, period);
		double offset = hinduSine(meanPosition(tee, anomalistic));
		double contraction = Math.abs(offset) * change * size;
		double equation = hinduArcsin(offset * (size - contraction));
		return mod(aLong - equation, 360);
	}
	
	
	/*- hindu-solar-longitude -*/

	// TYPE rational-moment -> rational-angle
	// Solar longitude at moment $tee$.
	
	public static double solarLongitude(double tee) {
		return truePosition(tee, SIDEREAL_YEAR, 14d/360, ANOMALISTIC_YEAR, 1d/42);
	}


	/*- hindu-zodiac -*/

	// TYPE rational-moment -> hindu-solar-month
	// Zodiacal sign of the sun, as integer in range 1..12,
	// at moment $tee$.
	
	public static int zodiac(double tee) {
		return (int)quotient(solarLongitude(tee), deg(30)) + 1;
	}


	/*- hindu-solar-on-or-before? -*/

	// TYPE (hindu-solar-date hindu-solar-date) -> boolean
	// True if Hindu solar $s-date1$ is on or before $s-date2$.
	
	public static boolean onOrBefore(HinduSolar d1, HinduSolar d2) {
		return d1.year < d2.year || d1.year == d2.year && (d1.month < d2.month || d1.month == d2.month && d1.day <= d2.day);
	}
	
	
	/*- hindu-calendar-year -*/

	// TYPE rational-moment -> hindu-solar-year
	// Determine solar year at given moment $tee$.
	
	public static long calendarYear(double tee) {
		return (long)Math.round((tee - OldHinduSolar.EPOCH) / SIDEREAL_YEAR - solarLongitude(tee) / deg(360));
	}
	
	
	/*- hindu-equation-of-time -*/

	// TYPE fixed-date -> rational-moment
	// Time from mean to true midnight of $date$.
	// (This is a gross approximation to the correct value.)
	
	public static double equationOfTime(long date) {
		double offset = hinduSine(meanPosition(date, ANOMALISTIC_YEAR));
		double equationSun = offset * 3438d/60 * (Math.abs(offset) / 1080 - 14d/360);
		return dailyMotion(date) * 1d/360 * equationSun * 1d/360 * SIDEREAL_YEAR;
	}


	/*- ascensional-difference -*/

	// TYPE (fixed-date location) -> rational-angle
	// Difference between right and oblique ascension
	// of sun on $date$ at $locale$.
	
	public static double ascensionalDifference(long date, Location locale) {
		double sinDecl = 1397d/3438 * hinduSine(tropicalLongitude(date));
		double lat = locale.latitude;
		double diurnalRadius = hinduSine(deg(90) + hinduArcsin(sinDecl));
		double tanLat = hinduSine(lat) / hinduSine(deg(90) + lat);
		double earthSine = sinDecl * tanLat;
		return hinduArcsin(-(earthSine / diurnalRadius));
	}
	
	
	/*- hindu-tropical-longitude -*/

	// TYPE fixed-date -> rational-angle
	// Hindu tropical longitude on fixed $date$.
	// Assumes precession with maximum of 27 degrees
	// and period of 7200 sidereal years
	// (= 1577917828/600 days).
	
	public static double tropicalLongitude(long date) {
		long days = (long)Math.floor(date - OldHinduSolar.EPOCH);
		double precession = deg(27) - Math.abs(deg(54) - mod(deg(27) + deg(108) * (600d / 1577917828d) * days, 108));
		return mod(solarLongitude(date) - precession, 360);
	}
	
	
	/*- rising-sign -*/

	// TYPE fixed-date -> rational-amplitude
	// Tabulated speed of rising of current zodiacal sign on
	// $date$.
	
	public static double risingSign(long date) {
		int index = (int)mod(quotient(tropicalLongitude(date), deg(30)), 6);
		return rs[index];
	}
	private static final double[] rs = new double[] {1670d/1800, 1795d/1800, 1935d/1800, 1935d/1800, 1795d/1800, 1670d/1800};


	/*- daily-motion -*/

	// TYPE fixed-date -> rational-angle
	// Sidereal daily motion of sun on $date$.
	
	public static double dailyMotion(long date) {
		double meanMotion = deg(360) / SIDEREAL_YEAR;
		double anomaly = meanPosition(date, ANOMALISTIC_YEAR);
		double epicycle = 14d/360 - Math.abs(hinduSine(anomaly)) / 1080;
		int entry = (int)quotient(anomaly, deg(225) / 60);
		double sineTableStep = hinduSineTable(entry + 1) - hinduSineTable(entry);
		double factor = sineTableStep * (-3438/225) * epicycle;
		return meanMotion * (factor + 1);
	}
	
	
	/*- solar-sidereal-difference -*/

	// TYPE fixed-date -> rational-angle
	// Difference between solar and sidereal day on $date$.
	
	public static double solarSiderealDifference(long date) {
		return dailyMotion(date) * risingSign(date);
	}


	/*- hindu-sunrise -*/

	// TYPE fixed-date -> rational-moment
	// Sunrise at hindu-locale on $date$.
	
	public static double sunrise(long date) {
		return date + 1d/4 + (UJJAIN.longitude - HINDU_LOCALE.longitude) / deg(360) +
			equationOfTime(date) + (1577917828d/1582237828d / deg(360)) *
				(ascensionalDifference(date, HINDU_LOCALE) + 1d/4 * solarSiderealDifference(date));
	}


	//
	// auxiliary methods
	//


	/*- alt-hindu-sunrise -*/

	// TYPE fixed-date -> rational-moment
	// Astronomical sunrise at Hindu locale on $date$,
	// rounded to nearest minute, as a rational number.
	
	public static double altSunrise(long date) {
		try {
			double rise = Date.sunrise(date, UJJAIN);
			return 1d/24 * 1d/60 * Math.round(rise * 24 * 60);
		} catch(BogusTimeException ex) {
			return 0;	// should never happen unless Ujjain is moved to the north pole
		}
	}
	
	
	/*- hindu-solar-longitude-after -*/

	// TYPE (moment season) -> moment
	// Moment UT of the first time at or after $tee$
	// when Hindu solar longitude will be $phi$ degrees.
	
	public static double solarLongitudeAfter(double tee, double phi) {
		double varepsilon = 1d/1000000;
		double tau = tee + SIDEREAL_YEAR * (1d/360) * mod(phi - solarLongitude(tee), deg(360));
		double l = Math.max(tee, tau - 5);
		double u = tau + 5;
		double lo = l, hi = u, x = (hi + lo) / 2;
		while(hi - lo >= varepsilon) {
			if(mod(solarLongitude(x) - phi, 360) < deg(180))
				hi = x;
			else
				lo = x;
			
			x = (hi + lo) / 2;
		}
		return x;
	}


	/*- mesha-samkranti -*/

	// TYPE gregorian-year -> rational-moment
	// Fixed moment of Mesha samkranti (Vernal equinox)
	// in Gregorian $g-year$.

	public static double meshaSamkranti(long gYear) {
		long jan1 = Gregorian.toFixed(gYear, JANUARY, 1);
		return solarLongitudeAfter(jan1, deg(0));
	}
	
	
	//
	// object methods
	//
	
	public String format() {
		return java.text.MessageFormat.format("{0}, {1} {2} {3,number,#} S.E.",
			new Object[]{
				nameFromDayOfWeek(toFixed(), OldHinduSolar.dayOfWeekNames),
				new Integer(day),
				nameFromMonth(month, OldHinduLunar.monthNames),
				new Long(year)
			}
		);
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof HinduSolar))
			return false;
		
		return internalEquals(obj);
	}
}

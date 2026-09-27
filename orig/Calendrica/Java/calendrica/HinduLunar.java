package calendrica;


public class HinduLunar extends Date {
	
	//
	// fields
	//


	public long		year;
	public int		month;
	public boolean	leapMonth;
	public int		day;
	public boolean	leapDay;


	//
	// constructors
	//


	public HinduLunar() { }
	
	public HinduLunar(long date) {
		super(date);
	}
	
	public HinduLunar(Date date) {
		super(date);
	}
	
	public HinduLunar(long year, int month, boolean leapMonth, int day, boolean leapDay) {
		this.year		= year;
		this.month		= month;
		this.leapMonth	= leapMonth;
		this.day		= day;
		this.leapDay	= leapDay;
	}

	
	//
	// constants
	//


	/*- hindu-lunar-era -*/

	// TYPE standard-year
	// Years from Kali Yuga until Vikrama era.
	
	public static final int LUNAR_ERA = 3044;


	/*- hindu-synodic-month -*/

	// TYPE rational
	// Mean time from new moon to new moon.
	
	public static final double SYNODIC_MONTH = 29 + 7087771d/13358334;


	/*- hindu-sidereal-month -*/

	// TYPE rational
	// Mean length of Hindu sidereal month.
	
	public static final double SIDEREAL_MONTH = 27 + 4644439d/14438334;


	/*- hindu-anomalistic-month -*/

	// TYPE rational
	// Time from apogee to apogee, with bija correction.
	
	public static final double ANOMALISTIC_MONTH = 1577917828d / (57753336 - 488199);
	
	//
	// date conversion methods
	//
	
	
	/*- fixed-from-hindu-lunar -*/

	// TYPE hindu-lunar-date -> fixed-date
	// Fixed date corresponding to Hindu lunar date $l-date$.
	
	public static long toFixed(long year, int month, boolean leapMonth, int day, boolean leapDay) {
		return new HinduLunar(year, month, leapMonth, day, leapDay).toFixed();
	}

	public long toFixed() {
		double approx = OldHinduSolar.EPOCH + HinduSolar.SIDEREAL_YEAR *
			(year + LUNAR_ERA + (month - 1) / 12d);
		long s = (long)Math.floor(approx - (1 / deg(360)) *
			(mod(HinduSolar.solarLongitude(approx) - (month - 1) * deg(30) + deg(180), deg(360)) - 180));
		int k = lunarDay(s + 1d/4);
		long x;
		if(3 < k && k < 27) {
			x = k;
		} else {
			HinduLunar mid = new HinduLunar(s - 15);
			if(mid.month < month || (mid.leapMonth && !leapMonth))
				x = mod(k + 15, 30) - 15;
			else
				x = mod(k - 15, 30) + 15;
		}
		long est = s + day - x;
		long tau = est - mod(lunarDay(est + 1d/4) - day + 15, 30) + 15;
		long date = tau - 1;
		for(; !(onOrBefore(this, new HinduLunar(date))); ++date);
		return date;
	}
	
	
	/*- hindu-lunar-from-fixed -*/

	// TYPE fixed-date -> hindu-lunar-date
	// Hindu lunar date equivalent to fixed $date$.
	
	public void fromFixed(long date) {
		double critical = HinduSolar.sunrise(date);
		day = lunarDay(critical);
		leapDay = day == lunarDay(HinduSolar.sunrise(date - 1));
		double lastNewMoon = newMoonBefore(critical);
		double nextNewMoon = newMoonBefore(Math.floor(lastNewMoon) + 35);
		int solarMonth = HinduSolar.zodiac(lastNewMoon);
		leapMonth = solarMonth == HinduSolar.zodiac(nextNewMoon);
		month = adjustedMod(solarMonth + 1, 12);
		year = HinduSolar.calendarYear(nextNewMoon) -
			LUNAR_ERA -
			(leapMonth && month == 1 ? -1 : 0);
	}
	
	public void fromArray(int[] a) {
		year		= a[0];
		month		= a[1];
		leapMonth	= a[2] != 0;
		day			= a[3];
		leapDay		= a[4] != 0;
	}

	
	//
	// support methods
	//
	
	
	/*- hindu-new-moon-before -*/

	// TYPE rational-moment -> rational-moment
	// Approximate moment of last new moon preceding
	// moment $tee$.
	
	public static double newMoonBefore(double tee) {
		double varepsilon = Math.pow(2, -34);
		double tau = tee - (1.0d / deg(360)) * lunarPhase(tee) * SYNODIC_MONTH;
		double l = tau - 1;
		double u = Math.min(tee, tau + 1);
		double lo = l, hi = u, x = (hi + lo) / 2;
		while(!(HinduSolar.zodiac(lo) == HinduSolar.zodiac(hi) || hi - lo < varepsilon)) {
			if(lunarPhase(x) < deg(180))
				hi = x;
			else
				lo = x;
			
			x = (hi + lo) / 2;
		}
		return x;
	}


	/*- hindu-lunar-on-or-before? -*/

	// TYPE (hindu-lunar-date hindu-lunar-date) -> boolean
	// True if Hindu lunar date $l-date1$ is on or before
	// Hindu lunar date $l-date2$.
 	
	public static boolean onOrBefore(HinduLunar d1, HinduLunar d2) {
		return d1.year < d2.year || d1.year == d2.year && (d1.month < d2.month || 
			d1.month == d2.month && (d1.leapMonth && !d2.leapMonth || 
				d1.leapMonth == d2.leapMonth && (d1.day < d2.day || d1.day == d2.day && (!d1.leapDay || d2.leapDay))));
	}
	
	
	/*- lunar-day-after -*/

	// TYPE (rational-moment rational) -> rational-moment
	// Time lunar-day (tithi) number $k$ begins at or after
	// moment $tee$.  $k$ can be fractional (for karanas).
	
	public static double lunarDayAfter(double tee, double k) {
		double varepsilon = Math.pow(2, -17);
		double phase = (k - 1) * 12;
		double tau = tee + (1d / 360) * mod(phase - lunarPhase(tee), deg(360)) * SYNODIC_MONTH;
		double l = Math.max(tee, tau - 2);
		double u = tau + 2;
		double lo = l, hi = u, x = (hi + lo) / 2;
		while(hi - lo >= varepsilon) {
			if(mod(lunarPhase(x) - phase, 360) < deg(180))
				hi = x;
			else
				lo = x;
			
			x = (hi + lo) / 2;
		}
		return x;
	}
	
	
	/*- hindu-lunar-longitude -*/

	// TYPE rational-moment -> rational-angle
	// Lunar longitude at moment $tee$.
	
	public static double lunarLongitude(double tee) {
		return HinduSolar.truePosition(tee, SIDEREAL_MONTH, 32d/360, ANOMALISTIC_MONTH, 1d/96);
	}


	/*- hindu-lunar-phase -*/

	// TYPE rational-moment -> rational-angle
	// Longitudinal distance between the sun and moon
	// at moment $tee$.
	
	public static double lunarPhase(double tee) {
		return mod(lunarLongitude(tee) - HinduSolar.solarLongitude(tee), 360);
	}


	/*- lunar-day -*/

	// TYPE rational-moment -> hindu-lunar-day
	// Phase of moon (tithi) at moment $tee$, as an integer in
	// the range 1..30.
	
	public static int lunarDay(double tee) {
		return (int)quotient(lunarPhase(tee), deg(12)) + 1;
	}


	//
	// auxiliary methods
	//


	/*- lunar-station -*/

	// TYPE fixed-date -> nakshatra
	// Hindu lunar station (nakshatra) at sunrise on $date$.

	public static int lunarStation(long date) {
		double critical = HinduSolar.sunrise(date);
		return (int)quotient(lunarLongitude(critical), deg(800) / 60) + 1;
	}
	
	
	/*- hindu-lunar-new-year -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Hindu lunisolar new year
	// in Gregorian $g-year$.
	
	public static long newYear(long gYear) {
		long jan1 = Gregorian.toFixed(gYear, JANUARY, 1);
		double mina = HinduSolar.solarLongitudeAfter(jan1, deg(330));
		double newMoon = lunarDayAfter(mina, 1);
		long hDay = (long)Math.floor(newMoon);
		double critical = HinduSolar.sunrise(hDay);
		return hDay + (newMoon < critical || lunarDay(HinduSolar.sunrise(hDay + 1)) == 2 ? 0 : 1);
	}
	
	
	/*- karana -*/

	// TYPE {1-60} -> {0-10}
	// Number (0-10) of the name of the $n$-th (1-60)
	// Hindu karana.
	
	public static int karana(int n) {
		if(n == 1)
			return 0;
		else if(n > 57)
			return n - 50;
		else
			return adjustedMod(n - 1, 7);
	}


	/*- yoga -*/

	// TYPE fixed-date -> {1-27}
	// Hindu yoga on $date$.

	public static int yoga(long date) {
		return (int)Math.floor(
			mod((HinduSolar.solarLongitude(date) +
				lunarLongitude(date)) * 60d/800, deg(27))
		) + 1;
	}


	/*- sacred-wednesdays-in-gregorian -*/

	// TYPE gregorian-year -> list-of-fixed-dates
	// List of Wednesdays in Gregorian year $g-year$
	// that are day 8 of Hindu lunar months.

	public static FixedVector sacredWednesdaysInGregorian(long gYear) {
		return sacredWednesdays(
			Gregorian.toFixed(gYear, JANUARY, 1),
			Gregorian.toFixed(gYear, DECEMBER, 31)
		);
	}


	/*- sacred-wednesdays -*/

	// TYPE gregorian-year -> list-of-fixed-dates
	// List of Wednesdays between fixed dates $start$ and
	// $end$ (inclusive) that are day 8 of Hindu lunar months.

	public static FixedVector sacredWednesdays(long start, long end) {
		long wed = kDayOnOrAfter(start, WEDNESDAY);
		FixedVector result = new FixedVector();
		while(wed <= end) {
			HinduLunar hDate = new HinduLunar(wed);
			if(hDate.day == 8)
				result.addFixed(wed);
			wed += 7;
		}
		return result;
	}


	//
	// object methods
	//


	protected String toStringFields() {
		return  "year=" + year + ",month=" + month + ",leapMonth=" + leapMonth + ",day=" + day +
			",leapDay=" + leapDay;
	}
	
	public String format() {
		return java.text.MessageFormat.format("{0}, {1}{2} {3}{4} {5,number,#} V.E.",
			new Object[]{
				nameFromDayOfWeek(toFixed(), OldHinduLunar.dayOfWeekNames),
				new Integer(day),
				leapDay ? " II" : "",
				nameFromMonth(month, OldHinduLunar.monthNames),
				leapMonth ? " II" : "",
				new Long(year)
			}
		);
	}

	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(!(obj instanceof HinduLunar))
			return false;
		
		HinduLunar o = (HinduLunar)obj;
		
		return
			o.year		== year			&&
			o.month		== month		&&
			o.leapMonth	== leapMonth	&&
			o.day		== day			&&
			o.leapDay	== leapDay		;
	}
}

package calendrica;


public class OldHinduSolar extends StandardDate {

	//
	// constructors
	//

	public OldHinduSolar() { }
	
	public OldHinduSolar(long date) {
		super(date);
	}
	
	public OldHinduSolar(Date date) {
		super(date);
	}
	
	public OldHinduSolar(long year, int month, int day) {
		super(year, month, day);
	}
	
	//
	// constants
	//


	/*- hindu-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of the Hindu calendar (Kali Yuga).

	public static final long EPOCH = Julian.toFixed(Julian.BCE(3102), FEBRUARY, 18);


	/*- arya-solar-year -*/

	// TYPE rational
	// Length of Old Hindu solar year.

	public static final double ARYA_SOLAR_YEAR = 1577917500d/4320000;


	/*- arya-solar-month -*/

	// TYPE rational
	// Length of Old Hindu solar month.

	public static final double ARYA_SOLAR_MONTH = ARYA_SOLAR_YEAR / 12;


	/*- arya-jovian-period -*/

	// TYPE rational
	// Number of days in one revolution of Jupiter around the
	// Sun.

	public static final double ARYA_JOVIAN_PERIOD = 1577917500d/364224;

	
	//
	// date conversion methods
	//
	
	
	/*- fixed-from-old-hindu-solar -*/

	// TYPE hindu-solar-date -> fixed-date
	// Fixed date corresponding to Old Hindu solar date.
	
	public static long toFixed(long year, int month, int day) {
		return (long)Math.floor(
			EPOCH +
			year * ARYA_SOLAR_YEAR +
			(month - 1) * ARYA_SOLAR_MONTH +
			day - 1d/4
		);
	}

	public long toFixed() {
		return toFixed(year, month, day);
	}
	

	/*- old-hindu-solar-from-fixed -*/

	// TYPE fixed-date -> hindu-solar-date
	// Old Hindu solar date equivalent to fixed $date$.

	public void fromFixed(long date) {
		double sun = dayCount(date) + 1d/4;
		year = quotient(sun, ARYA_SOLAR_YEAR);
		month = 1 + (int)mod(quotient(sun, ARYA_SOLAR_MONTH), 12);
		day = 1 + (int)Math.floor(mod(sun, ARYA_SOLAR_MONTH));
	}

	
	//
	// support methods
	//


	/*- hindu-day-count -*/

	// TYPE hindu-moment -> hindu-moment
	// Elapsed days (Ahargana) to $date$ since Hindu epoch (KY).

	public static long dayCount(long date) {
		return date - EPOCH;
	}
	
	public static double dayCount(double date) {
		return date - EPOCH;
	}

	
	//
	// auxiliary methods
	//


	/*- jovian-year -*/

	// TYPE fixed-date -> {1-60}
	// Year of Jupiter cycle at fixed $date$.

	public static int jovianYear(long date) {
		return 1 + (int)mod(quotient(dayCount(date), ARYA_JOVIAN_PERIOD / 12), 60);
	}

	
	//
	// object methods
	//

	public static final String[] dayOfWeekNames = new String[] {
		"Ravivara", 
		"Chandravara", 
		"Mangalavara", 
		"Buddhavara", 
		"Brihaspatvara", 
		"Sukravara", 
		"Sanivara"};

	public static final String[] monthNames = new String[] {
		"Mesha",
		"Vrishabha",
		"Mithuna",
		"Karka",
		"Simha",
		"Kanya",
		"Tula",
		"Vrischika",
		"Dhanu",
		"Makara",
		"Kumbha",
		"Mina"};
	
	public String format() {
		return java.text.MessageFormat.format("{0}, {1} {2} {3,number,#} K.Y.",
			new Object[]{
				nameFromDayOfWeek(toFixed(), dayOfWeekNames),
				new Integer(day),
				nameFromMonth(month, monthNames),
				new Long(year)
			}
		);
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof OldHinduSolar))
			return false;
		
		return internalEquals(obj);
	}
}

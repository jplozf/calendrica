package calendrica;


public class Persian extends StandardDate {

	//
	// constructors
	//

	public Persian() { }
	
	public Persian(long date) {
		super(date);
	}
	
	public Persian(Date date) {
		super(date);
	}
	
	public Persian(long year, int month, int day) {
		super(year, month, day);
	}
	
	//
	// constants
	//


	/*- persian-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of the Persian calendar.

	public static final long EPOCH = Julian.toFixed(Julian.CE(622), MARCH, 19);
	
	
	/*- tehran -*/

	// TYPE location
	// Location of Tehran, Iran.
	
	public static final Location TEHRAN = new Location("Tehran, Iran", deg(35.68), deg(51.42), mt(1100), 3.5);

	
	//
	// date conversion methods
	//
	

	/*- fixed-from-persian -*/

	// TYPE persian-date -> fixed-date
	// Fixed date of Astronomical Persian date.

	public static long toFixed(long year, int month, int day) {
		long newYear = newYearOnOrBefore(EPOCH + 180 + (long)Math.floor(MEAN_TROPICAL_YEAR * (0 < year ? year - 1 : year)));
		return ((newYear - 1)
			+ (month <= 7 ? 31 * (month - 1) : 30 * (month - 1) + 6)
			+ day);
	}

	public long toFixed() {
		return toFixed(year, month, day);
	}
	
	
	/*- persian-from-fixed -*/

	// TYPE fixed-date -> persian-date
	// Astronomical Persian date (year month day)
	// corresponding to fixed $date$.
	
	public void fromFixed(long date) {
		long newYear = newYearOnOrBefore(date);
		long y = 1 + (long)Math.round((newYear - EPOCH) / MEAN_TROPICAL_YEAR);
		year = 0 < y ? y : y - 1;
		long dayOfYear = 1 + date - toFixed(year, 1, 1);
		month = dayOfYear < 186 ? (int)Math.ceil(dayOfYear / 31d) : (int)Math.ceil((dayOfYear - 6) / 30d);
		day = (int)(date - (toFixed(year, month, 1) - 1));
	}

	
	//
	// support methods
	//
	
	
	/*- midday-in-tehran -*/

	// TYPE fixed-date -> angle
	// Universal time of midday on fixed $date$ in Tehran.
	
	public static double middayInTehran(long date) {
		return universalFromStandard(midday(date, TEHRAN), TEHRAN);
	}
	
	
	/*- persian-new-year-on-or-before -*/

	// TYPE fixed-date -> fixed-date
	// Fixed date of Astronomical Persian New Year on or
	// before fixed $date$.
	
	public static long newYearOnOrBefore(long date) {
		double approx = estimatePriorSolarLongitude(middayInTehran(date), SPRING);
		long i;
		for(i = (long)Math.floor(approx) - 1; !(solarLongitude(middayInTehran(i)) <= SPRING + deg(2)); ++i);
		return i;
	}

	
	//
	// auxiliary methods
	//


	/*- naw-ruz -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Persian New Year (Naw-Ruz) in Gregorian
	// year.

	public static long nawRuz(long gYear) {
		long pYear = 1 + gYear - Gregorian.yearFromFixed(EPOCH);
		return toFixed(pYear <= 0 ? pYear - 1 : pYear, 1, 1);
	}

	
	//
	// object methods
	//

	public static final String[] dayOfWeekNames = new String[] {
		"Yek-shanbeh", 
		"Do-shanbeh", 
		"Se-shanbeh", 
		"Char-shanbeh", 
		"Panj-shanbeh", 
                "Jom`eh",
                "Shanbeh"}; 

	public static final String[] monthNames = new String[] {
		"Farvardin",
		"Ordibehesht",
		"Xordad",
		"Tir",
		"Mordad",
		"Shahrivar",
		"Mehr",
		"Aban",
		"Azar",
		"Dey",
		"Bahman",
		"Esfand"};
	
	public String format() {
		return java.text.MessageFormat.format("{0}, {1} {2} {3,number,#} A.P.",
			new Object[]{
				nameFromDayOfWeek(toFixed(), dayOfWeekNames),
				new Integer(day),
				nameFromMonth(month, monthNames),
				new Long(year)
			}
		);
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof Persian))
			return false;
		
		return internalEquals(obj);
	}
}

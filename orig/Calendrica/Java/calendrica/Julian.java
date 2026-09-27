package calendrica;


public class Julian extends StandardDate {

	//
	// constants
	//


	/*- julian-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of the Julian calendar.
	
	public static final long EPOCH = Gregorian.toFixed(0, DECEMBER, 30);


	//
	// constructors
	//


	public Julian() { }
	
	public Julian(long date) {
		super(date);
	}
	
	public Julian(Date date) {
		super(date);
	}
	
	public Julian(long year, int month, int day) {
		super(year, month, day);
	}
	
	
	//
	// date conversion methods
	//


	/*- fixed-from-julian -*/

	// TYPE julian-date -> fixed-date
	// Fixed date equivalent to the Julian date.

	public static long toFixed(long year, int month, int day) {
		long y = year < 0 ? year + 1 : year;
		return EPOCH - 1
			+ 365 * (y - 1)
			+ quotient(y - 1, 4)
			+ quotient(367 * month - 362, 12)
			+ (month <= 2 ? 0 :
				(isLeapYear(year) ? -1 : -2))
			+ day;
	}
	
	public long toFixed() {
		return toFixed(year, month, day);
	}
	
	
	/*- julian-from-fixed -*/

	// TYPE fixed-date -> julian-date
	// Julian (year month day) corresponding to fixed $date$.
	
	public void fromFixed(long date) {
		long approx = quotient(4 * (date - EPOCH) + 1464, 1461);
		year = approx <= 0 ? approx - 1 : approx;
		long priorDays = date - toFixed(year, JANUARY, 1);
		int correction = date < toFixed(year, MARCH, 1) ? 0 : (isLeapYear(year) ? 1 : 2);
		month = (int)quotient(12 * (priorDays + correction) + 373, 367);
		day = (int)(date - toFixed(year, month, 1) + 1);
	}
	
	
	//
	// support methods
	//


	/*- bce -*/

	// TYPE standard-year -> julian-year
	// Negative value to indicate a BCE Julian year.

	public static long BCE(long n) {
		return -n;
	}


	/*- ce -*/

	// TYPE standard-year -> julian-year
	// Positive value to indicate a CE Julian year.
	
	public static long CE(long n) {
		return n;
	}


	/*- julian-leap-year? -*/

	// TYPE julian-year -> boolean
	// True if $j-year$ is a leap year on the Julian calendar.
	
	public static boolean isLeapYear(long jYear) {
		return mod(jYear, 4) == (jYear > 0 ? 0 : 3);
	}
	
	//
	// auxiliary methods
	//


	/*- julian-in-gregorian -*/

	// TYPE (julian-month julian-day gregorian-year)
	// TYPE -> list-of-fixed-dates
	// List of the fixed dates of Julian month, day
	// that occur in Gregorian year.
	
	public static FixedVector inGregorian(int jMonth, int jDay, long gYear) {
		long jan1 = Gregorian.toFixed(gYear, JANUARY, 1);
		long dec31 = Gregorian.toFixed(gYear, DECEMBER, 31);
		long y = new Julian(jan1).year;
		long yPrime = y == -1 ? 1 : y + 1;
		long date1 = toFixed(y, jMonth, jDay);
		long date2 = toFixed(yPrime, jMonth, jDay);
		FixedVector result = new FixedVector(1, 1);
		if(jan1 <= date1 && date1 <= dec31)
			result.addFixed(date1);
		if(jan1 <= date2 && date2 <= dec31)
			result.addFixed(date2);
		return result;
	}


	/*- eastern-orthodox-christmas -*/

	// TYPE gregorian-year -> list-of-fixed-dates
	// List of zero or one fixed dates of Eastern Orthodox
	// Christmas in Gregorian year.
	
	public static FixedVector easternOrthodoxChristmas(long gYear) {
		return inGregorian(DECEMBER, 25, gYear);
	}
	
	
	//
	// object methods
	//
	
	public String format() {
		return java.text.MessageFormat.format("{0} {1} {2,number,#} {3}",
			new Object[]{
				new Integer(day),
				nameFromMonth(month, Gregorian.monthNames),
				new Long(year < 0 ? -year : year),
				year < 0 ? "B.C.E." : "C.E."
			}
		);
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof Julian))
			return false;
		
		return internalEquals(obj);
	}
}

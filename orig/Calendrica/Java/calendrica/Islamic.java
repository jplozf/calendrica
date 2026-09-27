package calendrica;


public class Islamic extends StandardDate {

	//
	// constructors
	//


	public Islamic() { }
	
	public Islamic(long date) {
		super(date);
	}
	
	public Islamic(Date date) {
		super(date);
	}
	
	public Islamic(long year, int month, int day) {
		super(year, month, day);
	}

	
	//
	// constants
	//


	/*- islamic-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of the Islamic calendar.
	
	public static final long EPOCH = Julian.toFixed(Julian.CE(622), JULY, 16);
	
	
	/*- mecca -*/

	// TYPE location
	// Location of Mecca.
	
	public static final Location MECCA = new Location("Mecca, Saudi Arabia", angle(21, 25, 24), angle(39, 49, 24), mt(1000), 2);
	
	
	//
	// date conversion methods
	//
	
	
	/*- fixed-from-islamic -*/

	// TYPE islamic-date -> fixed-date
	// Fixed date equivalent to Islamic date.
	
	public static long toFixed(long year, int month, int day) {
		return day
			+ 29 * (month - 1)
			+ quotient((6 * month) - 1, 11)
			+ (year - 1) * 354
			+ quotient(3 + 11 * year, 30)
			+ EPOCH - 1;
	}

	public long toFixed() {
		return toFixed(year, month, day);
	}
	
	
	/*- islamic-from-fixed -*/

	// TYPE fixed-date -> islamic-date
	// Islamic date (year month day) corresponding to fixed
	// $date$.
	
	public void fromFixed(long date) {
		year = quotient(30 * (date - EPOCH) + 10646, 10631);
		long priorDays = date - toFixed(year, 1, 1);
		month = (int)quotient(11 * priorDays + 330, 325);
		day = (int)(1 + date - toFixed(year, month, 1));
	}
	
	
	//
	// support methods
	//


	/*- islamic-leap-year? -*/

	// TYPE islamic-year -> boolean
	// True if $i-year$ is an Islamic leap year.
	
	public static boolean isLeapYear(long iYear) {
		return mod(11 * iYear + 14, 30) < 11;
	}
	


	/*- asr -*/
	
	public static double asr(long date, Location locale)
		throws BogusTimeException
	{
		double noon = universalFromStandard(midday(date, locale), locale);
		double phi = locale.latitude;
		double delta = arcSinDegrees(sinDegrees(obliquity(noon)) * sinDegrees(solarLongitude(noon)));
		double altitude = arcSinDegrees(sinDegrees(phi) * sinDegrees(delta) + cosDegrees(phi) * cosDegrees(delta));
		double h = arcTanDegrees(tanDegrees(altitude) / (1 + 2 * tanDegrees(altitude)), 1);
		return dusk(date, locale, -h);
	}

	
	//
	// auxiliary methods
	//


	/*- islamic-in-gregorian -*/

	// TYPE (islamic-month islamic-day gregorian-year)
	// TYPE -> list-of-fixed-dates
	// List of the fixed dates of Islamic month, day
	// that occur in Gregorian year.
	
	public static FixedVector inGregorian(int iMonth, int iDay, long gYear) {
		long jan1 = Gregorian.toFixed(gYear, JANUARY, 1);
		long dec31 = Gregorian.toFixed(gYear, DECEMBER, 31);
		long y = new Islamic(jan1).year;
		long date1 = toFixed(y, iMonth, iDay);
		long date2 = toFixed(y + 1, iMonth, iDay);
		long date3 = toFixed(y + 2, iMonth, iDay);
		FixedVector result = new FixedVector(1, 1);
		if(jan1 <= date1 && date1 <= dec31)
			result.addFixed(date1);
		if(jan1 <= date2 && date2 <= dec31)
			result.addFixed(date2);
		if(jan1 <= date3 && date3 <= dec31)
			result.addFixed(date3);
		return result;
	}


	/*- mawlid-an-nabi -*/

	// TYPE gregorian-year -> list-of-fixed-dates
	// List of fixed dates of Mawlid-an-Nabi occurring in
	// Gregorian year.
	
	public static FixedVector mawlidAnNabi(long gYear) {
		return inGregorian(3, 12, gYear);
	}
	
	
	//
	// object methods
	//


	public static final String[] dayOfWeekNames = new String[] {
		"yaum al-ahad", 
		"yaum al-ithnayna", 
		"yaum ath-thalatha'", 
		"yaum al-arba`a'", 
		"yaum al-hamis", 
		"yaum al-jum`a", 
		"yaum as-sabt"};

	public static final String[] monthNames = new String[] {
		"Muharram",
		"Safar",
		"Rabi I",
		"Rabi II",
		"Jumada I",
		"Jumada II",
		"Rajab",
		"Sha`ban",
		"Ramadan",
		"Shawwal",
		"Dhu al-Qa`da",
		"Dhu al-Hijja"};
	
	public String format() {
		return java.text.MessageFormat.format("{0}, {1} {2} {3,number,#} A.H.",
			new Object[]{
				nameFromDayOfWeek(toFixed(), dayOfWeekNames),
				new Integer(day),
				nameFromMonth(month, monthNames),
				new Long(year)
			}
		);
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof Islamic))
			return false;
		
		return internalEquals(obj);
	}
}

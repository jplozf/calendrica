package calendrica;


public class Coptic extends StandardDate {

	//
	// constructors
	//

	public Coptic() { }
	
	public Coptic(long date) {
		super(date);
	}
	
	public Coptic(Date date) {
		super(date);
	}
	
	public Coptic(long year, int month, int day) {
		super(year, month, day);
	}
	
	//
	// constants
	//


	/*- coptic-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of the Coptic calendar.
	
  	public static final long EPOCH = Julian.toFixed(Julian.CE(284), AUGUST, 29);

	
	//
	// date conversion methods
	//
	

	/*- fixed-from-coptic -*/

	// TYPE coptic-date -> fixed-date
	// Fixed date of Coptic date.
	
	public static long toFixed(long year, int month, int day) {
		return EPOCH - 1
			+ 365 * (year - 1)
			+ quotient(year, 4)
			+ 30 * (month - 1)
			+ day;
	}

	public long toFixed() {
		return toFixed(year, month, day);
	}
	
	
	/*- coptic-from-fixed -*/

	// TYPE fixed-date -> coptic-date
	// Coptic equivalent of fixed $date$.
	
	public void fromFixed(long date) {
		year = quotient(4 * (date - EPOCH) + 1463, 1461);
		month = 1 + (int)quotient(date - toFixed(year, 1, 1), 30);
		day = (int)(date + 1 - toFixed(year, month, 1));
	}
	
	//
	// support methods
	//


	/*- coptic-leap-year? -*/

	// TYPE coptic-year -> boolean
	// True if $c-year$ is a leap year on the Coptic calendar.
	
	public static boolean isLeapYear(long cYear) {
		return mod(cYear, 4) == 3;
	}	
	
	
	//
	// auxiliary methods
	//


	/*- coptic-in-gregorian -*/

	// TYPE (coptic-month coptic-day gregorian-year)
	// TYPE -> list-of-fixed-dates
	// List of the fixed dates of Coptic month, day
	// that occur in Gregorian year.
	
	public static FixedVector inGregorian(int cMonth, int cDay, long gYear) {
		long jan1 = Gregorian.toFixed(gYear, JANUARY, 1);
		long dec31 = Gregorian.toFixed(gYear, DECEMBER, 31);
		long y = new Coptic(jan1).year;
		long date1 = toFixed(y, cMonth, cDay);
		long date2 = toFixed(y + 1, cMonth, cDay);
		FixedVector result = new FixedVector(1, 1);
		if(jan1 <= date1 && date1 <= dec31)
			result.addFixed(date1);
		if(jan1 <= date2 && date2 <= dec31)
			result.addFixed(date2);
		return result;
	}


	/*- coptic-christmas -*/

	// TYPE gregorian-year -> list-of-fixed-dates
	// List of zero or one fixed dates of Coptic Christmas
	// in Gregorian year.
	
	public static FixedVector christmas(long gYear) {
		return inGregorian(4, 29, gYear);
	}
	
	
	//
	// object methods
	//
	
	public static final String[] dayOfWeekNames = new String[] {	
            "Tkyriaka",
            "Pesnau",
            "Pshoment",
            "Peftoou",
            "Ptiou",
            "Psoou",
            "Psabbaton"};

	public static final String[] monthNames = new String[] {
		"Tut",
		"Babah",
		"Hatur",
		"Kiyahk",
		"Tubah",
		"Amshir",
		"Baramhat",
		"Baramundah",
		"Bashans",
		"Ba'unah",
		"Abib",
		"Misra",
		"al-Nasi"};
	
	public String format() {
		return java.text.MessageFormat.format("{0}, {1} {2} {3,number,#} A.M.",
			new Object[]{
				nameFromDayOfWeek(toFixed(), dayOfWeekNames),
				new Integer(day),
				nameFromMonth(month, monthNames),
				new Long(year)
			}
		);
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof Coptic))
			return false;
		
		return internalEquals(obj);
	}
}

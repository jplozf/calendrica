package calendrica;


public class Bahai extends Date {
	
	//
	// fields
	//

	public long major;
	public int cycle;
	public int year;
	public int month;
	public int day;

	//
	// constructors
	//

	public Bahai() { }
	
	public Bahai(long date) {
		super(date);
	}
	
	public Bahai(Date date) {
		super(date);
	}
	
	public Bahai(long major, int cycle, int year, int month, int day) {
		this.major = major;
		this.cycle = cycle;
		this.year = year;
		this.month = month;
		this.day = day;
	}
	
	//
	// constants
	//


	/*- bahai-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of Bahai calendar.

	public static final long EPOCH = Gregorian.toFixed(1844, MARCH, 21);


	/*- ayyam-i-ha -*/

	// TYPE bahai-month
	// Signifies intercalary period of 4 or 5 days.

	public static final int AYYAM_I_HA = 0;
	
	
	//
	// date conversion methods
	//
	
	
	/*- fixed-from-bahai -*/

	// TYPE bahai-date -> fixed-date
	// Fixed date equivalent to the Bahai date.
	
	public static long toFixed(long major, int cycle, int year, int month, int day) {
		long gYear = 361 * (major - 1)
			+ 19 * (cycle - 1)
			+ year
			- 1
			+ Gregorian.yearFromFixed(EPOCH);
		long monthDays;
		if(month == AYYAM_I_HA)
			monthDays = 342;
		else if(month == 19) {
			monthDays = Gregorian.isLeapYear(gYear + 1) ? 347 : 346;
		} else {
			monthDays = 19 * (month - 1);
		}
		return Gregorian.toFixed(gYear, MARCH, 20)
			+ monthDays
			+ day;
	}

	public long toFixed() {
		return toFixed(major, cycle, year, month, day);
	}
	
	
	/*- bahai-from-fixed -*/

	// TYPE fixed-date -> bahai-date
	// Bahai (major cycle year month day) corresponding to fixed
	// $date$.
	
	public void fromFixed(long date) {
		long gYear = Gregorian.yearFromFixed(date);
		long start = Gregorian.yearFromFixed(EPOCH);
		long years = gYear
			- start
			- (date <= Gregorian.toFixed(gYear, MARCH, 20) ? 1 : 0);
		major = 1 + quotient(years, 361);
		cycle = 1 + (int)quotient(mod(years, 361), 19);
		year = 1 + (int)mod(years, 19);
		long days = date - toFixed(major, cycle, year, 1, 1);
		if(date >= toFixed(major, cycle, year, 19, 1)) {
			month = 19;
		} else if(date >= toFixed(major, cycle, year, AYYAM_I_HA, 1)) {
			month = AYYAM_I_HA;
		} else {
			month = (int)(1 + quotient(days, 19));
		}
		day = (int)(date + 1 - toFixed(major, cycle, year, month, 1));
	}
	
	
	public void fromArray(int[] a) {
		major	= a[0];
		cycle	= a[1];
		year	= a[2];
		month	= a[3];
		day		= a[4];
	}
	
	
	//
	// support methods
	//
	
	
	//
	// auxiliary methods
	//
	
	
	/*- bahai-new-year -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Bahai New Year in Gregorian year.
	
	public static long newYear(long gYear) {
		return Gregorian.toFixed(gYear, MARCH, 21);
	}
	
	
	//
	// object methods
	//

	protected String toStringFields() {
		return "major=" + major + ",cycle=" + cycle +
			",year=" + year + ",month=" + month + ",day=" + day;
	}

	public static final String[] dayOfWeekNames = new String[] {
		"Jamal", 
		"Kamal", 
		"Fidal", 
		"`Idal", 
		"Istijlal", 
		"Istiqlal",
		"Jalal"};

	public static final String[] dayOfMonthNames = new String[] {
		"Baha'",
		"Jalal",
		"Jamal",
		"`Azamat",
		"Nur",
		"Rahmat",
		"Kalimat",
		"Kamal",
		"Asma'",
		"`Izzat",
		"Mashiyyat",
		"`Ilm",
		"Qudrat",
		"Qawl",
		"Masa'il",
		"Sharaf",
		"Sultan",
		"Mulk",
		"`Ala'"};

	public static final String[] monthNames = new String[] {
		"Ayyam-i-Ha",
		"Baha'",
		"Jalal",
		"Jamal",
		"`Azamat",
		"Nur",
		"Rahmat",
		"Kalimat",
		"Kamal",
		"Asma'",
		"`Izzat",
		"Mashiyyat",
		"`Ilm",
		"Qudrat",
		"Qawl",
		"Masa'il",
		"Sharaf",
		"Sultan",
		"Mulk",
		"`Ala'"};

	public static final String[] yearNames = new String[] {
		"Alif",
		"Ba'",
		"Ab",
		"Dal",
		"Bab",
		"Vav",
		"Abad",
		"Jad",
		"Baha'",
		"Hubb",
		"Bahhaj",
		"Javab",
		"Ahad",
		"Vahhab",
		"Vidad",
		"Badi'",
		"Bahi",
		"Abha",
		"Vahid"};
	
	public String format() {
		return java.text.MessageFormat.format("{0}, {1} {2}, {3} of Vahid {4}, Kull-i-Shay {5} B.E.",
			new Object[]{
				nameFromDayOfWeek(toFixed(), dayOfWeekNames),
				nameFromNumber(day, dayOfMonthNames),
				nameFromMonth(month + 1, monthNames),
				nameFromNumber(year, yearNames),
				new Integer(cycle),
				new Long(major)
			}
		);
	}
	
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(!(obj instanceof Bahai))
			return false;
		
		Bahai o = (Bahai)obj;
		
		return
			o.major	== major	&&
			o.cycle	== cycle	&&
			o.year	== year		&&
			o.month	== month	&&
			o.day	== day		;
	}
}

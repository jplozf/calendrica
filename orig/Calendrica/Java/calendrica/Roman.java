package calendrica;


public class Roman extends Date {
	
	//
	// fields
	//


	public long		year;
	public int		month;
	public int		event;
	public int		count;
	public boolean	leapDay;


	//
	// constructors
	//


	public Roman() { }
	
	public Roman(long date) {
		super(date);
	}
	
	public Roman(Date date) {
		super(date);
	}
	
	public Roman(long year, int month, int event, int count, boolean leapDay) {
		this.year	= year;
		this.month	= month;
		this.event	= event;
		this.count	= count;
		this.leapDay	= leapDay;
	}

	
	//
	// constants
	//

	
	/*- kalends -*/

	// TYPE roman-event
	// Class of Kalends.

	public static final int KALENDS = 1;


	/*- ides -*/

	// TYPE roman-event
	// Class of Nones.

	public static final int NONES	= 2;


	/*- nones -*/

	// TYPE roman-event
	// Class of Ides.
	
	public static final int IDES	= 3;


	//
	// date conversion methods
	//
	
	
	/*- fixed-from-roman -*/

	// TYPE roman-date -> fixed-date
	// Fixed date for Roman name.
	
	public static long toFixed(long year, int month, int event, int count, boolean leapDay) {
		long approx = 0;
		if(event == KALENDS) {
			approx = Julian.toFixed(year, month, 1);
		} else if(event == NONES) {
			approx = Julian.toFixed(year, month, nonesOfMonth(month));
		} else if(event == IDES) {
			approx = Julian.toFixed(year, month, idesOfMonth(month));
		}
		
		return approx
			- count
			+ ((Julian.isLeapYear(year) && month == MARCH && event == KALENDS && (16 >= count && count >= 6)) ? 0 : 1)
			+ (leapDay ? 1 : 0);
	}


	public long toFixed() {
		return toFixed(year, month, event, count, leapDay);
	}


	/*- roman-from-fixed -*/

	// TYPE fixed-date -> roman-date
	// Roman name for fixed $date$.

	public void fromFixed(long date) {
		Julian j = new Julian(date);
		int month = j.month;
		int day = j.day;
		long year = j.year;
		int monthPrime = adjustedMod(month + 1, 12);
		long yearPrime = monthPrime == 1 ? year + 1 : year;
		long kalends1 = toFixed(yearPrime, monthPrime, KALENDS, 1, false);
		if(day == 1) {
			this.year = year;
			this.month = month;
			this.event = KALENDS;
			this.count = 1;
			this.leapDay = false;
		} else if(day <= nonesOfMonth(month)) {
			this.year = year;
			this.month = month;
			this.event = NONES;
			this.count = nonesOfMonth(month) - day + 1;
			this.leapDay = false;
		} else if(day <= idesOfMonth(month)) {
			this.year = year;
			this.month = month;
			this.event = IDES;
			this.count = idesOfMonth(month) - day + 1;
			this.leapDay = false;
		} else if(month != FEBRUARY || !Julian.isLeapYear(year)) {
			this.year = yearPrime;
			this.month = monthPrime;
			this.event = KALENDS;
			this.count = (int)(kalends1 - date + 1);
			this.leapDay = false;
		} else if(day < 25) {
			this.year = year;
			this.month = MARCH;
			this.event = KALENDS;
			this.count = 30 - day;
			this.leapDay = false;
		} else {
			this.year = year;
			this.month = MARCH;
			this.event = KALENDS;
			this.count = 31 - day;
			this.leapDay = day == 25;
		}
	}
	
	
	public void fromArray(int[] a) {
		year		= a[0];
		month		= a[1];
		event		= a[2];
		count		= a[3];
		leapDay	= a[4] != 0;
	}

	
	//
	// support methods
	//


	/*- ides-of-month -*/

	// TYPE roman-month -> ides
	// Date of Ides in Roman $month$.
	
	public static int idesOfMonth(int month) {
		return (month == MARCH || month == MAY || month == JULY || month == OCTOBER) ? 15 : 13;
	}

	
	/*- nones-of-month -*/

	// TYPE roman-month -> nones
	// Date of Nones in Roman $month$.
	
	public static int nonesOfMonth(int month) {
		return idesOfMonth(month) - 8;
	}


	//
	// object methods
	//


	protected String toStringFields() {
		return "year=" + year + ",month=" + month +
			",event=" + event + ",count=" + count + ",leapDay=" + leapDay;
	}


	public static final String[] countNames = new String[] {
		"",
		"pridie ",
		"ante diem iii ",
		"ante diem iv ",
		"ante diem v ",
		"ante diem vi ",
		"ante diem vii ",
		"ante diem viii ",
		"ante diem ix ",
		"ante diem x ",
		"ante diem xi ",
		"ante diem xii ",
		"ante diem xiii ",
		"ante diem xiv ",
		"ante diem xv ",
		"ante diem xvi ",
		"ante diem xvii ",
		"ante diem xviii ",
		"ante diem xix "};


	public static final String[] eventNames = new String[] {
		"Kalends",
		"Nones",
		"Ides"};

	
	public String format() {
		return java.text.MessageFormat.format("{0}{1} {2}, {3,number,#} {4}",
			new Object[]{
				leapDay ? "ante diem bis vi " : nameFromNumber(count, countNames),
				nameFromNumber(event, eventNames),
				nameFromMonth(month, Gregorian.monthNames),
				new Long(year < 0 ? -year : year),
				year < 0 ? "B.C.E." : "C.E."
			}
		);
	}
	

	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(!(obj instanceof Roman))
			return false;
		
		Roman o = (Roman)obj;
		
		return
			o.year	== year			&&
			o.month	== month		&&
			o.event	== event		&&
			o.count	== count		&&
			o.leapDay == leapDay	;
	}
}

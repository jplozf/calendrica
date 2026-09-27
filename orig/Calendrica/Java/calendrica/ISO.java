package calendrica;


public class ISO extends Date {
	
	//
	// fields
	//

	public long year;
	public int week;
	public int day;

	//
	// constructors
	//

	public ISO() { }
	
	public ISO(long date) {
		super(date);
	}
	
	public ISO(Date date) {
		super(date);
	}
	
	public ISO(long year, int week, int day) {
		this.year = year;
		this.week = week;
		this.day = day;
	}
	
	//
	// date conversion methods
	//


	/*- fixed-from-iso -*/

	// TYPE iso-date -> fixed-date
	// Fixed date equivalent to ISO (year week day).
	
	public static long toFixed(long year, int week, int day) {
		return nthKDay(week, SUNDAY, Gregorian.toFixed(year - 1, DECEMBER, 28)) + day;
	}
	
	public long toFixed() {
		return toFixed(year, week, day);
	}
	
	
	/*- iso-from-fixed -*/

	// TYPE fixed-date -> iso-date
	// ISO (year week day) corresponding to the fixed $date$.
	
	public void fromFixed(long date) {
		long approx = Gregorian.yearFromFixed(date - 3);
		year = date >= toFixed(approx + 1, 1, 1) ? approx + 1 : approx;
		week = (int)quotient(date - toFixed(year, 1, 1), 7) + 1;
		day = (int)adjustedMod(date, 7);
	}
	
	
	public void fromArray(int[] a) {
		year	= a[0];
		week	= a[1];
		day		= a[2];
	}
	
	
	//
	// object methods
	//

	protected String toStringFields() {
		return "year=" + year + ",week=" + week + ",day=" + day;
	}
	
	public String format() {
		return java.text.MessageFormat.format("{0}, Week {1}, {2,number,#}",
			new Object[]{
				nameFromDayOfWeek(toFixed(), Gregorian.dayOfWeekNames),
				new Integer(week),
				new Long(year)
			}
		);
	}
	
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(!(obj instanceof ISO))
			return false;
		
		ISO o = (ISO)obj;
		
		return
			o.year	== year		&&
			o.week	== week		&&
			o.day	== day		;
	}
}

package calendrica;


public class Armenian extends StandardDate {
	
	//
	// constructors
	//

	public Armenian() { }
	
	public Armenian(long date) {
		super(date);
	}
	
	public Armenian(Date date) {
		super(date);
	}
	
	public Armenian(long year, int month, int day) {
		super(year, month, day);
	}
	
	//
	// constants
	//


	/*- armenian-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of the Armenian calendar.
	// = July 11, 552 CE (Julian).
	
  	public static final long EPOCH = 201443;


	//
	// date conversion methods
	//


	/*- fixed-from-armenian -*/

	// TYPE armenian-date -> fixed-date
	// Fixed date of Armenian date.
	
	public static long toFixed(long year, int month, int day) {
		return EPOCH + Egyptian.toFixed(year, month, day) - Egyptian.EPOCH;
	}
	
	public long toFixed() {
		return toFixed(year, month, day);
	}
	
	
	/*- armenian-from-fixed -*/

	// TYPE fixed-date -> armenian-date
	// Armenian equivalent of fixed $date$.
	
	public void fromFixed(long date) {
		Egyptian d = new Egyptian(date + Egyptian.EPOCH - EPOCH);
		year = d.year;
		month = d.month;
		day = d.day;
	}


	//
	// support methods
	//


	//
	// auxiliary methods
	//


	//
	// object methods
	//

	public static final String[] dayOfWeekNames = new String[] {
                "Miashabathi",
                "Erkoushabathi",
                "Erekhshabathi",
                "Chorekhshabathi",
                "Hingshabathi",
                "Urbath",
                "Shabath"};

	public static final String[] monthNames = new String[] {
		"Nawasardi", 
		"Hori", 
		"Sahmi", 
		"Tre", 
		"Kaloch", 
		"Arach", 
		"Mehekani", 
		"Areg", 
		"Ahekani", 
		"Mareri", 
		"Margach", 
		"Hrotich",
                "Aweleach"};
	
	public String format() {
		return java.text.MessageFormat.format("{0}, {1} {2} {3,number,#}",
			new Object[]{
				nameFromDayOfWeek(toFixed(), dayOfWeekNames),
				new Integer(day),
				nameFromMonth(month, monthNames),
				new Long(year)
			}
		);
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof Armenian))
			return false;
		
		return internalEquals(obj);
	}
}

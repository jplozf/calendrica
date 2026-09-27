package calendrica;


public class Egyptian extends StandardDate {
	
	//
	// constructors
	//

	public Egyptian() { }
	
	public Egyptian(long date) {
		super(date);
	}
	
	public Egyptian(Date date) {
		super(date);
	}
	
	public Egyptian(long year, int month, int day) {
		super(year, month, day);
	}

	
	//
	// constants
	//


	/*- egyptian-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of the Egyptian (Nabonasser)
	// calendar.
	// JD 1448638 = February 26, 747 BCE (Julian).
	
  	public static final long EPOCH = fixedFromJD(1448638);

	
	//
	// date conversion methods
	//


	/*- fixed-from-egyptian -*/

	// TYPE egyptian-date -> fixed-date
	// Fixed date of Egyptian date.
	
	public static long toFixed(long year, int month, int day) {
		return EPOCH + 365 * (year - 1) + 30 * (month - 1) + day - 1;
	}
	
	public long toFixed() {
		return toFixed(year, month, day);
	}
	
	
	/*- egyptian-from-fixed -*/

	// TYPE fixed-date -> egyptian-date
	// Egyptian equivalent of fixed $date$.
	
	public void fromFixed(long date) {
		long days = date - EPOCH;
		year = 1 + quotient(days, 365);
		month = (int)(1 + quotient(mod(days, 365), 30));
		day = (int)(days - 365 * (year - 1) - 30 * (month - 1) + 1);
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

	public static final String[] monthNames = new String[] {
		"Thoth", 
		"Phaophi", 
		"Athyr", 
		"Choiak", 
		"Tybi", 
		"Mechir", 
		"Phamenoth", 
		"Pharmuthi", 
		"Pachon", 
		"Payni", 
		"Epiphi", 
		"Mesori",
                "Epagomenai"};
	
	public String format() {
		return java.text.MessageFormat.format("{0} {1} {2,number,#}",
			new Object[]{
				new Integer(day),
				nameFromMonth(month, monthNames),
				new Long(year)
			}
		);
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof Egyptian))
			return false;
		
		return internalEquals(obj);
	}
}

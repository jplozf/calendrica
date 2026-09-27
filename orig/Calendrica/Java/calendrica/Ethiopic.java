package calendrica;


public class Ethiopic extends StandardDate {

	//
	// constructors
	//

	public Ethiopic() { }
	
	public Ethiopic(long date) {
		super(date);
	}
	
	public Ethiopic(Date date) {
		super(date);
	}
	
	public Ethiopic(long year, int month, int day) {
		super(year, month, day);
	}
	
	//
	// constants
	//


	/*- ethiopic-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of the Ethiopic calendar.
	
	public static final long EPOCH = Julian.toFixed(Julian.CE(8), AUGUST, 29);
	
	
	//
	// date conversion methods
	//


	/*- fixed-from-ethiopic -*/

	// TYPE ethiopic-date -> fixed-date
	// Fixed date of Ethiopic date.
	
	public static long toFixed(long year, int month, int day) {
		return Coptic.toFixed(year, month, day) - Coptic.EPOCH + EPOCH;
	}

	public long toFixed() {
		return toFixed(year, month, day);
	}
	
	
	/*- ethiopic-from-fixed -*/

	// TYPE fixed-date -> ethiopic-date
	// Ethiopic equivalent of fixed $date$.
	
	public void fromFixed(long date) {
		Coptic coptic = new Coptic(date + Coptic.EPOCH - EPOCH);
		month = coptic.month;
		day = coptic.day;
		year = coptic.year;
	}

	
	//
	// object methods
	//


	public static final String[] dayOfWeekNames = new String[] {
		"Ihud", 
		"Sanyo", 
		"Maksanyo", 
		"Rob", 
		"Hamus", 
		"Arb", 
		"Kidamme"};

	public static final String[] monthNames = new String[] {
		"Maskaram",
		"Teqemt",
		"Kehdar",
		"Takhsas",
		"Ter",
		"Yakatit",
		"Magabit",
		"Miyazya",
		"Genbot",
		"Sane",
		"Hamle",
		"Nahase",
		"Paguemen"};
	
	public String format() {
		return java.text.MessageFormat.format("{0}, {1} {2} {3,number,#} E.E.",
			new Object[]{
				nameFromDayOfWeek(toFixed(), dayOfWeekNames),
				new Integer(day),
				nameFromMonth(month, monthNames),
				new Long(year)
			}
		);
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof Ethiopic))
			return false;
		
		return internalEquals(obj);
	}
}

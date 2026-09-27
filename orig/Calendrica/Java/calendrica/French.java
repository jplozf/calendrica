package calendrica;


public class French extends StandardDate {

	//
	// constructors
	//

	public French() { }
	
	public French(long date) {
		super(date);
	}
	
	public French(Date date) {
		super(date);
	}
	
	public French(long year, int month, int day) {
		super(year, month, day);
	}
	
	//
	// constants
	//
	
	
	/*- french-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of the French Revolutionary
	// calendar.
	
	public static final long EPOCH = Gregorian.toFixed(1792, SEPTEMBER, 22);
	
	
	/*- paris -*/

	// TYPE location
	// Location of Paris.  Longitude corresponds to difference
	// of 9m 21s between Paris time zone and Universal Time.
	
	public static final Location PARIS = new Location("Paris, France", deg(48.51), angle(2, 20, 15), mt(27), 1);
	
	
	//
	// date conversion methods
	//
	
	
	/*- fixed-from-french -*/

	// TYPE french-date -> fixed-date
	// Fixed date of French Revolutionary date.
	
	public static long toFixed(long year, int month, int day) {
		long newYear = newYearOnOrBefore(
			(long)Math.floor(EPOCH + 180 + MEAN_TROPICAL_YEAR * (year - 1)));
		return newYear - 1 + 30 * (month - 1) + day;
	}

	public long toFixed() {
		return toFixed(year, month, day);
	}
	
	
	/*- french-from-fixed -*/

	// TYPE fixed-date -> french-date
	// French Revolutionary date of fixed $date$.
	
	public void fromFixed(long date) {
		long newYear = newYearOnOrBefore(date);
		year = (long)Math.round((newYear - EPOCH) / MEAN_TROPICAL_YEAR) + 1;
		month = 1 + (int)quotient(date - newYear, 30);
		day = 1 + (int)mod(date - newYear, 30);
	}
	
	
	//
	// support methods
	//


	/*- midnight-in-paris -*/

	// TYPE fixed-date -> moment
	// Universal time of true midnight at end of fixed date
	// in Paris.
	
	public static double midnightInParis(long date) {
		return universalFromStandard(midnight(date + 1, PARIS), PARIS);
	}
	
	
	/*- french-new-year-on-or-before -*/

	// TYPE fixed-date -> fixed-date
	// Fixed date of French Revolutionary New Year on or
	// before fixed $date$.
	
	public static long newYearOnOrBefore(long date) {
		double approx = estimatePriorSolarLongitude(midnightInParis(date), AUTUMN);
		long i;
		for(i = (long)(Math.floor(approx) - 1); !(AUTUMN <= solarLongitude(midnightInParis(i))); ++i);
		return i;
	}
	
	
	//
	// object methods
	//

	public static final String[] monthNames = new String[] {
		"Vendemiaire",
		"Brumaire",
		"Frimaire",
		"Nivose",
		"Pluviose",
		"Ventose",
		"Germinal",
		"Floreal",
		"Prairial",
		"Messidor",
		"Thermidor",
		"Fructidor",
                "Sansculottides"
        };

	public static final String[] dayOfWeekNames = new String[] {
		"Primidi",
		"Duodi",
		"Tridi",
		"Quartidi",
		"Quintidi",
		"Sextidi",
		"Septidi",
		"Octidi",
		"Nonidi",
		"Decadi"};

	public static final String[] specialDayNames = new String[] {
		"Jour de la Vertu",
		"Jour du Genie",
		"Jour du Labour",
		"Jour de la Raison",
		"Jour de la Recompense",
		"Jour de la Revolution"};

	public static final String[] decadeNames = new String[] {
		"I",
		"II",
		"III"};
		
	public String format() {
		if(month == 13) {
			return java.text.MessageFormat.format("{0} de l''Annee {1,number,#} de la Revolution",
				new Object[]{
					nameFromNumber(day, specialDayNames),
					new Long(year)
				}
			);
		} else {
			return java.text.MessageFormat.format("Decade {0}, {1} de {2} de l''Annee {3,number,#} de la Revolution",
				new Object[]{
					decadeNames[(int)quotient(day - 1, 10)],
					nameFromNumber(day, dayOfWeekNames),
					nameFromMonth(month, monthNames),
					new Long(year)
				}
			);
		}
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof French))
			return false;
		
		return internalEquals(obj);
	}
}

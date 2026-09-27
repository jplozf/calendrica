package calendrica;


public class ObservationalIslamic extends Islamic {

	//
	// constructors
	//


	public ObservationalIslamic() { }
	
	public ObservationalIslamic(long date) {
		super(date);
	}
	
	public ObservationalIslamic(Date date) {
		super(date);
	}
	
	public ObservationalIslamic(long year, int month, int day) {
		super(year, month, day);
	}


	//
	// constants
	//
	
	
	/*- islamic-locale -*/

	// TYPE location
	// Sample location for Observational Islamic calendar
	// (Cairo, Egypt).
	
	public static final Location CAIRO = new Location("Cairo, Egypt", deg(30.1), deg(31.3), mt(200), 2);
	public static final Location ISLAMIC_LOCALE = CAIRO;


	//
	// date conversion methods
	//
	
	
	/*- fixed-from-observational-islamic -*/

	// TYPE islamic-date -> fixed-date
	// Fixed date equivalent to Observational Islamic date.
	
	public static long toFixed(long iYear, int iMonth, int iDay) {
		long result;
		try {
			long midmonth = EPOCH + (long)Math.floor(((iYear - 1) * 12 + iMonth - 1d/2) * MEAN_SYNODIC_MONTH);
			result = phasisOnOrBefore(midmonth, ISLAMIC_LOCALE) + iDay - 1;
		} catch (BogusTimeException ex) {
			result = 0;	// Should never happen unless Cairo is moved to the north pole.
		}
		
		return result;
	}

	public long toFixed() {
		return toFixed(year, month, day);
	}
	
	
	/*- observational-islamic-from-fixed -*/

	// TYPE fixed-date -> islamic-date
	// Observational Islamic date (year month day)
	// corresponding to fixed $date$.
	
	public void fromFixed(long date) {
		try {
			long crescent = phasisOnOrBefore(date, ISLAMIC_LOCALE);
			long elapsedMonths = (long)Math.round((crescent - EPOCH) / MEAN_SYNODIC_MONTH);
			year = quotient(elapsedMonths, 12) + 1;
			month = (int)(mod(elapsedMonths, 12) + 1);
			day = (int)(date - crescent + 1);
		} catch(BogusTimeException ex) {
			// Should never happen unless Cairo is moved to the north pole.
		}
	}
}
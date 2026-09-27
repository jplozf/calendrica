package calendrica;


public class ModifiedFrench extends StandardDate {

	//
	// constructors
	//

	public ModifiedFrench() { }
	
	public ModifiedFrench(long date) {
		super(date);
	}
	
	public ModifiedFrench(Date date) {
		super(date);
	}
	
	public ModifiedFrench(long year, int month, int day) {
		super(year, month, day);
	}
	
	//
	// date conversion methods
	//
	

	/*- fixed-from-modified-french -*/

	// TYPE french-date -> fixed-date
	// Fixed date of French Revolutionary date.

	public static long toFixed(long year, int month, int day) {
		return French.EPOCH - 1 +
			365 * (year - 1) +
			quotient(year - 1, 4) -
			quotient(year - 1, 100) +
			quotient(year - 1, 400) -
			quotient(year - 1, 4000) +
			30 * (month - 1) +
			day;
	}

	public long toFixed() {
		return toFixed(year, month, day);
	}
	

	/*- modified-french-from-fixed -*/

	// TYPE fixed-date -> french-date
	// French Revolutionary date (year month day) of fixed
	// $date$.

	public void fromFixed(long date) {
		long approx = 1 + quotient(date - French.EPOCH + 2,
                                           1460969d/4000);
		year = date < toFixed(approx, 1, 1) ? approx - 1 : approx;
		month = 1 + (int)quotient(date - toFixed(year, 1, 1), 30);
		day = (int)(date - toFixed(year, month, 1) + 1);
	}


	//
	// support methods
	//


	/*- modified-french-leap-year? -*/

	// TYPE french-year -> boolean
	// True if $f-year$ is a leap year on the French
	// Revolutionary calendar.
	
	public static boolean isLeapYear(long fYear) {
		boolean result = false;
		
		if(mod(fYear, 4) == 0) {
			long m400 = mod(fYear, 400);
			if(m400 != 100 && m400 != 200 && m400 != 300) {
				if(!(mod(fYear, 4000) == 0))
					result = true;
			}
		}
		
		return result;
	}
	
	//
	// object methods
	//
		
	public String format() {
		return new French(year, month, day).format();
	}
	
	public boolean equals(Object obj) {
		if(!(obj instanceof ModifiedFrench))
			return false;
		
		return internalEquals(obj);
	}
}

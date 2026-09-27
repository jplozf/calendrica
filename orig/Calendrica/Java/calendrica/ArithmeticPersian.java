package calendrica;


public class ArithmeticPersian extends Persian {

	//
	// constructors
	//


	public ArithmeticPersian() { }
	
	public ArithmeticPersian(long date) {
		super(date);
	}
	
	public ArithmeticPersian(Date date) {
		super(date);
	}
	
	public ArithmeticPersian(long year, int month, int day) {
		super(year, month, day);
	}


	//
	// date conversion methods
	//
	
	
	/*- fixed-from-arithmetic-persian -*/

	// TYPE persian-date -> fixed-date
	// Fixed date equivalent to Persian date.

	public static long toFixed(long pYear, int month, int day) {
		long y = (0 < pYear) ? pYear - 474 : pYear - 473;
		long year = mod(y, 2820) + 474;
		return (EPOCH - 1)
			+ 1029983 * quotient(y, 2820)
			+ 365 * (year - 1)
			+ quotient(682 * year - 110, 2816)
			+ ((month <= 7) ? 31 * (month - 1) : 30 * (month - 1) + 6)
			+ day;
	}

	public long toFixed() {
		return toFixed(year, month, day);
	}


	/*- arithmetic-persian-from-fixed -*/

	// TYPE fixed-date -> persian-date
	// Persian date corresponding to fixed $date$.

	public void fromFixed(long date) {
		year = yearFromFixed(date);
		long dayOfYear = 1 + date - toFixed(year, 1, 1);
		month = (int)(dayOfYear < 186 ? Math.ceil(dayOfYear / 31d) : Math.ceil((dayOfYear - 6) / 30d));
		day = (int)(date - (toFixed(year, month, 1) - 1));
	}

	
	//
	// support methods
	//


	/*- arithmetic-persian-leap-year? -*/

	// TYPE persian-year -> boolean
	// True if $p-year$ is a leap year on the Persian calendar.

	public static boolean isLeapYear(long pYear) {
		long y = 0 < pYear ? pYear - 474 : pYear - 473;
		long year = mod(y, 2820) + 474;
		return mod((year + 38) * 682, 2816) < 682;
	}


	/*- arithmetic-persian-year-from-fixed -*/

	// TYPE fixed-date -> persian-year
	// Persian year corresponding to the fixed $date$.

	public static long yearFromFixed(long date) {
		long l0 = date - toFixed(475, 1, 1);
		long n2820 = quotient(l0, 1029983);
		long d1 = mod(l0, 1029983);
		long y2820 = d1 == 1029982 ? 2820 : quotient(2816d * d1 + 1031337, 1028522);
		long year = 474 + 2820 * n2820 + y2820;
		return 0 < year ? year : year - 1;
	}
}

package calendrica;


public class FutureBahai extends Bahai {

	//
	// constructors
	//


	public FutureBahai() { }
	
	public FutureBahai(long date) {
		super(date);
	}
	
	public FutureBahai(Date date) {
		super(date);
	}
	
	public FutureBahai(long major, int cycle, int year, int month, int day) {
		this.major = major;
		this.cycle = cycle;
		this.year = year;
		this.month = month;
		this.day = day;
	}


	//
	// constants
	//
	
	
	/*- haifa -*/

	// TYPE location
	// Location of Haifa, Israel.
	
	public static final Location HAIFA = new Location("Haifa, Israel", deg(32.82), deg(35), mt(0), 2);

	
	//
	// date conversion methods
	//
	
	
	/*- fixed-from-future-bahai -*/

	// TYPE bahai-date -> fixed-date
	// Fixed date of Bahai date.
	
	public static long toFixed(long bMajor, int bCycle, int bYear, int bMonth, int bDay) {
		long years = 361 * (bMajor - 1) +
			19 * (bCycle - 1) +
			bYear;
		if(bMonth == 19) {
			return newYearOnOrBefore(EPOCH + (long)Math.floor(MEAN_TROPICAL_YEAR * (years + 1d/2))) - 19 + bDay - 1;
		} else if(bMonth == AYYAM_I_HA) {
			return newYearOnOrBefore(EPOCH + (long)Math.floor(MEAN_TROPICAL_YEAR * (years - 1d/2))) + 342 + bDay - 1;
		} else {
			return newYearOnOrBefore(EPOCH + (long)Math.floor(MEAN_TROPICAL_YEAR * (years - 1d/2))) + (bMonth - 1) * 19 + bDay - 1;
		}
	}
	
	public long toFixed() {
		return toFixed(major, cycle, year, month, day);
	}
	
	
	/*- future-bahai-from-fixed -*/

	// TYPE fixed-date -> bahai-date
	// Future Bahai date corresponding to fixed $date$.
	
	public void fromFixed(long date) {
		long newYear = newYearOnOrBefore(date);
		long years = (long)Math.round((newYear - EPOCH) / MEAN_TROPICAL_YEAR);
		major = quotient(years, 361) + 1;
		cycle = (int)quotient(mod(years, 361), 19) + 1;
		year = (int)mod(years, 19) + 1;
		long days = date - newYear;
		if(date >= toFixed(major, cycle, year, 19, 1)) {
			month = 19;
		} else if(date >= toFixed(major, cycle, year, AYYAM_I_HA, 1)) {
			month = AYYAM_I_HA;
		} else {
			month = (int)quotient(days, 19) + 1;
		}
		day = (int)(date + 1 - toFixed(major, cycle, year, month, 1));
	}
	
	
	//
	// support methods
	//
	
	
	/*- sunset-in-haifa -*/

	// TYPE fixed-date -> moment
	// Universal time of sunset of evening before fixed $date$
	// in Haifa.
	
	public static double sunsetInHaifa(long date) {
		try {
			return universalFromStandard(sunset(date - 1, HAIFA), HAIFA);
		} catch(BogusTimeException ex) {
			return 0; // Should never happen unless Haifa is moved to the north pole.
		}
	}
	
	
	/*- future-bahai-new-year-on-or-before -*/

	// TYPE fixed-date -> fixed-date
	// Fixed date of Future Bahai New Year on or before
	// fixed $date$.
	
	public static long newYearOnOrBefore(long date) {
		double approx = estimatePriorSolarLongitude(sunsetInHaifa(date), SPRING);
		long i;
		for(i = (long)Math.floor(approx) - 1; !(solarLongitude(sunsetInHaifa(i)) <= SPRING + deg(2)); ++i);
		return i;
	}
	
	
	//
	// auxiliary methods
	//


	/*- feast-of-ridvan -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Feast of Ridvan in Gregorian year.
	
	public static long feastOfRidvan(long gYear) {
		long years = gYear - Gregorian.yearFromFixed(EPOCH);
		long major = 1 + quotient(years, 361);
		int cycle = 1 + (int)quotient(mod(years, 361), 19);
		int year = 1 + (int)mod(years, 19);
		return toFixed(major, cycle, year, 2, 13);
	}
	
	
	//
	// object methods
	//
	
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(!(obj instanceof FutureBahai))
			return false;
		
		FutureBahai o = (FutureBahai)obj;
		
		return
			o.major	== major	&&
			o.cycle	== cycle	&&
			o.year	== year		&&
			o.month	== month	&&
			o.day	== day		;
	}
}

package calendrica;

public abstract class Ecclesiastical extends ProtoDate {

	//
	// auxiliary methods
	//


	/*- orthodox-easter -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Orthodox Easter in Gregorian year.
	
	public static long orthodoxEaster(long gYear) {
		long shiftedEpact = mod(14 + 11 * mod(gYear, 19), 30);
		long jYear = gYear > 0 ? gYear : gYear - 1;
		long paschalMoon = Julian.toFixed(jYear, APRIL, 19) - shiftedEpact;
		return kDayAfter(paschalMoon, SUNDAY);
	}


	/*- alt-orthodox-easter -*/

	// TYPE gregorian-year -> fixed-date
	// Alternate calculation of fixed date of Orthodox Easter 
	// in Gregorian year.
	
	public static long altOrthodoxEaster(long gYear) {
		long paschalMoon = 354 * gYear
			+ 30 * quotient((7 * gYear) - 8, 19)
			+ quotient(gYear, 4)
			- quotient(gYear, 19)
			- 272;
		return kDayAfter(paschalMoon, SUNDAY);
	}


	/*- easter -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Easter in Gregorian year.
	
	public static long easter(long gYear) {
		long century = 1 + quotient(gYear, 100);
		long shiftedEpact = mod(14
								+ 11 * mod(gYear, 19)
								- quotient(3 * century, 4)
								+ quotient(5 + 8 * century, 25),
							30);
		long adjustedEpact = shiftedEpact == 0 || (shiftedEpact == 1 && 10 < mod(gYear, 19)) ?
			shiftedEpact + 1 :
			shiftedEpact;
		long paschalMoon = Gregorian.toFixed(gYear, APRIL, 19) - adjustedEpact;
		return kDayAfter(paschalMoon, SUNDAY);
	}


	/*- pentecost -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Pentecost in Gregorian year.
	
	public static long pentecost(long gYear) {
		return easter(gYear) + 49;
	}
	
	
	/*- astronomical-easter -*/

	// TYPE gregorian-year -> fixed-date
	// Date of (proposed) astronomical Easter in Gregorian
	// year.
	
	public static long astronomicalEaster(long gYear) {
		long jan1 = Gregorian.toFixed(gYear, JANUARY, 1);
		double equinox = solarLongitudeAfter(jan1, SPRING);
		long paschalMoon = (long)Math.floor(apparentFromLocal(localFromUniversal(lunarPhaseAfter(equinox, FULL), JERUSALEM)));
		return kDayAfter(paschalMoon, SUNDAY);
	}
};

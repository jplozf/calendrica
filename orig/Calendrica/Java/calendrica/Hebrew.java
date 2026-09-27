package calendrica;


public class Hebrew extends StandardDate {

	//
	// constructors
	//


	public Hebrew() { }
	
	public Hebrew(long date) {
		super(date);
	}
	
	public Hebrew(Date date) {
		super(date);
	}
	
	public Hebrew(long year, int month, int day) {
		super(year, month, day);
	}

	
	//
	// constants
	//


	/*- tishri -*/

	// TYPE hebrew-month
	// Tishri is month number 7.
	
	public static final int TISHRI = 7;
	
	
	/*- nisan -*/

	// TYPE hebrew-month
	// Nisan is month number 1.
	
	public static final int NISAN = 1;


	/*- hebrew-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of the Hebrew calendar, that is,
	// Tishri 1, 1 AM.
	
	public static final long EPOCH = Julian.toFixed(Julian.BCE(3761), OCTOBER, 7);

	
	//
	// date conversion methods
	//
	
	
	/*- fixed-from-hebrew -*/

	// TYPE hebrew-date -> fixed-date
	// Fixed date of Hebrew date.
	
	public static long toFixed(long year, int month, int day) {
		long date =
			newYear(year)
			+ day - 1;
		
		if(month < TISHRI) {
			for(int m = TISHRI; m <= lastMonthOfYear(year); ++m)
				date += lastDayOfMonth(m, year);

			for(int m = NISAN; m < month; ++m)
				date += lastDayOfMonth(m, year);

		} else {
			for(int m = TISHRI; m < month; ++m)
				date += lastDayOfMonth(m, year);
		}
		
		return date;
	}

	public long toFixed() {
		return toFixed(year, month, day);
	}
	
	
	/*- hebrew-from-fixed -*/

	// TYPE fixed-date -> hebrew-date
	// Hebrew (year month day) corresponding to fixed $date$.
	// The fraction can be approximated by 365.25.

	public void fromFixed(final long date) {
		long approx = 1 + quotient(date - EPOCH, 35975351d/98496);
		for(year = approx - 1; newYear(year) <= date; ++year);
		year--;

		int start = date < toFixed(year, 1, 1) ? TISHRI : NISAN;
		for(month = start; !(date <= toFixed(year, month, lastDayOfMonth(month, year))); ++month);

		day = (int)(1 + date - toFixed(year, month, 1));
	}
	
	
	//
	// support methods
	//


	/*- hebrew-leap-year? -*/

	// TYPE hebrew-year -> boolean
	// True if $h-year$ is a leap year on Hebrew calendar.
	
	public static boolean isLeapYear(long hYear) {
		return mod(1 + 7 * hYear, 19) < 7;
	}


	/*- last-month-of-hebrew-year -*/

	// TYPE hebrew-year -> hebrew-month
	// Last month of Hebrew year.
	
	public static int lastMonthOfYear(long hYear) {
		return isLeapYear(hYear) ? 13 : 12;
	}


	/*- last-day-of-hebrew-month -*/

	// TYPE (hebrew-month hebrew-year) -> hebrew-day
	// Last day of month in Hebrew year.
	
	public static int lastDayOfMonth(int hMonth, long hYear) {
		return ( (hMonth == 2 || hMonth == 4 || hMonth == 6 || hMonth == 10 || hMonth == 13)
			|| (hMonth == 12 && !isLeapYear(hYear))
			|| (hMonth == 8 && !hasLongMarheshvan(hYear))
			|| (hMonth == 9 && hasShortKislev(hYear)) ) ? 29 : 30;
	}


        /*- molad -*/

        // TYPE (hebrew-month hebrew-year) -> rational-moment
        // Moment of mean conjunction of $h-month$ in Hebrew
        // $h-year$.

	public static double molad (int hMonth, long hYear) {
                long y = hMonth < TISHRI ? hYear + 1 : hYear;
                long monthsElapsed = hMonth - TISHRI + quotient(235 * y - 234, 19);
                return EPOCH - 876/25920d 
                  + monthsElapsed * (29 + hr(12) + 793/25920d);
        }


	/*- hebrew-calendar-elapsed-days -*/

	// TYPE hebrew-year -> integer
	// Number of days elapsed from the (Sunday) noon prior
	// to the epoch of the Hebrew calendar to the mean
	// conjunction (molad) of Tishri of Hebrew year $h-year$,
	// or one day later.
	
	public static long calendarElapsedDays(long hYear) {
		long monthsElapsed = quotient(235 * hYear - 234, 19);
		double partsElapsed = 12084 + 13753d * monthsElapsed;
		long day = 29 * monthsElapsed + quotient(partsElapsed, 25920);
		return mod(3 * (day + 1), 7) < 3 ? day + 1 : day;
	}


	/*- hebrew-new-year -*/

	// TYPE hebrew-year -> fixed-date
	// Fixed date of Hebrew new year $h-year$.
	
	public static long newYear(long hYear) {
		return EPOCH
			+ calendarElapsedDays(hYear)
			+ newYearDelay(hYear);
	}
	
	
	/*- hebrew-new-year-delay -*/

	// TYPE hebrew-year -> {0,1,2}
	// Delays to start of Hebrew year to keep ordinary year in
	// range 353-356 and leap year in range 383-386.
	
	public static int newYearDelay(long hYear) {
		long ny0 = calendarElapsedDays(hYear - 1);
		long ny1 = calendarElapsedDays(hYear);
		long ny2 = calendarElapsedDays(hYear + 1);
		if(ny2 - ny1 == 356)
			return 2;
		else if(ny1 - ny0 == 382)
			return 1;
		else
			return 0;
	}


	/*- days-in-hebrew-year -*/

	// TYPE hebrew-year -> {353,354,355,383,384,385}
	// Number of days in Hebrew year.
	
	public static int daysInYear(long hYear) {
		return (int)(newYear(hYear + 1) - newYear(hYear));
	}


	/*- long-marheshvan? -*/

	// TYPE hebrew-year -> boolean
	// True if Marheshvan is long in Hebrew year.
	
	public static boolean hasLongMarheshvan(long hYear) {
		int days = daysInYear(hYear);
		return days == 355 || days == 385;
	}


	/*- short-kislev? -*/

	// TYPE hebrew-year -> boolean
	// True if Kislev is short in Hebrew year.
	
	public static boolean hasShortKislev(long hYear) {
		int days = daysInYear(hYear);
		return days == 353 || days == 383;
	}
	
	
	/*- jewish-dusk -*/

	// TYPE (fixed-date location) -> moment
	// Standard time of Jewish dusk on fixed $date$
	// at $locale$ (as per Vilna Gaon).
	
	public static double jewishDusk(long date, Location locale)
		throws BogusTimeException
	{
		return dusk(date, locale, angle(4, 40, 0));
	}
	
	
	/*- jewish-sabbath-ends -*/

	// TYPE (fixed-date location) -> moment
	// Standard time of end of Jewish sabbath on fixed $date$
	// at $locale$ (as per Berthold Cohn).
	
	public static double jewishSabbathEnds(long date, Location locale)
		throws BogusTimeException
	{
		return dusk(date, locale, angle(7, 5, 0));
	}

	
	/*- jewish-morning-end -*/

	// TYPE (fixed-date location) -> moment
	// Standard time on fixed $date$ at $locale$ of end of
	// morning according to Jewish ritual.
	
	public static double jewishMorningEnd(long date, Location locale)
		throws BogusTimeException
	{
		return standardFromSundial(date, 10, locale);
	}


	//
	// auxiliary methods
	//


	/*- yom-kippur -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Yom Kippur occurring in Gregorian year.

	public static long yomKippur(long gYear) {
		long hYear = 1 + gYear - Gregorian.yearFromFixed(EPOCH);
		return toFixed(hYear, TISHRI, 10);
	}


	/*- passover -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Passover occurring in Gregorian year.
	
	public static long passover(long gYear) {
		long hYear = gYear - Gregorian.yearFromFixed(EPOCH);
		return toFixed(hYear, NISAN, 15);
	}
	
	
	/*- classical-passover-eve -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Classical (observational) Passover Eve
	// (Nisan 14) occurring in Gregorian year.
	
	public static long classicalPassoverEve(long gYear) {
		long jan1 = Gregorian.toFixed(gYear, JANUARY, 1);
		double equinox = solarLongitudeAfter(jan1, SPRING);
		try {
			long newMoon = phasisOnOrBefore((long)Math.floor(equinox) + 10, JERUSALEM);
			double set = universalFromStandard(sunset(newMoon + 14, JERUSALEM), JERUSALEM);
			long nisan = equinox < set ? newMoon : phasisOnOrBefore(newMoon + 45, JERUSALEM);
			return nisan + 13;
		} catch(BogusTimeException ex) {
			return 0;	// Should never happen, unless Jerusalem gets moved to the north pole.
		}
	}


	/*- omer -*/

	// TYPE fixed-date -> omer-count
	// Number of elapsed weeks and days in the omer at $date$.
	// Returns bogus if that date does not fall during the
	// omer.
	
	public static int[] omer(long date)
		throws BogusDateException
	{
		int[] result;
		
		long c = date - passover(Gregorian.yearFromFixed(date));
		if(1 <= c && c <= 49)
			result = new int[] {(int)quotient(c, 7), (int)mod(c, 7)};
		else
			throw new BogusDateException();
		
		return result;
	}
	
	
	/*- purim -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Purim occurring in Gregorian year.
	
	public static long purim(long gYear) {
		long hYear = gYear - Gregorian.yearFromFixed(EPOCH);
		int lastMonth = lastMonthOfYear(hYear);
		return toFixed(hYear, lastMonth, 14);
	}


	/*- ta-anit-esther -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Ta'anit Esther occurring in
	// Gregorian year.
		
	public static long taAnitEsther(long gYear) {
		long purimDate = purim(gYear);
		return dayOfWeekFromFixed(purimDate) == SUNDAY ? purimDate - 3 : purimDate - 1;
	}
	
	
	/*- tishah-be-av -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Tishah be-Av occurring in Gregorian year.
	
	public static long tishahBeAv(long gYear) {
		long hYear = gYear - Gregorian.yearFromFixed(EPOCH);
		long ninthOfAv = toFixed(hYear, 5, 9);
		return dayOfWeekFromFixed(ninthOfAv) == SATURDAY ? ninthOfAv + 1 : ninthOfAv;
	}


	/*- birkath-ha-hama -*/

	// TYPE gregorian-year -> list-of-fixed-dates
	// List of fixed date of Birkath ha-Hama occurring in
	// Gregorian year, if it occurs.
	
	public static FixedVector birkathHaHama(long gYear) {
		FixedVector dates = Coptic.inGregorian(7, 30, gYear);
		if(dates.size() != 0 && mod(new Coptic(dates.fixedAt(0)).year, 28) == 17)
			return dates;
		else
			return new FixedVector();
	}


	/*- sh-ela -*/

	// TYPE gregorian-year -> list-of-fixed-dates
	// List of fixed dates of Sh'ela occurring in
	// Gregorian year.
	
	public static FixedVector shEla(long gYear) {
		return Coptic.inGregorian(3, 26, gYear);
	}
	
	
	/*- yom-ha-zikkaron -*/

	// TYPE gregorian-year -> fixed-date
	// Fixed date of Yom ha-Zikkaron occurring in Gregorian
	// year.
	
	public static long yomHaZikkaron(long gYear) {
		long hYear = gYear - Gregorian.yearFromFixed(EPOCH);
		long iyar4 = toFixed(hYear, 2, 4);
		return WEDNESDAY < dayOfWeekFromFixed(iyar4) ? kDayBefore(iyar4, WEDNESDAY) : iyar4;
	}
	
	
	/*- hebrew-birthday -*/

	// TYPE (hebrew-date hebrew-year) -> fixed-date
	// Fixed date of the anniversary of Hebrew $birthdate$
	// occurring in Hebrew $h-year$.
	
	public static long birthday(Hebrew birthDate, long hYear) {
		return birthDate.month == lastMonthOfYear(birthDate.year) ?
			toFixed(hYear, lastMonthOfYear(hYear), birthDate.day) :
			toFixed(hYear, birthDate.month, 1) + birthDate.day - 1;
	}
	
	
	/*- hebrew-birthday-in-gregorian -*/

	// TYPE (hebrew-date gregorian-year)
	// TYPE -> list-of-fixed-dates
	// List of the fixed dates of Hebrew $birthday$
	// that occur in Gregorian $g-year$.
	
	public static FixedVector birthdayInGregorian(Hebrew birthDate, long gYear) {
		long jan1 = Gregorian.toFixed(gYear, JANUARY, 1);
		long dec31 = Gregorian.toFixed(gYear, DECEMBER, 31);
		long y = new Hebrew(jan1).year;
		long date1 = birthday(birthDate, y);
		long date2 = birthday(birthDate, y + 1);
		FixedVector result = new FixedVector(1, 1);
		if(jan1 <= date1)
			result.addFixed(date1);
		if(date2 <= dec31)
			result.addFixed(date2);
		return result;
	}


	/*- yahrzeit -*/

	// TYPE (hebrew-date hebrew-year) -> fixed-date
	// Fixed date of the anniversary of Hebrew $death-date$
	// occurring in Hebrew $h-year$.
	
	public static long yahrzeit(Hebrew deathDate, long hYear) {
		long result;
		
		if(deathDate.month == 8 && deathDate.day == 30 && !hasLongMarheshvan(deathDate.year + 1))
			result = toFixed(hYear, 9, 1) - 1;
		else if(deathDate.month == 9 && deathDate.day == 30 && hasShortKislev(deathDate.year + 1))
			result = toFixed(hYear, 10, 1) - 1;
		else if(deathDate.month == 13)
			result = toFixed(hYear, lastMonthOfYear(hYear), deathDate.day);
		else if(deathDate.day == 30 && deathDate.month == 12 && !isLeapYear(hYear))
			result = toFixed(hYear, 11, 30);
		else
			result = toFixed(hYear, deathDate.month, 1) + deathDate.day - 1;
	
		return result;
	}


	/*- yahrzeit-in-gregorian -*/

	// TYPE (hebrew-date gregorian-year)
	// TYPE -> list-of-fixed-dates
	// List of the fixed dates of yahrzeit
	// that occur in Gregorian year.
	
	public static FixedVector yahrzeitInGregorian(Hebrew deathDate, long gYear) {
		long jan1 = Gregorian.toFixed(gYear, JANUARY, 1);
		long dec31 = Gregorian.toFixed(gYear, DECEMBER, 31);
		long y = new Hebrew(jan1).year;
		long date1 = yahrzeit(deathDate, y);
		long date2 = yahrzeit(deathDate, y + 1);
		FixedVector result = new FixedVector(1, 1);
		if(jan1 <= date1)
			result.addFixed(date1);
		if(date2 <= dec31)
			result.addFixed(date2);
		return result;
	}
	

	//
	// object methods
	//
		
	public static final String[] dayOfWeekNames = new String[] {
		"yom rishon", 
		"yom sheni", 
		"yom shelishi", 
		"yom revi`i", 
		"yom hamishi", 
		"yom shishi", 
		"yom shabbat"};

	public static final String[] monthNames = new String[] {
		"Nisan",
		"Iyyar",
		"Sivan",
		"Tammuz",
		"Av",
		"Elul",
		"Tishri",
		"Marheshvan",
		"Kislev",
		"Tevet",
		"Shevat",
		"Adar"};

	public static final String[] leapYearMonthNames = new String[] {
		"Nisan",
		"Iyyar",
		"Sivan",
		"Tammuz",
		"Av",
		"Elul",
		"Tishri",
		"Marheshvan",
		"Kislev",
		"Tevet",
		"Shevat",
		"Adar I",
		"Adar II"};
	
	public String format() {
		return java.text.MessageFormat.format("{0}, {1} {2} {3,number,#} A.M.",
			new Object[]{
				nameFromDayOfWeek(toFixed(), dayOfWeekNames),
				new Integer(day),
				nameFromMonth(month, isLeapYear(year) ? leapYearMonthNames : monthNames),
				new Long(year)
			}
		);
	}

	public boolean equals(Object obj) {
		if(!(obj instanceof Hebrew))
			return false;
		
		return internalEquals(obj);
	}
}

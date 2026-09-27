package calendrica;


public class OldHinduLunar extends Date {
	
	//
	// fields
	//

	public long year;
	public int month;
	public boolean leapMonth;
	public int day;

	//
	// constructors
	//

	public OldHinduLunar() { }
	
	public OldHinduLunar(long date) {
		super(date);
	}
	
	public OldHinduLunar(Date date) {
		super(date);
	}
	
	public OldHinduLunar(long year, int month, boolean leapMonth, int day) {
		this.year	= year;
		this.month	= month;
		this.leapMonth	= leapMonth;
		this.day	= day;
	}
	
	//
	// constants
	//


	/*- arya-lunar-month -*/

	// TYPE rational
	// Length of Old Hindu lunar month.

	public static final double ARYA_LUNAR_MONTH = 1577917500d/53433336;
	

	/*- arya-lunar-day -*/

	// TYPE rational
	// Length of Old Hindu lunar day.

	public static final double ARYA_LUNAR_DAY = ARYA_LUNAR_MONTH / 30;

	//
	// date conversion methods
	//
	

	/*- fixed-from-old-hindu-lunar -*/

	// TYPE old-hindu-lunar-date -> fixed-date
	// Fixed date corresponding to Old Hindu lunar date.

	public static long toFixed(long year, int month, boolean leapMonth, int day) {
		double mina = (12 * year - 1) * OldHinduSolar.ARYA_SOLAR_MONTH;
		double lunarNewYear = ARYA_LUNAR_MONTH * (quotient(mina, ARYA_LUNAR_MONTH) + 1);
		return (long)Math.floor(
			OldHinduSolar.EPOCH +
			lunarNewYear +
			ARYA_LUNAR_MONTH * (!leapMonth &&
					Math.ceil((lunarNewYear - mina) / (OldHinduSolar.ARYA_SOLAR_MONTH - ARYA_LUNAR_MONTH)) <= month ?
						month : month - 1) +
			(day - 1) * ARYA_LUNAR_DAY +
			3d/4
		);
	}

	public long toFixed() {
		return toFixed(year, month, leapMonth, day);
	}
	

	/*- old-hindu-lunar-from-fixed -*/

	// TYPE fixed-date -> old-hindu-lunar-date
	// Old Hindu lunar date equivalent to fixed $date$.

	public void fromFixed(long date) {
		double sun = OldHinduSolar.dayCount(date) + 1d/4;
		double newMoon = sun - mod(sun, ARYA_LUNAR_MONTH);
		leapMonth = OldHinduSolar.ARYA_SOLAR_MONTH - ARYA_LUNAR_MONTH >= mod(newMoon, OldHinduSolar.ARYA_SOLAR_MONTH) &&
			mod(newMoon, OldHinduSolar.ARYA_SOLAR_MONTH) > 0;
		month = 1 + (int)mod(Math.ceil(newMoon / OldHinduSolar.ARYA_SOLAR_MONTH), 12);
		day = 1 + (int)mod(quotient(sun, ARYA_LUNAR_DAY), 30);
		year = (long)Math.ceil((newMoon + OldHinduSolar.ARYA_SOLAR_MONTH) / OldHinduSolar.ARYA_SOLAR_YEAR) - 1;
	}
	
	public void fromArray(int[] a) {
		year		= a[0];
		month		= a[1];
		leapMonth	= a[2] != 0;
		day			= a[3];
	}
	
	//
	// support methods
	//


	/*- old-hindu-lunar-leap-year? -*/

	// TYPE old-hindu-lunar-year -> boolean
	// True if $l-year$ is a leap year on the
	// old Hindu calendar.

	public static boolean isLeapYear(long lYear) {
		return mod(lYear * OldHinduSolar.ARYA_SOLAR_YEAR - OldHinduSolar.ARYA_SOLAR_MONTH, ARYA_LUNAR_MONTH) >=
			23902504679d/1282400064;
	}

	
	//
	// object methods
	//

	protected String toStringFields() {
		return  "year=" + year + ",month=" + month + ",leapMonth=" + leapMonth + ",day=" + day;
	}

	public static final String[] dayOfWeekNames = new String[] {
		"Ravivara", 
		"Chandravara", 
		"Mangalavara", 
		"Buddhavara", 
		"Brihaspatvara", 
		"Sukravara", 
		"Sanivara"};

	public static final String[] monthNames = new String[] {
		"Chaitra",
		"Vaisakha",
		"Jyaishtha",
		"Ashadha",
		"Sravana",
		"Bhadrapada",
		"Asvina",
		"Kartika",
		"Margasirsha",
		"Pausha",
		"Magha",
		"Phalguna"};
	
	public String format() {
		return java.text.MessageFormat.format("{0}, {1} {2}{3} {4,number,#} K.Y.",
			new Object[]{
				nameFromDayOfWeek(toFixed(), dayOfWeekNames),
				new Integer(day),
				nameFromMonth(month, monthNames),
				leapMonth ? " II" : "",
				new Long(year)
			}
		);
	}
	
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(!(obj instanceof OldHinduLunar))
			return false;
		
		OldHinduLunar o = (OldHinduLunar)obj;
		
		return
			o.year	== year				&&
			o.month	== month			&&
			o.leapMonth	== leapMonth	&&
			o.day	== day				;
	}
}

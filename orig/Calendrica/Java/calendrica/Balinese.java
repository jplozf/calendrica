package calendrica;

	
public class Balinese extends ProtoDate {

	//
	// fields
	//


		public boolean luang;
		public int dwiwara;
		public int triwara;
		public int caturwara;
		public int pancawara;
		public int sadwara;
		public int saptawara;
		public int asatawara;
		public int sangawara;
		public int dasawara;


	//
	// constants
	//


	/*- bali-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of a Balinese Pawukon cycle.
	
	public static final long EPOCH = fixedFromJD(146);


	//
	// constructors
	//

	public Balinese() { }
	
	public Balinese(long date) {
		super(date);
	}
	
	public Balinese(Date date) {
		super(date);
	}
	
	public Balinese(boolean luang, int dwiwara, int triwara, int caturwara, int pancawara,
		int sadwara, int saptawara, int asatawara, int sangawara, int dasawara) {
		this.luang		= luang;
		this.dwiwara	= dwiwara;
		this.triwara	= triwara;
		this.caturwara	= caturwara;
		this.pancawara	= pancawara;
		this.sadwara	= sadwara;
		this.saptawara	= saptawara;
		this.asatawara	= asatawara;
		this.sangawara	= sangawara;
		this.dasawara	= dasawara;
	}
	
	//
	// date conversion methods
	//


	/*- bali-pawukon-from-fixed -*/

	// TYPE fixed-date -> balinese-date
	// Positions in ten cycles of Balinese Pawukon calendar.
	
	public void fromFixed(long date) {
		luang		= luangFromFixed(date);
		dwiwara		= dwiwaraFromFixed(date);
		triwara		= triwaraFromFixed(date);
		caturwara	= caturwaraFromFixed(date);
		pancawara	= pancawaraFromFixed(date);
		sadwara		= sadwaraFromFixed(date);
		saptawara	= saptawaraFromFixed(date);
		asatawara	= asatawaraFromFixed(date);
		sangawara	= sangawaraFromFixed(date);
		dasawara	= dasawaraFromFixed(date);
	}
	
	
	public void fromArray(int[] a) {
		luang		= a[0] != 0;
		dwiwara		= a[1];
		triwara		= a[2];
		caturwara	= a[3];
		pancawara	= a[4];
		sadwara		= a[5];
		saptawara	= a[6];
		asatawara	= a[7];
		sangawara	= a[8];
		dasawara	= a[9];
	}
	
	
	//
	// support methods
	//


	/*- bali-day-from-fixed -*/

	// TYPE fixed-date -> 0-209
	// Position of $date$ in 210-day Pawukon cycle.

	public static int dayFromFixed(long date) {
		return (int)mod(date - EPOCH, 210);
	}


	/*- bali-luang-from-fixed -*/

	// TYPE fixed-date -> boolean
	// Membership of $date$ in "1-day" Balinese cycle.

	public static boolean luangFromFixed(long date) {
		return (mod(dasawaraFromFixed(date), 2) == 0);
	}


	/*- bali-dwiwara-from-fixed -*/

	// TYPE fixed-date -> 1-2
	// Position of $date$ in 2-day Balinese cycle.

	public static int dwiwaraFromFixed(long date) {
		return mod(dasawaraFromFixed(date) + 1, 2) + 1;
	}


	/*- bali-triwara-from-fixed -*/

	// TYPE fixed-date -> 1-3
	// Position of $date$ in 3-day Balinese cycle.

	public static int triwaraFromFixed(long date) {
		return mod(dayFromFixed(date), 3) + 1;
	}


	/*- bali-caturwara-from-fixed -*/

	// TYPE fixed-date -> 1-4
	// Position of $date$ in 4-day Balinese cycle.

	public static int caturwaraFromFixed(long date) {
		return adjustedMod(asatawaraFromFixed(date), 4);
	}


	/*- bali-pancawara-from-fixed -*/

	// TYPE fixed-date -> 1-5
	// Position of $date$ in 5-day Balinese cycle.

	public static int pancawaraFromFixed(long date) {
		return mod(dayFromFixed(date) + 1, 5) + 1;
	}


	/*- bali-sadwara-from-fixed -*/

	// TYPE fixed-date -> 1-6
	// Position of $date$ in 6-day Balinese cycle.

	public static int sadwaraFromFixed(long date) {
		return mod(dayFromFixed(date), 6) + 1;
	}


	/*- bali-saptawara-from-fixed -*/

	// TYPE fixed-date -> 1-7
	// Position of $date$ in Balinese week.

	public static int saptawaraFromFixed(long date) {
		return mod(dayFromFixed(date), 7) + 1;
	}


	/*- bali-asatawara-from-fixed -*/

	// TYPE fixed-date -> 1-8
	// Position of $date$ in 8-day Balinese cycle.

	public static int asatawaraFromFixed(long date) {
		int day = dayFromFixed(date);
		return mod(Math.max(6, 4 + mod(day - 70, 210)), 8) + 1;
	}


	/*- bali-sangawara-from-fixed -*/

	// TYPE fixed-date -> 1-9
	// Position of $date$ in 9-day Balinese cycle.

	public static int sangawaraFromFixed(long date) {
		return mod(Math.max(0, dayFromFixed(date) - 3), 9) + 1;
	}


	/*- bali-dasawara-from-fixed -*/

	// TYPE fixed-date -> 0-9
	// Position of $date$ in 10-day Balinese cycle.

	public static int dasawaraFromFixed(long date) {
		int i = pancawaraFromFixed(date);
		int j = saptawaraFromFixed(date);
		return mod(pancawaraI[i - 1] + saptawaraJ[j - 1] + 1, 10);
	}
	private static final int[] pancawaraI = new int[] {5, 9, 7, 4, 8};
	private static final int[] saptawaraJ = new int[] {5, 4, 3, 7, 8, 6, 9};


	/*- bali-week-from-fixed -*/

	// TYPE fixed-date-> 1-30
	// Week number of $date$ in Balinese cycle.

	public static int weekFromFixed(long date) {
		return (int)quotient(dayFromFixed(date), 7) + 1;
	}


	/*- bali-on-or-before -*/

	// TYPE (balinese-date fixed-date) -> fixed-date
	// Last fixed date on or before $date$
	// with Pawukon $b-date$.

	public static long onOrBefore(Balinese bDate, long date) {
		int a5 = bDate.pancawara - 1;
		int a6 = bDate.sadwara - 1;
		int b7 = bDate.saptawara - 1;
		int b35 = mod(a5 + 14 + 15 * (b7 - a5), 35);
		int days = a6 + 36 * (b35 - a6);
		int capDelta = dayFromFixed(0);
		return date - mod((date + capDelta) - days, 210);
	}


	public static int day(Balinese bDate) {
		return (int)(onOrBefore(bDate, EPOCH + 209) - EPOCH + 1);
	}


	public static int week(Balinese bDate) {
		return (int)(quotient(day(bDate) - 1, 7) + 1);
	}


	/*- positions-in-interval -*/

	// TYPE (positive-integer positive-integer
	// TYPE non-negative-integer fixed-date fixed-date)
	// TYPE -> list-of-fixed-dates
	// List of occurrences of $n$-th day of $c$-day cycle
	// between $start$ and $end$ dates (inclusive).
	// $cap-Delta$ is position in cycle of RD 0.

	public static FixedVector positionsInInterval(int n, int c, int capDelta, long start, long end) {
		FixedVector result = new FixedVector();
		long pos = start;
		do {
			pos += mod(n - start - capDelta - 1, c);
			if(pos > end)
				break;
			result.addFixed(pos);
			pos += 1;
		} while(true);
		return result;
	}

	
	//
	// auxiliary methods
	//


	/*- kajeng-keliwon-in-gregorian -*/

	// TYPE gregorian-year -> list-of-fixed-dates
	// Occurrences of Kajeng Keliwon (9th day of
	// each 15-day subcycle of Pawukon) in Gregorian year.

	public static FixedVector kajengKeliwonInGregorian(long gYear) {
		long jan1 = Gregorian.toFixed(gYear, JANUARY, 1);
		long dec31 = Gregorian.toFixed(gYear, DECEMBER, 31);
		int capDelta = dayFromFixed(0);
		return positionsInInterval(9, 15, capDelta, jan1, dec31);
	}


	/*- tumpek-in-gregorian -*/

	// TYPE gregorian-year -> list-of-fixed-dates
	// Occurrences of Tumpek (14th day of Pawukon and
	// every 35th subsequent day) within Gregorian year.

	public static FixedVector tumpekInGregorian(long gYear) {
		long jan1 = Gregorian.toFixed(gYear, JANUARY, 1);
		long dec31 = Gregorian.toFixed(gYear, DECEMBER, 31);
		int capDelta = dayFromFixed(0);
		return positionsInInterval(14, 35, capDelta, jan1, dec31);
	}
	
	
	//
	// object methods
	//

	protected String toStringFields() {
		return
			"luang="		+ luang		+
			",dwiwara="		+ dwiwara	+
			",triwara="		+ triwara	+
			",caturwara="	+ caturwara	+
			",pancawara="	+ pancawara	+
			",sadwara="		+ sadwara	+
			",saptawara="	+ saptawara	+
			",asatawara="	+ asatawara	+
			",sangawara="	+ sangawara	+
			",dasawara="	+ dasawara	;
	}


	public static final String[] dwiwaraNames = new String[] {
		"Menga",
		"Pepet"};


	public static final String[] triwaraNames = new String[] {
		"Pasah",
		"Beteng",
		"Kajeng"};


	public static final String[] caturwaraNames = new String[] {
		"Sri",
		"Laba",
		"Jaya",
		"Menala"};


	public static final String[] pancawaraNames = new String[] {
		"Umanis",
		"Paing",
		"Pon",
		"Wage",
		"Keliwon"};


	public static final String[] sadwaraNames = new String[] {
		"Tungleh",
		"Aryang",
		"Urukung",
		"Paniron",
		"Was",
		"Maulu"};


	public static final String[] saptawaraNames = new String[] {
		"Redite",
		"Coma",
		"Anggara",
		"Buda",
		"Wraspati",
		"Sukra",
		"Saniscara"};


	public static final String[] asatawaraNames = new String[] {
		"Sri",
		"Indra",
		"Guru",
		"Yama",
		"Ludra",
		"Brahma",
		"Kala",
		"Uma"};


	public static final String[] sangawaraNames = new String[] {
		"Dangu",
		"Jangur",
		"Gigis",
		"Nohan",
		"Ogan",
		"Erangan",
		"Urungan",
		"Tulus",
		"Dadi"};


	public static final String[] dasawaraNames = new String[] {
		"Pandita",
		"Pati",
		"Suka",
		"Duka",
		"Sri",
		"Manuh",
		"Manusa",
		"Raja",
                "Dewa",
		"Raksasa"};


	public static final String[] weekNames = new String[] {
		"Sinta",
		"Landep",
		"Ukir",
		"Kulantir",
		"Taulu",

		"Gumbreg",
		"Wariga",
		"Warigadian",
		"Jukungwangi",
		"Sungsang",
		
		"Dunggulan",
		"Kuningan",
		"Langkir",
		"Medangsia",
		"Pujut",
		
		"Pahang",
		"Krulut",
		"Merakih",
		"Tambir",
		"Medangkungan",
		
		"Matal",
		"Uye",
		"Menail",
		"Parangbakat",
		"Bala",
		
		"Ugu",
		"Wayang",
		"Kelawu",
		"Dukut",
		"Watugunung"};

	public String format() {
		return java.text.MessageFormat.format("{0}{1} {2} {3} {4} {5} {6} {7} {8} {9}",
			new Object[]{
				luang ? "Luang " : "",
				nameFromNumber(dwiwara,			dwiwaraNames),
				nameFromNumber(triwara,			triwaraNames),
				nameFromNumber(caturwara,		caturwaraNames),
				nameFromNumber(pancawara,		pancawaraNames),
				nameFromNumber(sadwara,			sadwaraNames),
				nameFromNumber(saptawara,		saptawaraNames),
				nameFromNumber(asatawara,		asatawaraNames),
				nameFromNumber(sangawara,		sangawaraNames),
				nameFromNumber(dasawara,	dasawaraNames)
			}
		) +
		java.text.MessageFormat.format(" ({0})",
			new Object[]{
				nameFromNumber(week(this),	weekNames)
			}
		);
	}

	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(!(obj instanceof Balinese))
			return false;
		
		Balinese o = (Balinese)obj;
		
		return
			o.luang		== luang		&&
			o.dwiwara	== dwiwara		&&
			o.triwara	== triwara		&&
			o.caturwara	== caturwara	&&
			o.pancawara	== pancawara	&&
			o.sadwara	== sadwara		&&
			o.saptawara	== saptawara	&&
			o.asatawara	== asatawara	&&
			o.sangawara	== sangawara	&&
			o.dasawara	== dasawara		;
	}
}

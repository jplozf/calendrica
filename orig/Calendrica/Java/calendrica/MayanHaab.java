package calendrica;

	
public class MayanHaab extends ProtoDate {

	//
	// fields
	//


		public int month;
		public int day;


	//
	// constants
	//


	/*- mayan-haab-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of haab cycle.
	
	public static final long EPOCH = MayanLongCount.EPOCH - ordinal(new MayanHaab(18, 8));


	//
	// constructors
	//

	public MayanHaab() { }
	
	public MayanHaab(long date) {
		super(date);
	}
	
	public MayanHaab(Date date) {
		super(date);
	}
	
	public MayanHaab(int month, int day) {
		this.month	= month;
		this.day	= day;
	}
	
	//
	// date conversion methods
	//


	/*- mayan-haab-from-fixed -*/

	// TYPE fixed-date -> mayan-haab-date
	// Mayan haab date of fixed $date$.
	
	public void fromFixed(long date) {
		long count = mod(date - EPOCH, 365);
		day = (int)mod(count, 20);
		month = 1 + (int)quotient(count, 20);
	}
	
	
	public void fromArray(int[] a) {
		month	= a[0];
		day		= a[1];
	}
	
	
	//
	// support methods
	//


	/*- mayan-haab-ordinal -*/

	// TYPE mayan-haab-date -> non-negative-integer
	// Number of days into cycle of Mayan haab date.
	
	public static int ordinal(MayanHaab hDate) {
		return (hDate.month - 1) * 20 + hDate.day;
	}

	
	//
	// auxiliary methods
	//
	
	
	/*- mayan-haab-on-or-before -*/

	// TYPE (mayan-haab-date fixed-date) -> fixed-date
	// Fixed date of latest date on or before fixed $date$
	// that is Mayan haab date $haab$.
	
	public static long onOrBefore(MayanHaab haab, long date) {
		return date - mod(date - EPOCH - ordinal(haab), 365);
	}
	
	
	//
	// object methods
	//

	protected String toStringFields() {
		return "month=" + month + ",day=" + day;
	}

	public static final String[] monthNames = new String[] {
		"Pop",
		"Uo",
		"Zip",
		"Zotz",
		"Tzec",
		"Xul",
		"Yaxkin",
		"Mol",
		"Chen",
		"Yax",
		"Zac",
		"Ceh",
		"Mac",
		"Kankin",
		"Muan",
		"Pax",
		"Kayab",
		"Cumku",
		"Uayeb"};
	
	public String format() {
		return java.text.MessageFormat.format("{0} {1}",
			new Object[]{
				new Integer(day),
				nameFromMonth(month, monthNames)
			}
		);
	}

	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(!(obj instanceof MayanHaab))
			return false;
		
		MayanHaab o = (MayanHaab)obj;
		
		return
			o.month	== month	&&
			o.day	== day		;
	}
}

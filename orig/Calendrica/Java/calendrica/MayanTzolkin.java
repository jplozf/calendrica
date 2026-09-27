package calendrica;

	
public class MayanTzolkin extends ProtoDate {

	//
	// fields
	//

		public int number;
		public int name;

	//
	// constants
	//


	/*- mayan-tzolkin-epoch -*/

	// TYPE fixed-date
	// Start of tzolkin date cycle.
	
	public static final long EPOCH = MayanLongCount.EPOCH - ordinal(new MayanTzolkin(4, 20));


	//
	// constructors
	//

	public MayanTzolkin() { }
	
	public MayanTzolkin(long date) {
		super(date);
	}
	
	public MayanTzolkin(Date date) {
		super(date);
	}
	
	public MayanTzolkin(int number, int name) {
		this.number	= number;
		this.name	= name;
	}
	
	//
	// date conversion methods
	//


	/*- mayan-tzolkin-from-fixed -*/

	// TYPE fixed-date -> mayan-tzolkin-date
	// Mayan tzolkin date of fixed $date$.
	
	public void fromFixed(long date) {
		long count = date - EPOCH + 1;
		number = (int)adjustedMod(count, 13);
		name = (int)adjustedMod(count, 20);
	}
	
	public void fromArray(int[] a) {
		number	= a[0];
		name	= a[1];
	}
	
	
	//
	// support methods
	//


	/*- mayan-tzolkin-ordinal -*/

	// TYPE mayan-tzolkin-date -> non-negative-integer
	// Number of days into Mayan tzolkin cycle of $t-date$.
	
	public static int ordinal(MayanTzolkin tDate) {
		return mod(tDate.number - 1 + 39 * (tDate.number - tDate.name), 260);
	}

	
	//
	// auxiliary methods
	//
	
	
	/*- mayan-tzolkin-on-or-before -*/

	// TYPE (mayan-tzolkin-date fixed-date) -> fixed-date
	// Fixed date of latest date on or before fixed $date$
	// that is Mayan tzolkin date $tzolkin$.
	
	public static long onOrBefore(MayanTzolkin tzolkin, long date) {
		return date - mod(date - EPOCH - ordinal(tzolkin), 260);
	}


	/*- mayan-calendar-round-on-or-before -*/

	// TYPE (mayan-haab-date mayan-tzolkin-date fixed-date)
	// TYPE -> fixed-date
	// Fixed date of latest date on or before $date$, that is
	// Mayan haab date $haab$ and tzolkin date $tzolkin$.
	
	public static long calendarRoundOnOrBefore(MayanHaab haab, MayanTzolkin tzolkin, long date)
		throws BogusDateException
	{
		long haabCount = MayanHaab.ordinal(haab) + MayanHaab.EPOCH;
		long tzolkinCount = ordinal(tzolkin) + EPOCH;
		long diff = tzolkinCount - haabCount;
		long result;
		if(mod(diff, 5) == 0)
			result = date - mod(date - haabCount - 365 * diff, 18980);
		else
			throw new BogusDateException();

		return result;
	}
	
	
	//
	// object methods
	//

	protected String toStringFields() {
		return "number=" + number + ",name=" + name;
	}

	public static final String[] names = new String[] {
		"Imix",
		"Ik",
		"Akbal",
		"Kan",
		"Chicchan",
		"Cimi",
		"Manik",
		"Lamat",
		"Muluc",
		"Oc",
		"Chuen",
		"Eb",
		"Ben",
		"Ix",
		"Men",
		"Cib",
		"Caban",
		"Etznab",
		"Cauac",
		"Ahau"};
	
	public String format() {
		return java.text.MessageFormat.format("{0} {1}",
			new Object[]{
				new Integer(number),
				nameFromNumber(name, names)
			}
		);
	}

	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(!(obj instanceof MayanTzolkin))
			return false;
		
		MayanTzolkin o = (MayanTzolkin)obj;
		
		return
			o.number	== number	&&
			o.name		== name		;
	}
}

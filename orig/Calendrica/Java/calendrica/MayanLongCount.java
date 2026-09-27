package calendrica;


public class MayanLongCount extends Date {
	
	//
	// fields
	//

	public long baktun;
	public int katun;
	public int tun;
	public int uinal;
	public int kin;

	//
	// constructors
	//

	public MayanLongCount() { }
	
	public MayanLongCount(long date) {
		super(date);
	}
	
	public MayanLongCount(Date date) {
		super(date);
	}
	
	public MayanLongCount(long baktun, int katun, int tun, int uinal, int kin) {
		this.baktun	= baktun;
		this.katun	= katun;
		this.tun	= tun;
		this.uinal	= uinal;
		this.kin	= kin;
	}
	
	//
	// constants
	//
	
	
	/*- mayan-epoch -*/

	// TYPE fixed-date
	// Fixed date of start of the Mayan calendar, according
	// to the Goodman-Martinez-Thompson correlation.
	// That is, August 11, -3113.
	
	public static final long EPOCH = fixedFromJD(584283);
	
	
	//
	// date conversion methods
	//
	
	
	/*- fixed-from-mayan-long-count -*/

	// TYPE mayan-long-count-date -> fixed-date
	// Fixed date corresponding to the Mayan long $count$,
	// which is a list (baktun katun tun uinal kin).

	public static long toFixed(long baktun, int katun, int tun, int uinal, int kin) {
		return EPOCH +
			baktun * 144000 +
			katun * 7200 +
			tun * 360 +
			uinal * 20 +
			kin;
	}

	public long toFixed() {
		return toFixed(baktun, katun, tun, uinal, kin);
	}
	
	
	/*- mayan-long-count-from-fixed -*/

	// TYPE fixed-date -> mayan-long-count-date
	// Mayan long count date of fixed $date$.
	
	public void fromFixed(long date) {
		long longCount = date - EPOCH;
		baktun = quotient(longCount, 144000);
		int dayOfBaktun = (int)mod(longCount, 144000);
		katun = (int)quotient(dayOfBaktun, 7200);
		int dayOfKatun = mod(dayOfBaktun, 7200);
		tun = (int)quotient(dayOfKatun, 360);
		int dayOfTun = mod(dayOfKatun, 360);
		uinal = (int)quotient(dayOfTun, 20);
		kin = mod(dayOfTun, 20);
	}
	
	
	public void fromArray(int[] a) {
		baktun	= a[0];
		katun	= a[1];
		tun		= a[2];
		uinal	= a[3];
		kin		= a[4];
	}
	
	
	//
	// object methods
	//

	protected String toStringFields() {
		return "baktun=" + baktun + ",katun=" + katun +
			",tun=" + tun + ",uinal=" + uinal + ",kin=" + kin;
	}
	
	public String format() {
		return java.text.MessageFormat.format("{0}.{1}.{2}.{3}.{4}",
			new Object[]{
				new Long(baktun),
				new Integer(katun),
				new Integer(tun),
				new Integer(uinal),
				new Integer(kin)
			}
		);
	}
	
	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(!(obj instanceof MayanLongCount))
			return false;
		
		MayanLongCount o = (MayanLongCount)obj;
		
		return
			o.baktun	== baktun	&&
			o.katun		== katun	&&
			o.tun		== tun		&&
			o.uinal		== uinal	&&
			o.kin		== kin		;
	}
}

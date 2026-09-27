package calendrica;


public abstract class StandardDate extends Date {
	
	//
	// fields
	//

	public long year;
	public int month;
	public int day;

	//
	// constructors
	//

	public StandardDate() { };
	
	public StandardDate(long date) {
		super(date);
	}
	
	public StandardDate(Date date) {
		super(date);
	}

	public StandardDate(long year, int month, int day) {
		this.year	= year;
		this.month	= month;
		this.day	= day;
	}
	
	public StandardDate(int[] a) {
		fromArray(a);
	}

	//
	// date conversion methods
	//
	
	public void fromArray(int[] a) {
		year	= a[0];
		month	= a[1];
		day		= a[2];
	}
	
	//
	// object methods
	//
	
	protected String toStringFields() {
		return  "year=" + year + ",month=" + month + ",day=" + day;
	}

	protected boolean internalEquals(Object obj) {
		StandardDate o = (StandardDate)obj;

		if(this == obj)
			return true;

		return
			o.year	== year		&&
			o.month	== month	&&
			o.day	== day		;
	}
}

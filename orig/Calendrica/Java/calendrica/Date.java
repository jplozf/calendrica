package calendrica;


public abstract class Date extends ProtoDate {

	//
	// constructors
	//
	
	public Date() { }
	
	public Date(long date) {
		super(date);
	}
	
	public Date(Date date) {
		super(date);
	}

	//	
	// date conversion methods
	//

	public abstract long toFixed();
	
	public void convertTo(ProtoDate toDate) {
		convert(this, toDate);
	}
}

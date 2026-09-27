package calendrica;

import java.io.Serializable;


public class Time implements Cloneable, Serializable {
	
	//
	// fields
	//

	public int hour;
	public int minute;
	public double second;

	//
	// constructors
	//

	public Time() { }
	
	public Time(double moment) {
		fromMoment(moment);
	}
	
	public Time(int hour, int minute, double second) {
		this.hour	= hour;
		this.minute	= minute;
		this.second	= second;
	}
	
	//
	// time conversion methods
	//
	
	public static double toMoment(int hour, int minute, double second) {
		return hour / 24d + minute / (24d * 60) + second / (24d * 60 * 60);
	}

	public double toMoment() {
		return toMoment(hour, minute, second);
	}
	

	/*- time-from-moment -*/

	// TYPE moment -> time
	// Time of day (hour minute second) from moment $tee$.

	public void fromMoment(double tee) {
		hour = (int)Math.floor(ProtoDate.mod(tee * 24, 24));
		minute = (int)Math.floor(ProtoDate.mod(tee * 24 * 60, 60));
		second = ProtoDate.mod(tee * 24 * 60 * 60, 60);
	}

	
	//
	// object methods
	//

	public String toString() {
		return this.getClass().getName() + "[hour=" + hour + ",minute=" + minute + ",second=" + second + "]";
	}
	
	public String format() {
		int modHour = ProtoDate.mod(hour, 12);
		int aHour = modHour == 0 ? 12 : modHour;
		return java.text.MessageFormat.format("{0,number,00}:{1,number,00}:{2,number,00} {3}",
			new Object[]{
				new Integer(aHour),
				new Integer(minute),
				new Integer((int)second),
				ProtoDate.mod(hour, 24) < 12 ? "A.M." : "P.M."
			}
		);
	}

	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(!(obj instanceof Time))
			return false;
		
		Time o = (Time)obj;
		
		return
			o.hour		== hour		&&
			o.minute	== minute	&&
			o.second	== second	;
	}
}

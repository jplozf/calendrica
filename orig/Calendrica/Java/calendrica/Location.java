package calendrica;

import java.io.Serializable;


public class Location implements Cloneable, Serializable {
	
	//
	// fields
	//


	public String name;
	public double latitude;
	public double longitude;
	public double elevation;
	public double zone;


	//
	// constructors
	//


	public Location() { }
	
	public Location(String name, double latitude, double longitude, double elevation, double zone) {
		this.name = name;
		this.latitude = latitude;
		this.longitude = longitude;
		this.elevation = elevation;
		this.zone = zone;
	}
	
	//
	// auxilliary methods
	//
	

	public static final Location URBANA = new Location("Urbana, IL, USA", 40.1, -88.2, ProtoDate.mt(225), -6);

	public static final Location LOS_ANGELES = new Location("Los Angeles, CA, USA", ProtoDate.angle(34, 4, 0), -ProtoDate.angle(118, 15, 0), ProtoDate.mt(0), -8);
	
	
	//
	// object methods
	//


	public String toString() {
		return this.getClass().getName() + "[latitude=" + latitude + ",longitude=" + longitude + ",elevation=" + elevation + ",zone=" + zone + "]";
	}

	
	public String format() {
		return java.text.MessageFormat.format("{0}: lat {1} long {2} elev {3} zone {4}",
			new Object[]{
				new String(name),
				new Double(latitude),
				new Double(longitude),
				new Double(elevation),
				new Double(zone)
			}
		);
	}


	public boolean equals(Object obj) {
		if(this == obj)
			return true;
		
		if(!(obj instanceof Location))
			return false;
		
		Location o = (Location)obj;
		
		return
			o.latitude	== latitude		&&
			o.longitude	== longitude	&&
			o.elevation	== elevation	&&
			o.zone		== zone			;
	}
}
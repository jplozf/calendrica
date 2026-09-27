package calendrica;

import java.util.Vector;


public class FixedVector extends Vector {
	//
	// constructors
	//
	
	public FixedVector(int initialCapacity, int capacityIncrement) {
		super(initialCapacity, capacityIncrement);
	}
	
	public FixedVector(int initialCapacity) {
		super(initialCapacity);
	}
	
	public FixedVector() { }
	
	public final synchronized void addFixed(long fixed) {
		super.addElement(new Long(fixed));
	}
	
	public final synchronized long fixedAt(int index) {
		return ((Long)super.elementAt(index)).longValue();
	}
}

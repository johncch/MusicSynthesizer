package as.adamsmith.etherealdialpad.dsp;

import java.util.ArrayList;

/**
 * Based largely off http://gist.github.com/376028
 * 
 * A Unit generator
 * 
 * @author rndmcnlly
 * @author johncch
 */
public abstract class UGen {
	public static final int CHUNK_SIZE = 256;
	public static final int SAMPLE_RATE = 22050;

	ArrayList<UGen> kids = new ArrayList<UGen>(0);
	
	// fill CHUNK_SIZE samples
	// and return true if you actually did any work
	abstract public boolean render(final float[] buffer);

	final public synchronized UGen chuck(UGen that) {
		if(!that.kids.contains(this)) that.kids.add(this);
		return that; // returns RHS
	}
	
	final public synchronized UGen unchuck(UGen that) {		
		if(that.kids.contains(this)) that.kids.remove(this);
		return that; // returns RHS
	}
	
	protected void zeroBuffer(final float[] buffer) {
		for(int i = 0; i < CHUNK_SIZE; i++) {
			buffer[i] = 0;
		}
	}
	
	protected boolean renderKids(final float[] buffer) {
		boolean didSomeRealWork = false;
		for(int k = 0; k < kids.size(); k++) {
			didSomeRealWork |= kids.get(k).render(buffer);
		}
		return didSomeRealWork;
	}
}
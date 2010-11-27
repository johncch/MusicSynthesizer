package as.adamsmith.etherealdialpad.dsp;

import android.util.FloatMath;
import android.util.Log;

/**
 * Based largely off http://gist.github.com/376028
 * 
 * Any modifications in this code is to suit our own purposes.
 * 
 * @author rndmcnlly
 * @author johncch
 */
public class WtOsc extends UGen {
	public static final int BITS = 8;
	public static final int ENTRIES = 1<<(BITS-1);
	public static final int MASK = ENTRIES-1;
	
	private float phase;
	private float cyclesPerSample;
	
	final float[] table;
	
	public WtOsc () {
		table = new float[ENTRIES];
		Log.i("Information", "Entries length is " + ENTRIES);
	}
	
	public synchronized void setFreq(float freq) {
		cyclesPerSample = freq/SAMPLE_RATE;
	} 
	
	public synchronized boolean render(final float[] buffer) { // assume t is in 0.0 to 1.0
		for(int i = 0; i < CHUNK_SIZE; i++) {
			float scaled = phase * ENTRIES;
			final float fraction = scaled-(int)scaled;
			final int index = (int)scaled;
			// Log.i("INFORENDER", scaled + ", " + fraction + ", " + index + ": " + (index & MASK) + "::" + ((index + 1) & MASK));
			buffer[i] += (1.0f-fraction) * table[index&MASK] + fraction * table[(index+1)&MASK];
			phase = (phase+cyclesPerSample) - (int)phase;
			// Log.i("INFORENDER", "phase is " + phase);
		}
		
		return true;
	}
	
	public WtOsc fillWithSin() {
		final float dt = (float)(2.0*Math.PI/ENTRIES);
		for(int i = 0; i < ENTRIES; i++) {
			table[i] = FloatMath.sin(i*dt);
		}
		return this;
	}
	
	public WtOsc fillWithHardSin(final float exp) {
		final float dt = (float)(2.0*Math.PI/ENTRIES);
		for(int i = 0; i < ENTRIES; i++) {
			table[i] = (float) Math.pow(FloatMath.sin(i*dt),exp);
		}
		return this;
	}
	
	public WtOsc fillWithZero() {
		for(int i = 0; i < ENTRIES; i++) {
			table[i] = 0;
		}
		return this;
	}
	
	public WtOsc fillWithSqr() {
		for(int i = 0; i < ENTRIES; i++) {
			table[i] = (i < ENTRIES/2) ? 0.99f : -0.99f;
		}
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < ENTRIES; i++) {
			buf.append(table[i]);
			buf.append(",");
		}
		Log.i("SQUARE", "table=" + buf.toString());
		return this;
	}
	
	public WtOsc fillWithSqrDuty(float fraction) {
		for(int i = 0; i < ENTRIES; i++) {
			table[i] = (float)i/ENTRIES<fraction?1f:-1f;
		}
		return this;
	}
	
	public WtOsc fillWithSaw() {
		float dt = (float)(2.0/ENTRIES);
		for(int i = 0; i < ENTRIES; i++) {
			table[i] = 1.0f - i*dt;
		}
		return this;
	}
	
	public WtOsc fillWithTri() {
		Log.i("TESTTEST", "TESTTEST");
		float dt = 4.0f / ENTRIES;
		int direction = 1;
		float sum = 0f;
		for(int i = 0; i < ENTRIES; i++) {
			if(i < ENTRIES / 2) {
				table[i] = 1.0f - i * dt;
			} else {
				table[i] = -3.0f + i*dt;
			}
		}
		StringBuffer buf = new StringBuffer();
		for(int i = 0; i < ENTRIES; i++) {
			buf.append(table[i]);
			buf.append(",");
		}
		Log.i("TRIANGLE", "table=" + buf.toString());
		return this;
	}
}
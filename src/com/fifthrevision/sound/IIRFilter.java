package com.fifthrevision.sound;

import android.util.Log;

/**
 * A simple Infinite Impulse Response filter. The algorithm is very well known 
 * and basic. This implementation implements directly from 
 * http://www.dspguide.com/ch19/1.htm
 * 
 * @author johncch
 *
 */
public abstract class IIRFilter extends Unit {

	protected float[] a;
	protected float[] b;
	
	protected float cutOffFreq;
	
	public final Unit setFreq(float freq) {
		if(validFreq(freq) && freq != cutOffFreq) {
			cutOffFreq = freq;
			calcCoeff();
		}
		return this;
	}
	
	protected boolean validFreq(float f) {
		return f > 0;
	}
	
	@Override
	public void render(float[] buffer) {
		if(a == null || b == null) return;
		
		float[] out = new float[buffer.length];
		int minIndex = Math.max(a.length - 1, b.length);
		
		for(int i = minIndex; i < buffer.length; i++) {
			for(int j = 0; j < a.length; j++) {
				out[i] += a[j] * buffer[i - j];
			}
			for(int j = 0; j < b.length; j++) {
				out[i] += b[j] * out[i - j - 1];
			}
		}
		
		//for(int i = 0; i < buffer.length; i++) {
		//	buffer[i] = out[i];
		//}
		System.arraycopy(out, 0, buffer, 0, out.length);
	}

	protected abstract void calcCoeff();
	
}

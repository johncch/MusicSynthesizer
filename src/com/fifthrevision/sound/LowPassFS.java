package com.fifthrevision.sound;

/**
 * Four stage low pass filter
 * @author johncch
 *
 */
public class LowPassFS extends IIRFilter {

	@Override
	protected void calcCoeff() {
		float freqFrac = cutOffFreq/Unit.SAMPLE_RATE;
	    float x = (float) Math.exp(-14.445 * freqFrac);
	    a = new float[] { (float) Math.pow(1 - x, 4) };
	    b = new float[] { 4 * x, -6 * x * x, 4 * x * x * x, -x * x * x * x };
	}
	
	@Override
	protected boolean validFreq(float f) {
		return (f > 60);
	}

}

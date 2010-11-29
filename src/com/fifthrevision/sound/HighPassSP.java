package com.fifthrevision.sound;

/**
 * Single Pole high pass filter
 * 
 * @author johncch
 *
 */
public class HighPassSP extends IIRFilter {

	@Override
	protected void calcCoeff() {
		float fracFreq = cutOffFreq/Unit.SAMPLE_RATE;
		float x = (float) Math.exp(-2 * Math.PI * fracFreq);
		a = new float[] { (1 + x) / 2, -(1 + x) /2 };
		b = new float[] { x };
	}

}

package com.fifthrevision.sound;

import android.util.Log;

public class LowPassSP extends IIRFilter {

	@Override
	protected void calcCoeff() {
		float fracFreq = cutOffFreq/(Unit.SAMPLE_RATE * 2);
		Log.d("TEST", "fracFreq is " + fracFreq);
		float x = (float) Math.exp(-2 * Math.PI * fracFreq);
		a = new float[] {1f-x};
		b = new float[] {x};
		
		super.calcCoeff();
	}
	
}

package com.mobilelab.synth;

import android.util.Log;
import as.adamsmith.etherealdialpad.dsp.Dac;
import as.adamsmith.etherealdialpad.dsp.ExpEnv;
import as.adamsmith.etherealdialpad.dsp.WtOsc;

public class SynthManager {

	public static final int SINE_WAVE = 25;
	public static final int TRIANGLE_WAVE = 26;
	public static final int SQUARE_WAVE = 27;
	public static final int SAW_WAVE = 28;
	
	private int currentWaveShape = 25;
	
	// private ArrayList<UGen> pipeline = new ArrayList<UGen>();
	WtOsc oscillator;
	Dac dac;
	
	SynthesizerRunner run = new SynthesizerRunner();
	
	public SynthManager(){
		dac = new Dac();
		oscillator = new WtOsc();
		oscillator.chuck(dac);
		run.setDac(dac);
		Thread thread = new Thread(run);
		thread.start();
	}
	
	public void setCurrentWaveShape(int waveShape) {
		if(waveShape >= SINE_WAVE && waveShape <= SAW_WAVE) {
			this.currentWaveShape = waveShape;
		}
	}
	
	public void play(float freq) {
		if(run.stopped) {
			this.setWave();
			run.stopped = false;
		}
		oscillator.setFreq(freq);
	}
	
	private void setWave() {
		switch(this.currentWaveShape) {
		case SINE_WAVE:
			oscillator.fillWithSin();
			break;
		case TRIANGLE_WAVE:
			oscillator.fillWithSqr();
			break;
		case SQUARE_WAVE:
			oscillator.fillWithSqr();
			break;
		case SAW_WAVE:
			oscillator.fillWithSaw();
			break;
		}
	}
	
	public void stop() {
		oscillator.fillWithZero();
		run.stopped = true;
	}
	
}

class SynthesizerRunner implements Runnable {

	public boolean stopped = true;
	private Dac dac;
	
	public void setDac(Dac dac) {
		this.dac = dac;
	}
	
	@Override
	public void run() {
		dac.open();
		while(true) {
			dac.tick();
		}
	}

}


/**
 * This is temporary, will be removed in the future
 * 
 * @author johncch
 *
 */
class AudioThread extends Thread {
	Dac ugDac;
	
	public void init() {
		WtOsc ugOscA1 = new WtOsc();
		WtOsc ugOscA2 = new WtOsc();
		ExpEnv ugEnvA = new ExpEnv();
		
		ugOscA1.fillWithHardSin(70.0f);
		ugOscA2.fillWithHardSin(20.0f);
		
		ugDac = new Dac();
	
		/* Delay ugDelay = new Delay(UGen.SAMPLE_RATE/2);
		
		ugEnvA.chuck(ugDelay);
		ugDelay.chuck(ugDac);
		
		ugOscA1.chuck(ugEnvA);
		ugOscA2.chuck(ugEnvA);
		
		ugEnvA.setFactor(ExpEnv.hardFactor);*/
		
		ugOscA1.chuck(ugDac);
		ugOscA2.chuck(ugDac);
		
		ugOscA1.setFreq(440.0f);
		ugOscA2.setFreq(1200.0f);
	}				
	
	@Override
	public void run() {
		ugDac.open();
		while(!isInterrupted()) {
			ugDac.tick();
		}
		ugDac.close();
	}
}

package com.mobilelab.synth;

import java.util.ArrayList;

import com.fifthrevision.sound.AmpMod;
import com.fifthrevision.sound.LowPassSP;
import com.fifthrevision.sound.Osc;
import com.fifthrevision.sound.Unit;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;
import as.adamsmith.etherealdialpad.dsp.Dac;
import as.adamsmith.etherealdialpad.dsp.ExpEnv;
import as.adamsmith.etherealdialpad.dsp.UGen;
import as.adamsmith.etherealdialpad.dsp.WtOsc;

public class SynthManager {

	public static final String TAG = "SYNTH_MANAGER";
	
	public static final int SINE_WAVE = 25;
	public static final int TRIANGLE_WAVE = 26;
	public static final int SQUARE_WAVE = 27;
	public static final int SAW_WAVE = 28;
	
	private int currentWaveShape = 25;

	private ArrayList<Osc> oscillators = new ArrayList<Osc>();
	
	private SynthesizerRunner runner = new SynthesizerRunner();
	
	public SynthManager(){
		
		//Test
		// runner.addIntoPipeline(new AmpMod().setFreq(100));
		runner.addIntoPipeline(new LowPassSP().setFreq(400));
		
		Thread thread = new Thread(runner);
		thread.start();
	}
	
	public void setCurrentWaveShape(int waveShape) {
		if(waveShape >= SINE_WAVE && waveShape <= SAW_WAVE) {
			this.currentWaveShape = waveShape;
			this.setWave();
		}
	}
	
	public void setSoundSourceFreq(int id, float freq) {
		Log.d(TAG, "Setting frequency " + freq);
		this.oscillators.get(id).setFreq(freq);
	}
	
	public void addSoundSource(int id) {
		if ( oscillators.size() < (id + 1) ) {
			for(int i = oscillators.size(); i < (id + 1); i++){
				oscillators.add(new Osc());
				Log.d(TAG, "CREATED NEW OSCILLATOR");
			}
			Log.d(TAG, "FINISHED CREATING OSCILLATORS");
			this.setWave();
			Log.d(TAG, "SET WAVES!");
		}
		Osc osc = oscillators.get(id);
		Log.d(TAG, "ADDING INTO RUNNER");
		runner.addIntoOscillators(osc);
		Log.d(TAG, "AFTER ADDING INTO RUNNER");
	}
	
	public void removeSoundSource(int id) {
		Osc osc = oscillators.get(id);
		runner.removeFromOscillators(osc);
	}
	
	private void setWave() {
		switch(this.currentWaveShape) {
			case SINE_WAVE:
				for(int i = 0; i < oscillators.size(); i++) {
					oscillators.get(i).fillWithSin();
				}
				break;
			case TRIANGLE_WAVE:
				for(int i = 0; i < oscillators.size(); i++) {
					oscillators.get(i).fillWithTri();
				}
				break;
			case SQUARE_WAVE:
				for(int i = 0; i < oscillators.size(); i++) {
					oscillators.get(i).fillWithSqr();
				}
				break;
			case SAW_WAVE:
				for(int i = 0; i < oscillators.size(); i++) {
					oscillators.get(i).fillWithSaw();
				}
				break;
		}
	}
	
}

class SynthesizerRunner implements Runnable {
	
	private static String TAG = "SYNTHESIZER_RUNNER";
	
	private final float[] localBuffer;
	private final AudioTrack track;
	private final short [] target = new short[UGen.CHUNK_SIZE];
	private final short [] silentTarget = new short[UGen.CHUNK_SIZE];
	
	private ArrayList<Unit> pipeline = new ArrayList<Unit>();
	private ArrayList<Unit> oscillators = new ArrayList<Unit>(); 
	
	public boolean running = true;
	
	public SynthesizerRunner() {
		localBuffer = new float[Unit.CHUNK_SIZE];
		
		int minSize = AudioTrack.getMinBufferSize(
				UGen.SAMPLE_RATE,
				AudioFormat.CHANNEL_CONFIGURATION_MONO,
        		AudioFormat.ENCODING_PCM_16BIT);
		
		track = new AudioTrack(
        		AudioManager.STREAM_MUSIC,
        		UGen.SAMPLE_RATE,
        		AudioFormat.CHANNEL_CONFIGURATION_MONO,
        		AudioFormat.ENCODING_PCM_16BIT,
        		Math.max(UGen.CHUNK_SIZE*4, minSize),
        		AudioTrack.MODE_STREAM);
	}
	
	public synchronized void addIntoOscillators(Unit unit) {
		Log.d("ADDDING", "Adding Oscillators");
		oscillators.add(unit);
	}
	
	public synchronized void removeFromOscillators(Unit unit) {
		Log.d("REMOVVING", "removing Oscillators");
		if(oscillators.contains(unit)) {
			oscillators.remove(unit);
		}
	}
	
	public synchronized void addIntoPipeline(Unit unit) {
		pipeline.add(unit);
	}
	
	public synchronized void removeFromPipeline(Unit unit) {
		if(pipeline.contains(unit)) {
			pipeline.remove(unit);
		}
	}
	
	/**
	 * Pipeline rendering
	 */
	//@Override
	public void run() {
		
		track.play();
		
		while(running) {
			//synchronized(this) {
				if(oscillators.size() == 0) {
					track.write(silentTarget, 0, silentTarget.length);
				} else {
					//Log.d("OSCILLATOR", "Writing data");
					zeroBuffer(localBuffer);
					
					synchronized(this) {
						for(int i = 0; i < oscillators.size(); i++) {
							oscillators.get(i).render(localBuffer);
						}
						
						for(int i = 0; i < pipeline.size(); i++) {
							pipeline.get(i).render(localBuffer);
						}
					}
					
					for(int i = 0; i < Unit.CHUNK_SIZE; i++) {
						// target[i] = (short)(32768.0f*localBuffer[i]);
						float scaledDownMagnitude = 32768.0f / oscillators.size();
						target[i] = (short)(scaledDownMagnitude * localBuffer[i]);
					}
					
					track.write(target, 0, target.length);
				}
			//}
			
			//Log.d(TAG, "Outside Synck block");
		}
		
		track.stop();
		track.release();
	}
	
	protected void zeroBuffer(final float[] buffer) {
		for(int i = 0; i < Unit.CHUNK_SIZE; i++) {
			buffer[i] = 0;
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

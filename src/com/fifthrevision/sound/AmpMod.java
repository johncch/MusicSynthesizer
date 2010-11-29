package com.fifthrevision.sound;

public class AmpMod extends Unit {
	private float phase;
	private float cyclesPerSample;
	
	public synchronized Unit setFreq(float freq) {
		cyclesPerSample = freq/SAMPLE_RATE;
		return this;
	}
	
	@Override
	public Unit render(final float[] buffer) {
		for(int i = 0; i < CHUNK_SIZE; i++) {
			float scaled = phase * WaveTable.ENTRIES;
			final float fraction = scaled-(int)scaled;
			final int index = (int)scaled;
			buffer[i] *= ((1.0f-fraction) * WaveTable.SinWave[index & WaveTable.MASK] 
			          + fraction * WaveTable.SinWave[(index+1) & WaveTable.MASK]);
			phase = (phase + cyclesPerSample) - (int)phase;
		}	
		return this;
	}

}

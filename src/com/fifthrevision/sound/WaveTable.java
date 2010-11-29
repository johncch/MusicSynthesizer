package com.fifthrevision.sound;

import android.util.FloatMath;

/**
 * This class provides lookup tables and basic parameters for other classes.
 * It exists mainly as a static class 
 * 
 * @author johncch
 *
 */
public class WaveTable {

	public static final int BITS = 8;
	public static final int ENTRIES = 1<<(BITS-1);
	public static final int MASK = ENTRIES-1;

	public static float[] SawToothWave = new float[ENTRIES];
	public static float[] TriangularWave = new float[ENTRIES];
	public static float[] SinWave = new float[ENTRIES];
	public static float[] SquareWave = new float[ENTRIES];

	static {
		float dt = (float)(2.0*Math.PI/ENTRIES);
		for(int i = 0; i < ENTRIES; i++) {
			SinWave[i] = FloatMath.sin(i*dt);
		}

		for(int i = 0; i < ENTRIES; i++) {
			SquareWave[i] = (i < ENTRIES/2) ? 1f : -1f;
		}

		dt = (float)(2.0/ENTRIES);
		for(int i = 0; i < ENTRIES; i++) {
			SawToothWave[i] = 1.0f - i*dt;
		}

		dt = 4.0f / ENTRIES;
		for(int i = 0; i < ENTRIES; i++) {
			if(i < ENTRIES / 2) {
				TriangularWave[i] = 1.0f - i * dt;
			} else {
				TriangularWave[i] = -3.0f + i*dt;
			}
		}

	}

}

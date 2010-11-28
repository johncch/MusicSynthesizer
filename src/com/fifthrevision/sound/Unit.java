package com.fifthrevision.sound;

/**
 * The basis class of the sound 
 * 
 * @author johncch
 */
public abstract class Unit {
	public static final int CHUNK_SIZE = 256;
	public static final int SAMPLE_RATE = 22050;
	
	abstract public void render(final float[] buffer);
}

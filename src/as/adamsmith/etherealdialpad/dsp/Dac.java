package as.adamsmith.etherealdialpad.dsp;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;


public class Dac extends UGen {
	private final float[] localBuffer;
	private boolean isClean;
	private final AudioTrack track;
	private final short [] target = new short[UGen.CHUNK_SIZE];
	private final short [] silentTarget = new short[UGen.CHUNK_SIZE];
	
	public Dac() {
		localBuffer = new float[CHUNK_SIZE];
		
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
	
	public boolean render(final float[] _buffer) {
		if(!isClean) {
			zeroBuffer(localBuffer);

			isClean = true;
		}
		// localBuffer is always clean right here, does it stay that way?
		isClean = !renderKids(localBuffer);
		return !isClean; // we did some work if the buffer isn't clean
	}
	
	public void open() {
		track.play();
	}
	
	public void tick() {
		//Log.d("Inside DAC", "Ticking");
		render(localBuffer);
		
		if(isClean) {
			// sleeping is messy, so lets just queue this silent buffer
			track.write(silentTarget, 0, silentTarget.length);
		} else {
			for(int i = 0; i < CHUNK_SIZE; i++) {
				target[i] = (short)(32768.0f*localBuffer[i]);
			}
			
			track.write(target, 0, target.length);
		}
	}
	
	public void close() {
		track.stop();
        track.release();
	}
}

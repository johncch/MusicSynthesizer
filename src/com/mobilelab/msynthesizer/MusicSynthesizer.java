package com.mobilelab.msynthesizer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.ToggleButton;
import android.widget.ViewFlipper;


import com.mobilelab.synth.SynthManager;

public class MusicSynthesizer extends Activity implements OnTouchListener{

	public static String Tag = "MUSIC_SYNTHESIZER";

	private ViewFlipper flipper;
	private DrawView drawView;

	private SynthManager sm;

	/*flipper buttons*/
	ToggleButton panel1Btn; 
	ToggleButton panel2Btn; 
	ToggleButton panel3Btn;

	/*lfo buttons*/
	private ToggleButton lfo1Btn;
	private ToggleButton lfo2Btn;
	private SeekBar lfo1SeekBar;
	private SeekBar lfo2SeekBar;

	/*oscillator wave buttons*/
	private ToggleButton sqrBtn;
	private ToggleButton triBtn;
	private ToggleButton sawBtn;
	private ToggleButton sinBtn;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		flipper = (ViewFlipper) findViewById(R.id.flipper);
		drawView = (DrawView) findViewById(R.id.XYPad);

		drawView.setOnTouchListener(this);
		
		
		/*initialize panels buttons and set listeners*/
		panel1Btn = (ToggleButton) findViewById(R.id.panel1B);
		panel2Btn = (ToggleButton) findViewById(R.id.panel2B);
		panel3Btn = (ToggleButton) findViewById(R.id.panel3B);
		//ImageButton back = (ImageButton) findViewById(R.id.back);
		setPanelListeners();

		/*initialize lfo buttons and set listeners*/
		lfo1Btn = (ToggleButton) this.findViewById(R.id.LFO1);
		lfo2Btn = (ToggleButton) this.findViewById(R.id.LFO2);
		lfo1SeekBar = (SeekBar) this.findViewById(R.id.LFO1Bar);
		lfo2SeekBar = (SeekBar) this.findViewById(R.id.LFO2Bar);
		initLFOListeners();

		/*initialize oscillator wave buttons and set listeners*/
		sqrBtn = (ToggleButton) this.findViewById(R.id.SquareWave);
		triBtn = (ToggleButton) this.findViewById(R.id.TriangleWave);
		sawBtn = (ToggleButton) this.findViewById(R.id.SawWave);
		sinBtn = (ToggleButton) this.findViewById(R.id.SineWave);    
		setWaveListeners();
		 
		sm = new SynthManager();
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item){
		super.onOptionsItemSelected(item);
		Intent intent = new Intent(MusicSynthesizer.this, About.class);  //intent to about activity
		startActivity(intent);
		return true;
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		menu.add(0, Menu.FIRST, Menu.NONE, "About"); //create menu button
		return true;
	}
	
	private void setPanelListeners(){
		/* if button1 is pressed, display 1st panel*/
		panel1Btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				panel2Btn.setChecked(false);
				panel3Btn.setChecked(false);
				panel1Btn.setChecked(true);
				
				if (flipper.getDisplayedChild() != 0){
					flipper.setInAnimation(inFromRightAnimation());
					// flipper.setOutAnimation(outToRightAnimation());
					flipper.setDisplayedChild(0);  
				}
			}
		});

		/* if button2 is pressed, display 2st panel*/
		panel2Btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				panel1Btn.setChecked(false);
				panel3Btn.setChecked(false);
				panel2Btn.setChecked(true);
				
				/*only do animation if panel isn't currently displayed*/
				if (flipper.getDisplayedChild() != 1){
					flipper.setInAnimation(inFromRightAnimation());
					//flipper.setOutAnimation(outToRightAnimation());
					flipper.setDisplayedChild(1);   
				}
			}
		});

		/* if button3 is pressed, display 3srd panel*/
		panel3Btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				panel1Btn.setChecked(false);
				panel2Btn.setChecked(false);
				panel3Btn.setChecked(true);
				
				/*only do animation if panel isn't currently displayed*/
				if (flipper.getDisplayedChild() != 2){
					flipper.setInAnimation(inFromRightAnimation());
					//flipper.setOutAnimation(outToLeftAnimation());
					flipper.setDisplayedChild(2); 
				}
			}
		});

	}
	
	private void initLFOListeners() {
		lfo1Btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if(lfo1Btn.isChecked()){
					sm.addAmpMod(10 + lfo1SeekBar.getProgress());
				} else {
					sm.removeAmpMod();
				}
			}
		});
		
		lfo2Btn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				if(lfo2Btn.isChecked()) {
					
				} else {
					
				}
			}
		});
		
		lfo1SeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			//@Override
			public void onProgressChanged(SeekBar seekbar, int progress, boolean fromUser) {
				sm.setAmpModFreq(10 + progress);
			}

			//@Override
			public void onStartTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}

		//	@Override
			public void onStopTrackingTouch(SeekBar arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
	}

	private void setWaveListeners(){
		sqrBtn.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Log.d("TEST", "TESTTT");
				/*make them behave as radio buttons*/
				triBtn.setChecked(false);
				sawBtn.setChecked(false);
				sinBtn.setChecked(false);
				sqrBtn.setChecked(true);
				/*set wave*/
				sm.setCurrentWaveShape(SynthManager.SQUARE_WAVE);
			}
		});

		triBtn.setOnClickListener(new View.OnClickListener() {	
			public void onClick(View v) {
				/*make them behave as radio buttons*/
				sqrBtn.setChecked(false);
				sawBtn.setChecked(false);
				sinBtn.setChecked(false);
				triBtn.setChecked(true);
				
				/*set wave*/
				sm.setCurrentWaveShape(SynthManager.TRIANGLE_WAVE);
			}
		});

		sawBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				/*make them behave as radio buttons*/
				sqrBtn.setChecked(false);
				triBtn.setChecked(false);
				sinBtn.setChecked(false);
				sawBtn.setChecked(true);

				/*set wave*/
				sm.setCurrentWaveShape(SynthManager.SAW_WAVE);
			}
		});

		sinBtn.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				/*make them behave as radio buttons*/
				triBtn.setChecked(false);
				sawBtn.setChecked(false);
				sqrBtn.setChecked(false);
				sinBtn.setChecked(true);

				/*set wave*/
				sm.setCurrentWaveShape(SynthManager.SINE_WAVE);
			}
		});
	}

	

	/*** inFromRightAnimation
	 *   Animation for panels to "slide in" from the right when their button is pressed
	 *   @return inFromRight - animation to be used for panels to slide in
	 ***/
	private Animation inFromRightAnimation() {
		Animation inFromRight = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT,  +1.0f, Animation.RELATIVE_TO_PARENT,  0.0f,
				Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
		inFromRight.setDuration(200);
		inFromRight.setInterpolator(new AccelerateInterpolator());
		return inFromRight;
	}

	/*** outToLeftAnimation
	 *   Animation for panels to "slide out" to the left when another panel button is pressed
	 *   @return outtoLeft - animation to be used for panels to slide out
	 ***/
	private Animation outToLeftAnimation() {
		Animation outtoLeft = new TranslateAnimation(
				Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,  -1.0f,
				Animation.RELATIVE_TO_PARENT,  0.0f, Animation.RELATIVE_TO_PARENT,   0.0f);
		outtoLeft.setDuration(500);
		outtoLeft.setInterpolator(new AccelerateInterpolator());
		return outtoLeft;
	}

	//@Override
	public boolean onTouch(View view, MotionEvent event) {
		
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		// dumpEvent(event);
		int id = 0;

		switch(actionCode) {
		case MotionEvent.ACTION_DOWN:
			id = event.getPointerId(0);
			sm.addSoundSource(id);
			break;
		case MotionEvent.ACTION_POINTER_DOWN:
			id = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			Log.d(Tag, "Adding Pointer " + id);
			sm.addSoundSource(id);
			break;
		case MotionEvent.ACTION_MOVE:
			Log.d(Tag, "Action Move");
			for(int i = 0; i < event.getPointerCount(); i++) {
				id = event.getPointerId(i);
				float freq = 400 + event.getY(i) * 4;
				sm.setSoundSourceFreq(id, freq);
			}
			break;
		case MotionEvent.ACTION_UP:
			id = event.getPointerId(0);
			sm.removeSoundSource(id);
			break;
		case MotionEvent.ACTION_POINTER_UP:
			id = action >> MotionEvent.ACTION_POINTER_ID_SHIFT;
			sm.removeSoundSource(id);
			break;
		}
		drawView.onTouch(view, event);
		return true;
	}

	/*	//@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(group.getId() == waveGroup.getId()) {
			if(checkedId == sqrBtn.getId()) {
				sm.setCurrentWaveShape(SynthManager.SQUARE_WAVE);
			} else if (checkedId == triBtn.getId()) {
				sm.setCurrentWaveShape(SynthManager.TRIANGLE_WAVE);
			} else if (checkedId == sawBtn.getId()) {
				sm.setCurrentWaveShape(SynthManager.SAW_WAVE);
			} else {
				sm.setCurrentWaveShape(SynthManager.SINE_WAVE);
			}
		}
	}*/

	/* private void dumpEvent(MotionEvent event) {
		 String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
		 StringBuilder sb = new StringBuilder();
		 int action = event.getAction();
		 int actionCode = action;
		 sb.append("event ACTION_").append(names[actionCode]);
		 sb.append("[Coord: " + event.getX() + ", " + event.getY() + "]");
		 Log.d("MS", sb.toString());
	 }*/

	private void dumpEvent(MotionEvent event) {
		String names[] = { "DOWN" , "UP" , "MOVE" , "CANCEL" , "OUTSIDE" ,
				"POINTER_DOWN" , "POINTER_UP" , "7?" , "8?" , "9?" };
		StringBuilder sb = new StringBuilder();
		int action = event.getAction();
		int actionCode = action & MotionEvent.ACTION_MASK;
		sb.append("event ACTION_" ).append(names[actionCode]);
		if (actionCode == MotionEvent.ACTION_POINTER_DOWN
				|| actionCode == MotionEvent.ACTION_POINTER_UP) {
			sb.append("(pid " ).append(
					action >> MotionEvent.ACTION_POINTER_ID_SHIFT);
			sb.append(")" );
		}
		sb.append("[" );
		for (int i = 0; i < event.getPointerCount(); i++) {
			sb.append("#" ).append(i);
			sb.append("(pid " ).append(event.getPointerId(i));
			sb.append(")=" ).append((int) event.getX(i));
			sb.append("," ).append((int) event.getY(i));
			if (i + 1 < event.getPointerCount())
				sb.append(";" );
		}
		sb.append("]" );
		Log.d("MSX", sb.toString());
	}
}  
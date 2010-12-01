package com.mobilelab.msynthesizer;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageView;

// DrawView is a view. It listens to mouse click events and draws a point at the point that it was clicked on.
public class DrawView extends ImageView implements OnTouchListener {
     HashMap<Point, Long> points = new HashMap<Point, Long>();
     Paint paint = new Paint();
     boolean released = false;
     Random gen = new Random();
     
     public DrawView(Context context, AttributeSet attrs) {
	     super(context, attrs);
	     setFocusable(true);
	     setFocusableInTouchMode(true);
	     this.setOnTouchListener(this);
	     paint.setAntiAlias(true);
	   //  this.setBackgroundColor(android.graphics.Color.BLACK);
	   
     }

	 @Override
	 public void onDraw(Canvas canvas) {
			 canvas.drawColor(android.graphics.Color.TRANSPARENT);
		 
	     for (Point point : points.keySet()) {
	    	 if (((new Date().getTime()) - points.get(point)) < 1000){
	    		 paint.setColor(android.graphics.Color.rgb(Math.max((int)point.x,255), Math.max((int)point.y,255), gen.nextInt(255)));
	    		 canvas.drawCircle(point.x, point.y, point.size, paint);
	    	 }
	    	 else {
	    		 paint.setColor(Color.TRANSPARENT);
	    	     canvas.drawCircle(point.x, point.y, point.size, paint);
	    	 }
	     }
	 }
	 
	 public boolean onTouch(View view, MotionEvent event) {
		Point point = new Point();
		point.x = event.getX();
		point.y = event.getY();
		point.size = Math.max(gen.nextInt(40), 15);
		points.put(point, new Date().getTime());
		invalidate();
		released = false;
	     return true;
	 }
	 
	
	 
	 /* private void dumpEvent(MotionEvent event) {
		 String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
		 StringBuilder sb = new StringBuilder();
		 int action = event.getAction();
		 int actionCode = action;
		 sb.append("event ACTION_").append(names[actionCode]);
		 sb.append("[Coord: " + event.getX() + ", " + event.getY() + "]");
		 Log.d("DRAWVIEW", sb.toString());
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
		 Log.d("DRAWVIEWX", sb.toString());
	 }
}

class Point {
	float x, y;
	int size;
     
    @Override
    public String toString() {
    	return x + ", " + y;
    }
}



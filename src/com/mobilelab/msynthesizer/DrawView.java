package com.mobilelab.msynthesizer;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

// DrawView is a view. It listens to mouse click events and draws a point at the point that it was clicked on.
public class DrawView extends View implements OnTouchListener {
     List<Point> points = new ArrayList<Point>();
     Paint paint = new Paint();
     
     public DrawView(Context context, AttributeSet attrs) {
	     super(context, attrs);
	     setFocusable(true);
	     setFocusableInTouchMode(true);
	     this.setOnTouchListener(this);
	     paint.setColor(Color.WHITE);
	     paint.setAntiAlias(true);
	     this.setBackgroundColor(Color.GRAY);
     }

	 @Override
	 public void onDraw(Canvas canvas) {
	     for (Point point : points) {
	         canvas.drawCircle(point.x, point.y, 5, paint);
	     }
	 }

	 public boolean onTouch(View view, MotionEvent event) {
		 dumpEvent(event);
	     Point point = new Point();
	     point.x = event.getX();
	     point.y = event.getY();
	     points.add(point);
	     invalidate();
	     return true;
	 }
	 
	 private void dumpEvent(MotionEvent event) {
		 String names[] = {"DOWN", "UP", "MOVE", "CANCEL", "OUTSIDE", "POINTER_DOWN", "POINTER_UP", "7?", "8?", "9?"};
		 StringBuilder sb = new StringBuilder();
		 int action = event.getAction();
		 int actionCode = action;
		 sb.append("event ACTION_").append(names[actionCode]);
		 sb.append("[Coord: " + event.getX() + ", " + event.getY() + "]");
		 Log.d("DRAWVIEW", sb.toString());
	 }
}

class Point {
	float x, y;
     
    @Override
    public String toString() {
    	return x + ", " + y;
    }
}



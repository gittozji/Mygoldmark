package com.phlofy.mygoldmark;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

public class MyContainer extends View{
	float height;
	float proportion=0;
	public MyContainer(Context context,AttributeSet set) {
		super(context,set);
		// TODO Auto-generated constructor stub
	}
	protected void onDraw(Canvas canvas){
		super.onDraw(canvas);
		
		View view  = findViewById(R.id.fragment_main_mycontainer);
		float h = this.getHeight();//father高
		float hh = h*3/25;
		float w = this.getWidth();
		float ww = w*2/11;
		Paint paint = new Paint();
		// 去锯齿
		paint.setAntiAlias(true);
		paint.setColor(Color.BLUE);
		// ----------设置填充风格后绘制----------
		paint.setStyle(Paint.Style.FILL);
		paint.setColor(Color.GREEN);
		//canvas.drawCircle(120, 40, 30, paint);
		//绘制矩形
		if(proportion<=0.33){
			paint.setColor(Color.RED);
		}
		if(proportion>0.33&&proportion<=0.66){
			paint.setColor(Color.YELLOW);
		}
		
		if(proportion>0&&proportion<1){
			height=hh+(h-2*hh)*(1-proportion);
			canvas.drawRect(ww, height, w-ww, h-hh, paint);
		}
		if(proportion>=1){
			canvas.drawRect(ww, hh, w-ww, h-hh, paint);
		}
		//外边框
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(3);
		Path path=new Path();
		paint.setColor(Color.BLUE);
		path.moveTo(ww, hh);
		path.lineTo(ww, h-hh);
		path.lineTo(w-ww, h-hh);
		path.lineTo(w-ww, hh);
		path.lineTo(ww, hh);
		canvas.drawPath(path, paint);
	}
	public void setProportion(float proportion){
		this.proportion=proportion;
		invalidate();
	}
}

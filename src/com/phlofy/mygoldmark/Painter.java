package com.phlofy.mygoldmark;

import android.R.integer;
import android.R.string;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;

import java.security.PublicKey;
import java.util.*;

public class Painter extends View{
	//������
	private GetPasswordListener getPasswordListener = null;
	String password = null;
	float currentX=0,currentY=0;//��ǰ����
	float radius;//�뾶
	Point[] points;//�Ÿ���
	int[] current_points;//��ѡ�ĵ㼯��
	int number_points=0;//��ѡ��ĸ���
	Context context = null;
	public Painter(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
		//��ȡ��Ļ��С
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getResources().getDisplayMetrics(); 
		float screenWidth = dm.widthPixels; // �����Ļ���
		float screenHeight = dm.heightPixels;
		float gap = screenHeight-7*screenWidth/6;
		points = new Point[10];
		current_points = new int[10];
		radius=screenWidth/14;
		points[0]=new Point(screenWidth/6, screenWidth/6+gap);
		points[1]=new Point(3*screenWidth/6, screenWidth/6+gap);
		points[2]=new Point(5*screenWidth/6, screenWidth/6+gap);
		points[3]=new Point(screenWidth/6, 3*screenWidth/6+gap);
		points[4]=new Point(3*screenWidth/6, 3*screenWidth/6+gap);
		points[5]=new Point(5*screenWidth/6, 3*screenWidth/6+gap);
		points[6]=new Point(screenWidth/6, 5*screenWidth/6+gap);
		points[7]=new Point(3*screenWidth/6, 5*screenWidth/6+gap);
		points[8]=new Point(5*screenWidth/6, 5*screenWidth/6+gap);
		points[9]=new Point(radius*2, radius*2);
		
		// TODO Auto-generated constructor stub
	}
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//int color1 = Color.argb(0, 0, 0, 0);
		//canvas.drawColor(Color.BLACK);
		Paint paint = new Paint();
		paint.setAntiAlias(true);
		paint.setStrokeWidth(3);
		int color = Color.argb(127, 93, 189, 238);
		paint.setColor(color);
		paint.setStyle(Paint.Style.STROKE);
		
		for(int i=0;i<10;i++){
			if(isExist(i))
				paint.setStyle(Paint.Style.FILL);
			else
				paint.setStyle(Paint.Style.STROKE);
			canvas.drawCircle(points[i].getX(), points[i].getY(), radius, paint);
		}
		paint.setStrokeWidth(3);
		paint.setStyle(Paint.Style.STROKE);
		paint.setColor(Color.WHITE);
		Path path = setPath();
		canvas.drawPath(path,paint);
	}
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		currentX=event.getX();
		currentY=event.getY();
		int a = isPointArea();
		if(a!=-1){
			current_points[number_points++]=a;
		}
		invalidate();
		//System.out.println(a+"\n");
		if(event.getAction()==MotionEvent.ACTION_UP){
			StringBuffer sBuffer = new StringBuffer();
			for(int i=0;i<number_points;i++){
				sBuffer.append(current_points[i]);
			}
			if(isExist(9))
				password = "00";
			else {
				password = sBuffer.toString();
			}
			if(number_points!=0)
				getPasswordListener.getPass(password);
			number_points=0;
		}	
		return true;
	}
	//������ѡ����Լ���ǰ����
	private Path setPath(){
		Path path = new Path();
		if(number_points>0){
			path.moveTo(points[current_points[0]].getX(), points[current_points[0]].getY());
			for(int i=1;i<number_points;i++)
				path.lineTo(points[current_points[i]].getX(), points[current_points[i]].getY());
			path.lineTo(currentX, currentY);
		}else{
			path.moveTo(currentX, currentY);
		}			
		return path;
	}
	private int isPointArea(){
		for(int i=0;i<points.length;i++){
			float x_r=points[i].getX()-currentX;
			float y_r=points[i].getY()-currentY;
			if((x_r*x_r+y_r*y_r)<=(radius*radius)&&!isExist(i)){
				return i;
			}
				
		}
		return -1;
	}
	private boolean isExist(int e) {
		for(int i=0;i<number_points;i++){
			if(current_points[i]==e)
				return true;
		}
		return false;
	}
	public String getPassword(){
		return password;
	}
	//ΪGetPasswordListener�ӿ���Ӽ�����
	public void setGetPasswordListener(GetPasswordListener getPasswordListener){
		this.getPasswordListener = getPasswordListener;
	}
	//����ӿ�
	public interface GetPasswordListener{
		public void getPass(String password);
	}
}

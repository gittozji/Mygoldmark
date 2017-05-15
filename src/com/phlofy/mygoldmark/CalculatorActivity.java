package com.phlofy.mygoldmark;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

public class CalculatorActivity extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layout_calculate);
		Window gw = getWindow();
		WindowManager.LayoutParams lp = gw.getAttributes();
		gw.setGravity(Gravity.BOTTOM);
		/*DisplayMetrics dm = new DisplayMetrics();
		dm = this.getResources().getDisplayMetrics(); 
		int screenWidth = dm.widthPixels; // 获得屏幕宽度
		int screenHeight = dm.heightPixels;// 获得屏幕高度
		lp.x = 0; // 新位置X坐标
        lp.y = screenHeight/2; // 新位置Y坐标
        lp.width = screenWidth; // 宽度
        lp.height = screenHeight/2; // 高度
        lp.alpha = 1; // 透明度
        gw.setAttributes(lp);*/
	}
	
}
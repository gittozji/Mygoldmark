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
		int screenWidth = dm.widthPixels; // �����Ļ���
		int screenHeight = dm.heightPixels;// �����Ļ�߶�
		lp.x = 0; // ��λ��X����
        lp.y = screenHeight/2; // ��λ��Y����
        lp.width = screenWidth; // ���
        lp.height = screenHeight/2; // �߶�
        lp.alpha = 1; // ͸����
        gw.setAttributes(lp);*/
	}
	
}
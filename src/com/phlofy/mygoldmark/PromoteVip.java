package com.phlofy.mygoldmark;

import android.app.Activity;
import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import java.lang.*;

public class PromoteVip extends Activity{

	//变量声明
	Myapp app = null;
	Mygolddatabase mdb = null;
	TextView promotionAB = null;
	EditText promotionBB = null;
	EditText promotionCB = null;
	Button save = null;
	Button cancel = null;
	String nullString = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_promotion);
		
		app = (Myapp) getApplication();
		
		promotionAB = (TextView) findViewById(R.id.promotion_a_b);
		promotionBB = (EditText) findViewById(R.id.promotion_b_b);
		promotionCB = (EditText) findViewById(R.id.promotion_c_b);
		save = (Button) findViewById(R.id.promotion_save);
		cancel = (Button) findViewById(R.id.promotion_cancel);
		nullString = promotionBB.getText().toString();
		promotionAB.setText(app.getVip1()+app.getVip2()+"");
		
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(promotionBB.getText().toString().equals(nullString)){
					Toast.makeText(PromoteVip.this, "还未输入经验值！", Toast.LENGTH_SHORT).show();
				}else{
					if(promotionCB.getText().toString().equals("17")){
						mdb = new Mygolddatabase(PromoteVip.this,"mygolddatabase",1);
						ContentValues values = new ContentValues();
						long newVip = (Long.parseLong(promotionBB.getText().toString()))+app.getVip1();
						values.put("vip", newVip);
						mdb.getReadableDatabase().update("list_a", values,"account=?", new String[]{app.getAccount()});
						app.setVip1(newVip);
						Toast.makeText(PromoteVip.this, "升级成功", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(PromoteVip.this, "无效或过期密钥", Toast.LENGTH_SHORT).show();
					}
				}
				
				
			}
		});
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		
	}
	
}

package com.phlofy.mygoldmark;

import com.phlofy.mygoldmark.Painter.GetPasswordListener;

import android.app.Activity;
import android.os.Bundle;
import android.view.View.OnCreateContextMenuListener;

public class GestruePass extends Activity{
	Painter painter = null;
	String passString = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_creactgesture);
		painter = (Painter) findViewById(R.id.creactgesture_painter);
		painter.setGetPasswordListener(new GetPasswordListener() {
			
			@Override
			public void getPass(String password) {
				// TODO Auto-generated method stub
				passString = password;
			}
		});
	}
	
}

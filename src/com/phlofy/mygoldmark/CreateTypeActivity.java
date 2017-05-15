package com.phlofy.mygoldmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import android.app.Activity;
import android.app.ApplicationErrorReport.CrashInfo;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class CreateTypeActivity extends Activity{
	Myapp app = null;
	ImageView iv = null;
	EditText et = null;
	GridView gv = null;
	Button b = null;
	Mygolddatabase mdb = null;
	Mydatabase db = null;
	int ig_retrun_virtual = -1;
	int ig_retrun_index = -1;
	byte[] be = null;
	ArrayList<Map<String,Object>> listItems = null;
	String null_string = null;
	
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_createtype);
		//初始化设置输入法为无
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
		app = (Myapp)getApplication();
		iv = (ImageView) findViewById(R.id.createtype_imag);
		et = (EditText) findViewById(R.id.createtype_edit);
		gv = (GridView) findViewById(R.id.createtype_grid_gridview);
		b = (Button) findViewById(R.id.createtype_button);
		mdb = new Mygolddatabase(CreateTypeActivity.this, "mygolddatabase", 1);
		db = new Mydatabase(CreateTypeActivity.this, app.getAccount(), 1);
		//====================创建图像listItems集合===============================
		Cursor c = mdb.getReadableDatabase().rawQuery("select * from list_c", null);
		listItems = new ArrayList<Map<String,Object>>();
		while(c.moveToNext()){
			Map<String,Object>listItem = new HashMap<String,Object>();
			listItem.put("id", c.getInt(c.getColumnIndex("id")));
			listItem.put("icon", c.getBlob(c.getColumnIndex("icon")));
	    	listItems.add(listItem);
	    }
		MyAdapter myAdapter = new MyAdapter(CreateTypeActivity.this,listItems,R.layout.sign_grid_icon,new String[]{"icon","id"},new int[]{R.id.sign_grid_icon,R.id.sign_grid_text}); 
		gv.setAdapter(myAdapter);
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				be = (byte[]) listItems.get(arg2).get("icon");
				iv.setImageBitmap(BitmapFactory.decodeByteArray(be, 0, be.length));
				ig_retrun_virtual = arg2;
			}
		});
		null_string = et.getText().toString();
		//保存事件
		b.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(ig_retrun_virtual==-1){
					Toast.makeText(CreateTypeActivity.this, "还未选择图片", Toast.LENGTH_SHORT).show();
				}else{
					ig_retrun_index = ig_retrun_virtual;
					//保存新建类型
					if(et.getText().toString().equals(null_string)){
						Toast.makeText(CreateTypeActivity.this, "类型名不能为空", Toast.LENGTH_SHORT).show();						
					}
					else{
						try{
							db.getReadableDatabase().execSQL("insert into type values(?,?)",new Object[]{et.getText().toString(),be});
							Type p = new Type(et.getText().toString(),be);
							LinkedList<Type> link = app.getMylink();
							link.add(p);
							app.setMylink(link);
							finish();
						}catch(Exception e){
							Toast.makeText(CreateTypeActivity.this, "该类型已存在", Toast.LENGTH_SHORT).show();
						}
					}
				}
			}
		});
	}
	public void cancel_OnClick(View source){
		finish();
	}
	public void onDestroy(){
		super.onDestroy();
		if(mdb!=null){
			mdb.close();
		}
		if(db!=null){
			db.close();
		}
	}
}

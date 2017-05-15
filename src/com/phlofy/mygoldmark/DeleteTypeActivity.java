package com.phlofy.mygoldmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.ContentValues;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DeleteTypeActivity extends Activity{
	Myapp app = null;
	LinkedList<Type> mylink = null;
	ListView lv = null;
	ImageView iv = null;
	TextView tv = null;
	Button delete = null;
	List<Map<String,Object>> listItems = null;
	Mydatabase db = null;
	String null_string = null;
	int ig_retrun_virtual = -1;
	byte[] be = null;
	boolean deleteRight = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_deletetype);
		app = (Myapp) getApplication();
		mylink = app.getMylink();	
		db = new Mydatabase(DeleteTypeActivity.this, app.getAccount(), 1);
		lv = (ListView) findViewById(R.id.layout_delete_list);
		iv = (ImageView) findViewById(R.id.layout_delete_imag);
		tv = (TextView) findViewById(R.id.layout_delete_edit);
		delete = (Button) findViewById(R.id.layout_delete_button);
		listItems = new ArrayList<Map<String,Object>>();
		for(int i=0;i<mylink.size();i++){
			Map<String,Object>listItem = new HashMap<String,Object>();
			listItem.put("name", mylink.get(i).name);
			listItem.put("icon", mylink.get(i).icon);
	    	listItems.add(listItem);
		}
		MyAdapter simpleAdapter = new MyAdapter(DeleteTypeActivity.this,listItems,R.layout.delete_list,new String[]{"name","icon"},new int[]{R.id.delete_list_text,R.id.delete_list_imag}); 
		lv.setAdapter(simpleAdapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				be = (byte[]) listItems.get(arg2).get("icon");
				iv.setImageBitmap(BitmapFactory.decodeByteArray(be, 0, be.length));
				tv.setText((String)(listItems.get(arg2).get("name")));
				ig_retrun_virtual = arg2;
			}
		});
		delete.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(deleteRight == false){
					deleteRight = true;
					Toast.makeText(DeleteTypeActivity.this, "双击删除", Toast.LENGTH_SHORT).show();
					new Thread(){
						public void run() {
							try {
								Thread.sleep(500);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							deleteRight = false;
						}
						
					}.start();
				}else{
					if(ig_retrun_virtual==-1){
						Toast.makeText(DeleteTypeActivity.this, "还未选择类型", Toast.LENGTH_SHORT).show();			
					}
					else{
						if(tv.getText().toString().equals("其他")){
							Toast.makeText(DeleteTypeActivity.this, "抱歉，该类型不允许删除", Toast.LENGTH_SHORT).show();			
						}
						else{
							mylink.remove(ig_retrun_virtual);
							app.setMylink(mylink);
							ContentValues values = new ContentValues();
							values.put("type", "其他");
							db.getReadableDatabase().update("bill", values , "type=?", new String[]{tv.getText().toString()});
							db.getReadableDatabase().delete("type", "name=?", new String[]{tv.getText().toString()});
							
							finish();
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
		if(db!=null){
			db.close();
		}
	}
}

package com.phlofy.mygoldmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ChooseIcon {
	Mygolddatabase mdb;
	Cursor cursor=null;
	List<Map<String,Object>> listItems = null;
	ImageButton ig = null;
	GridView gv;
	ImageView gv_imag = null;
	int ig_retrun_index = -1;
	int ig_retrun_virtual = -1;
	Context context = null;
	RelativeLayout lay = null;
	
	
	ChooseIcon(Context context,Mygolddatabase mdb){
		this.context = context;
		this.mdb = mdb;
	}
	public void ininitialize(final ImageButton ig){
		mdb = new Mygolddatabase(context, "mygolddatabase", 1);
		//为头像按钮事件提供布局
		lay = (RelativeLayout)((Activity) context).getLayoutInflater().inflate(R.layout.sign_grid,null);
		gv = (GridView) lay.findViewById(R.id.sign_grid_gridview);
		gv_imag = (ImageView) lay.findViewById(R.id.sign_grid_imag);
		try{
			byte[] b = (byte[]) listItems.get(ig_retrun_index).get("icon");
			gv_imag.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
		}catch(Exception e){}
		
		//从数据库中得到头像byte[]和名字数据放到listItems中
		cursor = mdb.getReadableDatabase().rawQuery("select * from list_b", null);
		listItems = new ArrayList<Map<String,Object>>();
		while(cursor.moveToNext()){
			Map<String,Object>listItem = new HashMap<String,Object>();
			listItem.put("id", cursor.getInt(cursor.getColumnIndex("id")));
			listItem.put("icon", cursor.getBlob(cursor.getColumnIndex("icon")));
	    	listItems.add(listItem);
	    }
		//生成adapter给GridView
		MyAdapter simpleAdapter = new MyAdapter(context,listItems,R.layout.sign_grid_icon,new String[]{"icon","id"},new int[]{R.id.sign_grid_icon,R.id.sign_grid_text}); 
		gv.setAdapter(simpleAdapter);
		
		//产生对话框放置GridView
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(lay)
		.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//设置ImageButton的图片
				if(ig_retrun_virtual==-1){
					Toast.makeText(context, "还未选择图片", Toast.LENGTH_LONG).show();
				}else{
					ig_retrun_index = ig_retrun_virtual;
					byte[] b = (byte[]) listItems.get(ig_retrun_index).get("icon");
					BitmapDrawable bd = new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0, b.length));
					ig.setBackgroundDrawable(bd);
					
					//以下if语句为UserManager请求代理
					if(((Activity) context).toString().equals("UserManager")){
						Myapp app = (Myapp)((Activity) context).getApplication();
						app.setIcon(b);
						ContentValues values = new ContentValues();
						values.put("icon", b);
						mdb.getReadableDatabase().update("list_a", values, "account=?", new String[]{app.getAccount()});
					}
				}
			}
		})
		.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
			}
		})
		.create()
		.show();	
		gv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				// TODO Auto-generated method stub
				byte[] b = (byte[]) listItems.get(arg2).get("icon");
				
				gv_imag.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
				ig_retrun_virtual = arg2;
			}
		});
	}
	public int getIndex(){
		return ig_retrun_index;
	}
	public List<Map<String,Object>> getListItems(){
		return listItems;
	}
}

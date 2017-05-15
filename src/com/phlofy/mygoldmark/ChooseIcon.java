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
		//Ϊͷ��ť�¼��ṩ����
		lay = (RelativeLayout)((Activity) context).getLayoutInflater().inflate(R.layout.sign_grid,null);
		gv = (GridView) lay.findViewById(R.id.sign_grid_gridview);
		gv_imag = (ImageView) lay.findViewById(R.id.sign_grid_imag);
		try{
			byte[] b = (byte[]) listItems.get(ig_retrun_index).get("icon");
			gv_imag.setImageBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
		}catch(Exception e){}
		
		//�����ݿ��еõ�ͷ��byte[]���������ݷŵ�listItems��
		cursor = mdb.getReadableDatabase().rawQuery("select * from list_b", null);
		listItems = new ArrayList<Map<String,Object>>();
		while(cursor.moveToNext()){
			Map<String,Object>listItem = new HashMap<String,Object>();
			listItem.put("id", cursor.getInt(cursor.getColumnIndex("id")));
			listItem.put("icon", cursor.getBlob(cursor.getColumnIndex("icon")));
	    	listItems.add(listItem);
	    }
		//����adapter��GridView
		MyAdapter simpleAdapter = new MyAdapter(context,listItems,R.layout.sign_grid_icon,new String[]{"icon","id"},new int[]{R.id.sign_grid_icon,R.id.sign_grid_text}); 
		gv.setAdapter(simpleAdapter);
		
		//�����Ի������GridView
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setView(lay)
		.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				// TODO Auto-generated method stub
				//����ImageButton��ͼƬ
				if(ig_retrun_virtual==-1){
					Toast.makeText(context, "��δѡ��ͼƬ", Toast.LENGTH_LONG).show();
				}else{
					ig_retrun_index = ig_retrun_virtual;
					byte[] b = (byte[]) listItems.get(ig_retrun_index).get("icon");
					BitmapDrawable bd = new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0, b.length));
					ig.setBackgroundDrawable(bd);
					
					//����if���ΪUserManager�������
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
		.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
			
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

package com.phlofy.mygoldmark;


import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ActionBar.OnNavigationListener;
import android.app.AlertDialog.Builder;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.Toast;

public class SaveOrDelete extends Activity{
	Fragment_two two = null;
	Intent intent = null;
	Bundle bundle = null;
	final int ITEM_DELETE = 0x11a;
	final int ITEM_RETURN = 0x11b;
	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.saveordelete);

		intent = getIntent();
		bundle= intent.getBundleExtra("extra");
		two = new Fragment_two();
		two.setArguments(bundle);
		getFragmentManager().beginTransaction().replace(R.id.saveordelete_fragment, two).commit();
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,ITEM_DELETE,0,"删除账单");
		menu.add(0,ITEM_RETURN,0,"返回上层");
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem mi){
		switch(mi.getItemId()){
		case ITEM_DELETE:
			AlertDialog.Builder builder = new AlertDialog.Builder(SaveOrDelete.this);
			builder.setTitle("My 金迹")
			.setIcon(R.drawable.ic_launcher)
			.setMessage("\n确定删除内部ID为: "+bundle.get("id")+" 的账单？\n")
			.setPositiveButton("确定", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					Myapp app = (Myapp) getApplication();
					Mydatabase db = new Mydatabase(SaveOrDelete.this, app.getAccount(), 1);
					db.getReadableDatabase().delete("bill", "id=?", new String[]{bundle.get("id")+""});
					float f = (Float) bundle.get("money");
					app.setCost(app.getCost()-f);
					finish();
				}
				
			})
			.setNegativeButton("取消", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					
				}
				
			})
			.create()
			.show();
			break;
		case ITEM_RETURN:
			finish();
			break;
		default:
			finish();
		}
		
		return true;
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		
		if(two.getPopupExist()){
			two.getPopup().dismiss();
			two.setPopupExist(false);
		}	
		else
			super.onKeyDown(keyCode, event);
		return true;
	}
	
}

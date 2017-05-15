package com.phlofy.mygoldmark;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.phlofy.mygoldmark.Painter.GetPasswordListener;

import android.R.integer;
import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class UserManager extends Activity{
	//=====================������==================
	final int ITEM_DELETE = 0x11a; 
	final int ITEM_MODIFY = 0x11b;
	final int ITEM_MONEY = 0x11C;
	final int ITEM_LANDING = 0x11d;
	final int ITEM_SIGN = 0x11e;
	final int ITEM_PASS = 0x120;
	
	//=====================������==================
	Myapp app = null;
	ImageButton ig = null;
	List<Map<String,Object>> listItems,listItems2;
	Mygolddatabase mdb = null;
	ChooseIcon ci = null;
	//---------Button��-----------------
	Button userNameButton = null;
	Button userRuputationButton = null;
	Button userVipButton = null;
	Button et_a3 = null;
	Button et_b3 = null;
	Button et_c3 = null;
	//---------TextView��---------------
	TextView userEditB = null;
	TextView userEditC = null;
	//---------PopupWindow--------------
	View root = null;
	PopupWindow popupWindow = null;
	
	@SuppressLint("NewApi")
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		root = getLayoutInflater().inflate(R.layout.layout_creactgesture, null);
		//actionBar�Ĳ��Ըı�
		ActionBar bar = getActionBar();
		Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.title);
		BitmapDrawable bitmapDrawable = new BitmapDrawable(bitmap);
		bar.setBackgroundDrawable(bitmapDrawable);
		bar.setHomeButtonEnabled(true);		
		
		setContentView(R.layout.layout_user);
		app = (Myapp) getApplication();
		app.wayB(UserManager.this);
		
		ig = (ImageButton) findViewById(R.id.user_imagebutton);
		mdb = new Mygolddatabase(UserManager.this, "mygolddatabase", 1);
		//�޸�ͷ��Ķ���
		ci = new ChooseIcon(UserManager.this, mdb);
			
		userNameButton = (Button) findViewById(R.id.user_name_button);
		userRuputationButton = (Button) findViewById(R.id.user_ruputation_button);
		userVipButton = (Button) findViewById(R.id.user_vip_button);
		
		userEditB = (TextView) findViewById(R.id.user_edit_b);
		userEditC = (TextView) findViewById(R.id.user_edit_c);
		
		//��ʾͷ��
		byte[] b = app.getIcon();
		BitmapDrawable bd = new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0, b.length));
		ig.setBackgroundDrawable(bd);	
		ig.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//Ϊͷ��ť�¼��ṩ����
				ci.ininitialize(ig);
			}
		});
		
		//�����ǳ�
		userNameButton.setText(app.getName());
		userNameButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final LinearLayout lay = (LinearLayout)getLayoutInflater().inflate(R.layout.fragment_text,null);
				final EditText et_text = (EditText)lay.findViewById(R.id.fragment_text_edit);
				et_text.setText(userNameButton.getText().toString());
				final AlertDialog.Builder builder = new AlertDialog.Builder(UserManager.this);
				builder.setIcon(R.drawable.ic_launcher)
				.setTitle("�޸��ǳ�")
				.setView(lay)
				.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
					}
				})
				.setPositiveButton("�޸�", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						userNameButton.setText(et_text.getText().toString());
						app.setName(et_text.getText().toString());
						ContentValues values = new ContentValues();
						values.put("name", et_text.getText().toString());
						mdb.getReadableDatabase().update("list_a", values, "account=?", new String[]{app.getAccount()});
						Toast.makeText(UserManager.this, "�޸ĳɹ�", Toast.LENGTH_SHORT).show();
					}
				})
				.create()
				.show();		
			}
		});
		
		//���óɾ�
		userRuputationButton.setText(app.getReputasion());
		userRuputationButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final AlertDialog.Builder builder = new AlertDialog.Builder(UserManager.this);
				builder.setIcon(R.drawable.ic_launcher)
				.setTitle("�޸ĳɾ�")
				.setMessage("\n��Ǹ������ʱ��ԭ�򣬸ù���ʵ�ֵ��㷨��δ��ơ�����\n")
				.setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
					}
				})
				.create()
				.show();		
			}
		});
		
		//����vip
		userVipButton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final AlertDialog.Builder builder = new AlertDialog.Builder(UserManager.this);
				builder.setIcon(R.drawable.ic_launcher)
				.setTitle("����vip")
				.setMessage("\nvip�ȼ��ɾ���ֵ����������ֵ���˵��Ĺ�ϵΪ:\n1�˵�=1����\n")
				.setPositiveButton("��Կ����", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						ComponentName comp = new ComponentName(UserManager.this,PromoteVip.class);
						Intent intent = new Intent();
						intent.setComponent(comp);
						startActivity(intent);
					}
				})
				.setNegativeButton("ȷ������", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
					}
				})
				.create()
				.show();		
			}
		});
		
		//Ϊ�����Ѻ�ϵ������Text
		userEditB.setText(app.getHistory_cost()+"");
		userEditC.setText(app.getHistory_proportion()+"%");
		
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(0,ITEM_MONEY,0,"ÿ�½��");
		menu.add(0,ITEM_MODIFY,0,"�޸�����");
		menu.add(0,ITEM_DELETE,0,"ɾ���˺�");
		menu.add(0,ITEM_LANDING,0,"���½UI");
		menu.add(0,ITEM_SIGN,0,"��ע��UI");
		menu.add(0,ITEM_PASS,0,"��ȫ����");
		return true;
	}
	public boolean onOptionsItemSelected(MenuItem mi){
		switch(mi.getItemId()){
		case android.R.id.home:
			finish();
			break;
		case ITEM_MONEY :
			final LinearLayout lay = (LinearLayout)getLayoutInflater().inflate(R.layout.fragment_text_a,null);
			final EditText et_text = (EditText)lay.findViewById(R.id.fragment_text_a_edit);
			et_text.setText(app.getIncome()+"");
			final AlertDialog.Builder builder = new AlertDialog.Builder(UserManager.this);
			builder.setIcon(R.drawable.ic_launcher)
			.setTitle("����ÿ��Ԥ����")
			.setView(lay)
			.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				}
			})
			.setPositiveButton("�޸�", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					float money=(float) Double.parseDouble(et_text.getText().toString());
					Caculate caculate = new Caculate();
					float money1 = caculate.exchange(money,1);
					app.setIncome(money1);
					ContentValues values = new ContentValues();
					values.put("income", money1);
					mdb.getReadableDatabase().update("list_a", values, "account=?", new String[]{app.getAccount()});
					Toast.makeText(UserManager.this, "�޸ĳɹ�", Toast.LENGTH_SHORT).show();
				}
			})
			.create()
			.show();	
			break;
		case ITEM_MODIFY:
			final LinearLayout lay1 = (LinearLayout)getLayoutInflater().inflate(R.layout.modify_password,null);
			final EditText et_a = (EditText)lay1.findViewById(R.id.modify_passwork_a);
			final EditText et_b = (EditText)lay1.findViewById(R.id.modify_passwork_b);
			final EditText et_c = (EditText)lay1.findViewById(R.id.modify_passwork_c);
			final AlertDialog.Builder builder1 = new AlertDialog.Builder(UserManager.this);
			builder1.setIcon(R.drawable.ic_launcher)
			.setTitle("�޸�����")
			.setView(lay1)
			.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				}
			})
			.setPositiveButton("�޸�", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					if(et_a.getText().toString().equals(app.getPassword())&&et_b.getText().toString().equals(et_c.getText().toString())){
						app.setPassword(et_b.getText().toString());
						ContentValues values = new ContentValues();
						values.put("password", et_b.getText().toString());
						mdb.getReadableDatabase().update("list_a", values, "account=?", new String[]{app.getAccount()});
						final SharedPreferences preferences = getSharedPreferences("phlofy",MODE_WORLD_READABLE);
						Editor editor = preferences.edit();
						editor.putString("password", et_b.getText().toString());
						editor.commit();
						Toast.makeText(UserManager.this, "�޸ĳɹ�", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(UserManager.this, "�޸�ʧ��", Toast.LENGTH_SHORT).show();
					}
					
				}
			})
			.create()
			.show();	
			break;
		case ITEM_DELETE:
			final LinearLayout lay2 = (LinearLayout)getLayoutInflater().inflate(R.layout.layout_deleteaccount,null);
			final EditText edit = (EditText)lay2.findViewById(R.id.deleteaccount_edit);
			final AlertDialog.Builder builder2 = new AlertDialog.Builder(UserManager.this);
			builder2.setIcon(R.drawable.ic_launcher)
			.setTitle("ɾ���˺�")
			.setView(lay2)
			.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				}
			})
			.setPositiveButton("ɾ��", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					if(edit.getText().toString().equals(app.getPassword())){
						mdb.getReadableDatabase().delete("list_a", "account=?", new String[]{app.getAccount()});
						Mydatabase db = new Mydatabase(UserManager.this, app.getAccount(), 1);
						deleteDatabase(app.getAccount());
						Intent intent = new Intent(UserManager.this,LandingActivity.class);		
						startActivity(intent);
						finish();
					}else{
						Toast.makeText(UserManager.this, "ɾ��ʧ��", Toast.LENGTH_SHORT).show();
					}
					
				}
			})
			.create()
			.show();	
			break;
		case ITEM_LANDING :
			Intent intent = new Intent(UserManager.this,LandingActivity.class);		
			startActivity(intent);
			break;
		case ITEM_SIGN :
			Intent intent1 = new Intent(UserManager.this,SignActivity.class);		
			startActivity(intent1);
			break;
		case ITEM_PASS :
			final LinearLayout lay3 = (LinearLayout)getLayoutInflater().inflate(R.layout.modify_gestrue,null);
			et_a3 = (Button)lay3.findViewById(R.id.modify_Gestrue_a);
			et_b3 = (Button)lay3.findViewById(R.id.modify_Gestrue_b);
			et_c3 = (Button)lay3.findViewById(R.id.modify_Gestrue_c);
			et_a3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					DisplayMetrics dm = new DisplayMetrics();
					dm = getResources().getDisplayMetrics(); 
					int screenWidth = dm.widthPixels; // �����Ļ���
					int screenHeight = dm.heightPixels;// �����Ļ�߶�
					Painter painter = (Painter) root.findViewById(R.id.creactgesture_painter);
					popupWindow = new PopupWindow(root, screenWidth, screenHeight);
					popupWindow.showAtLocation(et_a3, Gravity.BOTTOM,0,0);
					painter.setGetPasswordListener(new GetPasswordListener() {
						
						@Override
						public void getPass(String password) {
							// TODO Auto-generated method stub
							et_a3.setText(password);
							popupWindow.dismiss();
						}
					});
				}
			});
			et_b3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					DisplayMetrics dm = new DisplayMetrics();
					dm = getResources().getDisplayMetrics(); 
					int screenWidth = dm.widthPixels; // �����Ļ���
					int screenHeight = dm.heightPixels;// �����Ļ�߶�
					Painter painter = (Painter) root.findViewById(R.id.creactgesture_painter);
					popupWindow = new PopupWindow(root, screenWidth, screenHeight);
					popupWindow.showAtLocation(et_b3, Gravity.BOTTOM,0,0);
					painter.setGetPasswordListener(new GetPasswordListener() {
						
						@Override
						public void getPass(String password) {
							// TODO Auto-generated method stub
							et_b3.setText(password);
							popupWindow.dismiss();
						}
					});
				}
			});
			et_c3.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					DisplayMetrics dm = new DisplayMetrics();
					dm = getResources().getDisplayMetrics(); 
					int screenWidth = dm.widthPixels; // �����Ļ���
					int screenHeight = dm.heightPixels;// �����Ļ�߶�
					Painter painter = (Painter) root.findViewById(R.id.creactgesture_painter);
					popupWindow = new PopupWindow(root, screenWidth, screenHeight);
					popupWindow.showAtLocation(et_c3, Gravity.BOTTOM,0,0);
					painter.setGetPasswordListener(new GetPasswordListener() {
						
						@Override
						public void getPass(String password) {
							// TODO Auto-generated method stub
							et_c3.setText(password);
							popupWindow.dismiss();
						}
					});
				}
			});
		
			final AlertDialog.Builder builder3 = new AlertDialog.Builder(UserManager.this);
			builder3.setIcon(R.drawable.ic_launcher)
			.setTitle("��������")
			.setView(lay3)
			.setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {
				
				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
				}
			})
			.setPositiveButton("�޸�", new DialogInterface.OnClickListener(){

				@Override
				public void onClick(DialogInterface arg0, int arg1) {
					// TODO Auto-generated method stub
					final SharedPreferences preferences = getSharedPreferences("phlofy",MODE_WORLD_READABLE);
					String passString = preferences.getString("Gesture", "00");
					if(et_a3.getText().toString().equals(passString)&&et_b3.getText().toString().equals(et_c3.getText().toString())){
						Editor editor = preferences.edit();
						editor.putString("Gesture", et_b3.getText().toString());
						editor.commit();
						Toast.makeText(UserManager.this, "�޸ĳɹ�", Toast.LENGTH_SHORT).show();
					}else{
						Toast.makeText(UserManager.this, "�޸�ʧ��", Toast.LENGTH_SHORT).show();
					}
					
				}
			})
			.create()
			.show();	
			break;
		}
		return true;
	}
	public void userReturnOnClick(View source){
		finish();
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		userVipButton.setText("vip-"+app.getVip());
		super.onResume();
	}
	public void onDestroy(){
		super.onDestroy();
		if(mdb!=null){
			mdb.close();
		}
	}
	public String toString(){
		return "UserManager";
	}
}

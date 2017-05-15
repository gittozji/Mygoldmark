/*
 * 增加账目的Fragment~
 */

package com.phlofy.mygoldmark;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Fragment;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint({ "NewApi", "ValidFragment" })
public class Fragment_two extends Fragment implements OnClickListener{
	Myapp app = null;
	Mydatabase db = null;
	String nullStr = null;
	//通过app获取LinkedList<Type> mylink
	LinkedList<Type> mylink = null;
	//====================计算器部分============================
	final int[] ids = {
			R.id.b0,R.id.b1,R.id.b2,R.id.b3,R.id.b4,R.id.b5,R.id.b6,R.id.b7,R.id.b8,
			R.id.b9,R.id.ba,R.id.bd,R.id.bdot,R.id.bi,R.id.bm,R.id.br,R.id.bc,R.id.ok,R.id.bdel
	};
	StringBuffer s1 = new StringBuffer();
	StringBuffer s2 = new StringBuffer();
	int count=0;
	Button[] button = new Button[ids.length];
	
	TextView tv,ftv,ftv2,ftv3;
	EditText et_text;
	View root;
	PopupWindow popup;
	boolean popupExist = false;
	Button calculate = null;
	Button typeButton = null;
	Button textButton = null;
	Button btime = null;
	Button save = null;
	Button cancel = null;
	float moneyFromModif = 0;
	Bundle bundle = null;
	public Fragment_two(){}
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		app = (Myapp)getActivity().getApplication();
		root = getActivity().getLayoutInflater().inflate(R.layout.layout_calculate, null);
		for(int i=0;i<ids.length;i++){
			button[i] = (Button)root.findViewById(ids[i]);
			button[i].setOnClickListener(this);
		}
		tv = (TextView) root.findViewById(R.id.textview);
		
		if(!getActivity().toString().equals("InletActivity")){
			bundle = getArguments();
		}
	}
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub		
		final View view = inflater.inflate(R.layout.fragment_two, null);
		db = new Mydatabase(getActivity(),app.getAccount(),1);
		calculate = (Button)view.findViewById(R.id.fragment_two_button_a);
		btime = (Button)view.findViewById(R.id.fragment_two_g);
		save = (Button)view.findViewById(R.id.fragment_two_d);
		cancel = (Button)view.findViewById(R.id.fragment_two_e);
		ftv = (TextView)view.findViewById(R.id.fragment_two_edit_a);
		ftv3 = (TextView)view.findViewById(R.id.fragment_two_edit_c);
		ftv2 = (TextView)view.findViewById(R.id.fragment_two_edit_b);
		nullStr = ftv2.getText().toString();
		//初始化不是来自InletActivity的事件数据
		if(!getActivity().toString().equals("InletActivity")){
			save.setText("修改");
			ftv.setText(bundle.get("money")+"");
			ftv2.setText(bundle.get("type")+"");
			ftv3.setText(bundle.get("remark")+"");
			moneyFromModif = (Float) bundle.get("money");
			btime.setText(bundle.get("time")+"");
		}else{
			//btime显示的初始化时间
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			btime.setText(sdf.format(new Date()));
		}
		
		//=====================保存or修改按钮事件=====================
		
		save.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if(!((ftv2.getText().toString()).equals(nullStr))){
					//产生金额变量并赋值
					float money = 0;
					
					if(!(ftv.getText().toString().equals(nullStr))){
						money=(float) Double.parseDouble(ftv.getText().toString());
						
					}
					//产生事件字段
					try{
						if(getActivity().toString().equals("InletActivity")){
							//保存
							db.getReadableDatabase().execSQL("insert into bill values(null,?,?,?,?)",new Object[]{btime.getText().toString(),money,ftv2.getText().toString(),ftv3.getText().toString()});
							ftv.setText(null);
							ftv2.setText(null);
							ftv3.setText(null);
							Toast.makeText(getActivity(), "保存成功！", Toast.LENGTH_SHORT).show();
						}else{
							//修改
							ContentValues values = new ContentValues();
							values.put("buildtime", btime.getText().toString());
							values.put("type", ftv2.getText().toString());
							values.put("remark", ftv3.getText().toString());
							values.put("money", money);
							db.getReadableDatabase().update("bill", values,"id=?", new String[]{bundle.get("id")+""});
							Toast.makeText(getActivity(), "修改成功！", Toast.LENGTH_SHORT).show();

						}
						
						//如果时间是本月则改变Fragment_main的数据
						SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
						String da= sdf.format(new Date());
						if(btime.getText().toString().contains(da)){
							if(getActivity().toString().equals("InletActivity")){
								//来自InletActivity
								app.setCost(app.getCost()+money);
								Fragment_main f = (Fragment_main) getActivity().getFragmentManager().findFragmentById(R.id.fragment_a);
								f.setContent();
							}else{
								//来自Modif
								app.setCost(app.getCost()+money-moneyFromModif);
								moneyFromModif=money;
							}
							
						}
					}catch(Exception e){
						Toast.makeText(getActivity(), "保存失败！", Toast.LENGTH_SHORT).show();
					}
				}
				else{
					Toast.makeText(getActivity(), "类型不能为空哦！", Toast.LENGTH_SHORT).show();
				}
			}
		});
		
		//=====================取消按钮事件=====================
		
		cancel.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				ftv.setText(null);
				ftv2.setText(null);
				ftv3.setText(null);
			}
		});
		
		//=====================时间按钮事件=====================
		btime.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Calendar c = Calendar.getInstance();
				new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
					
					@Override
					public void onDateSet(DatePicker arg0, int arg1, int arg2, int arg3) {
						// TODO Auto-generated method stub
						String arg2_s = null,arg3_s = null;
						if(arg2+1<10){
							arg2_s="0"+(arg2+1);
						}else{
							arg2_s=(arg2+1)+"";
						}
						if(arg3<10){
							arg3_s="0"+arg3;
						}else{
							arg3_s=arg3+"";
						}
						btime.setText(arg1+"-"+arg2_s+"-"+arg3_s);
					}
				}
				,c.get(Calendar.YEAR)
				,c.get(Calendar.MONTH)
				,c.get(Calendar.DAY_OF_MONTH)).show();
			}		
		});
		//====================计算器按钮事件================
		calculate.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				popupExist = true;
				//获取宽、高
				try{
					DisplayMetrics dm = new DisplayMetrics();
					dm = getActivity().getResources().getDisplayMetrics(); 
					int screenWidth = dm.widthPixels; // 获得屏幕宽度
					int screenHeight = dm.heightPixels;// 获得屏幕高度
					popup = new PopupWindow(root, screenWidth, screenHeight/2);
					popup.showAtLocation(view.findViewById(R.id.fragment_two_button_a), Gravity.BOTTOM,0,0);
					s1.append(ftv.getText());
					s2.append(ftv.getText());
					tv.setText(s1);
				}catch(Exception e){}				
			}
		});
		textButton = (Button)view.findViewById(R.id.fragment_two_button_c);
		ftv3 = (TextView)view.findViewById(R.id.fragment_two_edit_c);
		//==================备注按钮事件==========================
		textButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				final LinearLayout lay = (LinearLayout)getActivity().getLayoutInflater().inflate(R.layout.fragment_text,null);
				et_text = (EditText)lay.findViewById(R.id.fragment_text_edit);
				et_text.setText(ftv3.getText().toString());
				final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setIcon(R.drawable.ic_launcher)
				.setTitle("备注写字板")
				.setView(lay)
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
					}
				})
				.setPositiveButton("确定", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						ftv3.setText(et_text.getText().toString());
					}
				})
				.create()
				.show();		
			}
		});
		typeButton = (Button)view.findViewById(R.id.fragment_two_button_b);
		
		
		//==============类型按钮事件=================
		typeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//==============为类型按钮事件创建SimmpleAdapter
				mylink = app.getMylink();	
				List<Map<String,Object>> listItems = new ArrayList<Map<String,Object>>();
				for(int i=0;i<mylink.size();i++){
					Map<String,Object>listItem = new HashMap<String,Object>();
					listItem.put("name", mylink.get(i).name);
					listItem.put("icon", mylink.get(i).icon);
			    	listItems.add(listItem);
				}
				final MyAdapter simpleAdapter = new MyAdapter(getActivity(),listItems,R.layout.fragment_type,new String[]{"name","icon"},new int[]{R.id.fragment_type_text,R.id.fragment_type_image}); 
				//final MyAdapter simpleAdapter = new MyAdapter(getActivity(),listItems,R.layout.fragment_type,new String[]{"name"},new int[]{R.id.fragment_type_text}); 
				AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
				builder.setTitle("选择类型")
				.setIcon(R.drawable.ic_launcher)
				.setAdapter(simpleAdapter, new Dialog.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						ftv2.setText(mylink.get(arg1).name);
						
					}
				})
				.setPositiveButton("创建类型", new DialogInterface.OnClickListener(){

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getActivity(),CreateTypeActivity.class);		
						startActivity(intent);
					}
					
				})
				.setNegativeButton("删除类型", new DialogInterface.OnClickListener() {
					
					@Override
					public void onClick(DialogInterface arg0, int arg1) {
						// TODO Auto-generated method stub
						Intent intent = new Intent(getActivity(),DeleteTypeActivity.class);		
						startActivity(intent);
					}
				})
				.create()
				.show();
			}
		});
		return view;
	}
	//计算器内核，几个月写的代码，现在来看确实吃力或者说70%的细节看不懂。惭愧，敲了这些连自己都看不懂的代码~
	@Override
	public void onClick(View arg0) {
		// TODO Auto-generated method stub
		switch(arg0.getId()){
		case R.id.b0:
			print("0");
			break;
		case R.id.b1:
			print("1");
			break;
		case R.id.b2:
			print("2");
			break;	
		case R.id.b3:
			print("3");
			break;
		case R.id.b4:
			print("4");
			break;
		case R.id.b5:
			print("5");
			break;
		case R.id.b6:
			print("6");
			break;
		case R.id.b7:
			print("7");
			break;
		case R.id.b8:
			print("8");
			break;
		case R.id.b9:
			print("9");
			break;
		case R.id.ba:
			print("+");
			break;
		case R.id.bi:
			print("=");
			break;
		case R.id.bm:
			print("*");
			break;
		case R.id.bd:
			print("/");
			break;
		case R.id.br:
			print("-");
			break;
		case R.id.bdot:
			print(".");
			break;
		case R.id.ok:
			print("ok");
			popupExist = false;
			break;
		case R.id.bc:
			print("cancel");
			popupExist = false;
			break;
		case R.id.bdel:
			print("delete");
			break;
		}
	}
	
	public void print(String arg0){
		if(s1.toString().equals("Sorry,内部运算出错了!")||s1.toString().equals("一次性金额不该大于100万哦!")){
			s1 = new StringBuffer();
		}
		if(arg0.matches("\\d|\\.")){
			if(s1.length()==0){
				if(arg0.matches("\\.")){
					s1.append("0.");
					s2.append("0.");
				}
				else{
					s1.append(arg0);
					s2.append(arg0);
				}
			}
			else{
				if(s1.charAt(s1.length()-1)=='.'&&arg0.equals(".")){
					//不做处理
				}
				else{
					s1.append(arg0);
					s2.append(arg0);
				}
			}
		}
		if(arg0.matches("-|\\+|\\/|\\*")){
			if(s1.length()==0){
				//不做处理
			}
			else{
				if(s1.charAt(s1.length()-1)=='*'||s1.charAt(s1.length()-1)=='/'||s1.charAt(s1.length()-1)=='+'||s1.charAt(s1.length()-1)=='-'){
					s1.setCharAt(s1.length()-1,arg0.charAt(0));
					s2.setCharAt(s2.length()-2,arg0.charAt(0));
				}
				else{
					s1.append(arg0);
					s2.append("#"+arg0+"#");
					count++;
				}
			}
			if(s1.length()==0 && arg0.matches("-")){
				s1.append(arg0);
				s2.append(arg0);
			}
		}
		if(arg0.matches("\\=")){
			s1 = new StringBuffer();
			try{
			Caculate caculate = new Caculate(s2.toString(),count*2+1);
			s1.append(caculate.exchange(caculate.getDouble(),1)+"");
			s2 = new StringBuffer();
			s2.append(caculate.exchange(caculate.getDouble(),1)+"");	
			ftv.setText(s1);
			}catch(Exception e){
				s1.append("Sorry,内部运算出错了!");
				s2 = new StringBuffer();
			}
			count=0;
		}		
		if(arg0.equals("delete")){
			if(s1.length()>0){
				s1.deleteCharAt(s1.length()-1);
			}
			if(s2.length()>0){
				if(s2.charAt(s2.length()-1)=='#'){
					s2.deleteCharAt(s2.length()-1);
					s2.deleteCharAt(s2.length()-1);
					s2.deleteCharAt(s2.length()-1);
					count--;
				}
				else{
					s2.deleteCharAt(s2.length()-1);
				}
			}
		}
		if(arg0.equals("cancel")){
			s1=new StringBuffer();
			s2=new StringBuffer();
			count=0;
			popup.dismiss();
		}
		if(arg0.equals("ok")){
			s1 = new StringBuffer();
			try{
			Caculate caculate = new Caculate(s2.toString(),count*2+1);
			s1.append(caculate.exchange(caculate.getDouble(),1)+"");
			s2 = new StringBuffer();
			s2.append(caculate.exchange(caculate.getDouble(),1)+"");	
			if(caculate.getDouble()>1000000||caculate.getDouble()<-1000000){
				s1 = new StringBuffer();
				s1.append("一次性金额不该大于100万哦!");
				ftv.setText("0");
			}
			else{
				ftv.setText(s1);
				popup.dismiss();
				s1 = new StringBuffer();
			}
			s2 = new StringBuffer();
			}catch(Exception e){
				s1.append("Sorry,内部运算出错了!");
				s2 = new StringBuffer();
			}
			count=0;
		}
		tv.setText(s1);
	}
	public PopupWindow getPopup(){
		return popup;
	}
	public boolean getPopupExist(){
		return popupExist;
	}
	public void setPopupExist(boolean b){
		popupExist = b;
	}
	public void onDestroy(){
		super.onDestroy();
		if(db!=null){
			db.close();
		}
	}
}

package com.phlofy.mygoldmark;

import java.util.LinkedList;

import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;

public class AsyncIcon {
	ImageView iv = null;
	byte [] b = null;
	String typeName = null;
	LinkedList<Type> mylink = null;
	Handler mHandrler = new Handler(){
		public void handleMessage(Message msg){
			setImageViewIcon((ImageAndDrawable)msg.obj);
		}
	};
	AsyncIcon(ImageView iv ,String typeName,LinkedList<Type> mylink){
		this.iv=iv;
		this.typeName = typeName;
		this.mylink = mylink;
	}
	public void work(){
		new Thread(){
			public void run(){
				byte[] b = null;
				for(int i=0;i<mylink.size();i++){
					b = mylink.get(i).getIconByName(typeName);
					if(b!=null)
						break;
				}
				BitmapDrawable bd = new BitmapDrawable(BitmapFactory.decodeByteArray(b, 0, b.length));
				ImageAndDrawable iad = new ImageAndDrawable(iv, bd);
				Message massage = mHandrler.obtainMessage(0,iad);
				mHandrler.sendMessage(massage);
			}
		}.start();
	}
	private void setImageViewIcon(ImageAndDrawable iad){
		iad.iv.setBackgroundDrawable(iad.bd);
	}
}

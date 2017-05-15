package com.phlofy.mygoldmark;

import java.util.LinkedList;

import android.R.integer;

public class MyCreateList {
	private LinkedList<Result> myList = null;
	
	MyCreateList(){
		myList = new LinkedList<Result>();
	}
	public void add(int id,String buildtime,float money,String type,String remark){
		long index = isExist(buildtime);
		Result result = null;
		if(index>=0){
			//buildtime已存在，获取一个
			 result = myList.get((int) index);
			
		}else{
			//buildtime未存在，创建一个
			result = new Result(buildtime);
			myList.add(result);
		}
		result.addSum(money);
		Bill bill = new Bill(id,money,type,remark);
		result.addBillList(bill);
	}
	public long isExist(String buildtime){
		for(long i=0;i<myList.size();i++){
			if(myList.get((int) i).getBuildtime().equals(buildtime)){
				return i;
			}
		}
		return -1;
	}
	public LinkedList<Result> getMyList(){
		return myList;
	}
}

package com.phlofy.mygoldmark;

import java.util.LinkedList;

public class Result {
	private String buildtime;
	private float sum;
	public LinkedList<Bill> billList = null;;
	Result(String buildtime){
		this.buildtime=buildtime;
		billList = new LinkedList<Bill>();
		sum=0;
	}
	public void setBuildtime(String buildtime){
		this.buildtime=buildtime;
	}
	public String getBuildtime(){
		return buildtime;
	}
	public void addSum(float sum){
		this.sum+=sum;
	}
	public float getSum(){
		return new Caculate().exchange(sum, 1);
	}
	public void addBillList(Bill bill){
		billList.add(bill);
	}
	public LinkedList<Bill> getbillList(){
		return billList;
	}
}

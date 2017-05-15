package com.phlofy.mygoldmark;

public class Bill {
	private long id;
	private float money;
	private String type;
	private String remark;
	Bill(int id,float money,String type,String remark){
		this.id=id;
		this.money=money;
		this.type=type;
		this.remark=remark;
	}
	public void setId(int id){
		this.id=id;
	}
	public long getId(){
		return id;
	}
	public void setMoney(float money){
		this.money=money;
	}
	public float getMoney(){
		return money;
	}
	public void setType(String type){
		this.type=type;
	}
	public String getType(){
		return type;
	}
	public void setRemark(String remark){
		this.remark=remark;
	}
	public String getRemark(){
		return remark;
	}
}

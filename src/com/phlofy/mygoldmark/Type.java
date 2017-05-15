package com.phlofy.mygoldmark;

public class Type{
	public String name;
	public byte[] icon;
	Type(String name,byte[] icon){
		this.name=name;
		this.icon=icon;
	}
	public byte[] getIconByName(String name){
		if(name.equals(this.name))
			return icon;
		return null;
	}
}

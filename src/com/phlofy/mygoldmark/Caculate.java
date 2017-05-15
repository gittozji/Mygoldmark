/*
 * 计算器，处理形如“5#+#8#*#7”的字符串算式
 */

package com.phlofy.mygoldmark;

import java.math.BigDecimal;
import java.util.Scanner;

public class Caculate {
	double ddd=0;
	public static void main(String[] args) {
		
	}
	Caculate(){}
	Caculate(String cost,int count){
	   String[] ss= new String[count];
	   Scanner scanner = new Scanner(cost);
	   scanner.useDelimiter("\\#"); 
	   int i=0;
	   while(scanner.hasNext()){
		   ss[i] = scanner.next();	
		   i++;
	   }
	   if(count==1){
		   ddd=Double.parseDouble(cost);
	   }
	   else{
		   ddd = cacu(ss,count,0);
	   }
   }
	
   public double getDouble(){
	  return ddd;
   }
   private double cacu(String[] ss,int count,double answer){
	   int i=0,j=0,c=0;	   
	   boolean yesoron=false;
	   if(count<=1)
		   return answer;
	   else{
		   
		   if(ss[i=inde(ss)].equals("*")){
			   answer = Double.parseDouble(ss[i-1])*Double.parseDouble(ss[i+1]);
			   
		   }
		   if(ss[i=inde(ss)].equals("/")){
			   answer = Double.parseDouble(ss[i-1])/Double.parseDouble(ss[i+1]);
		   }
		   if(ss[i=inde(ss)].equals("+")){
			   answer = Double.parseDouble(ss[i-1])+Double.parseDouble(ss[i+1]);
		   }
		   if(ss[i=inde(ss)].equals("-")){
			   answer = Double.parseDouble(ss[i-1])-Double.parseDouble(ss[i+1]);
		   }
		   if((i=inde(ss))==-1){
			   
		   }
		   else{
			   String [] sq = new String[ss.length-2];
			   int d=0;
			   for(j=0;j<ss.length;j++){
				   if(j==i-1||j==i+1){
					   
				   }
				   if(j==i){
					   sq[d]=answer+"";
					   d++;
				   }
					   
				   if(j!=i-1&&j!=i&&j!=i+1){
					   sq[d]=ss[j];
					  
					   d++;
				   }
				  
			   }
			   answer = cacu(sq,count-2,answer);
		   }
		   
	   }
	return answer;		   	  
   }
   
   public int inde(String[] ss){
	   boolean md=false,ar=false;
	   for(int i=0;i < ss.length;i++){
		  if(ss[i].equals("*")||ss[i].equals("/")){
			  md=true;
		  }
		  if(ss[i].equals("+")||ss[i].equals("-")){
			  ar=true;
		  }
	   }
	   if(md){
		   for(int i=0;i < ss.length;i++){
			   if(ss[i].equals("*")||ss[i].equals("/")){
				   return i;
			   }
		   }   
	   }
	
	   if(ar){
		   for(int i=0;i < ss.length;i++){
			   if(ss[i].equals("+")||ss[i].equals("-")){
				   return i;
			   }
		   }   
	   }
	   
	   return -1;   
   }
   public float exchange(double value, int w){
	   BigDecimal b = new BigDecimal(value);
	   float f = b.setScale(w, 4).floatValue();
	   return f;
   }
}

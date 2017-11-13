package blastPatric;

import java.io.*;
import java.util.*;

/* 

*/

class Hit{

	String desc;
	String id;
	int length;
	LinkedList hsps;

	Hit(){
		desc="";
		id="";
		length=0;
		hsps=new LinkedList();
	}

	Hit(String id){
		this.id=id;
		desc="";
		length=0;
	}

	void setId(String id){
		this.id=id;
	}

	void setDesc(String s){
		desc=s;
	}

	void setLength(int n){
		length=n;
	}

	String getId(){
		return id;
	}

	String getDesc(){
		return desc;
	}

	int getLength(){
		return length;
	}

	void addHsp(Hsp hsp){
		hsps.add(hsp);
	}


	double getCoverage(){

		int [] t=new int[length+1];
		for(int i=0;i<length;i++){
			t[i]=0;
		}


		for(int i=0;i<hsps.size();i++){
			Hsp h=(Hsp)hsps.get(i);
			int d=0;
			int f=0;
			if(h.sStart<h.sEnd){
				d=h.sStart;
				f=h.sEnd;
			}else{
				f=h.sStart;
				d=h.sEnd;
			}
			for(int j=d;j<=f;j++){
				t[j]=1;
			}
		}
		double tot=0;
		for(int i=0;i<length+1;i++){
			tot=tot+t[i];
		}

		return(tot/length*100);

	}


	String getHit(){
	String res="*****************************************************************\n";
	res=res+"id:"+id+"\n";
	res=res+desc+"\n";
	res=res+"CDS_length:"+length+"\n";
	res=res+"CDS_coverage: "+getCoverage()+"%\n";
	for(int i=0;i<hsps.size();i++){
		Hsp h=(Hsp)hsps.get(i);
		res=res+"HSP_"+(i+1)+"\t"+h.getHsp()+"\n";
	}
	return res;

	}


}

import java.io.*;
import java.util.*;

/* 

*/

public class Hit{

	String desc;
	String id;
	int length;
	LinkedList hsps;

	Hsp maxHsp;

	Hit(){
		desc="";
		id="";
		length=0;
		hsps=new LinkedList();
		maxHsp=new Hsp();
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

		int [] t=new int[length];
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
			if(f>length){
				System.out.println("problem with "+desc+" "+length+": "+d+"-"+f);
				return 0;
			}
			for(int j=d-1;j<f;j++){
				t[j]=1;
			}
		}
		double tot=0;
		for(int i=0;i<length;i++){
			tot=tot+t[i];
		}
		return(tot/length*100);

	}

	double getCoverage(Hsp h){

		int [] t=new int[length];
		for(int i=0;i<length;i++){
			t[i]=0;
		}

		int d=0;
		int f=0;
		if(h.sStart<h.sEnd){
			d=h.sStart;
			f=h.sEnd;
		}else{
			f=h.sStart;
			d=h.sEnd;
		}
		if(f>length){
			System.out.println("problem with "+desc+" "+length+": "+d+"-"+f);
			return 0;
		}

		for(int j=d-1;j<f;j++){
			t[j]=1;
		}

		double tot=0;
		for(int i=0;i<length;i++){
			tot=tot+t[i];
		}

		return(tot/length*100);
	}

	Hsp getMaxHsp(){
		return (new Hsp());

	}


	String getHit(){
		String res="";
		res=res+"id:"+id+"\n";
		res=res+desc+"\n";
		res=res+"prot_length:"+length+"\n";
		if(getCoverage()<=100){
		res=res+"prot_coverage: "+getCoverage()+"%\n";
		}else{
		res=res+"coverage: 100% with overlaps("+getCoverage()+"%)\n";
		}
		for(int i=0;i<hsps.size();i++){
			Hsp h=(Hsp)hsps.get(i);
			res=res+"HSP_"+(i+1)+"\t"+h.getHsp()+"\n";
		}
		res=res+"*****************************************************************\n";
		return res;

	}


}

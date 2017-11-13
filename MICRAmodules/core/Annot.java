import java.io.*;
import java.util.*;

/* 

*/

public class Annot
{

	int deb;
	int fin;
	String type;
	String strand;
	int phase;
	String attributes;


	public Annot(){}

	public Annot(int d,int f,String t,String c,int p,String a){
		deb=d;
		fin=f;
		type=t;
		strand = c;
		attributes=a;
		phase=p;
	}

	int getDeb(){
		return deb;
	}

	int getFin(){
		return fin;
	}

	String getType(){
		return type;
	}

	String getStrand(){
		return strand;
	}

	int getPhase(){
		return phase;
	}

	String getAttributes(){
		return attributes;
	}


	void setDeb(int n){
		deb=n;
	}

	void setFin(int n){
		fin=n;
	}

	void setType(String t){
		type=t;
	}

	void setStrand(String s){
		strand=s;
	}

	void setPhase(int n){
		phase=n;
	}

	void setAttributes(String s){
		attributes=s;
	}


	void print(){
		System.out.println(deb+";"+fin+";"+type+";"+strand+";"+phase+";"+attributes);
	}


	String getAnnot(){
		String res="";
		String [] seq=attributes.split(";");

		for(int i=0;i<seq.length;i++){
			if(seq[i].indexOf("Name=")!=-1){
				res=res+seq[i].substring(5,seq[i].length())+",";
			}

			if(seq[i].indexOf("product=")!=-1){
				res=res+seq[i].substring(8,seq[i].length())+",";
			}
			if(seq[i].indexOf("locus_tag=")!=-1){
				res=seq[i].substring(10,seq[i].length())+","+res;
			}
		}
		if(res.length()>1){
			return res.substring(0,res.length()-1);
		}else{
			return attributes;
		}
	}


	String  getAnnot2(){
		return(deb+";"+fin+";"+type+";"+strand+";"+phase+";"+attributes);
	}


}

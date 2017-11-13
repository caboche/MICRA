import java.io.*;
import java.util.*;

/* 

*/

public class HitBis{

	String name;
	LinkedList hsp;
	double coverage;


	HitBis(String name,double coverage,LinkedList hsp){
		this.name=name;
		this.hsp=hsp;
		this.coverage=coverage;
	}

	void addHsp(Hsp h){
		hsp.add(h);
	}


	String print(){
		String res="";
		res= res+name+" "+coverage+" "+hsp.size()+"\n";
		return res;
	}

	String print2(){
		String res="";
		String tmp=name.substring(1,name.indexOf(" "));
		String tmp2=name.substring(name.indexOf(" ")+2,name.length());
		res= "<TR><TD>"+name.substring(1,name.length())+"</TD>";
		return res;
	}

	String print3(){
		String res="";
		String tmp=name.substring(1,name.indexOf(" "));
		String tmp2=name.substring(name.indexOf(" ")+2,name.length());
		res= "<TR bgcolor=\"yellow\"><TD>"+name.substring(1,name.length())+"</TD>";
		return res;
	}

	String printHTML(){
		String res="";
		System.out.println(name);
		String tmp=name.substring(1,name.indexOf(" "));
		String tmp2=name.substring(name.indexOf(" ")+2,name.length());
		res= "<TR><TD>"+name+"</TD><TD>"+coverage+"</TD>";
		return res;
	}

}

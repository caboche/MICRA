package blastPatric;

import java.io.*;
import java.util.*;
import java.text.*;

/* 

*/

class CDS{

	int beg;
	int end;
	String desc;
	LinkedList ids;
	LinkedList annot;
	int nb_annot;
	double coverage;
	LinkedList annotation;
	String fids;

	public CDS(){}

	public CDS(int beg,int end,String desc,double cov,String fids){
		this.beg=beg;
		this.end=end;
		this.desc=desc;
		coverage=cov;
		this.fids=fids;
		ids=new LinkedList();
		annot=new LinkedList();
		annot.add(desc);
		nb_annot=1;
		annotation=new LinkedList();

	}

	public CDS(int beg,int end,String desc){
		this.beg=beg;
		this.end=end;
		this.desc=desc;
		ids=new LinkedList();
		annot=new LinkedList();
		annot.add(desc);
		nb_annot=1;
		coverage=0;
		annotation=new LinkedList();
	}


	void addId(String n){

		ids=new LinkedList();
		ids.add(n);
	}

	void addAnnot(String s){
		nb_annot++;
		if(annot.indexOf(s)==-1){
			annot.add(s);
		}
	}



	void addAnnot(String s,CDS c){
		nb_annot++;
		if(annot.indexOf(s)==-1){
			annot.add(s);
		}
		annotation.add(c);
	}


	void addAnnot(LinkedList l){

		for(int i=0;i<annot.size();i++){
			String s=(String)l.get(i);
				if(annot.indexOf(s)==-1){
					annot.add(s);
				}
		}
		nb_annot=nb_annot+l.size();
	}


	void setCoverage(double val){

		if(val>coverage)coverage=val;
	}


	void setBeg(int n){
		beg=n;
	}

	void setEnd(int n){
		end=n;
	}

	void setDesc(String s){
		desc=s;
	}

	boolean isInsideAnnot(String s){
		for(int i=0;i<annot.size();i++){
			String a=(String)annot.get(i);
			if(a.equals(s))return true;
		}
		return false;
	}

	boolean estIncluse(CDS c){
		if((c.beg==beg)&&(c.end<end))return true;
		if((c.beg>beg)&&(c.end==end))return true;
		if((c.beg>beg)&&(c.end<end))return true;
		return false;
	}

	boolean overlap(CDS c){
		if((c.beg>=beg)&&(c.beg<=end))return true;
		if((c.end>=beg)&&(c.end<=end))return true;
		return false;

	}

	String print(){
		DecimalFormat f = new DecimalFormat("##.00");
		String res="";
		String descF=desc.replaceAll(";"," ");
		res=beg+"\t"+end+"\t"+(Math.abs(end-beg+1))+"\t"+f.format(coverage)+"\t"+descF+"\t"+"|";
		for(int i=0;i<ids.size();i++){
			res=res+(String)ids.get(i)+"|";
		}
		return res;
	}


	String printBis(){
		DecimalFormat f = new DecimalFormat("##.00");
		String res="";
		res=beg+"-"+end+" "+desc+" (cov="+f.format(coverage)+"%)<br>";
		String [] seq=fids.split("\\|");
		for(int i=0;i<seq.length;i++){
			res=res+seq[i]+"| ";
		}
		return res;
	}


	String print2(){
		String res="";
		res=beg+"\t"+end+"\t"+(Math.abs(end-beg+1))+"\t"+nb_annot+"\t"+desc+"\t"+"|";
		for(int i=0;i<annot.size();i++){
			res=res+(String)annot.get(i)+"|";
		}
		return res;
	}

	String printAnnot(){
		String res="";

		res="<tr><td>"+beg+"</td><td>"+end+"</td><td>"+(Math.abs(end-beg+1))+"</td><td>"+coverage+"</td><td>"+desc+"</td><td>";
		String [] seq=fids.split("\\|");
		for(int i=0;i<seq.length;i++){
			res=res+seq[i]+" ";
		}

		res=res+"</td></tr>";

		return res;
	}




	String printHTML(int unique){
		String res="";
		res="<tr><td>"+beg+"</td>"+"<td>"+end+"</td><td>"+(Math.abs(end-beg+1))+"</td><td><a href=\"#"+unique+"\">"+(nb_annot)+" </a></td><td>"+desc+"</td><td>";
		String [] seq=fids.split("\\|");
		for(int i=0;i<seq.length;i++){
			res=res+seq[i]+"";
		}
		res=res+"</td><td>"+coverage;
		res=res+"</td>";
		res=res+"</tr>";
	
		return res;
	}


	String printCsv(int unique){
		String res="";
		res=beg+";"+end+";"+desc+";"+coverage+";";
		String [] seq=fids.split("\\|");
		for(int i=0;i<seq.length;i++){
			res=res+seq[i]+"";
		}
		return res;
	}







}

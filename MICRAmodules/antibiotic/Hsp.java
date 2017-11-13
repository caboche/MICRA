import java.io.*;
import java.util.*;
import java.text.DecimalFormat;

/* 

*/

public class Hsp{

	int qStart;
	int qEnd;
	int sStart;
	int sEnd;
	String evalue;
	String score;

	int length;
	int identity;
	int similarity;
	int gaps;


	Hsp(){
		qStart=0;
		qEnd=0;
		sStart=0;
		sEnd=0;
		evalue="";
		score="";
		length=0;
		identity=0;
		similarity=0;
		gaps=0;
	}


	Hsp(String l){
		String [] seq=l.split("\t");
		for(int i=0;i<seq.length;i++){

			if(seq[i].indexOf("sim:")!=-1){
				similarity=Integer.parseInt(seq[i].substring(seq[i].indexOf("sim:")+4,seq[i].length()));
			}

			if(seq[i].indexOf("gaps:")!=-1){
				gaps=Integer.parseInt(seq[i].substring(seq[i].indexOf("gaps:")+5,seq[i].length()));
			}

			if(seq[i].indexOf("iden:")!=-1){
				identity=Integer.parseInt(seq[i].substring(seq[i].indexOf("iden:")+5,seq[i].length()));
			}

			if(seq[i].indexOf("len:")!=-1){
				length=Integer.parseInt(seq[i].substring(seq[i].indexOf("len:")+4,seq[i].length()));
			}

			if(seq[i].indexOf("q.start:")!=-1){
				qStart=Integer.parseInt(seq[i].substring(seq[i].indexOf("q.start:")+8,seq[i].length()));
			}

	
			if(seq[i].indexOf("q.end:")!=-1){
				qEnd=Integer.parseInt(seq[i].substring(seq[i].indexOf("q.end:")+6,seq[i].length()));
			}

			if(seq[i].indexOf("s.start:")!=-1){
				sStart=Integer.parseInt(seq[i].substring(seq[i].indexOf("s.start:")+8,seq[i].length()));
			}

			if(seq[i].indexOf("s.end:")!=-1){
				sEnd=Integer.parseInt(seq[i].substring(seq[i].indexOf("s.end:")+6,seq[i].length()));
			}

			if(seq[i].indexOf("evalue:")!=-1){
				evalue=seq[i].substring(seq[i].indexOf("evalue:")+7,seq[i].length());
			}
	
			if(seq[i].indexOf("bit-score:")!=-1){
				score=seq[i].substring(seq[i].indexOf("bit-score:")+10,seq[i].length());
			}

		}
	}


	void setEvalue(String s){
		evalue=s;
	}

	void setScore(String s){
		score=s;
	}

	void setqStart(int n){
		qStart=n;
	}
	void setqEnd(int n){
		qEnd=n;
	}
	void setsStart(int n){
		sStart=n;
	}
	void setsEnd(int n){
		sEnd=n;
	}


	void setIdentity(int i){
		identity=i;
	}

	void setSimilarity(int i){
		similarity=i;
	}

	void setGaps(int i){
		gaps=i;
	}

	void setLength(int i){
		length=i;
	}

	double getIden(){
		return ((double)identity/length*100.0);
	}


	double getSim(){
		return ((double)similarity/length*100.0);
	}


	String getHsp(){
		return("q.start:"+qStart+"\t"+"q.end:"+qEnd+"\t"+"s.start:"+sStart+"\t"+"s.end:"+sEnd+"\t"+"evalue:"+evalue+"\t"+"bit-score:"+score+"\t"+"len:"+length+"\t"+"iden:"+identity+"\t"+"sim:"+similarity+"\t"+"gaps:"+gaps);
	}

	String printHTML(){
		return("</TD><TD>"+qStart+"-"+qEnd+"</TD></TR>");
	}


	String print(){
		return(qStart+"-"+qEnd+" "+sStart+" "+sEnd+" "+getIden()+" "+identity+";"+length);
	}

	String print2(){
		DecimalFormat f = new DecimalFormat();
		f.setMaximumFractionDigits(2);
		return("<TD>"+qStart+"-"+qEnd+"</TD><TD> "+f.format(getIden())+"</TD><TD>"+f.format(getSim())+"</TD>");
	}



}

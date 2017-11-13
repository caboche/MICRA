package blastPatric;

import java.io.*;
import java.util.*;

/* 

*/

public class Hsp{

	int qStart;
	int qEnd;
	int sStart;
	int sEnd;
	String evalue;
	String score;


	Hsp(){
	qStart=0;
	qEnd=0;
	sStart=0;
	sStart=0;
	evalue="";
	score="";
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

	String getHsp(){
		return("q.start:"+qStart+"\t"+"q.end:"+qEnd+"\t"+"s.start:"+sStart+"\t"+"s.end:"+sEnd+"\t"+"evalue:"+evalue+"\t"+"bit-score:"+score);
	}



}

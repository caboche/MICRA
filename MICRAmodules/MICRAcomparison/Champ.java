import java.io.*;
import java.util.*;

/* Class for variant calls */

public class Champ{
	/* SNV or DIP */
	String type;
	/* reference allele */
	String allele;
	/* coding region */
	String annot;
	/* aa change */
	String change;

	public Champ(){}


	public Champ(String t,String a){
		type=t;
		allele=a;
		annot="";
		change="";
	}


	void addAnnot(String s){
		annot=s;
	}

	void addChange(String s){
		change=s;
	}

	String getAnnot(){
		return annot;
	}

	String getChange(){
		return change;
	}

	public String getType(){
	return type;
	}


	public String getAllele(){
	return allele;
	}


	public void addAllele(String s){
		allele=allele+s;
	}
}

import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.DecimalFormat;

public class Couple{

	boolean db;
	boolean ardb;

	int nb_db;
	int nb_ardb;

	LinkedList resistance;
	String requirement;


	Couple(){
		db=false;
		ardb=false;
		nb_db=0;
		nb_ardb=0;
		resistance=new LinkedList();
		requirement="";
	}

	void setDB(){
		db=true;
	}

	void setARDB(){
		ardb=true;
	}

	void increaseDB(){
		nb_db++;
	}

	void increaseARDB(){
		nb_ardb++;
	}

	void setRequirement(String s){
		requirement=s;
	}

	void addRes(String r){
		if (r.equals(""))return;
		if(resistance.indexOf(r)==-1){
			resistance.add(r);
		}
	}


	void print(){
		System.out.println("("+db+";"+ardb+";"+nb_db+";"+nb_ardb+")");
	}


}

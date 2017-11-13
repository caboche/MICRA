
import java.io.*;
import java.util.*;
import java.text.*; 


public class Couple2
{

	int nb;
	String name;

	Couple2(){}

	Couple2(int nb,String name){
		this.nb=nb;
		this.name=name;
	}

	Couple2(String name){
		this.name=name;
		nb=1;
	}

	void addName(String s){
		name=s;
	}

	void addNb(){
		nb++;
	}


}

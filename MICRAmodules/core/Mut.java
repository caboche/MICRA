import java.io.*;
import java.util.*;

/* 

*/

public class Mut{

	String alt;
	double n;

	Mut(double n,String alt){
		this.n=n;
		this.alt=alt;	
	}

	Mut(){}

	double getNb(){
		return n;
	}

	String getAlt(){
		return alt;
	}

	boolean isequals(Mut m){
		if(alt.equals(m.alt))return true;
		return false;
	}

	void incremente(double nb){
		n=n+nb;
	}

	void print(){
		System.out.println("MUT "+alt+" "+n);

	}

}

import java.io.*;
import java.util.*;

public class Couple{

	double val;
	int n;

	Couple(){}

	Couple(double val,int n){
		this.val=val;
		this.n=n;
	}

	void setValue(double r){
		val=r;
	}

	void setN(int nn){
		n=nn;
	}

	double getValue(){
		return val;
	}

	int getN(){
		return n;
	}
}

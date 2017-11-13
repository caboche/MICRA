import java.io.*;
import java.util.*;

public class Genome{

	String name;
	int size;
	double coverage;
	double depth;
	double sd;
	double mapped;
	int min;
	int max;
	int [] lowComplexity;
	int snp;
	int snp_cds;
	int snp_change;
	int dip;
	int dip_cds;


	Genome(){}

	Genome(String n){
		name=n;
	}


	double getRate(){
		Integer i1=new Integer(snp);
		double d1=i1.doubleValue();
		Integer i2=new Integer(dip);
		double d2=i2.doubleValue();
		Integer i3=new Integer(size);
		double d3=i3.doubleValue();
		double mr=(d1+d2)/((coverage/100)*d3);
		return mr;
	}

	void addSize(int s){
		size=s;
	}

	void addCoverage(double cov){
		coverage=cov;
	}

	void addDepth(double d){
		depth=d;
	}

	void addSd(double s){
		sd=s;
	}

	void addMapped(double m){
		mapped=m;
	}

	void addMin(int m){
		min=m;
	}

	void addMax(int m){
		max=m;
	}


	void addSnp(int i){
		snp=i;
	}

	void addSnp_cds(int i){
		snp_cds=i;
	}

	void addSnp_change(int i){
		snp_change=i;
	}

	void addDip(int i){
		dip=i;
	}

	void addDip_cds(int i){
		dip_cds=i;
	}

	String getName(){
		return name;
	}


	int getSize(){
		return size;
	}

	double getCoverage(){
		return coverage;
	}

	double getDepth(){
		return depth;
	}

	double getSd(){
		return sd;
	}

	double getMapped(){
		return mapped;
	}

	int getMin(){
		return min;
	}

	int getMax(){
		return max;
	}

	int getSnp(){
		return snp;
	}

	int getSnp_cds(){
		return snp_cds;
	}


	int getSnp_change(){
		return snp_change;
	}

	int getDip(){
		return dip;
	}

	int getDip_cds(){
		return dip_cds;
	}

	void setLowComplexity(){
		lowComplexity=new int[size];
		for(int i=0;i<size;i++){
			lowComplexity[i]=0;
		}
	}

}

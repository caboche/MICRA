import java.io.*;
import java.util.*;
import java.text.DecimalFormat;
/* 

*/

public class Prot{

	int len;
	String desc;
	LinkedList hits;
	double coverage;
	String name;
	boolean is_resume;

	Prot(){}

	Prot(String desc,int len){
		this.desc=desc;
		this.len=len;
		hits=new LinkedList();
		is_resume=false;
		coverage=0;
		name="";
	}


	void addHit(HitBis h){
		hits.add(h);
	}


	String print(){
	String res="";
		res=res+len+" "+desc;
		for(int i=0;i<hits.size();i++){
			HitBis h=(HitBis)hits.get(i);
			res=res+h.print();
			LinkedList hh=h.hsp;
			res=res+" size "+hh.size();
			for(int j=0;j<hh.size();j++){
				Hsp hhh=(Hsp)hh.get(j);
				res=res+"\n"+hhh.print()+" @ ";
			}
		}
	return res;
	}


	String print2(double seuil,double min_cov,double iden){
		String res="";
		DecimalFormat f = new DecimalFormat();
		f.setMaximumFractionDigits(2);
		for(int i=0;i<hits.size();i++){
				HitBis h=(HitBis)hits.get(i);
				LinkedList hh=h.hsp;
				for(int j=0;j<hh.size();j++){
					Hsp hhh=(Hsp)hh.get(j);
					if(getCoverage(hhh)>=seuil){
						if((getCoverage(hhh)>=min_cov)&&(hhh.getIden()>=iden)){
							is_resume=true;
							res=res+h.print3()+"<TD>"+f.format(getCoverage(hhh))+"</TD>"+hhh.print2()+"</TR>\n";
						}else{
							res=res+h.print2()+"<TD>"+f.format(getCoverage(hhh))+"</TD>"+hhh.print2()+"</TR>\n";
						}
					}
				}
			}
		return res;

	}


	double getCoverage(Hsp h){

		int [] t=new int[len];
		for(int i=0;i<len;i++){
			t[i]=0;
		}

			int d=0;
			int f=0;
			if(h.sStart<h.sEnd){
				d=h.sStart;
				f=h.sEnd;
			}else{
				f=h.sStart;
				d=h.sEnd;
			}
			if(f>len){
				System.out.println("problem with "+desc+" "+len+": "+d+"-"+f);
				return 0;
			}
			for(int j=d-1;j<f;j++){
				t[j]=1;
			}

		double tot=0;
		for(int i=0;i<len;i++){
			tot=tot+t[i];
		}

		return(tot/len*100);
	}

	Hsp getMaxHsp(double s){
		double max_cov=0;
		Hsp max=null;
		String n="";
		for(int i=0;i<hits.size();i++){
		HitBis hit=(HitBis)hits.get(i);
		LinkedList hsp=hit.hsp;
			for(int j=0;j<hsp.size();j++){
				Hsp h=(Hsp)hsp.get(j);
				if(h.getIden()>=s){
					if(getCoverage(h)>max_cov){
						max_cov=getCoverage(h);
						max=h;
						n=hit.name;
					}
				}
		
			}
		}
		name=n;
		coverage=max_cov;
		return (max);

	}

	int getNbHsp(){
		int c=0;
		for(int i=0;i<hits.size();i++){
			HitBis hit=(HitBis)hits.get(i);
			c=c+hit.hsp.size();
		}
		return c;
	}


	double getMaxCov(){
		double max=0;
		for(int i=0;i<hits.size();i++){
			HitBis h=(HitBis)hits.get(i);
			if(h.coverage>max)max=h.coverage;
		}
		return max;
	}

}

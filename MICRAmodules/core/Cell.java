import java.io.*;
import java.util.*;
import java.text.*; 

/* Class encoding a cell of the genome table 
to analyse the sam mapping file */

public class Cell{


	char base;
	LinkedList mutations;
	double nb_reads;
	/* theorical depth with deletion */
	double nb_reads2;
	double defaut;
	LinkedList quality;
	boolean deletion;
	double nb_end;
	boolean lowCov;

	Cell(){}

	Cell(char base){
		this.base=base;
		mutations=new LinkedList();
		nb_reads=0;
		nb_reads2=0;
		nb_end=0;
		defaut=0;
		deletion = false;
		lowCov=false;
	}


	char getBase(){
		return base;
	}

	void addEnd(double n){
		nb_end=nb_end+n;
	}

	void addRead(double n){
		nb_reads=nb_reads+n;
	}

	void addRead2(double n){
		nb_reads2=nb_reads2+n;
	}

	void addDefaut(double n){
		defaut=defaut+n;
	}

	double getProf(){
		return nb_reads2;
	}

	double getProfSeq(){
		return nb_reads;
	}

	double getEnd(){
		return nb_end;
	}


	boolean getLowCov(){
		return lowCov;
	}

	void setLowCov(){
		lowCov=true;
	}

	void addMut(Mut m,double qual){
		if(mutations.size()==0){
			mutations.add(m);
	
		}
		else{
			boolean added=false;
			for(int i=0;i<mutations.size();i++){
				Mut mm=(Mut)mutations.get(i);
				if(mm.isequals(m)){
					mm.incremente(m.n);
					added=true;
					break;
				}	
			}
			if(!added){
				mutations.add(m);
			}
	
		}
	}


	LinkedList getList(double freq){
		LinkedList res=new LinkedList();
			for(int i=0;i<mutations.size();i++){
				Mut m=(Mut)mutations.get(i);
				if(m.alt.length()>1){
				if((m.getNb()/(nb_reads2-nb_end)*100)>=freq)res.add(m);
				}else{
					if((m.getNb()/(nb_reads2)*100)>=freq)res.add(m);
				}
			}
		return res;
	}


	double getNbBase(){
		double res=0;
			for(int i=0;i<mutations.size();i++){
				Mut m=(Mut)mutations.get(i);
				res=res+m.getNb();
			}
		return (nb_reads2-res);
	}

	Mut getBaseMax(){
		Mut res=new Mut(getNbBase(),(""+base));

		double max=getNbBase();
			for(int i=0;i<mutations.size();i++){
				Mut m=(Mut)mutations.get(i);
				if(m.getNb()>max){
					max=m.getNb();
					res=m;
				}
			}
		return res;
	}

	void print(){
		System.out.println("ref:"+base+" depth:"+nb_reads+" theorical "+nb_reads2+" ");
		for(int i=0;i<mutations.size();i++){
			Mut m=(Mut)mutations.get(i);
			m.print();
			System.out.println(m.getNb()/(nb_reads)*100);
			System.out.println(m.getNb()+" "+(nb_reads));
		}
	}


}

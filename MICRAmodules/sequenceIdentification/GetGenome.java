import java.io.*;
import java.util.*;
import java.text.*; 


public class GetGenome
{
	
	String file;
	int n1;
	int n2;
	int m1;
	int m2;
	int type;
	String path;
	

	public GetGenome(){

	}

	public GetGenome(String file,int n1,int n2,int m1,int m2,int type,String path){
		this.file=file;	
		this.n1=n1;
		this.n2=n2;
		this.m1=m1;
		this.m2=m2;
		this.type=type;
		this.path=path;

		/* genomes */
		if((type==0)||(type==2)){
			new Echantillon(file,n1);
			BInterface b=new BInterface ("sample.fa",m1,path);
			b.run();
		}
		/* plasmids */
		if((type==1)||(type==2)){
			new Echantillon(file,n2);
			BInterfacePlasmid b=new BInterfacePlasmid ("sample.fa",m2,path);
			b.run();
		}
		
	File f1=new File("sample.fa");
	f1.delete();
	
	}


}

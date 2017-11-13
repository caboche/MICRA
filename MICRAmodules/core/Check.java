import java.io.*;
import java.util.*;


/* class for checking input files; for each genome/plasmid : fasta file and gff */


public class Check{

	String genome_dir;
	String plasmide_dir;
	PrintWriter out;
	String sep;
	int errors;


	Check(){}

	Check(String genome_dir,String sep,PrintWriter out){
		System.out.println("Checking input files...");
		out.println("Checking input files...");
		this.genome_dir=genome_dir;
		this.out=out;
		plasmide_dir="";
		this.sep=sep;
		errors=0;
		checkGenome();
		if(errors!=0){
			System.out.println(errors+"error(s) detected");
		}

	}

	Check(String genome_dir,String plasmide_dir,String sep,PrintWriter out){
		System.out.println("Checking input files...");
		out.println("Checking input files...");
		this.genome_dir=genome_dir;
		this.out=out;
		this.plasmide_dir=plasmide_dir;
		this.sep=sep;
		errors=0;
		checkGenome();
		checkPlasmide();
		if(errors!=0){
			System.out.println(errors+"error(s) detected");
		}
	}


	void checkGenome(){
		File directory=new File(genome_dir);
		String [] list;
		list=directory.list();

		/* files with .fasta are renamed with .fa extension */
		for(int i=0;i<list.length;i++){ 
			if(list[i].endsWith(".fasta")){
				String name=list[i].substring(list[i].lastIndexOf(sep)+1,list[i].lastIndexOf("."));
				if(genome_dir.endsWith(sep)){
					File f1=new File(genome_dir+list[i]);
					File f2=new File(genome_dir+name+".fa");
					f1.renameTo(f2);
				}else{
					File f1=new File(genome_dir+sep+list[i]);
					File f2=new File(genome_dir+sep+name+".fa");
					f1.renameTo(f2);
				}
					System.out.println("File "+list[i]+" was renamed to "+name+".fa");
					out.println("File "+list[i]+" was renamed to "+name+".fa");
			}		
		}

		list=directory.list();

		/* for each .fa files checking for corresponding gff */
		for(int i=0;i<list.length;i++){ 
			if(list[i].endsWith(".fa")){
				String name=list[i].substring(list[i].lastIndexOf(sep)+1,list[i].lastIndexOf("."));
				if(!isInside((name+".gff"),list)){
					System.out.println("ERROR: gff is missing for "+list[i]+" file");
					out.println("ERROR: gff is missing for "+list[i]+" file");
					try{
						createGff(genome_dir,name+".gff");
					}catch (IOException e) {
		       				System.out.println("ERROR creating gff file");
					} 
					System.out.println("corresponding empty gff was created");
					out.println("corresponding empty gff was created");
					errors++;
				}
			}		
		}
		if(errors==0){
			System.out.println("All genome files are OK");
			out.println("All genome files are OK");
		}
	}


	void checkPlasmide(){
		File directory=new File(plasmide_dir);
		String [] list;
		list=directory.list();

		/* files with .fasta are renamed with .fa extension */
		for(int i=0;i<list.length;i++){ 
			if(list[i].endsWith(".fasta")){
				String name=list[i].substring(list[i].lastIndexOf(sep)+1,list[i].lastIndexOf("."));
				if(plasmide_dir.endsWith(sep)){
					File f1=new File(genome_dir+list[i]);
					File f2=new File(genome_dir+name+".fa");
					f1.renameTo(f2);
				}else{
					File f1=new File(genome_dir+sep+list[i]);
					File f2=new File(genome_dir+sep+name+".fa");
					f1.renameTo(f2);
				}
				System.out.println("File "+list[i]+" was renamed to "+name+".fa");
				out.println("File "+list[i]+" was renamed to "+name+".fa");
			}		
		}

		list=directory.list();
		/* for each .fa files checking for corresponding gff */
		for(int i=0;i<list.length;i++){ 
			if(list[i].endsWith(".fa")){
				String name=list[i].substring(list[i].lastIndexOf(sep)+1,list[i].lastIndexOf("."));
				if(!isInside((name+".gff"),list)){
						System.out.println("ERROR: gff is missing for "+list[i]+" file");
						out.println("ERROR: gff is missing for "+list[i]+" file");
					try{
						createGff(plasmide_dir,name+".gff");
					}catch (IOException e) {
	       					System.out.println("ERROR creating gff file");
					} 
					System.out.println("corresponding empty gff was created");
					out.println("corresponding empty gff was created");
					errors++;
				}
			}		
		}
		if(errors==0){
			System.out.println("All plasmid files are OK");
			out.println("All plasmid files are OK");
		}


	}

	 boolean isInside(String s,String [] tab){
		for(int i=0;i<tab.length;i++){
			if(tab[i].equals(s))return true;
		}
		return false;
	}

	 void createGff(String rep,String name) throws IOException{
		if(!rep.endsWith(sep)){
			rep=rep+sep;
		}
		PrintWriter out=new PrintWriter(new FileWriter(rep+name));
		System.out.println(rep+name);
		out.println("##gff-version 3");
		out.println("#!gff-spec-version 1.14");
		out.println("#!source-version NCBI C++ formatter 0.2");
		out.println("##Type DNA NC_014383.1");
		out.println("###");
		out.close();
	}
}

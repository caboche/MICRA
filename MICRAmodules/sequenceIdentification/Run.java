import java.io.*;
import java.util.*;

/* Main class of sequence identification module */

public class Run
{
	public static void main(String [] args)	throws IOException 
	{	

		/* fastq input file */
		String input="";
		/* number of reads for genome sequence identification */
		int n1=1000;
		/* number of reads for plasmid sequence identification */
		int n2=10000;
		/* number of genome sequences to be selected */
		int m1=5;
		/* number of plasmid sequences to be selected */
		int m2=5;
		/* type 0: genomes ; 1: plasmids ; 2: genomes + plasdmids */
		int type=0;
		String gi_genomes="";
		String gi_plasmids="";
		String path="";

		if(isInside(args,"-h")!=-1){
			help();
		}


		if(isInside(args,"-F")!=-1){
			try {
				input=args[isInside(args,"-F")+1];
				if((input.endsWith(".fastq"))||(input.endsWith(".txt"))){}
				else{
					System.out.println("ERROR: input file must be a fastq file (.fastq) OR a GI list in text format (.txt)\n");
					help();
				}
				

			}
			catch (Exception e) {
				System.out.println("ERROR with input read file\n");
				help();
			}

		}else{
			System.out.println("ERROR: you have to give an input read file\n");
			help();
		}

		if(isInside(args,"-n1")!=-1){
			try {
				n1=Integer.parseInt(args[isInside(args,"-n1")+1]);
					if(n1<0){
						System.out.println("ERROR: -n1 must be a positive integer\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -n1 must be a number\n");
				help();
			}

		}

		if(isInside(args,"-n2")!=-1){
			try {
				n2=Integer.parseInt(args[isInside(args,"-n2")+1]);
					if(n2<0){
						System.out.println("ERROR: -n2 must be a positive integer\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -n2 must be a number\n");
				help();
			}

		}

		if(isInside(args,"-m1")!=-1){
			try {
				m1=Integer.parseInt(args[isInside(args,"-m1")+1]);
					if(m1<0){
						System.out.println("ERROR: -m1 must be a positive integer\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -m1 must be a number\n");
				help();
			}

		}


		if(isInside(args,"-m2")!=-1){
			try {
				m2=Integer.parseInt(args[isInside(args,"-m2")+1]);
					if(m2<0){
						System.out.println("ERROR: -m2 must be a positive integer\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -m2 must be a number\n");
				help();
			}

		}


		if(isInside(args,"-type")!=-1){
			try {
				type=Integer.parseInt(args[isInside(args,"-type")+1]);
					if((type<0)||(type>2)){
						System.out.println("ERROR: -type must be 0, 1 or 2\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -type must be 0, 1 or 2\n");
				help();
			}

		}


		if(isInside(args,"-o")!=-1){
			path=args[isInside(args,"-o")+1];

		}

		new File(path).mkdirs();


		LocalGi lg=new LocalGi();
		int local_genomes=0;
		int local_plasmids=0;
		int download_genomes=0;
		int download_plasmids=0;
		LinkedList toDo_genomes=new LinkedList();
		LinkedList toDo_plasmids=new LinkedList();
		if(!path.equals("")){
			if(!path.endsWith("/"))path=path+"/";

		}


		System.out.println("type "+type);
		

		if(input.endsWith(".fastq")){
			System.out.println("Identification of reference sequences");
			GetGenome gg=new GetGenome(input,n1,n2,m1,m2,type,path);
			if(type==0){
				gi_genomes=path+"genomeList.txt";
				lg=new LocalGi(gi_genomes,new File("allGenomes"),path);
				local_genomes=local_genomes+lg.local;
				download_genomes=download_genomes+lg.download;
				toDo_genomes=lg.todo;
			}else{

				if(type==1){
					gi_genomes=path+"plasmidList.txt";
					lg=new LocalGi(gi_genomes,new File("allPlasmids"),path);
					local_plasmids=local_plasmids+lg.local;
					download_plasmids=download_plasmids+lg.download;
					toDo_plasmids=lg.todo;
				}else{
					gi_genomes=path+"genomeList.txt";
					lg=new LocalGi(gi_genomes,new File("allGenomes"),path);
					local_genomes=local_genomes+lg.local;
					download_genomes=download_genomes+lg.download;
					toDo_genomes=lg.todo;
					gi_plasmids=path+"plasmidList.txt";
					lg=new LocalGi(gi_plasmids,new File("allPlasmids"),path);
					local_plasmids=local_plasmids+lg.local;
					download_plasmids=download_plasmids+lg.download;
					toDo_plasmids=lg.todo;
				}

			}

		/* toDo_plasmids contains the sequence to be downloaded */
		System.out.println("local_genomes "+local_genomes);
		System.out.println("local_plasmids "+local_plasmids);
		System.out.println("download_genomes "+download_genomes);
		System.out.println("download_plasmids "+download_plasmids);
		
		}else{
			

			lg=new LocalGi(input,path);
			local_genomes=local_genomes+lg.local;
			download_genomes=download_genomes+lg.download;
			toDo_genomes=lg.todo;

			/* toDo_plasmids contains the sequence to be downloaded */
			System.out.println("local_sequences "+local_genomes);
			
			System.out.println("download_sequences "+download_genomes);
			
			if(toDo_genomes.size()!=0){
				PrintWriter out=new PrintWriter(new FileWriter("tmp.txt"));
					for(int i=0;i<toDo_genomes.size();i++){
						out.println((String)toDo_genomes.get(i));
					}
				out.close();

			}

			System.out.println(toDo_genomes.size()+" sequence(s) to be downloaded");
			if(toDo_genomes.size()!=0){
			/* download */
			System.out.println("Downloading of missing reference sequences ...");
			new Data("tmp.txt",path);
			
			File fff=new File("tmp.txt");
			fff.delete();
			}

		}

		

}

	static int isInside(String [] tab, String s){

			for(int i=0;i<tab.length;i++){
				if(tab[i].equals(s)) return i;
			}
			return (-1);

	}

	static void help(){
		System.out.println("Usage: java Run [options] -F <input_file> [options]");
		System.out.println("-F file_name \t [mandatory] name of the input file (fastq or gi list in txt format)");
		System.out.println("-type \t [facultative] 0: genomes ; 1: plasmids ; 2: genomes + plasdmids [0]");
		System.out.println("-n1 \t [facultative] number of reads for genomes [1000]");
		System.out.println("-n2 \t [facultative] number of reads for plasmids [10000]");
		System.out.println("-m1 \t [facultative] number of organisms for genomes [5]");
		System.out.println("-m2 \t [facultative] number of organisms for plasmids [5]");
		System.out.println("-o \t [facultative] output directory []");
		System.out.println("-h \t print this help");
		
		System.exit(1);
	}
	
}


import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/* Main class of core module */
public class Run{

public static void main(String [] args)	throws IOException{

	long t1=0;
	long t2=0;
	long t0=0;
	String out_dir="";
	String sep="";
	String reads="";
	/* PAIRED-END */
	String reads1="";
	String reads2="";

	String genomes_dir="";
	String plasmids_dir="";
	int nb_reads=0;
	/* String array containing the list of ref. seq l=1 if user do not give a ref l=2 
	 ref[1] = ref_user ref[0]=closest ref*/
	Genome [] ref=new Genome[1];


	/* mapper for phylo */
	Mapper mapper1;
	/* mapper for closer ref genome*/
	Mapper mapper2;


	String conf="conf.txt";
	boolean cgview=false;


	int min_cov=5;
	double min_freq=90;
	int min_cov_dip=10;
	/* coverage to consider a plasmid */
	double seuil_cov=70;

	LinkedList user_plasmids=new LinkedList();

	/* to skip de novo and blast steps */
	boolean skip=false;
	/* blast patric minimum cds coverage % */
	double seuil=80;
	boolean sam=false;

	String feature="CDS";

	int contig_size=500;

	LinkedList <Genome> genomes=new LinkedList();
	LinkedList <Genome> plasmids=new LinkedList();
	LinkedList <Genome> plas_int=new LinkedList();
	String project=""; /* directory+sep*/
	String directory="";


	/* OS detection */
	String os = (String)System.getProperties().get("os.name");
	if(os.indexOf("indows")!=-1){
			sep="\\";
			}else{
				sep="/";
			}

		if(isInside(args,"-h")!=-1){
			help();
		}


		if(isInside(args,"-G")!=-1){
			try {	
				genomes_dir=args[isInside(args,"-G")+1];
				if(genomes_dir.endsWith(sep)){
					genomes_dir=genomes_dir.substring(0,(genomes_dir.length()-1));
				}

			}
			catch (Exception e) {
				System.out.println("ERROR with input genome directory\n");
				help();
			}

		}else{
			System.out.println("ERROR: you have to give an input directory containing the list of reference genomes\n");
			help();
		}





		if(isInside(args,"-1")!=-1){
			if(isInside(args,"-2")!=-1){
				if((isInside(args,"-R")==-1)){
					try {
						reads1=args[isInside(args,"-1")+1];
						reads2=args[isInside(args,"-2")+1];

					}
					catch (Exception e) {
						System.out.println("ERROR with input read file\n");
						help();
					}
				}else{
					System.out.println("ERROR: you have to choose between single (-R) or paired-end (-1 -2) \n");
					help();
				}
			}
			else{
				System.out.println("ERROR: you have to give a second input read file for paired-end data with option - 2\n");
				help();
			}
		}



		if(isInside(args,"-2")!=-1){
			if(isInside(args,"-1")!=-1){
				if((isInside(args,"-R")==-1)){
					try {
						reads1=args[isInside(args,"-1")+1];
						reads2=args[isInside(args,"-2")+1];

					}
					catch (Exception e) {
						System.out.println("ERROR with input read file\n");
						help();
					}
				}else{
					System.out.println("ERROR: you have to choose between single (-R) or paired-end (-1 -2) \n");
					help();
				}
			}
			else{
				System.out.println("ERROR: you have to give a second input read file for paired-end data with option - 1\n");
				help();
			}
		}


		if(isInside(args,"-R")!=-1){
			if((reads1=="")&&(reads2=="")){
					try {
						reads=args[isInside(args,"-R")+1];
				

					}
					catch (Exception e) {
						System.out.println("ERROR with input read file\n");
						help();
					}
			}else{
				System.out.println("ERROR: you have to choose between single (-R) or paired-end (-1 -2) \n");
				help();
			}

		}


		if((reads=="")&&(reads1=="")&&(reads2=="")){
		System.out.println("ERROR: you have to give at least one read file (-R) or paired-end reads (-1 -2) \n");
		help();
		}


		if(isInside(args,"-P")!=-1){
				try {
					plasmids_dir=args[isInside(args,"-P")+1];
					if(plasmids_dir.endsWith(sep)){
						plasmids_dir=plasmids_dir.substring(0,(plasmids_dir.length()-1));
					}

				}
				catch (Exception e) {
					System.out.println("ERROR with input plasmid directory\n");
					help();
				}

		}

		if(isInside(args,"-o")!=-1){
				try {
					out_dir=args[isInside(args,"-o")+1];

				}
				catch (Exception e) {
					System.out.println("ERROR with output directory\n");
					help();
				}

		}

		if(isInside(args,"-plasmids")!=-1){
				try {
					String s=args[isInside(args,"-plasmids")+1];
					String [] ss=s.split(",");
					if(ss.length==0){
						System.out.println("plasmid names must be separated by a coma");
						System.out.println("e.g. Hmoins,bactec");
						help();
					}else{
						for(int i=0;i<ss.length;i++){
							user_plasmids.add(ss[i]);
						}
					}

				}
				catch (Exception e) {
					System.out.println("ERROR with output directory\n");
					help();
				}

		}

		if(isInside(args,"-ref")!=-1){
				try {
					ref=new Genome[2];
					ref[1]=new Genome(args[isInside(args,"-ref")+1]);

				}
				catch (Exception e) {
					System.out.println("ERROR -ref option\n");
					help();
				}

		}


		if(isInside(args,"-f")!=-1){
				try {
					feature=args[isInside(args,"-f")+1];

				}
				catch (Exception e) {
					System.out.println("ERROR -f option\n");
					help();
				}

		}



		if(isInside(args,"-conf")!=-1){
				try {
					conf=args[isInside(args,"-conf")+1];

				}
				catch (Exception e) {
					System.out.println("ERROR with configuration file\n");
					help();
				}

		}

		if(isInside(args,"-CGView")!=-1){
				try {
					/* test si jar present ou on le distrib ??? */
					cgview=true;

				}
				catch (Exception e) {
					System.out.println("ERROR with CGView option\n");
					help();
				}

		}

		if(isInside(args,"-low_cov")!=-1){
				try {
					min_cov=Integer.parseInt(args[isInside(args,"-low_cov")+1]);
						if(min_cov<0){
							System.out.println("ERROR: -low_cov must be a positive integer\n");
							help();
						}

				}
				catch (Exception e) {
					System.out.println("ERROR: -low_cov must be a number\n");
					help();
				}

		}

		if(isInside(args,"-low_cov_indel")!=-1){
				try {
					min_cov_dip=Integer.parseInt(args[isInside(args,"-low_cov_indel")+1]);
						if(min_cov_dip<0){
							System.out.println("ERROR: -low_cov must be a positive integer\n");
							help();
						}

				}
				catch (Exception e) {
					System.out.println("ERROR: -low_cov_indel must be a number\n");
					help();
				}

		}

		if(isInside(args,"-min_freq")!=-1){
				try {
					min_freq=Double.parseDouble(args[isInside(args,"-min_freq")+1]);;
						if(min_freq<0){
							System.out.println("ERROR: -min_freq must be positive\n");
							help();
						}
						if(min_freq>100){
							System.out.println("ERROR: -min_freq must be comprized between 0 and 100\n");
							help();
						}

				}
				catch (Exception e) {
					System.out.println("ERROR: -min_freq must be a number\n");
					help();
				}

		}

		if(isInside(args,"-plas_cov")!=-1){
				try {
					seuil_cov=Double.parseDouble(args[isInside(args,"-plas_cov")+1]);;
						if(min_freq<0){
							System.out.println("ERROR: -plas_cov must be positive\n");
							help();
						}
						if(min_freq>100){
							System.out.println("ERROR: -plas_cov must be comprized between 0 and 100\n");
							help();
						}

				}
				catch (Exception e) {
					System.out.println("ERROR: -plas_cov must be a number\n");
					help();
				}

		}

		if(isInside(args,"-skip")!=-1){
			
					skip=true;


		}


		if(isInside(args,"-sam")!=-1){
					sam=true;
		}

	
		if(isInside(args,"-bt")!=-1){
				try {
					seuil=Double.parseDouble(args[isInside(args,"-bt")+1]);;
						if(seuil<0){
							System.out.println("ERROR: -bt must be positive\n");
							help();
						}
						if(seuil>100){
							System.out.println("ERROR: -bt must be comprized between 0 and 100\n");
							help();
						}

				}
				catch (Exception e) {
					System.out.println("ERROR: -bt must be a number\n");
					help();
				}

		}

		if(isInside(args,"-min_contig")!=-1){
				try {
					contig_size=Integer.parseInt(args[isInside(args,"-min_contig")+1]);
						if(contig_size<0){
							System.out.println("ERROR: --min_contig must be a positive integer\n");
							help();
						}

				}
				catch (Exception e) {
					System.out.println("ERROR: --min_contig must be a number\n");
					help();
				}

		}


		System.out.println("output "+out_dir);

		if((reads=="")&&(reads1!="")&&(reads2!="")){
			System.out.println("PAIRED-END reads");
			new Core(genomes_dir,reads1,reads2,plasmids_dir,out_dir,user_plasmids,ref,conf,cgview,min_cov,min_freq,min_cov_dip,seuil_cov,skip,seuil,feature,contig_size,sam);
		
		}else{
			if(reads!=""){
				System.out.println("SINGLE-END reads");
				new Core(genomes_dir,reads,plasmids_dir,out_dir,user_plasmids,ref,conf,cgview,min_cov,min_freq,min_cov_dip,seuil_cov,skip,seuil,feature,contig_size,sam);
			}else{
				System.out.println("ERROR: INPUT read files\n");
				help();
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
		System.out.println("Usage: java Run [options] -G <genomes_directory> [options] -R <read_file.fastq>");
		System.out.println("-G directory_name \t [mandatory] directory containing files (GFF and FASTA) for reference genomes");
		System.out.println("-R file_name \t [mandatory] name of the read fastq file");
		System.out.println("-P directory_name \t [facultative] directory containing files (GFF and FASTA) for reference genomes [none]");
		System.out.println("-o directory_name \t [facultative] name of output directory [result_date]");
		System.out.println("-f feature \t [facultative] feature to be studied in the GFF file [CDS]");
		System.out.println("-conf file_name \t [facultative] configuration file [conf.txt]");
		System.out.println("-CGView \t [facultative] generating images with CGView [false]");
		System.out.println("-ref name \t [facultative] to force a reference genome [none]");
		System.out.println("-plasmids list \t [facultative] to force plasmids to be considered e.g. Hmoins,bactec [none]");
		System.out.println("-low_cov int \t [facultative] low coverage threshold [5]");
		System.out.println("-min_contig int \t [facultative] minimum size of contigs produced in de novo assembly [500]");
		System.out.println("-min_freq double \t [facultative] minimum frequency [90%]");
		System.out.println("-plas_cov double \t [facultative] minimum coverage for a plasmid to be considered  [70%]");
		System.out.println("-bt double \t [facultative] minimum CDS coverage in blast step [80%]");
		System.out.println("-skip \t [facultative] to skip de novo and BLAST steps");
		System.out.println("-h \t print this help");
		
		System.exit(1);
	}


}

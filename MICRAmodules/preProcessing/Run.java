import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;

/* Main class of the preProcessing module */

public class Run
{
	public static void main(String [] args)	throws IOException 
	{	


		String reads="";
		String reads1="";
		String reads2="";
		String path="";
		PrintWriter log;
		String output="";

		/* FastQC */
		boolean fastqc=false;
		/* Cutadapt */
		boolean cutadapt=false;
		/* adapter trimming */
		boolean adapt=false;
		/*Automatic detection of adapter */
		boolean auto=true;
		/*threshold from which an over-represented word detected with FASTQC is considered as an adapter to trim with cutadapt*/
		double threshold=20.0;

		/* list of adapters */
		String a1="";
		String a2="";
		int quality=20;
		int min_length=20;

	
		

		if(isInside(args,"-h")!=-1){
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



		if(isInside(args,"-fastqc")!=-1){	
				fastqc=true;
		}


		if(isInside(args,"-cutadapt")!=-1){	
				cutadapt=true;
		}

		if(isInside(args,"-adapter")!=-1){	
				adapt=true;
		}


		if(isInside(args,"-threshold")!=-1){
			try {
				threshold=Double.parseDouble(args[isInside(args,"-threshold")+1]);;
					if(threshold<0){
						System.out.println("ERROR: -threshold must be positive\n");
						help();
					}
					if(threshold>100){
						System.out.println("ERROR: -threshold must be comprized between 0 and 100\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -threshold must be a number\n");
				help();
			}

		}


		if(isInside(args,"-q")!=-1){
			try {
				quality=Integer.parseInt(args[isInside(args,"-q")+1]);
				cutadapt=true;
					if(quality<0){
						System.out.println("ERROR: -q must be a positive integer\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -q must be a number\n");
				help();
			}

		}

		if(isInside(args,"-ml")!=-1){
			try {
				min_length=Integer.parseInt(args[isInside(args,"-ml")+1]);
				cutadapt=true;
					if(min_length<0){
						System.out.println("ERROR: -m must be a positive integer\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -ml must be a number\n");
				help();
			}

		}

		if(isInside(args,"-a")!=-1){
			try {
				a1=args[isInside(args,"-a")+1];
				auto=false;
				adapt=true;
				cutadapt=true;

			}
			catch (Exception e) {
				System.out.println("ERROR -a option\n");
				help();
			}

		}

		if(isInside(args,"-A")!=-1){
			try {
				a2=args[isInside(args,"-A")+1];
				auto=false;
				adapt=true;
				cutadapt=true;

			}
			catch (Exception e) {
				System.out.println("ERROR -a option\n");
				help();
			}

		}




		if(isInside(args,"-o")!=-1){
			try {
				output=args[isInside(args,"-o")+1];

			}
			catch (Exception e) {
				System.out.println("ERROR with output directory\n");
				help();
			}

		}


		String project="";
		

		if(output.equals("")){
			Date date = new Date();
			SimpleDateFormat dateFormatComp;
		 
			dateFormatComp = new SimpleDateFormat("MM_dd_yyyy_hh:mm:ss");

			project="results_"+dateFormatComp.format(date);
			output=project;

		}else{
			if(output.endsWith("/")){	
				project=output.substring(0,output.length()-1);
			}else project=output;

		}

		
		new File(project).mkdirs();
		
		log=new PrintWriter(new FileWriter(project+"/"+"log_preProcessing.txt"));

		if(reads!=""){
			
			Check ch=new Check(reads,log);
			reads=ch.currentName;
			
			
		}else{
			
			Check ch=new Check(reads1,reads2,log);
			reads1=ch.currentName1;
			reads2=ch.currentName2;
			

		}

		
		/* .fastq file */
		
		
		

		if(reads!=""){
			if((fastqc)||(cutadapt&&auto)){
				new File(project+"/"+"FASTQC").mkdirs();
				new FastQCInterface((project+"/"+"FASTQC"),reads,log);
				File rep=new File(project+"/"+"FASTQC");
				String [] lf=rep.list();
				for(int i=0;i<lf.length;i++){
					String text1=lf[i];
					if(text1.endsWith(".zip")){
						new Uncompress(project+"/"+"FASTQC/"+text1);
						Parser p=new Parser(text1.substring(0,text1.lastIndexOf("."))+"/fastqc_data.txt",log,threshold);
						
						if(cutadapt&&auto){
							a1=p.adapters;
						}
						delete(new File(text1.substring(0,text1.lastIndexOf("."))));
					}
				}

			}
			if(cutadapt){
				new File(project+"/"+"READS").mkdirs();
				new CutadaptInterface((project+"/"+"READS"),reads,a1,quality,min_length,log);
			}
		}else{
			

			if((fastqc)||(cutadapt&&auto)){
				new File(project+"/"+"FASTQC").mkdirs();
				new FastQCInterface((project+"/"+"FASTQC"),reads1,log);
				new FastQCInterface((project+"/"+"FASTQC"),reads2,log);
				File rep=new File(project+"/"+"FASTQC");
				String [] lf=rep.list();
				for(int i=0;i<lf.length;i++){
					String text1=lf[i];
					if(text1.endsWith(".zip")){
						new Uncompress(project+"/"+"FASTQC/"+text1);
						Parser p=new Parser(text1.substring(0,text1.lastIndexOf("."))+"/fastqc_data.txt",log,threshold);
						
						
							if(cutadapt&&auto){
								
								if(text1.indexOf(reads1.substring(reads1.lastIndexOf("/")+1,reads1.lastIndexOf(".")))!=-1){
									a1=p.adapters;
									
								}else{
									a2=p.adapters;
									
								}
							}
						delete(new File(text1.substring(0,text1.lastIndexOf("."))));
					}
				}

			}
			if(cutadapt){
				new File(project+"/"+"READS").mkdirs();
				new CutadaptInterface((project+"/"+"READS"),reads1,reads2,a1,a2,quality,min_length,log);
			}
		}

		log.close();

}

static int isInside(String [] tab, String s){

		for(int i=0;i<tab.length;i++){
			if(tab[i].equals(s)) return i;
		}
		return (-1);

	}

public static void delete(File file)
    	throws IOException{
 
    	if(file.isDirectory()){
    		if(file.list().length==0){
    		   file.delete();
    		}else{
        	   String files[] = file.list();
        	   for (String temp : files) {
        	      File fileDelete = new File(file, temp);
        	      delete(fileDelete);
        	   }
        	   if(file.list().length==0){
           	     file.delete();
        	   }
    		}
 
    	}else{
    		file.delete();
    		
    	}
   }



static void help(){
		System.out.println("Usage: java Run [options] -F <input_file> [options]");
		System.out.println("-R file_name \t [mandatory] file containing single reads (FASTQ or compressed)");
		System.out.println("-1 file_name \t [mandatory] file containing FORWARD paired-end reads (fastq)");
		System.out.println("-2 file_name \t [mandatory] file containing REVERSE paired-end reads (fastq)");
		System.out.println("-fastqc \t [facultative] to to run FastQC");
		System.out.println("-cutadapt \t [facultative] to to run cutadapt");
		System.out.println("-adapter \t [facultative] to automatically detect and trim adapters");
		System.out.println("-threshold double \t [facultative] minimum percentage of sequence detected in reads to be considered as adapter [20%]");
		System.out.println("-q int \t [facultative] quality threshold for trimming step [20]");
		System.out.println("-ml int \t [facultative] minimum size of reads to be conserved [20]");
		System.out.println("-a list_of_adapters \t [mandatory] list of adapter to be trimmed in reads (separated by coma)");
		System.out.println("-A list_of_adapters \t [mandatory] list of adapter to be trimmed in reads 2 (separated by coma)");
		System.out.println("-o \t [facultative] output directory []");
		System.out.println("-h \t print this help");
		
		System.exit(1);
	}
	
}


import java.io.*;
import java.util.*;


/* class for checking input files */


public class Check{

	String reads="";
	String reads1="";
	String reads2="";
	PrintWriter log;
	
	String currentName="";


	Check(){}


	
	int c;

	Check(String reads,PrintWriter log){
		System.out.println("Checking input files...");
		log.println("Checking input files...");
		currentName=reads;
		this.reads=reads;
		this.log=log;

		log.println("Input file: "+reads);

		checkExtension(reads);

		log.println("Output file: "+currentName);
		/* check the number of lines */
		System.out.print("checking number of lines... ");
		log.print("checking number of lines... ");
		try{
			isNumberCorrect(currentName);
		}catch (IOException e) {
       			System.out.println("ERROR with read input file");
		} 
		System.out.println("OK");
		log.println("OK");

	}


	int c1;
	int c2;

	String currentName1;
	String currentName2;

	Check(String reads1,String reads2,PrintWriter log){
		System.out.println("Checking input files...");
		log.println("Checking input files...");

		log.println("Input files: "+reads1+"&"+reads2);

		currentName1=reads1;
		currentName2=reads2;
		this.reads1=reads1;
		this.reads2=reads2;
		this.log=log;

		currentName=reads1;
		checkExtension(reads1);
		currentName1=currentName;
		currentName=reads2;
		checkExtension(reads2);
		currentName2=currentName;
	
		log.println("Output files: "+currentName1+"&"+currentName2);
		/* check the number of lines */
		System.out.print("checking number of lines... ");
		log.print("checking number of lines... ");
		try{
			isNumberCorrect(currentName1,currentName2);
		}catch (IOException e) {
       			System.out.println("ERROR with read input files");
		} 
		System.out.println("OK");
		log.println("OK");

		

	}

	

	/* 
	0=> zip
	1=> gz
	2=> .fastq
	3=> .FQ .fq .FASTQ ...
	4=> unkown format

	*/


	void checkExtension(String file){
		c=getExtension(file);
		
		if((c==0)||(c==1)){
			new Uncompress(file);
			String output=file.substring(0,file.lastIndexOf("."));
			currentName=output;
			if(c==0){
				
				File f=new File(file);
				f.delete();
			}


			c=getExtension(output);
			System.out.println("statut ext"+c);
			
			
		}

		if(c==3){
			/* rename the file into .fastq */
			File f=new File(currentName);
			String tmp=currentName.substring(0,currentName.lastIndexOf("."))+".fastq";
			f.renameTo(new File(tmp));
			currentName=tmp;
		}

		if(c==4){
			System.out.println("UNKNOWN format for reads");
			log.println("UNKNOWN format for reads");
			log.close();
			System.exit(1);
		}
	}


	int getExtension(String file){
		String e=file.substring(file.lastIndexOf("."),file.length());
		switch (e){
  			case ".zip":
    				return 0;
			case ".gz":
    				return 1;
			case ".fastq":
    				return 2;
			case ".fq":
    				return 3;
			case ".FQ":
    				return 3;
			case ".FASTQ":
    				return 3;
  			default:
   				return 4;

		}

	}

	/* count the number of lines in filename */
	int count(String filename) throws IOException { 
	   	 InputStream is = new BufferedInputStream(new FileInputStream(filename)); 
	   		byte[] c = new byte[1024]; 
	    		int count = 0; 
	   		 int readChars = 0; 
	   		 while ((readChars = is.read(c)) != -1) { 
	      			  for (int i = 0; i < readChars; ++i) { 
		   			 if (c[i] == '\n') 
		     			   ++count; 
	       		 } 
	   	 } 
	   	 return count; 
	}

	void isNumberCorrect(String filename) throws IOException{
		int nb=count(filename);
		if(nb%4!=0){
			System.out.println("FATAL ERROR: the number of reads is not a multiple of 4");	
			log.println("FATAL ERROR: the number of reads is not a multiple of 4");	
			log.close();
			System.exit(1);
		}

	}


	void isNumberCorrect(String filename1,String filename2) throws IOException{
		int nb1=count(filename1);
		if(nb1%4!=0){
			System.out.println("FATAL ERROR: the number of reads1 is not a multiple of 4");	
			log.println("FATAL ERROR: the number of reads1 is not a multiple of 4");	
			log.close();
			System.exit(1);
		}
		int nb2=count(filename2);
		if(nb2%4!=0){
			System.out.println("FATAL ERROR: the number of reads2 is not a multiple of 4");	
			log.println("FATAL ERROR: the number of reads2 is not a multiple of 4");
			log.close();	
			System.exit(1);
		}
		if(nb1!=nb2){
			System.out.println("FATAL ERROR: the number of paired-reads is not the same in the two given input files");	
			log.println("FATAL ERROR: the number of paired-reads is not the same in the two given input files");
			log.close();	
			System.exit(1);
		}

	}



 




}

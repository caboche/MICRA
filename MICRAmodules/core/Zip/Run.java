
import java.io.*;
import java.util.*;

public class Run
{
	public static void main(String [] args)	throws IOException 
	{	


		String dir="";
	
		if(isInside(args,"-h")!=-1){
			help();
		}


		if(isInside(args,"-dir")!=-1){
			try {
				dir=args[isInside(args,"-dir")+1];
				
				

			}
			catch (Exception e) {
				System.out.println("ERROR with input read file\n");
				help();
			}

		}else{
			System.out.println("ERROR: you have to give an input file\n");
			help();
		}

		
		new Zip(dir);
			 try{
              			delete(new File(dir));
 
          		 }catch(IOException e){
              			 e.printStackTrace();
              			 System.exit(0);
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
}


import java.io.*;
import java.util.*;


/* 
Main class of the MICRA comparison module 
Compare annotations or variant calls

*/

public class Run

{
	public static void main(String [] args)	throws IOException{

	
	/* Name of output directory */
	String path="comparison";

	/* 
	Comparison type
	type=0 -> annotations
	type=1 -> variants
	*/
	int type=-1;

	/* 
	-all option considers all the reference sequences (i.e. union) 
	else considers only common references in comparison (i.e. intersection mode)
	*/
	boolean all=false;

	/* mode for variant calls
	 0->SNV+INDEL; 1->SNV; 2->INDEL
	*/
	int mode=0;

	/* number of file to be compared (given in input) */
	int minus=1;

	/* the list of files to be compared */
	LinkedList files=new LinkedList();

	if(isInside(args,"-h")!=-1){
			help();
		}

	if(isInside(args,"-o")!=-1){
			path=args[isInside(args,"-o")+1];
			minus=minus+2;
		}
	

	if(isInside(args,"-all")!=-1){
		minus=minus+1;	
		all=true;
	}
		

	new File(path).mkdirs();

	if(isInside(args,"-mode")!=-1){
			mode=Integer.parseInt(args[isInside(args,"-mode")+1]);
			minus=minus+2;
		}
	
	
	type=Integer.parseInt(args[0]);
	
	if((type!=0)&&(type!=1)){
		System.out.println("ERROR: type must be 0 or 1");		
		help();
	}

	for(int i=minus;i<args.length;i++){
		files.add(args[i]);
	}

	if(files.size()==0){
		System.out.println("ERROR: no files to be compared");
		help();
	}


	if(files.size()<2){
		System.out.println("ERROR: at least two files are required");
		help();
	}



	/* Run the comparison */
	if(type==0){
		/* annotation comparison */
		new Compare(all,path,files);
	}else{
		/* variant comparison */
		new Compare_SNP(mode,path,files);
	}



	/* ZIP the output directory */
	String v1="";
	String v2=path;
	if(path.lastIndexOf("/")!=-1){
		v1=path.substring(0,path.lastIndexOf("/"));
		v2=path.substring(path.lastIndexOf("/")+1, path.length());
	}
	
	new Zip(v1,v2);





	/* deleting the temporary directory */

	 try{
 
              delete(new File(path));
 
          }catch(IOException e){
              	e.printStackTrace();
              	System.exit(0);
          }





	

	/* delete temporary files */ 
	File rep=new File(".");
	String [] listefic;
	listefic=rep.list();
	for(int i=0;i<listefic.length;i++){
		String text=listefic[i];
	
		if (text.endsWith("tmp")){
		
			File f1=new File(text);
			f1.delete();
		}
	}

	File ff=new File("sortie.R");
	ff.delete();
	ff=new File("sortie.Rout");
	ff.delete();

	}




	static int isInside(String [] tab, String s){

		for(int i=0;i<tab.length;i++){
			if(tab[i].equals(s)) return i;
		}
		return (-1);

	}


	public static void delete(File file) throws IOException{
 
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
		System.out.println("Usage: java Compare 0/1 [options] directory1 <directory2> <...>");
		System.out.println("0 for CDS comparison ; 1 for mutations ");
		System.out.println("-all \t [facultative] this option considers all the reference sequences (i.e. union) else only the common reference sequences (i.e. intersection)");
		System.out.println("-mode \t [facultative] 0->SNV+INDEL; 1->SNV; 2->INDEL [0]");
		System.out.println("-o \t [facultative] output directory [comparison]");
		System.out.println("-h \t print this help");
		
		System.exit(1);
	}


}	


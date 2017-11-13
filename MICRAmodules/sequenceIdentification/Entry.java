import java.io.*;
import java.util.*;

public class Entry{

	Couple [] tab;
	String [] project;
	String sep;
	String path;

	Entry(){}

	Entry(Couple [] tab,String [] project,String sep,String path){
		this.tab=tab;
		this.project=project;
		this.sep=sep;
		this.path=path;
		try{
				run();
			}
			catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}


		void run () throws IOException{


		/* get fasta et genbank files*/
		PerlInterface pi=new PerlInterface(tab,project,sep);
		/* convert genbank in gff */
		PerlInterface2 pi2=new PerlInterface2(tab,project,sep);


		/* delete genbank files */
		File rep1=new File(path+"reference_genomes");
		if(rep1.exists()){
			String [] listefic1;
			listefic1=rep1.list();
			for(int i=0;i<listefic1.length;i++){
				String text=listefic1[i];
				if(text.endsWith(".gb")){
	
	
					File f1=new File(path+"reference_genomes"+sep+text);
					f1.delete();
		
				}
			}
			File f2=new File(path+"reference_genomes"+sep+"temp.fa");
			f2.delete();
		}
		rep1=new File(path+"reference_plasmids");
		if(rep1.exists()){
			String [] listefic1;
			listefic1=rep1.list();
			for(int i=0;i<listefic1.length;i++){
				String text=listefic1[i];
				if(text.endsWith(".gb")){
	
	
					File f1=new File(path+"reference_plasmids"+sep+text);
					f1.delete();
		
				}
			}
			File f2=new File(path+"reference_plasmids"+sep+"temp.fa");
			f2.delete();
		}

	}
}

import java.io.*;
import java.util.*;
import java.text.*; 
import java.net.*;

/* Running BLAST for plasmid sequences*/

public class BInterfacePlasmid
{
	
	String file;
	HashMap organisms;
	int number;
	int [] tab_num;
	String [] tab_gi;
	HashMap dico;
	String [] tab_name;
	String path;


	String res="";

	public BInterfacePlasmid(){
		
	}

	public BInterfacePlasmid(String file,int number,String path){
		this.file=file;	
		this.number=number;
		this.path=path;
		tab_num=new int[number];
		tab_gi=new String[number];
		dico=new HashMap();
		organisms = new HashMap();
		try{
			init();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public void init() throws IOException{

		BufferedReader in = new BufferedReader(new FileReader("external_scripts/dict_plasmids.txt"));
		String l="";
		while((l = in.readLine()) != null) {
			String [] seq=l.split(";");
			dico.put(seq[0],seq[1]);
		}
		in.close();

	}


	public void run(){

		try {
	
		Runtime runtime = Runtime.getRuntime();
		final Process process;
		process= runtime.exec( "blastn -query "+file+" -db BLAST_db/plasmids.fasta -num_threads 40 -outfmt 7 -evalue 1e-5 -num_alignments 1");

		Thread thread=new Thread() {
		public void run() {
			try {
			
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				PrintWriter out=new PrintWriter(new FileWriter(path+"plasmidList.txt"));
				PrintWriter out2=new PrintWriter(new FileWriter(path+"plasmidList.html"));
				out2.println("<!DOCTYPE html>");
				out2.println("<html>");
	    			out2.println("<head>");
	       			out2.println(" <meta charset=\"utf-8\" />");
	      			out2.println("  <title>Plasmid List</title>");
	   			out2.println(" </head>");
	  			out2.println("  <body>");
	    			out2.println("<h1> Plasmid list</h1>");
				out2.println("<ul>");

				String l = "";

				/*********************/
				HashMap giName=new HashMap();
					
				BufferedReader in=new BufferedReader(new InputStreamReader(getClass().getClassLoader().getResourceAsStream("gi_name.txt")));

		
				while((l = in.readLine()) != null) {
					String [] seq=l.split(";");
					giName.put(seq[0],seq[1]);
				}
				in.close();
				/********************/
			
				try {

				
					while((l = reader.readLine()) != null) {
					
						if((!l.startsWith("#"))&&(!l.equals(""))){
						
						
							String [] seq=seq=l.split("\t");
							String temp=seq[1].substring(seq[1].indexOf("|")+1,seq[1].lastIndexOf("|")+1);
							String gi=temp.substring(0,temp.indexOf("|"));
						
							if(organisms.get(gi)==null){
								organisms.put(gi,1);
							}else{
								int n=(Integer)organisms.get(gi);
							
							
								organisms.put(gi,(n+1));

							}	

											


						
						
						
						}
					}

					int nb_reads=organisms.size();

					Set cles = organisms.keySet();
					Iterator it = cles.iterator();
					while (it.hasNext()){
	   					String cle = (String)it.next(); 					
						update(cle,(Integer)organisms.get(cle));
						
					}


					for(int i=tab_num.length-1;i>=0;i--){
						if(tab_gi[i]!=null){
					
						String acc=(String)dico.get(tab_gi[i]);
						res=res+acc+",";


						String name=(String)giName.get(tab_gi[i])+":"+acc;
						out.println(acc+";"+name+";plasmid");
						out2.println("<li>accession:<a target=_blank href=\"http://www.ncbi.nlm.nih.gov/nuccore/"+acc+"\"> "+acc+"</a> - "+name+" - "+tab_num[i]+" read matches </li>");
					
						}	
					}
			
				
				
				} finally {
					process.getInputStream().close();
					reader.close();
					out.close();
					out2.println("</ul>");
					out2.println(" </body> ");
					out2.println("</html>");
					out2.close();
					res=res.substring(0,res.length()-1);
				
				}
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
	};
	thread.start();
	thread.join();

	Thread t2=new Thread() {
		public void run() {
			try {
				BufferedReader reader2 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String line = "";
				try {
					while((line = reader2.readLine()) != null) {
						System.out.println(line);
					}
				} finally {
					reader2.close();
				
				}
			} catch(IOException ioe) {
				ioe.printStackTrace();
			}
		}
	};

	t2.start();
	t2.join();

	process.waitFor();
	process.getOutputStream().close(); 
	process.getErrorStream().close();
	
	}catch (InterruptedException e) {
		System.out.println("Thread was interrupted");
	    }

	 catch (IOException e) {
		System.out.println("ERREUR BInterfacePlasmid");
	} 

	}

	void update(String gi,int n){

		int i=0;
		while(i<number){
			if(n>tab_num[i]){
				/* permutation */
				int tmp_n=tab_num[i];
				String tmp_gi=tab_gi[i];
		
				tab_num[i]=n;
				tab_gi[i]=gi;
		
				if(i!=0){
					tab_num[i-1]=tmp_n;
					tab_gi[i-1]=tmp_gi;
			
				}
			}else{return;}	
			i++;

		}

	}
}

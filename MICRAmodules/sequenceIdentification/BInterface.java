import java.io.*;
import java.util.*;
import java.text.*; 

/* Running BLAST for genome sequences*/


public class BInterface
{
	
	String file;
	HashMap organisms;
	HashMap dico;
	int number;
	int [] tab_num;
	String [] tab_gi;
	String [] tab_name;
	String path;

	String res="";

	public BInterface(){
		
	}

	public BInterface(String file,int number,String path){
		this.file=file;	
		this.number=number;
		this.path=path;
		
		tab_num=new int[number];
		tab_gi=new String[number];
		tab_name=new String[number];
		organisms = new HashMap();
		dico=new HashMap();
		try{
			init();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
		
	}

	public void init() throws IOException{

		BufferedReader in = new BufferedReader(new FileReader("external_scripts/dict_genomes.txt"));
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
		process= runtime.exec( "blastn -query "+file+" -db BLAST_db/ncbi.fa -num_threads 40 -outfmt 5 -evalue 1e-5 -num_alignments 10");
		
		Thread thread=new Thread() {
		public void run() {
			try {
			
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				PrintWriter out=new PrintWriter(new FileWriter(path+"genomeList.txt"));
				PrintWriter out2=new PrintWriter(new FileWriter(path+"genomeList.html"));
				out2.println("<!DOCTYPE html>");
				out2.println("<html>");
	    			out2.println("<head>");
	       			out2.println(" <meta charset=\"utf-8\" />");
	      			out2.println("  <title>Genome List</title>");
	   			out2.println(" </head>");
	  			out2.println("  <body>");
	    			out2.println("<h1> Genome list</h1>");
				out2.println("<ul>");
	   			
				String l = "";
				try {

				
					while((l = reader.readLine()) != null) {
						
						if((l.indexOf("<Hit_def>")!=-1)&&(!l.equals(""))){
						
							String temp=l.substring(l.indexOf("|")+1,l.lastIndexOf("|")+1);
							String gi=temp.substring(0,temp.indexOf("|"));
						
							String name="";
							if(l.lastIndexOf("chromosome")!=-1){
								name=l.substring(l.lastIndexOf("|")+2,l.lastIndexOf("chromosome")-1);
							}else{

								if(l.lastIndexOf(",")!=-1){
									name=l.substring(l.lastIndexOf("|")+2,l.lastIndexOf(","));
								}else{
									name=l.substring(l.lastIndexOf("|")+2,l.length()-10);
								}
							}

							name=name.replaceAll(" ","-");
							name=name.replaceAll("/","-");
							if(organisms.get(gi)==null){
								organisms.put(gi,new Couple2(name));
							}else{
								Couple2 c=(Couple2)organisms.get(gi);
								c.addNb();
								organisms.put(gi,c);
							}	

											


						
						
						
						}
					}

					int nb_reads=organisms.size();

					Set cles = organisms.keySet();
					Iterator it = cles.iterator();
					while (it.hasNext()){
	   					String cle = (String)it.next(); 					
						update(cle,(Couple2)organisms.get(cle));
					}

					for(int i=tab_num.length-1;i>=0;i--){
						if(tab_gi[i]!=null){
					
							String acc=(String)dico.get(tab_gi[i]);
							res=res+acc+",";
							String name=tab_name[i]+":"+acc;
							out.println(acc+";"+name+";genome");
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
		System.out.println("ERREUR BInterface");
	} 

	}

	void update(String gi,Couple2 c){
		int n=c.nb;

		int i=0;
		while(i<number){
			if(n>tab_num[i]){
				/* permutation */
				int tmp_n=tab_num[i];
				String tmp_gi=tab_gi[i];
				String tmp_name=tab_name[i];
				tab_num[i]=n;
				tab_gi[i]=gi;
				tab_name[i]=c.name;
				if(i!=0){
					tab_num[i-1]=tmp_n;
					tab_gi[i-1]=tmp_gi;
					tab_name[i-1]=tmp_name;
				}
			}else{return;}	
			i++;
		}

	}

}

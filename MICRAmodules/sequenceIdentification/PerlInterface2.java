import java.io.*;
import java.util.*;
import java.text.*; 

/* convert genbank to GFF3 */

public class PerlInterface2{
	
	Couple [] tab;
	String [] project;
	String sep;
	
	public PerlInterface2(){

	}

	public PerlInterface2(Couple[] t,String [] project,String sep){
		tab=t;
		this.project=project;
		this.sep=sep;
		for(int i=0;i<tab.length;i++){
			run(tab[i],project[i]);

		}
	}

	public void run(final Couple c,final String pro){
	
		try {
	
		Runtime runtime = Runtime.getRuntime();
		final Process process;
		process= runtime.exec("perl external_scripts/bp_genbank2gff3.pl -out stdout "+pro+sep+c.getName()+".gb");
	
		new Thread() {
			public void run() {
				try {
			
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					PrintWriter out=new PrintWriter(new FileWriter(pro+sep+c.getName()+".gff"));
					String l = "";
					try {
						boolean b=true;
						while((l = reader.readLine()) != null) {
							if(l.equals("##FASTA"))b=false;
							 if(b)out.println(l);	
						}

					} finally {
						reader.close();
						out.close();
					}
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}.start();


		new Thread() {
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getErrorStream()));
					String line = "";
					try {
						while((line = reader.readLine()) != null) {
							System.out.println(line);
						}
					} finally {
						reader.close();
					}
				} catch(IOException ioe) {
					ioe.printStackTrace();
				}
			}
		}.start();
		process.waitFor();
		}catch (InterruptedException e) {
			System.out.println("Thread was interrupted");
		    }

		 catch (IOException e) {
			System.out.println("ERREUR perlInterface2");
		} 
	}


}

import java.io.*;
import java.util.*;
import java.text.*; 

/* convert genbank to GFF3 */

public class PerlInterface4
{
	
	
	String file;
	PrintWriter out;
	

	public PerlInterface4(){

	}

	public PerlInterface4(String file,PrintWriter out){
			this.file=file;
			this.out=out;
	}

	public void run(){
	
		try {
	
		Runtime runtime = Runtime.getRuntime();
		final Process process;
	
		 process= runtime.exec("perl external_scripts/bp_genbank2gff3.pl -out stdout "+file);

		new Thread() {
			public void run() {
				try {
			
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String l = "";
					try {
						boolean b=true;
						while((l = reader.readLine()) != null) {
							if(l.equals("##FASTA"))b=false;
							 if(b)out.println(l);	
						}
			
				
				
					} finally {
						reader.close();
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
			System.out.println("ERREUR perlInterface4");
		} 

	}


}

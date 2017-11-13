import java.io.*;
import java.util.*;
import java.text.*; 

/* randomly subsample of n reads from a fastq file */


public class Echantillon{
	
	String file;
	int n;

	public Echantillon(){}

	public Echantillon(String file,int n){
		this.file=file;	
		this.n=n;
		run();
	}




	public void run(){

	try {
	
	Runtime runtime = Runtime.getRuntime();
	final Process process;
	process= runtime.exec("external_scripts/./sampling_random.sh "+file+" "+n);

		new Thread() {
			public void run() {
				try {
			
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
					String l = "";
					try {
						while((l = reader.readLine()) != null) {
					
						}
					} finally {
						process.getInputStream().close();
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
		}.start();

		process.waitFor();
		process.getOutputStream().close(); 
		process.getErrorStream().close();
	
		}catch (InterruptedException e) {
			System.out.println("Thread was interrupted");
		    }

		 catch (IOException e) {
			System.out.println("ERREUR sample");
		} 

	}



}

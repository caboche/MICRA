import java.io.*;
import java.util.*;
import java.text.*; 

/* Running FastQC */

public class FastQCInterface
{
	String project;
	PrintWriter log;
	String reads;	
	String command="";
	

	public FastQCInterface(String project,String reads,PrintWriter log){
		this.project=project;
		this.reads=reads;
		this.log=log;
		
		String name=reads.substring(reads.lastIndexOf("/")+1,reads.length());
		command="FastQC/./fastqc -t 24 "+" -o "+project+" "+reads;
		
		System.out.println("###########################################");
		System.out.println("Running FastQC...");
		log.println("###########################################");
		log.println("Running FastQC...");
		
		run();
		
	}


public void run(){
	
	try {
	
	Runtime runtime = Runtime.getRuntime();
	final Process process;
	
	System.out.println("FastQC command "+command);
	process= runtime.exec(command);
	
	
	new Thread() {
		public void run() {
			try {
			
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
			
				String l = "";
				try {

					while((l = reader.readLine()) != null) {
					log.println(l);	
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
				BufferedReader reader2 = new BufferedReader(new InputStreamReader(process.getErrorStream()));
				String line = "";
				try {
					while((line = reader2.readLine()) != null) {

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
    	process.getInputStream().close();
    	process.getErrorStream().close();
	
	}catch (InterruptedException e) {
		System.out.println("Thread was interrupted");
	    }

	 catch (IOException e) {
		System.out.println("ERREUR");
	} 

}

	


	

}

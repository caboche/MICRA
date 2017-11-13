

import java.io.*;
import java.util.*;
import java.text.*; 

/*Running R script for variant calls */


public class RInterface_SNP{
	
	String file;
	
	public RInterface_SNP(){}

	public RInterface_SNP(String file){
		this.file=file;	
	}



	public void run(){
	
		try {
	
			Runtime runtime = Runtime.getRuntime();
			final Process process;
	
			process= runtime.exec("R CMD BATCH "+file);
	

			
			new Thread() {
				public void run() {
					try {
			
						BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
						String l = "";
						try {

							while((l = reader.readLine()) != null) {
					
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
			System.out.println("ERREUR");
		} 


	}


}

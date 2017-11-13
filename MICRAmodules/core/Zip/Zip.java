

import java.io.*;
import java.util.*;
import java.text.*; 


public class Zip
{
	
	String dir;
	

	public Zip(){}

	public Zip(String dir){
		this.dir=dir;	
		run();
	}




	public void run(){

		try {
	
		System.out.println("Creating zip file ...");
		Runtime runtime = Runtime.getRuntime();
		final Process process;
		process= runtime.exec("zip -r "+dir+".zip "+dir);
		System.out.println("zip -r "+dir+".zip "+dir);

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
			System.out.println("ERREUR");
		} 

	}



}

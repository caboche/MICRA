import java.io.*;
import java.util.*;
import java.text.*; 


public class Unzip{
	
	String file;

	public Unzip(){}

	public Unzip(String file){
		this.file=file;

		try{
			run();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}




	public void run() throws IOException{
	
		try {
	
		Runtime runtime = Runtime.getRuntime();
		final Process process;
	
		System.out.println("gunzip  "+file);
		process= runtime.exec("gunzip  "+file);
	
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

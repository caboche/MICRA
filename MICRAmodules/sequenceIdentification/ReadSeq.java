import java.io.*;
import java.util.*;
import java.text.*; 


public class ReadSeq{
	
	
	String name;
	String project;
	String sep;
	

	public ReadSeq(){

	}

	public ReadSeq(String name,String project,String sep){
		this.name=name;
		this.project=project;
		this.sep=sep;


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
	
	
		 process= runtime.exec("java -cp readseq.jar run -f fa "+project+sep+name+".fa");

		new Thread() {
			public void run() {
				try {
			
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			
					String l = "";
			
				
						while((l = reader.readLine()) != null) {
					
						}
			
				
				
			
						reader.close();
			
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
			System.out.println("ERREUR ReadSeq");
		} 
	}


}

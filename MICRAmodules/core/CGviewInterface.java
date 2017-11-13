import java.io.*;
import java.util.*;
import java.text.*; 


public class CGviewInterface
{
	
	String sortie;
	String genome;
	String reads;
	int mode;
	String un;
	String project;
	String file;
	String name;
	
	

	public CGviewInterface(String file,String project,String name){
		this.project=project;
		this.file=file;
		this.name=name;
		align();
	}

	
	public void align(){
	
		try {
	
		Runtime runtime = Runtime.getRuntime();
		final Process process;
		System.out.println("running CGView for "+name+" ...");
		 process= runtime.exec("java -jar cgview.jar -i "+file+" -o "+project+name+".svg -f svg");
		System.out.println("java -jar cgview.jar -i "+file+" -o "+project+name+".svg -f svg");
	
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

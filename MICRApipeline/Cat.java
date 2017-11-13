import java.io.*;
import java.util.*;
import java.text.*; 


public class Cat{
	
	String fic1;
	String fic2;
	String output;
	

	public Cat(){

	}

	public Cat(String fic1,String fic2,String output){
		this.fic1=fic1;	
		this.fic2=fic2;
		this.output=output;
		run();
	}

	public void run(){

		try {
	
		System.out.println("Merging files ...");
		Runtime runtime = Runtime.getRuntime();
		final Process process;
		process= runtime.exec("cat "+fic1+" "+fic2);
		System.out.println("cat "+fic1+" "+fic2);

		new Thread() {
			public void run() {
				try {
			
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					PrintWriter out=new PrintWriter(new FileWriter(output));
					String l = "";
					try {

						while((l = reader.readLine()) != null) {
							out.println(l);
						}
			
				
				
					} finally {
						process.getInputStream().close();
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

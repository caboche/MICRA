import java.io.*;
import java.util.*;
import java.text.*; 


public class MapperIndex{
	
	String genome;
	String command;
	String com;
	PrintWriter error;
	

	public MapperIndex(){}

	public MapperIndex(String fic,String command,PrintWriter error){
	
		genome=fic;
		this.command=command;
		this.error=error;
		com=init();	
		run();	
	}

	
	String init(){
		String res=command;
	
		res=res.replace("genome",genome);
	
		return res;

	}




	public void run(){
	
		try {
	
		Runtime runtime = Runtime.getRuntime();
		final Process process;
		
		process= runtime.exec(com);
	
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
							error.println(line);
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

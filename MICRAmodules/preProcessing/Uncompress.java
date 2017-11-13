import java.io.*;
import java.util.*;
import java.text.*; 

/* uncompress the fastq files */

public class Uncompress
{
	
	String file;
	

	public Uncompress(){

	}

	public Uncompress(String file){
		this.file=file;	
		run();
	}




	public void run(){

		try {
	
			System.out.println("Uncompress file ...");
			Runtime runtime = Runtime.getRuntime();
			final Process process;
			String path="";
			if(file.indexOf("/")!=-1){
				path=file.substring(0,file.lastIndexOf("/"));
			}
			
		
			String command="";
			if(file.endsWith(".zip")){
				if(!path.equals("")){
					command="unzip -o "+file+" -d "+path;
				}else{
					command="unzip -o "+file;
				}
			}
			if(file.endsWith(".gz")){
				command="gunzip "+file+" -f";
			}
			process= runtime.exec(command);
			System.out.println(command);

			//program output
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


			// error output
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
			System.out.println("ERROR");
		} 

	}



}

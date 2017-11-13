import java.io.*;
import java.util.*;
import java.text.*; 


public class SeqIdentification{
	
	
	String file;
	int n1;
	int n2;
	int m1;
	int m2;
	int type;
	String output;
	String command="";
	

	public SeqIdentification(){

	}

	public SeqIdentification(String file,int type,int n1,int n2,int m1,int m2,String output){
		this.file=file;
		this.type=type;
		this.n1=n1;
		this.n2=n2;
		this.m1=m1;
		this.m2=m2;
		this.output=output;

		command="java -jar seqIdentification.jar";
		if(!file.equals(""))command=command+" -F "+file;
		if(!output.equals(""))command=command+" -o "+output;
		if(type!=-1)command=command+" -type "+type;
		if(n1!=-1)command=command+" -n1 "+n1;
		if(n2!=-1)command=command+" -n2 "+n2;
		if(m1!=-1)command=command+" -m1 "+m1;
		if(m2!=-1)command=command+" -m2 "+m2;
		
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
	
		System.out.println("command1 "+command);
		 process= runtime.exec(command);
	
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
			System.out.println("ERREUR");
		} 

	}


}

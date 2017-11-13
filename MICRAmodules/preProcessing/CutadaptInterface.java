import java.io.*;
import java.util.*;
import java.text.*; 

/* Running cutadapt*/

public class CutadaptInterface
{
	
	
	String project;
	String a1;
	String a2;
	PrintWriter log;
	int quality;
	int min_length;
	String reads;

	String reads1;
	String reads2;
		
	String command="";
	

	public CutadaptInterface(String project,String reads,String a1,int quality,int min_length,PrintWriter log){
		this.project=project;
		this.reads=reads;
		this.log=log;
		this.a1=a1;
		this.quality=quality;
		this.min_length=min_length;

		command="cutadapt";
		if(a1!=""){
			String [] seq=a1.split(",");
			for(int i=0;i<seq.length;i++){
				command=command+" -a "+seq[i];
			}
		}


		String name=reads.substring(reads.lastIndexOf("/")+1,reads.length());
		command	=command+" -q "+quality+" -m "+min_length+" -o "+project+"/"+name+" "+reads;


		System.out.println("###########################################");
		System.out.println("Running Cutadapt...");
		log.println("###########################################");
		log.println("Running Cutadapt...");
		
		run();
		
	}


	public CutadaptInterface(String project,String reads1,String reads2,String a1,String a2,int quality,int min_length,PrintWriter log){
		this.project=project;
		this.reads1=reads1;
		this.reads2=reads2;
		this.log=log;
		this.a1=a1;
		this.a2=a2;
		this.quality=quality;
		this.min_length=min_length;

		
		command="cutadapt";
		if(a1!=""){

			String [] seq=a1.split(",");
			for(int i=0;i<seq.length;i++){
				command=command+" -a "+seq[0];
			}
		}

		if(a2!=""){
			String [] seq=a2.split(",");
			for(int i=0;i<seq.length;i++){
				command=command+" -A "+seq[0];
			}
		}else{
			command=command+" -A XXX";
		}

		String name1=reads1.substring(reads1.lastIndexOf("/")+1,reads1.length());
		String name2=reads2.substring(reads2.lastIndexOf("/")+1,reads2.length());
		command	=command+" -q "+quality+" -m "+min_length+" -o "+project+"/"+name1+" "+" -p "+project+"/"+name2+" "+reads1+" "+reads2;


		System.out.println("###########################################");
		System.out.println("Running Cutadapt...");
		log.println("###########################################");
		log.println("Running Cutadapt...");
		
		run();
		
	}

	






	public void run(){
	
		try {
	
		Runtime runtime = Runtime.getRuntime();
		final Process process;
	
		System.out.println("Cuadapt command "+command);
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

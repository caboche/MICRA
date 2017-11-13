import java.io.*;
import java.util.*;
import java.text.*; 


public class PreProcessing{
	
	
	String reads;
	String reads1;
	String reads2;
	String output;
	boolean fastqc;
	/* Cutadapt */
	boolean cutadapt;
	boolean adapt;
	//boolean auto=true;
	/*threshold from which an over-represented word detected with FASTQC is considered as an adapter to trim with cutadapt*/
	double threshold;
	String a1;
	String a2;
	int quality;
	int min_length;
	String command="";
	

	public PreProcessing(){

	}

	public PreProcessing(String reads,String output,boolean fastqc,boolean cutadapt,boolean adapt,double threshold,String a1,int quality,int min_length){
		this.reads=reads;
		this.fastqc=fastqc;
		this.cutadapt=cutadapt;
		this.adapt=adapt;
		this.threshold=threshold;
		this.a1=a1;
		this.quality=quality;
		this.min_length=min_length;
		this.output=output;

		command="java -jar MICRA_preProcessing.jar";
		if(!reads.equals(""))command=command+" -R "+reads;
		if(!output.equals(""))command=command+" -o "+output;
		if(!a1.equals(""))command=command+" -a "+a1;
		if(min_length!=-1)command=command+" -ml "+min_length;
		if(quality!=-1)command=command+" -q "+quality;
		if(threshold!=-1)command=command+" -threshold "+threshold;
		if(fastqc) command=command+" -fastqc";
		if(cutadapt) command=command+" -cutadapt";
		if(adapt) command=command+" -adapter";
		

		try{
			run();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}

	}



	public PreProcessing(String reads1, String reads2,String output,boolean fastqc,boolean cutadapt,boolean adapt,double threshold,String a1,String a2,int quality,int min_length){
		this.reads1=reads1;
		this.reads2=reads2;
		this.fastqc=fastqc;
		this.cutadapt=cutadapt;
		this.adapt=adapt;
		this.threshold=threshold;
		this.a1=a1;
		this.a2=a2;
		this.quality=quality;
		this.min_length=min_length;
		this.output=output;

		command="java -jar MICRA_preProcessing.jar";
		if(!reads1.equals(""))command=command+" -1 "+reads1;
		if(!reads2.equals(""))command=command+" -2 "+reads2;
		if(!output.equals(""))command=command+" -o "+output;
		if(!a1.equals(""))command=command+" -a "+a1;
		if(!a2.equals(""))command=command+" -A "+a2;
		if(min_length!=-1)command=command+" -ml "+min_length;
		if(quality!=-1)command=command+" -q "+quality;
		if(threshold!=-1)command=command+" -threshold "+threshold;
		if(fastqc) command=command+" -fastqc";
		if(cutadapt) command=command+" -cutadapt";
		if(adapt) command=command+" -adapter";
		
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
	
		System.out.println("command "+command);
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

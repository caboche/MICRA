import java.io.*;
import java.util.*;
import java.text.*; 


public class Core{
	
	
	String genome;
	String reads;
	String reads1;
	String reads2;
	String plasmids;
	String output;
	String feature;
	String conf;
	boolean cgview;
	String ref;
	String plamids_list;
	int low_cov;
	int min_contig;
	double min_freq;
	double plas_cov;
	double bt;
	boolean skip;
	boolean sam;
	String command="";	

	public Core(){

	}

	public Core(String genome,String reads, String plasmids,String output,String plasmids_list,String ref,String conf,boolean cgview, int low_cov, double min_freq,double plas_cov,boolean skip,int min_contig,String feature,double bt,boolean sam){
		this.genome=genome;
		this.reads=reads;
		this.plasmids=plasmids;
		this.output=output;
		this.feature=feature;
		this.conf=conf;
		this.cgview=cgview;
		this.ref=ref;
		this.plamids_list=plasmids_list;
		this.low_cov=low_cov;
		this.min_contig=min_contig;
		this.min_freq=min_freq;
		this.plas_cov=plas_cov;
		this.bt=bt;
		this.skip=skip;
		this.sam=sam;


		command="java -Xmx20g -jar MICRA_core.jar -G "+genome+" -R "+reads;
		if(!plasmids.equals(""))command=command+" -P "+plasmids;
		if(!output.equals(""))command=command+" -o "+output;
		if(!feature.equals(""))command=command+" -f "+feature;
		if(!conf.equals(""))command=command+" -conf "+conf;
		if(cgview)command=command+" -CGView";
		if(!ref.equals(""))command=command+" -ref "+ref;
		if(!plasmids_list.equals(""))command=command+" -plasmids "+plasmids_list;
		if(low_cov!=-1)command=command+" -low_cov "+low_cov;
		if(min_contig!=-1)command=command+" -min_contig "+min_contig;
		if(min_freq!=-1)command=command+" -min_freq "+min_freq;
		if(plas_cov!=-1)command=command+" -plas_cov "+plas_cov;
		if(bt!=-1)command=command+" -bt "+bt;
		if(skip)command=command+" -skip ";
		if(sam)command=command+" -sam ";

		try{
			run();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}




	/* PAIRED-END*/
	public Core(String genome,String reads1,String reads2, String plasmids,String output,String plasmids_list,String ref,String conf,boolean cgview, int low_cov, double min_freq,double plas_cov,boolean skip,int min_contig,String feature,double bt,boolean sam){
		this.genome=genome;
		this.reads1=reads1;
		this.reads2=reads2;
		this.plasmids=plasmids;
		this.output=output;
		this.feature=feature;
		this.conf=conf;
		this.cgview=cgview;
		this.ref=ref;
		this.plamids_list=plasmids_list;
		this.low_cov=low_cov;
		this.min_contig=min_contig;
		this.min_freq=min_freq;
		this.plas_cov=plas_cov;
		this.bt=bt;
		this.skip=skip;
		this.sam=sam;


		command="java -Xmx20g -jar MICRA_core.jar -G "+genome+" -1 "+reads1+" -2 "+reads2;
		if(!plasmids.equals(""))command=command+" -P "+plasmids;
		if(!output.equals(""))command=command+" -o "+output;
		if(!feature.equals(""))command=command+" -f "+feature;
		if(!conf.equals(""))command=command+" -conf "+conf;
		if(cgview)command=command+" -CGView";
		if(!ref.equals(""))command=command+" -ref "+ref;
		if(!plasmids_list.equals(""))command=command+" -plasmids "+plasmids_list;
		if(low_cov!=-1)command=command+" -low_cov "+low_cov;
		if(min_contig!=-1)command=command+" -min_contig "+min_contig;
		if(min_freq!=-1)command=command+" -min_freq "+min_freq;
		if(plas_cov!=-1)command=command+" -plas_cov "+plas_cov;
		if(bt!=-1)command=command+" -bt "+bt;
		if(skip)command=command+" -skip ";
		if(sam)command=command+" -sam ";

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

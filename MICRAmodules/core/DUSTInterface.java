

import java.io.*;
import java.util.*;
import java.text.*; 


public class DUSTInterface
{
	
	Genome genome;
	String file;
	String command;
	String com;
	PrintWriter error;

	boolean status;
	
	

	public DUSTInterface(){}

	public DUSTInterface(Genome genome,String file,String command,PrintWriter error){
		status=true;
		this.genome=genome;
		this.file=file;
		this.command=command;
		this.error=error;
		
		com=command.replace("input",file);
		
		genome.setLowComplexity();
		run();
	}


	
	boolean getStatus(){
		return status;
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
							if(l.startsWith(">")){
							}else{

								String t1=l.substring(0,l.indexOf("-")-1);
								String t2=l.substring(l.indexOf("-")+2,l.length());
								int deb=Integer.parseInt(t1);
								int fin=Integer.parseInt(t2);
								if(deb==0)deb=1;
								for(int i=deb-1;i<fin;i++){
									if((i>=0)&&(i<genome.size)){
										genome.lowComplexity[i]=1;
									}else{
										System.out.println("DUST problem with positions "+deb+" "+fin+" "+i);
									}
								}

							}
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
							error.println(line);
							status=false;
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

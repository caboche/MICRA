/*package MICRA.core;*/

import java.io.*;
import java.util.*;
import java.text.*; 


public class MapperInterface{
	
	String command;
	String sortie;
	String genome;
	String reads;
	String unmapped;
	String sam;
	String project;
	String sep;
	String com;
	boolean distinct;
	PrintWriter error;
	
	public MapperInterface(){}

	/* single-end reads */
	public MapperInterface(String command,String genome,String reads, String unmapped, String sam,PrintWriter error){
		this.command=command;
		this.reads=reads;
		this.genome=genome;
		this.project=project;
		this.sep=sep;	
		this.unmapped=unmapped;
		this.sam=sam;
		/* distinct = false if output in command line*/
		this.distinct=false;
		if(command.indexOf("output")==-1){
			this.distinct=true;
		}
		this.error=error;
		this.com=init();
		align();
	}

	String reads1;
	String reads2;

	/* paired-end reads */
	public MapperInterface(String command,String genome,String reads1,String reads2, String unmapped, String sam,PrintWriter error){
		this.command=command;
		this.reads1=reads1;
		this.reads2=reads2;
		this.genome=genome;
		this.project=project;
		this.sep=sep;	
		this.unmapped=unmapped;
		this.sam=sam;
		/* distinct = false if output in command line*/
		this.distinct=false;
		if(command.indexOf("output")==-1){
			this.distinct=true;
		}
		this.error=error;
		this.com=init_paired();
		align();
		
	}


	String init(){
		String res=command;
		res=res.replace("reads",reads);
		res=res.replace("genome",genome);
		if(!unmapped.equals("")){
			res=res.replace("unmapped",unmapped);
		}
		res=res.replace("output",sam);
		return res;
	}


	String init_paired(){
		String res=command;
		res=res.replace("reads1",reads1);
		res=res.replace("reads2",reads2);
		res=res.replace("genome",genome);
		if(!unmapped.equals("")){
			res=res.replace("unmapped",unmapped);
		}
		res=res.replace("output",sam);
		System.out.println("COMMAND "+res);
		return res;
	}



	public void align(){
	
		try {
	
			Runtime runtime = Runtime.getRuntime();
			final Process process;
			System.out.println("COMmapper "+com);
			process= runtime.exec(com);

			new Thread() {
				public void run() {
					try {
						if(distinct){
							BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
							PrintWriter out=new PrintWriter(new FileWriter(sam));
							String l = "";
			
							try {
								while((l = reader.readLine()) != null) {					
									out.println(l);
								}

							}finally {
								reader.close();
								out.close();
							}

						}else{
							BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
							String l = "";
							try {
	
								while((l = reader.readLine()) != null) {
								}
	
							} finally {
								reader.close();
							}
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
			System.out.println("ERROR with mapper");
		} 

	}

}

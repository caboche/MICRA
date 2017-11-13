import java.io.*;
import java.util.*;
import java.text.*; 


public class Antibio{
	
	
	String file;
	String drugs;
	double cov_db;
	double cov_ardb;
	double iden_db;
	double iden_ardb;
	String output;
	String command="";
	

	public Antibio(){}

	public Antibio(String file,String drugs,double cov_db,double cov_ardb,double iden_db,double iden_ardb,String output){
		this.file=file;
		this.drugs=drugs;
		this.cov_db=cov_db;
		this.cov_ardb=cov_ardb;
		this.iden_db=iden_db;
		this.iden_ardb=iden_ardb;
		this.output=output;

		command="java -jar antibio.jar";
		if(!file.equals(""))command=command+" -f "+file;
		if(!output.equals(""))command=command+" -o "+output;
		if(!drugs.equals(""))command=command+" -drugs "+drugs;
		if(cov_db!=-10)command=command+" -cov_db "+cov_db;
		if(cov_ardb!=-10)command=command+" -cov_ardb "+cov_ardb;
		if(iden_db!=-10)command=command+" -iden_db "+iden_db;
		if(iden_ardb!=-10)command=command+" -iden_ardb "+cov_db;
		

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

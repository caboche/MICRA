import java.io.*;
import java.util.*;
import java.text.*; 


public class PerlInterface{
	
	Couple [] tab;
	String [] project;
	String sep;
	
	

	public PerlInterface(){

	}

	public PerlInterface(Couple[] t,String [] project,String sep){
		tab=t;
		this.project=project;
		this.sep=sep;
		for(int i=0;i<tab.length;i++){
			run(tab[i],project[i]);

		}
	}




	String res="";
	public String run(Couple c,String pro){
	
		try {
	
		Runtime runtime = Runtime.getRuntime();
		final Process process;
		process= runtime.exec("perl external_scripts/bioPerl.pl "+c.getGi()+" "+c.getName()+" "+pro+" "+sep);
	
		new Thread() {
			public void run() {
				try {
			
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String l = "";
					try {

						while((l = reader.readLine()) != null) {
							res=res+l;	
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
			System.out.println("ERREUR PerlInterface");
		} 
		return res;

	}


}

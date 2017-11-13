import java.io.*;
import java.util.*;
import java.text.*; 


public class PerlWGS{
	
	String gi;
	String id;

	public PerlWGS(){}

	public PerlWGS(String gi){
		this.gi=gi;
		id="";
	}




	String res="";
	boolean is=false;


	public String run(){
	
		try {
	
		Runtime runtime = Runtime.getRuntime();
		final Process process;
		process= runtime.exec("perl external_scripts/annot.pl "+gi);

		new Thread() {
			public void run() {
				try {
			
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String l = "";
					try {
						int i=0;
						while((l = reader.readLine()) != null) {
							if(i==0){
								id=l;
							}	
								if(l.startsWith("WGS")){
								is=true;	
								break;}
						
					
							i++;
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
			System.out.println("ERREUR PerlWGS");
		} 
		if (is)return id;
		return "";

	}


}

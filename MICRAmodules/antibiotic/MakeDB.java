
import java.io.*;
import java.util.*;
import java.text.*; 


class MakeDB{
	
	public MakeDB(){
		try{
			run();
		}catch (IOException e) {
        		System.out.println("ERROR: formatdb/makeblastdb");
		} 

	}

	public void run() throws IOException {

		try {
	
		Runtime runtime = Runtime.getRuntime();
		final Process process;
	
			System.out.println("building BLAST database...");
			process= runtime.exec("makeblastdb -in db_temp.fasta -dbtype prot -out db_temp.fasta");
			System.out.println("makeblastdb -in db_temp.fasta -dbtype prot -out db_temp.fasta");

		new Thread() {
			public void run() {
				try {
			
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String l = "";
					try {
						while((l = reader.readLine()) != null) {
							System.out.println(l);
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
			System.out.println("ERREUR");
		} 

	}

}

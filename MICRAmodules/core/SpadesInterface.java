import java.io.*;
import java.util.*;
import java.text.*; 


public class SpadesInterface{
	
	
	String project;
	String sep;
	String command;
	PrintWriter log;
	
	public SpadesInterface(String project,String sep,String command,PrintWriter log){
		this.project=project;
		this.sep=sep;
		this.log=log;
		this.command=command;
		System.out.println("###########################################");
		System.out.println("STEP4: De novo assembly of unmapped reads with SPAdes");
		log.println("###########################################");
		log.println("STEP4: De novo assembly of unmapped reads with SPAdes");
		
		align();
		
	}

	public void align(){
	
		try {
	
		Runtime runtime = Runtime.getRuntime();
		final Process process;
		System.out.println("spades command "+command);
		final String output=command.substring((command.indexOf("-o")+3),command.length());
		System.out.println("output dir "+output);
		command=command.replaceAll("mapping",(project+"mapping"));
		System.out.println("spades command "+command);
		process= runtime.exec(command);

		new Thread() {
			public void run() {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
					String l = "";
					try {
						while((l = reader.readLine()) != null) {
						}

					} finally {
						reader.close();
						File f1=new File(output+sep+"contigs.fasta");
						f1.renameTo(new File(project+"deNovo_contigs.fa"));
						File f2=new File(output+sep+"spades.log");
						f2.renameTo(new File(project+"deNovo_stats.txt"));
						File del = new File(output);
			 				try {
		   					 recursifDelete(del);
		   					} catch (IOException e) {
		    						e.printStackTrace();
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

	

	 void recursifDelete(File path) throws IOException {
	       if (!path.exists()) throw new IOException(
		  "File not found '" + path.getAbsolutePath() + "'");
	       if (path.isDirectory()) {
		  File[] children = path.listFiles();
		  for (int i=0; children != null && i<children.length; i++)
		     recursifDelete(children[i]);
		  if (!path.delete()) throw new IOException(
		     "No delete path '" + path.getAbsolutePath() + "'");
	       }
	       else if (!path.delete()) throw new IOException(
		  "No delete file '" + path.getAbsolutePath() + "'");
	 }
	
}

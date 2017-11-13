import java.io.*;
import java.util.*;
import java.text.*; 


public class MiraInterface{
		
	String project;
	String sep;
	String command;
	PrintWriter log;
	

	public MiraInterface(String project,String sep,String command,PrintWriter log){
		this.project=project;
		this.sep=sep;
		this.log=log;
		this.command=command;
		System.out.println("###########################################");
		System.out.println("STEP4: De novo assembly of unmapped reads with MIRA");
		log.println("###########################################");
		log.println("STEP4: De novo assembly of unmapped reads with MIRA");

		align();
	}


	public void align(){
	
		try {
	
		Runtime runtime = Runtime.getRuntime();
		final Process process;

		System.out.println("mira command "+command);
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
						File f1=new File("unmapped_assembly"+sep+"unmapped_d_results"+sep+"unmapped_out.unpadded.fasta");
						f1.renameTo(new File(project+"deNovo_contigs.fa"));
						File f2=new File("unmapped_assembly"+sep+"unmapped_d_info"+sep+"unmapped_info_assembly.txt");
						f2.renameTo(new File(project+"deNovo_stats.txt"));
						File del = new File("unmapped_assembly");
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

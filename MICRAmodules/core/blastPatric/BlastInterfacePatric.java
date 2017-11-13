package blastPatric;

import java.io.*;
import java.util.*;
import java.text.*; 


class BlastInterfacePatric
{
	
	String file;
	String output;
	String command;
	String com;


	public BlastInterfacePatric(){

	}

	public BlastInterfacePatric(String file,String output,String command){
		this.file=file;	
		this.output=output;
		this.command=command;
		com=init();
		try{
			run();
		}catch (IOException e) {
        		System.out.println("ERROR: BLAST");
		} 
	}


String init(){
	String res=command;
	res=res.replace("input",file);
	
	return res;

}




public void run() throws IOException {

	try {
	
	Runtime runtime = Runtime.getRuntime();
	final Process process;
	
	process= runtime.exec(com);

	new Thread() {
		public void run() {
			try {
			
				BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
				PrintWriter out=new PrintWriter(new FileWriter(output));
				String l = "";
				try {

				
					Hit hit=new Hit();
					Hsp hsp=new Hsp();
				
					while((l = reader.readLine()) != null) {				
						if(l.indexOf("<BlastOutput_version>")!=-1){
							String tmp=l.substring((l.indexOf("<BlastOutput_version>")+21),(l.indexOf("</BlastOutput_version>")));
							out.println("#"+tmp);
						}
						if(l.indexOf("<BlastOutput_db>")!=-1){
							String tmp=l.substring((l.indexOf("<BlastOutput_db>")+16),(l.indexOf("</BlastOutput_db>")));
							out.println("#"+tmp);
						}
						if(l.indexOf("<Parameters_expect>")!=-1){
							String tmp=l.substring((l.indexOf("<Parameters_expect>")+19),(l.indexOf("</Parameters_expect>")));
							out.println("#"+tmp);
						}

						if(l.indexOf("<Iteration_query-def>")!=-1){
							String tmp=l.substring((l.indexOf("<Iteration_query-def>")+21),(l.indexOf("</Iteration_query-def>")));
							out.println("@"+tmp);
						}

						/* id */
						if(l.indexOf("<Hit_id>")!=-1){
							String tmp=l.substring((l.indexOf("<Hit_id>")+8),(l.indexOf("</Hit_id>")));
							hit.setId(tmp);
						}

						/* desc */
						if(l.indexOf("<Hit_def>")!=-1){
							String tmp=l.substring((l.indexOf("<Hit_def>")+9),(l.indexOf("</Hit_def>")));
							hit.setDesc(tmp);
						}

						/* length */
						if(l.indexOf("<Hit_len>")!=-1){
							String tmp=l.substring((l.indexOf("<Hit_len>")+9),(l.indexOf("</Hit_len>")));
							hit.setLength(Integer.parseInt(tmp));
						}

						/* bit score */
						if(l.indexOf("<Hsp_bit-score>")!=-1){
							String tmp=l.substring((l.indexOf("<Hsp_bit-score>")+15),(l.indexOf("</Hsp_bit-score>")));
							hsp.setScore(tmp);
						}

						/* evalue */
						if(l.indexOf("<Hsp_evalue>")!=-1){
							String tmp=l.substring((l.indexOf("<Hsp_evalue>")+12),(l.indexOf("</Hsp_evalue>")));
							hsp.setEvalue(tmp);
						}

						/* q.start */
						if(l.indexOf("<Hsp_query-from>")!=-1){
							String tmp=l.substring((l.indexOf("<Hsp_query-from>")+16),(l.indexOf("</Hsp_query-from>")));
							hsp.setqStart(Integer.parseInt(tmp));
						}

						/* q.end */
						if(l.indexOf("<Hsp_query-to>")!=-1){
							String tmp=l.substring((l.indexOf("<Hsp_query-to>")+14),(l.indexOf("</Hsp_query-to>")));
							hsp.setqEnd(Integer.parseInt(tmp));
						}

						/* s.start */
						if(l.indexOf("<Hsp_hit-from>")!=-1){
							String tmp=l.substring((l.indexOf("<Hsp_hit-from>")+14),(l.indexOf("</Hsp_hit-from>")));
							hsp.setsStart(Integer.parseInt(tmp));
						}

						/* s.end */
						if(l.indexOf("<Hsp_hit-to>")!=-1){
							String tmp=l.substring((l.indexOf("<Hsp_hit-to>")+12),(l.indexOf("</Hsp_hit-to>")));
							hsp.setsEnd(Integer.parseInt(tmp));
						}


					

						/* end of hit */
						if(l.indexOf("</Hit>")!=-1){
							out.print(hit.getHit());
							hit=new Hit();
						}


						/* end of hsp */
						if(l.indexOf("</Hsp>")!=-1){
							hit.addHsp(hsp);
							hsp=new Hsp();
						
						}

					}
				} finally {
					process.getInputStream().close();
					reader.close();
					out.close();				
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

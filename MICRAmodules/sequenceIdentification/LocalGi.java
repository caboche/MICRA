import java.io.*;
import java.util.*;
import java.text.*; 

/* copy of local files in the working directory */

public class LocalGi{
	
	String input;
	int local;
	int download;
	File repertoire;
	int type;
	/* 0 => genomes */
	/* 1 => plasmids */
	File rep; /* output directory */
	LinkedList todo;
	String path;
	HashMap dico;

	public LocalGi(){

	}

	
	public LocalGi(String input,File repertoire,String path){
		this.input=input;
		this.repertoire=repertoire;
		local=0;
		download=0;
		todo=new LinkedList();
		this.path=path;
		dico=new HashMap();
		
		if(repertoire.getName().indexOf("Genome")!=-1) type = 0;
		else type = 1;

		/* creation of output directories */
		
		if(type==0)rep=new File(path+"reference_genomes");
		else rep=new File(path+"reference_plasmids");

		if(!rep.exists()){
			System.out.println("creation rep "+rep.getName());
			rep.mkdirs();
		}

		
		try{
			run();
		}catch (IOException e) {
        		System.out.println("ERROR: Local GI");
		} 

	}


	public LocalGi(String input,String path){
		this.input=input;
		this.path=path;
		local=0;
		download=0;
		todo=new LinkedList();
		type=-1;
		dico=new HashMap();

		try{
			run2();
		}catch (IOException e) {
        		System.out.println("ERROR: Local GI 2");
		} 

	}


	public void run() throws IOException {

		if(type==0){
			
			BufferedReader in2 = new BufferedReader(new FileReader("external_scripts/dict_genomes.txt"));
			String line="";
			while((line = in2.readLine()) != null) {
				String [] seq2=line.split(";");
				dico.put(seq2[1],seq2[0]);
			}
			in2.close();			

		}else{

			BufferedReader in2 = new BufferedReader(new FileReader("external_scripts/dict_plasmids.txt"));
			String line="";
			while((line = in2.readLine()) != null) {
				String [] seq2=line.split(";");
				dico.put(seq2[1],seq2[0]);
			}
			in2.close();


		}

		
		
		BufferedReader in = new BufferedReader(new FileReader(input));
		
		String l="";
			while((l = in.readLine()) != null) {
			
				String [] seq=l.split(";");
				
				if(!isInside(seq[0])){
					todo.add(l);
					download++;
				}
				
			}
			in.close();

	}


	public void run2() throws IOException {
		
		BufferedReader in = new BufferedReader(new FileReader(input));
		
		String l="";
			while((l = in.readLine()) != null) {
			
				String [] seq=l.split(";");
				
				if(seq[2].equals("genome")){
					repertoire=new File("allGenomes");
					rep=new File(path+"reference_genomes");
					/*******/
					BufferedReader in2 = new BufferedReader(new FileReader("external_scripts/dict_genomes.txt"));
					String line="";
					while((line = in2.readLine()) != null) {
						String [] seq2=line.split(";");
						dico.put(seq2[1],seq2[0]);
					}
					in2.close();
					/*******/
					
				
				}
				if(seq[2].equals("plasmid")){
					repertoire=new File("allPlasmids");
					rep=new File(path+"reference_plasmids");
					/*******/
					BufferedReader in2 = new BufferedReader(new FileReader("external_scripts/dict_plasmids.txt"));
					String line="";
					while((line = in2.readLine()) != null) {
						String [] seq2=line.split(";");
						dico.put(seq2[1],seq2[0]);
					}
					in2.close();
					/*******/

				}

				if(!rep.exists()){
					System.out.println("creation rep "+rep.getPath());
					rep.mkdirs();
					}

				if(!isInside(seq[0])){
					todo.add(l);
					download++;
				}
				
			}
			in.close();

	}


	public boolean isInside(String gi){
		
		String [] listefichiers;
		if(dico.get(gi)==null)return false;
		String acc=(String)dico.get(gi);
		
		
		listefichiers=repertoire.list();
		for(int i=0;i<listefichiers.length;i++){
			
			if(listefichiers[i].endsWith(".fa")){
				
				String tmp=listefichiers[i].substring((listefichiers[i].lastIndexOf(":")+1),listefichiers[i].lastIndexOf(".fa"));
				
				if(acc.equals(tmp)){
					local++;
					String s=listefichiers[i].substring(0,listefichiers[i].lastIndexOf(".fa"));
					String s2=(s.substring(0,s.indexOf(":"))+":"+gi);
					System.out.println("copy of: "+s2);
					/* copy of gff and fa */
					copy(new File(repertoire.getPath()+"/"+s+".fa"),new File(rep.getPath()+"/"+s2+".fa"));
					copy(new File(repertoire.getPath()+"/"+s+".gff"),new File(rep.getPath()+"/"+s2+".gff"));
					return true;
				}
			}			
		} 
		return false;
	}


	public static boolean copy( File source, File destination ){  
		boolean resultat = false;
		java.io.FileInputStream sourceFile=null;
		java.io.FileOutputStream destinationFile=null;
		try {
		        destination.createNewFile();
		        sourceFile = new java.io.FileInputStream(source);
		        destinationFile = new java.io.FileOutputStream(destination);
		        byte buffer[]=new byte[512*1024];
		        int nbLecture;
		        while( (nbLecture = sourceFile.read(buffer)) != -1 ) {
		                destinationFile.write(buffer, 0, nbLecture);
		        }  
		    
		        resultat = true;
		} catch( java.io.FileNotFoundException f ) {
		} catch( java.io.IOException e ) {
		} finally {
		        try {
		                sourceFile.close();
		        } catch(Exception e) { }
		        try {
		                destinationFile.close();
		        } catch(Exception e) { }
		}  
		return( resultat );
	} 

}

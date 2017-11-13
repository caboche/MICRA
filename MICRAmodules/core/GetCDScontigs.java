import java.io.*;
import java.util.*;
import java.text.*; 



public class GetCDScontigs{

	String project;
	String sep;
	PrintWriter log;
	HashMap cds; 
	GetCDScontigs(){}
	GetCDScontigs(String project,String sep,PrintWriter log){

	System.out.println("*******GET CDS CONTIG*********************");

	this.project=project;
	this.sep=sep;
	this.log=log;
	cds=new HashMap();

	try{init();
		}catch (IOException e) {
	       		System.out.println("ERROR: init HashMap with the contig CDSs");
		} 

	try{trait();
		}catch (IOException e) {
	       		System.out.println("ERROR: extracting the contig CDSs");
		} 

	}

	void init() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(project+"blast.csv"));
		String l="";
		int deb=0;
		int fin=0;
		String desc="";
		double cov=0;
		int nn=0;
		String [] seq=new String[0];

		while ((l = input.readLine()) != null) {
		seq=l.split(";");
			if(seq.length==6){
				if(cds.get(seq[0])==null){

					deb=Integer.parseInt(seq[1]);
					fin=Integer.parseInt(seq[2]);
					desc=seq[3];
					cov=Double.parseDouble(seq[4]);
					CDScontig cc=new CDScontig(deb,fin,desc,cov);
					LinkedList list=new LinkedList();
					list.add(cc);
					nn++;
					cds.put(seq[0],list);
				
				}else{
					LinkedList ll=(LinkedList)cds.get(seq[0]);
					deb=Integer.parseInt(seq[1]);
					fin=Integer.parseInt(seq[2]);
					desc=seq[3];
					cov=Double.parseDouble(seq[4]);
					CDScontig cc=new CDScontig(deb,fin,desc,cov);
					ll.add(cc);
					nn++;
					cds.put(seq[0],ll);

				}
			}
		}

		System.out.println("number of contigs with CDSs "+cds.size());
		log.println("number of contigs with CDSs "+cds.size());
		System.out.println("number of CDSs "+nn);
		log.println("number of CDSs "+nn);
		input.close();

	}


	void trait() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(project+"deNovo_contigs.fa"));
		PrintWriter out=new PrintWriter(new FileWriter(project+"deNovo_genes.fa"));

		String l="";
		String id="";
		String seq="";

		while ((l = input.readLine()) != null) {

			if(l.startsWith(">")){
				if(!id.equals("")){
					if(cds.get(id)!=null){
						LinkedList ll=(LinkedList)cds.get(id);
				
						for(int i=0;i<ll.size();i++){
							CDScontig cc=(CDScontig)ll.get(i);
							out.println("> "+id+" "+cc.beg+"-"+cc.end+" "+cc.desc);
							String line="";
							int ct=0;
							for(int j=cc.beg;j<=cc.end;j++){
								line=line+seq.charAt(j-1);
								ct++;
								if((ct % 80)==0){
								out.println(line);
								line="";
								}	
							}
							if(!line.equals(""))out.println(line);
						}
					}

				}

				seq="";
				id=l.substring(1,l.length());

			}else{seq=seq+l;}

		}

		if(!id.equals("")){
				if(cds.get(id)!=null){
					LinkedList ll=(LinkedList)cds.get(id);
				
					for(int i=0;i<ll.size();i++){
						CDScontig cc=(CDScontig)ll.get(i);
						out.println("> "+id+" "+cc.beg+"-"+cc.end+" "+cc.desc);
						String line="";
						int ct=0;
						for(int j=cc.beg;j<=cc.end;j++){
							line=line+seq.charAt(j-1);
							ct++;
							if((ct % 80)==0){
								out.println(line);
								line="";
							}	
						}
						if(!line.equals(""))out.println(line);
					}
				}

		}

		input.close();
		out.close();
	}


}

import java.io.*;
import java.util.*;

/* 

*/

public class Alerte{

	String contigs;
	double seuil;
	boolean t3db;
	boolean ardb;
	boolean db;
	LinkedList antibio;
	double cov_db;
	double cov_ardb;
	double iden_db;
	double iden_ardb;

	PrintWriter log;
	String path;

	Alerte(){}

	Alerte(String contigs,double seuil,boolean t3db,boolean ardb,boolean db,LinkedList antibio,double cov_db,double cov_ardb,double iden_db,double iden_ardb,String path){

		this.contigs=contigs;
		this.seuil=seuil;
		this.t3db=t3db;
		this.ardb=ardb;
		this.db=db;
		this.antibio=antibio;
		this.cov_db=cov_db;
		this.cov_ardb=cov_ardb;
		this.iden_db=iden_db;
		this.iden_ardb=iden_ardb;
		this.path=path;

		try{

			log = new PrintWriter(new FileWriter(path+"log.txt"));

			log.println("************************************");
			log.println("File: "+contigs);
			log.println("List of considered drugs: "+antibio);
			log.println("Minimum DrugBank target coverage percentage: "+cov_db);
			log.println("Minimum ARDB target coverage percentage: "+cov_ardb);
			log.println("Minimum DrugBank target similarity percentage: "+iden_db);
			if(iden_ardb==-1){
			log.println("Minimum ARDB target similarity percentage: automatic");
			}else{
			log.println("Minimum ARDB target similarity percentage: "+iden_ardb);
			}
			log.println("************************************");
			run();
			log.close();
		}catch (IOException e) {
			System.out.println("ERROR: alerte");
		} 
	}



	public void run() throws IOException {	
		
		PrintWriter out=new PrintWriter(new FileWriter(path+"blast_results.html"));
	
		LinkedList resume=new LinkedList();
		for(int i=0;i<antibio.size();i++){
			resume.add(new Couple());	
		}



		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>BLAST results</title>");
		out.println("</head>");
		out.println("<body>");

		out.println("Welcome in the result page of the \"antibiotic susceptibility and resistance\" Module!<br/>");
		out.println("Consensus sequences obtained from the pipeline have been blasted against <a href=\"http://ardb.cbcb.umd.edu/index.html\" target=_blank> Antibiotic Resistance Genes Database</a> (ARDB) and <a href=\"http://www.drugbank.ca/\" target=_blank>DrugBank </a>.</br>");
		out.println("The List of considered drugs is:");
		out.println("<ul>");


		for(int i=0;i<antibio.size();i++){
			out.println("<li>"+(String)antibio.get(i)+"</li>");
		}
		out.println("</ul>");
		out.println("<ul>");


		if(ardb){
			if(!contigs.equals("")){
				out.println("<li>Results of contigs against <a href=\"#ARDB_contigs\"> ARDB </a> dabank</li>" );
			}
		}
		if(db){
			if(!contigs.equals("")){
				out.println("<li>Results of contigs against <a href=\"#DrugBank_contigs\"> DrugBank </a></li>" );
			}
		}
		out.println("</ul>");	

		if(ardb){

			GetIds gi=new GetIds("ARDB",antibio,new LinkedList());
			LinkedList ids=gi.ids;
			log.println(ids.size()+" ARDB IDs are considered: ");
			ConstructDB cdb=new ConstructDB("ARDB",ids,new LinkedList(),antibio,resume,log);
			if (cdb.val>0){

				/* ARDB */
	
				if(!contigs.equals("")){
					System.out.println("blasting sequences against ARDB...");
					new BlastInterface(contigs,"resARDB_c.txt","blastx -query input -db db_temp.fasta -num_threads 40 -outfmt 5 -evalue 1e-10");
					new Parser("resARDB_c.txt",out,"ARDB_contigs",seuil,antibio,resume,cov_db,cov_ardb,iden_db,iden_ardb,log,path);
				}
			}

		}
		if(db){
			/* DrugBank */

			GetIds gi=new GetIds("drugBank",antibio,new LinkedList());
			LinkedList ids=gi.ids;
			log.println(ids.size()+" DrugBank IDs are considered: ");
			ConstructDB cdb2=new ConstructDB("drugBank",ids,new LinkedList(),antibio,resume,log);
			if(cdb2.val>0){
				if(!contigs.equals("")){
					System.out.println("blasting sequences against DrugBank...");
					new BlastInterface(contigs,"resDrugBank_c.txt","blastx -query input -db db_temp.fasta -num_threads 40 -outfmt 5 -evalue 1e-10");
					new Parser("resDrugBank_c.txt",out,"DrugBank_contigs",seuil,antibio,resume,cov_db,cov_ardb,iden_db,iden_ardb,log,path);
				}
			}
		}


		out.println("</body>");
		out.println("</html>");

		out.close();

	
		System.out.println("***********************RESULTS*****************************");
		log.println("***********************RESULTS*****************************");
			for(int i=0;i<resume.size();i++){
				String a=(String)antibio.get(i);
				Couple c=(Couple)resume.get(i);
				if(c.nb_db!=0){
					System.out.println(a+ " corresponds to "+c.nb_db+" target(s) in DrugBank.");
					log.println(a+ " corresponds to "+c.nb_db+" target(s) in DrugBank.");
					if(c.db){
						System.out.println("Susceptibility to "+a+" ("+cov_db+"%;"+iden_db+"%)");
						log.println("Susceptibility to "+a+" ("+cov_db+"%;"+iden_db+"%)");
					}else{
						System.out.println("No susceptibility to "+a+" ("+cov_db+"%;"+iden_db+"%)");
						log.println("No susceptibility to "+a+" ("+cov_db+"%;"+iden_db+"%)");
					}
				}else{
					System.out.println(a+" have no entry in DrugBank.");
					log.println(a+" have no entry in DrugBank.");
				}
				if(c.nb_ardb!=0){
					System.out.println(a+ " corresponds to "+c.nb_ardb+" target(s) in ARDB.");
					log.println(a+ " corresponds to "+c.nb_ardb+" target(s) in ARDB.");
					if(c.ardb){
						if(iden_ardb==-1){
							System.out.println("Resistance to "+a+" ("+cov_ardb+"%; automatic)");
							log.println("Resistance to "+a+" ("+cov_ardb+"%; automatic)");
						}else{
							System.out.println("Resistance to "+a+" ("+cov_ardb+"%;"+iden_ardb+"%)");
							log.println("Resistance to "+a+" ("+cov_ardb+"%;"+iden_ardb+"%)");
						}
					}else{

						if(iden_ardb==-1){
							System.out.println("No resistance to "+a+" ("+cov_ardb+"%; automatic)");
							log.println("No resistance to "+a+" ("+cov_ardb+"%; automatic)");
						}else{
							System.out.println("No resistance to "+a+" ("+cov_ardb+"%;"+iden_ardb+"%)");
							log.println("No resistance to "+a+" ("+cov_ardb+"%;"+iden_ardb+"%)");
						}
					}
				}else{
					System.out.println(a+" have no entry in ARDB.");
					log.println(a+" have no entry in ARDB.");
				}			
			}
		
		System.out.println("***********************************************************");
		log.println("***********************************************************");

		File f=new File("db_temp.fasta");
		f.delete();
		f=new File("db_temp.fasta.phr");
		f.delete();
		f=new File("db_temp.fasta.pin");
		f.delete();

		f=new File("db_temp.fasta.psq");
		f.delete();

		f=new File("resARDB_c.txt");
		f.delete();

		f=new File("resDrugBank_c.txt");
		f.delete();

		}


}

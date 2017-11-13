import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/* Main class */

public class Run{
	public static void main(String [] args)	throws IOException {	


		String gene="";
		String contig="";
		double seuil=10.0;
		boolean t3db=false;
		boolean ardb=true;
		boolean db=true;

		double cov_db=30;
		double cov_ardb=30;
		double iden_db=97;
		double iden_ardb=-1;

		String path="";

		LinkedList antibio=new LinkedList();


		if((isInside(args,"-h")!=-1)||(isInside(args,"-help")!=-1)||(isInside(args,"--help")!=-1)){
			
				help();	

		}


		if(isInside(args,"-drugs")!=-1){
			try {

				
				String tmp=args[isInside(args,"-drugs")+1];
				if(tmp.equals("")){
					System.out.println("ERROR you have to give a drug list\n");
					help();
				}
				String [] seq=tmp.split("@");
				for(int i=0;i<seq.length;i++){
					antibio.add(seq[i]);
				}
				

			}
			catch (Exception e) {
				System.out.println("ERROR you have to give a drug list\n");
				help();
			}

		}

		if(isInside(args,"-f")!=-1){
			try {

				
				contig=args[isInside(args,"-f")+1];
				

			}
			catch (Exception e) {
				System.out.println("ERROR with input fasta file\n");
				help();
			}

		}


		if(contig.equals("")){
			System.out.println("ERROR: you have to give at a FASTA input file\n");
			help();

		}

		
		if(antibio.size()==0){
			System.out.println("ERROR: you have to give a drug list\n");
			help();

		}


		if(isInside(args,"-o")!=-1){
			try {

				
				path=args[isInside(args,"-o")+1];
				
								

			}
			catch (Exception e) {
				System.out.println("ERROR you have to give a drug list\n");
				help();
			}

		}


		if(isInside(args,"-cov_db")!=-1){
			try {
				cov_db=Double.parseDouble(args[isInside(args,"-cov_db")+1]);;
					if(cov_db<0){
						System.out.println("ERROR: -cov_db must be positive\n");
						help();
					}
					if(cov_db>100){
						System.out.println("ERROR: -cov_db must be comprized between 0 and 100\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -cov_db must be a number\n");
				help();
			}
		}


		if(isInside(args,"-cov_ardb")!=-1){
			try {
				cov_ardb=Double.parseDouble(args[isInside(args,"-cov_ardb")+1]);;
					if(cov_ardb<0){
						System.out.println("ERROR: -cov_ardb must be positive\n");
						help();
					}
					if(cov_ardb>100){
						System.out.println("ERROR: -cov_ardb must be comprized between 0 and 100\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -cov_ardb must be a number\n");
				help();
			}
		}


		if(isInside(args,"-sim_db")!=-1){
			try {
				iden_db=Double.parseDouble(args[isInside(args,"-sim_db")+1]);;
					if(iden_db<0){
						System.out.println("ERROR: -sim_db must be positive\n");
						help();
					}
					if(iden_db>100){
						System.out.println("ERROR: -sim_db must be comprized between 0 and 100\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -sim_db must be a number\n");
				help();
			}
		}


		if(isInside(args,"-sim_ardb")!=-1){
			try {
				iden_ardb=Double.parseDouble(args[isInside(args,"-sim_ardb")+1]);;
					if(iden_ardb<0){
						System.out.println("ERROR: -sim_ardb must be positive\n");
						help();
					}
					if(iden_ardb>100){
						System.out.println("ERROR: -sim_ardb must be comprized between 0 and 100\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -sim_ardb must be a number\n");
				help();
			}
		}

		if(path.equals("")){
			Date date = new Date();
			SimpleDateFormat dateFormatComp;
			dateFormatComp = new SimpleDateFormat("MM_dd_yyyy_hh:mm:ss");
			path="results_antibio"+dateFormatComp.format(date);
		}
		
		File f=new File(path);
		if(!f.exists()){
			f.mkdirs();
		}
		

		if(!path.endsWith("/"))path=path+"/";
			
		new Alerte(contig,seuil,t3db,ardb,db,antibio,cov_db,cov_ardb,iden_db,iden_ardb,path);
	}


	static int isInside(String [] tab, String s){

		for(int i=0;i<tab.length;i++){
			if(tab[i].equals(s)) return i;
		}
		return (-1);

	}

	static void help(){
		System.out.println("Usage: java Run [options] -f <fasta_file> -drugs \"drug1@drug2\" [options]");
		System.out.println("-f file_name \t [mandatory] fasta input file");
		System.out.println("-drugs \"d1@d2\" \t [mandatory] the list of drugs to be tested separared by @ symbols and comprised between quotes ");
		System.out.println("-o directory \t [facultative] output directory");
		System.out.println("-cov_db double [facultative] minimum DrugBank target coverage percentage to be considered as present [30.0]");
		System.out.println("-cov_ardb double [facultative] minimum ARDB target coverage percentage to be considered as present [30.0]");
		System.out.println("-sim_db double [facultative] minimum DrugBank target identity percentage to be considered as present [97.0]");
		System.out.println("-sim_ardb double [facultative] minimum ARDB target identity percentage to be considered as present [automatic]");
		System.out.println("-h \t print this help");
		
		System.exit(1);
	}
	
}

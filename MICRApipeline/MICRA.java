import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/* Main class */

public class MICRA{

public static void main(String [] args)	throws IOException{

	String file="";
	int n1=-1;
	int n2=-1;
	int m1=-1;
	int m2=-1;
	int type=-1;

	String genome="";
	String reads="";

	String reads1="";
	String reads2="";


	/*preProcessing */
	boolean fastqc=false;
	/* Cutadapt */
	boolean cutadapt=false;
	boolean adapt=false;
	//boolean auto=true;
	/*threshold from which an over-represented word detected with FASTQC is considered as an adapter to trim with cutadapt*/
	double threshold=-1;

	String a1="";
	String a2="";
	int quality=-1;
	int min_length=-1;

	String plasmids="";
	String output="";
	String feature="";
	String conf="";
	boolean cgview=false;
	String ref="";
	String plasmids_list="";
	int low_cov=-1;
	int min_contig=-1;
	double min_freq=-1;
	double plas_cov=-1;
	double bt=-1;
	boolean skip=false;
	boolean sam=false;

	boolean antibio=false;
	String drugs="ampicillin@amoxicillin@piperacillin@cefuroxim@cefoxitin@cefotaxim@ceftazidim@cefpodoxime@imipenem@meropenem@amikacin@gentamicin@kanamycin@tobramycin@streptomycin@ciprofloxacin@norfloxacin@tetracyclin@nitrofurantoin@trimethoprim@sulfamethoxazole@chloramphenicol@fosfomycin";
	double cov_db=-10;
	double cov_ardb=-10;
	double iden_db=-10;
	double iden_ardb=-10;
	/* only automatic preprocess */
	boolean only=false;
	


	if(isInside(args,"-h")!=-1){		
					help();
	}




	if(isInside(args,"-1")!=-1){
		if(isInside(args,"-2")!=-1){
			if((isInside(args,"-R")==-1)){
				try {
					reads1=args[isInside(args,"-1")+1];
					reads2=args[isInside(args,"-2")+1];

				}
				catch (Exception e) {
					System.out.println("ERROR with input read file\n");
					help();
				}
			}else{
				System.out.println("ERROR: you have to choose between single (-R) or paired-end (-1 -2) \n");
				help();
			}
		}
		else{
			System.out.println("ERROR: you have to give a second input read file for paired-end data with option - 2\n");
			help();
		}
	}

	if(isInside(args,"-2")!=-1){
		if(isInside(args,"-1")!=-1){
			if((isInside(args,"-R")==-1)){
				try {
					reads1=args[isInside(args,"-1")+1];
					reads2=args[isInside(args,"-2")+1];

				}
				catch (Exception e) {
					System.out.println("ERROR with input read file\n");
					help();
				}
			}else{
				System.out.println("ERROR: you have to choose between single (-R) or paired-end (-1 -2) \n");
				help();
			}
		}
		else{
			System.out.println("ERROR: you have to give a second input read file for paired-end data with option - 1\n");
			help();
		}
	}

	if(isInside(args,"-R")!=-1){
		if((reads1=="")&&(reads2=="")){
			try {
				reads=args[isInside(args,"-R")+1];
			}
			catch (Exception e) {
				System.out.println("ERROR with input read file\n");
				help();
			}
		}else{
			System.out.println("ERROR: you have to choose between single (-R) or paired-end (-1 -2) \n");
			help();
		}
	}


	if((reads=="")&&(reads1=="")&&(reads2=="")){
		System.out.println("ERROR: you have to give at least one read file (-R) or paired-end reads (-1 -2) \n");
		help();
	}


	if(isInside(args,"-fastqc")!=-1){	
		fastqc=true;
	}


	if(isInside(args,"-cutadapt")!=-1){	
		cutadapt=true;
	}

	if(isInside(args,"-adapter")!=-1){	
		adapt=true;
	}


	if(isInside(args,"-threshold")!=-1){
		try {
			threshold=Double.parseDouble(args[isInside(args,"-threshold")+1]);;
				if(threshold<0){
					System.out.println("ERROR: -threshold must be positive\n");
					help();
				}
				if(threshold>100){
					System.out.println("ERROR: -threshold must be comprized between 0 and 100\n");
					help();
				}

		}
		catch (Exception e) {
			System.out.println("ERROR: -threshold must be a number\n");
			help();
		}

	}


	if(isInside(args,"-q")!=-1){
		try {
			quality=Integer.parseInt(args[isInside(args,"-q")+1]);
			cutadapt=true;
				if(quality<0){
				System.out.println("ERROR: -q must be a positive integer\n");
				help();
				}

		}
		catch (Exception e) {
			System.out.println("ERROR: -q must be a number\n");
			help();
		}

	}

	if(isInside(args,"-ml")!=-1){
		try {
			min_length=Integer.parseInt(args[isInside(args,"-ml")+1]);
			cutadapt=true;
				if(min_length<0){
					System.out.println("ERROR: -m must be a positive integer\n");
					help();
				}

		}
		catch (Exception e) {
			System.out.println("ERROR: -ml must be a number\n");
			help();
		}

	}

	if(isInside(args,"-a")!=-1){
		try {
			a1=args[isInside(args,"-a")+1];
			adapt=true;
			cutadapt=true;

		}
		catch (Exception e) {
			System.out.println("ERROR -a option\n");
			help();
		}

	}

	if(isInside(args,"-A")!=-1){
		try {
			a2=args[isInside(args,"-A")+1];
			adapt=true;
			cutadapt=true;
		}
		catch (Exception e) {
			System.out.println("ERROR -a option\n");
			help();
		}

	}


	if(isInside(args,"-gi")!=-1){
			try {
				file=args[isInside(args,"-gi")+1];

			}
			catch (Exception e) {
				System.out.println("ERROR with GI file\n");
				help();
			}

	}


	if(isInside(args,"-n1")!=-1){
		try {
			n1=Integer.parseInt(args[isInside(args,"-n1")+1]);
				if(n1<0){
					System.out.println("ERROR: -n1 must be a positive integer\n");
					help();
				}

		}
		catch (Exception e) {
			System.out.println("ERROR: -n1 must be a number\n");
			help();
		}

	}

	if(isInside(args,"-n2")!=-1){
		try {
			n2=Integer.parseInt(args[isInside(args,"-n2")+1]);
				if(n2<0){
					System.out.println("ERROR: -n2 must be a positive integer\n");
					help();
				}

		}
		catch (Exception e) {
			System.out.println("ERROR: -n2 must be a number\n");
			help();
		}

	}

	if(isInside(args,"-m1")!=-1){
		try {
			m1=Integer.parseInt(args[isInside(args,"-m1")+1]);
			if(m1<0){
				System.out.println("ERROR: -m1 must be a positive integer\n");
				help();
			}

		}
		catch (Exception e) {
			System.out.println("ERROR: -m1 must be a number\n");
			help();
		}

	}


	if(isInside(args,"-m2")!=-1){
		try {
			m2=Integer.parseInt(args[isInside(args,"-m2")+1]);
			if(m2<0){
				System.out.println("ERROR: -m2 must be a positive integer\n");
				help();
			}

		}
		catch (Exception e) {
			System.out.println("ERROR: -m2 must be a number\n");
			help();
		}

	}


	if(isInside(args,"-type")!=-1){
		try {
			type=Integer.parseInt(args[isInside(args,"-type")+1]);
			if((type<0)||(type>2)){
				System.out.println("ERROR: -type must be 0, 1 or 2\n");
				help();
			}

		}
		catch (Exception e) {
			System.out.println("ERROR: -type must be 0, 1 or 2\n");
			help();
		}

	}




	if(isInside(args,"-o")!=-1){
			try {
				output=args[isInside(args,"-o")+1];

			}
			catch (Exception e) {
				System.out.println("ERROR with output directory\n");
				help();
			}

	}

	if(isInside(args,"-plasmids")!=-1){
			try {
				plasmids_list=args[isInside(args,"-plasmids")+1];

			}
			catch (Exception e) {
				System.out.println("ERROR with the plasmid list\n");
				help();
			}

	}

	if(isInside(args,"-ref")!=-1){
			try {
				
				ref=args[isInside(args,"-ref")+1];

			}
			catch (Exception e) {
				System.out.println("ERROR -ref option\n");
				help();
			}

	}


	if(isInside(args,"-f")!=-1){
			try {
				feature=args[isInside(args,"-f")+1];

			}
			catch (Exception e) {
				System.out.println("ERROR -f option\n");
				help();
			}

	}



	if(isInside(args,"-conf")!=-1){
			try {
				conf=args[isInside(args,"-conf")+1];

			}
			catch (Exception e) {
				System.out.println("ERROR with configuration file\n");
				help();
			}

	}

	if(isInside(args,"-CGView")!=-1){
			try {
				cgview=true;

			}
			catch (Exception e) {
				System.out.println("ERROR with CGView option\n");
				help();
			}

	}

	if(isInside(args,"-low_cov")!=-1){
			try {
				low_cov=Integer.parseInt(args[isInside(args,"-low_cov")+1]);
					if(low_cov<0){
						System.out.println("ERROR: -low_cov must be a positive integer\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -low_cov must be a number\n");
				help();
			}

	}



	if(isInside(args,"-min_freq")!=-1){
			try {
				min_freq=Double.parseDouble(args[isInside(args,"-min_freq")+1]);;
					if(min_freq<0){
						System.out.println("ERROR: -min_freq must be positive\n");
						help();
					}
					if(min_freq>100){
						System.out.println("ERROR: -min_freq must be comprized between 0 and 100\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -min_freq must be a number\n");
				help();
			}

	}

	if(isInside(args,"-plas_cov")!=-1){
			try {
				plas_cov=Double.parseDouble(args[isInside(args,"-plas_cov")+1]);;
					if(plas_cov<0){
						System.out.println("ERROR: -plas_cov must be positive\n");
						help();
					}
					if(plas_cov>100){
						System.out.println("ERROR: -plas_cov must be comprized between 0 and 100\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -plas_cov must be a number\n");
				help();
			}

	}

	if(isInside(args,"-skip")!=-1){
				skip=true;
	}


	if(isInside(args,"-only")!=-1){
			
				only=true;
	}

	if(isInside(args,"-sam")!=-1){
				sam=true;
	}

	if(isInside(args,"-bt")!=-1){
			try {
				bt=Double.parseDouble(args[isInside(args,"-bt")+1]);;
					if(bt<0){
						System.out.println("ERROR: -bt must be positive\n");
						help();
					}
					if(bt>100){
						System.out.println("ERROR: -bt must be comprized between 0 and 100\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -bt must be a number\n");
				help();
			}

	}

	if(isInside(args,"-min_contig")!=-1){
			try {
				min_contig=Integer.parseInt(args[isInside(args,"-min_contig")+1]);
				
					if(min_contig<0){
						System.out.println("ERROR: -min_contig must be a positive integer\n");
						help();
					}

			}
			catch (Exception e) {
				System.out.println("ERROR: -min_contig must be a number\n");
				help();
			}

	}


	if(isInside(args,"-antibio")!=-1){
			try {
				antibio=true;

			}
			catch (Exception e) {
				System.out.println("ERROR -antibio option\n");
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

	if(isInside(args,"-drugs")!=-1){
			try {
				drugs=args[isInside(args,"-drugs")+1];
			}
			catch (Exception e) {
				System.out.println("ERROR with a drug list\n");
				help();
			}
		}

	String project="";

	if(output.equals("")){
		Date date = new Date();
		SimpleDateFormat dateFormatComp;
	 
		dateFormatComp = new SimpleDateFormat("MM_dd_yyyy_hh:mm:ss");

		project="results_"+dateFormatComp.format(date);
		output=project;

	}else{
		if(output.endsWith("/")){	
			project=output.substring(0,output.length()-1);
		}else project=output;

	}



	/***********************************************************/
	/* PreProcessing */


	System.out.println("PreProcessing....");
	System.out.println("reads "+reads);
	System.out.println("reads1 "+reads1);
	System.out.println("reads2 "+reads2);

	if((reads=="")&&(reads1!="")&&(reads2!="")){
			System.out.println("PAIRED-END reads");
			new PreProcessing(reads1,reads2,output,fastqc,cutadapt,adapt,threshold,a1,a2,quality,min_length);
		
		}else{
			if(reads!=""){
				System.out.println("SINGLE-END reads");
				new PreProcessing(reads,output,fastqc,cutadapt,adapt,threshold,a1,quality,min_length);
			}else{
				System.out.println("ERROR: INPUT read files\n");
				help();
			}
		}

	/***********************************************************/


	BufferedReader inp = new BufferedReader(new FileReader(project+"/log_preProcessing.txt"));
	String line="";
	while ((line = inp.readLine()) != null) {
		if(line.startsWith("Output file:")){
			String tmp=line.substring(line.indexOf(":")+2,line.length());
			reads=tmp;

			File ff=new File(project+"/READS");		
			if(ff.exists()){
				if(reads.indexOf("/")!=-1){
				reads=reads.substring((reads.lastIndexOf("/"))+1,reads.length());
				}
				reads=project+"/READS/"+reads;
			
			}

			System.out.println("NEW!!!! "+reads);
		}

		if(line.startsWith("Output files:")){
			String tmp=line.substring(line.indexOf(":")+2,line.length());
			String [] s=tmp.split("&");
			reads1=s[0];
			reads2=s[1];
		
			File ff=new File(project+"/READS");		
			if(ff.exists()){
				if(reads1.indexOf("/")!=-1){
					reads1=reads1.substring((reads1.lastIndexOf("/"))+1,reads1.length());
				}
			
				if(reads2.indexOf("/")!=-1){
					reads2=reads2.substring((reads2.lastIndexOf("/"))+1,reads2.length());
				}
				reads1=project+"/READS/"+reads1;
				reads2=project+"/READS/"+reads2;
			}

			System.out.println("NEW!!!! "+reads1+" "+reads2);
		
		}
	}
	inp.close();



	/*******************************************************************************************/
	if(file.equals("")) {
		if(reads!=""){
			file=reads;
		}else{
			file=reads1;
		}

	}


	/*******************************************************************************************/


	System.out.println("PREPROCESS....");
	new SeqIdentification(file,type,n1,n2,m1,m2,project);

	String tmp=project+"/reference_genomes";

	File f=new File(tmp);
	if((f.isDirectory())&&(f.exists())){genome=project+"/reference_genomes";}
	else{
		System.out.println("ERROR with input genome directory\n");
		help();
	}

	tmp=project+"/reference_plasmids";
	f=new File(tmp);
	if((f.isDirectory())&&(f.exists()))plasmids=project+"/reference_plasmids";


	/*******************************************************************************************/


	if(!only){

	System.out.println("CORE....");




	if((reads=="")&&(reads1!="")&&(reads2!="")){
			System.out.println("PAIRED-END reads");
			new Core(genome,reads1,reads2,plasmids,output,plasmids_list,ref,conf,cgview,low_cov,min_freq,plas_cov,skip,min_contig,feature,bt,sam);
		
		}else{
			if(reads!=""){
				System.out.println("SINGLE-END reads");
				new Core(genome,reads,plasmids,output,plasmids_list,ref,conf,cgview,low_cov,min_freq,plas_cov,skip,min_contig,feature,bt,sam);
			}else{
				System.out.println("ERROR: INPUT read files\n");
				help();
			}
		}

	/*******************************************************************************************/

	if(antibio){
		File ff;
		System.out.println("Antibiotic module....");
	
		File fa=new File(project+"/sequences/assembly_contigs.fa");
		if(fa.exists()){
		new Cat((project+"/sequences/assembly_contigs.fa"),(project+"/sequences/mapping_consensus.fa"),(project+"/antibio_contigs.fa"));
		new File(project+"/antibio").mkdirs();
		new Antibio((project+"/antibio_contigs.fa"),drugs,cov_db,cov_ardb,iden_db,iden_ardb,(project+"/antibio"));
		ff=new File(project+"/antibio_contigs.fa");
		ff.delete();
		}else{
			new File(project+"/antibio").mkdirs();
			new Antibio((project+"/sequences/mapping_consensus.fa"),drugs,cov_db,cov_ardb,iden_db,iden_ardb,(project+"/antibio"));
		}



		/*complete the summary file */
	
		BufferedReader in = new BufferedReader(new FileReader(project+"/summary.html"));
		int nb_lines=count(project+"/summary.html");
		PrintWriter out=new PrintWriter(new FileWriter(project+"/summary_temp.html"));
		int c=0;
		String l="";
		while ((l = in.readLine()) != null) {
		c++;
		if(c==nb_lines-4){break;}
		out.println(l);
		}


		out.println("<h3> MICRA antibiotic susceptibility and resistance module</h3>");
		out.println("<div class=\"box\">");
		out.println("<ul>");
		out.println("<li>Results summary: <a target=\"_blank\" href=\"antibio/antibiogram.html\">MICRA e-antibiogram</a></li>");
		out.println("<li>The <a target=\"_blank\" href=\"antibio/log.txt\">Log file</a></li>");
		out.println("<li>The <a target=\"_blank\" href=\"antibio/blast_results.html\">detailled BLAST results</a></li>");
		out.println("</ul>");
		out.println("</div>");



		out.println("</div>");
		out.println("</div>");
		out.println("</div>");
		out.println("</body>");
		out.println("</html>");

		in.close();
		out.close();

		ff=new File(project+"/summary.html");
		ff.delete();
		ff=new File(project+"/summary_temp.html");
		ff.renameTo(new File(project+"/summary.html"));

	
	
	}





	/* delete .fa and .gff files which are not considered as reference */

	LinkedList ref_genome_names=new LinkedList();
	File fff = new File(project);
		File[] files = fff.listFiles();
		if (files != null) {
		    for (int i = 0; i < files.length; i++) {
			String name=files[i].getName();
		        if (files[i].isDirectory() == true) {
		           if(name.equals("annotations")||name.equals("plasmids")||name.equals("sequences")||name.equals("log")||name.equals("reference_genomes")||name.equals("reference_plasmids")){

			}else{
				//System.out.println("MM "+name);
				ref_genome_names.add(name);
		            }
		        } 
		    }
		}




	LinkedList ref_plasmid_names=new LinkedList();
	fff = new File(project+"/plasmids");
		files = fff.listFiles();
		if (files != null) {
		    for (int i = 0; i < files.length; i++) {
			String name=files[i].getName();
		        if (files[i].isDirectory() == true) {
				ref_plasmid_names.add(name);
		        } 
		    }
		}




	/* delete index and gff fa files not reference */

	fff = new File(genome);
		files = fff.listFiles();
		if (files != null) {
		    for (int i = 0; i < files.length; i++) {
			String name=files[i].getName();
			//System.out.println("SEGO "+name);
		        if ((files[i].isDirectory() == true) && (name.endsWith("_index"))) {
		            delete(files[i]);
		            
		        } else{

			String temp=name.substring(0,name.lastIndexOf("."));
			temp=temp.replaceAll(":","--");
			//System.out.println("DELETE "+name+" "+temp);
			if(!isRef(ref_genome_names,temp)){
				 delete(files[i]);
			}else{
				temp=name.replaceAll(":","--");
				files[i].renameTo(new File(project+"/reference_genomes/"+temp));
			}
			}


		    }
		}


	fff = new File(plasmids);
        files = fff.listFiles();
        if (files != null) {
            for (int i = 0; i < files.length; i++) {
		String name=files[i].getName();
                if ((files[i].isDirectory() == true) && (name.endsWith("_index"))) {
                    delete(files[i]);
                    
                } else{

		String temp=name.substring(0,name.lastIndexOf("."));
		temp=temp.replaceAll(":","--");
		//System.out.println("DELETE "+name+" "+temp);
		if(!isRef(ref_plasmid_names,temp)){
			 delete(files[i]);
		}
		else{
			temp=name.replaceAll(":","--");
			files[i].renameTo(new File(project+"/reference_plasmids/"+temp));
		}

		}
            }
        }


	/* MOVE genome and plasmid files */
	File fd=new File(project+"/genomeList.html");
	fd.renameTo(new File(project+"/reference_genomes/genomeList.html"));
	fd=new File(project+"/genomeList.txt");
	fd.renameTo(new File(project+"/reference_genomes/genomeList.txt"));
	fd=new File(project+"/plasmidList.html");
	fd.renameTo(new File(project+"/reference_plasmids/plasmidList.html"));
	fd=new File(project+"/plasmidList.txt");
	fd.renameTo(new File(project+"/reference_plasmids/plasmidList.txt"));

	fd=new File(project+"/log_preProcessing.txt");
	fd.renameTo(new File(project+"/log/log_preProcessing.txt"));

	}
	/*******************************************************************************************/


	String v1="";
	String v2=project;
	if(project.lastIndexOf("/")!=-1){
		v1=project.substring(0,project.lastIndexOf("/"));
		v2=project.substring(project.lastIndexOf("/")+1, project.length());
	}


	new Zip(v1,v2);


	/* deleting result directory */

	try{
	 
		delete(new File(project));
	 
	}catch(IOException e){
		e.printStackTrace();
		System.exit(0);
	}



}



	static int isInside(String [] tab, String s){

		for(int i=0;i<tab.length;i++){
			if(tab[i].equals(s)) return i;
		}
		return (-1);

	}

	static boolean isRef(LinkedList list, String s){

		for(int i=0;i<list.size();i++){
		String t=(String)list.get(i);
			if(t.equals(s)) return true;
		}
		return false;

	}


	public static void delete(File file) throws IOException{
 
    	if(file.isDirectory()){
    		if(file.list().length==0){
 
    		   file.delete();
    		  
 
    		}else{
 
        	   String files[] = file.list();
 
        	   for (String temp : files) {
        	      File fileDelete = new File(file, temp);
        	     delete(fileDelete);
        	   }
 
        	   if(file.list().length==0){
           	     file.delete();
        	    
        	   }
    		}
 
    	}else{
    		file.delete();
    		
    	}
    }







	static	int count(String filename) throws IOException { 
	   	 InputStream is = new BufferedInputStream(new FileInputStream(filename)); 
	   		byte[] c = new byte[1024]; 
	    		int count = 0; 
	   		 int readChars = 0; 
	   		 while ((readChars = is.read(c)) != -1) { 
	      			  for (int i = 0; i < readChars; ++i) { 
		   			 if (c[i] == '\n') 
		     			   ++count; 
	       		 } 
	   	 } 
	   	 return count; 
	}






	static void help(){
		System.out.println("Usage: java Run [options] -R <read_file.fastq> [options]");
		System.out.println("-gi file_name \t [facultative] name of the GI input file in txt format)");
		System.out.println("-type \t [facultative] 0: genomes ; 1: plasmids ; 2: genomes + plasdmids [0]");
		System.out.println("-n1 \t [facultative] number of reads for genomes [1000]");
		System.out.println("-n2 \t [facultative] number of reads for plasmids [10000]");
		System.out.println("-m1 \t [facultative] number of organisms for genomes [5]");
		System.out.println("-m2 \t [facultative] number of organisms for plasmids [5]");
		System.out.println("-R file_name \t [mandatory] name of the read fastq file");
		System.out.println("-1 file_name \t [mandatory] file containing FORWARD paired-end reads (fastq)");
		System.out.println("-2 file_name \t [mandatory] file containing REVERSE paired-end reads (fastq)");
		System.out.println("-o directory_name \t [facultative] name of output directory [result_date]");
		System.out.println("-f feature \t [facultative] feature to be studied in the GFF file [CDS]");
		System.out.println("-conf file_name \t [facultative] configuration file [conf.txt]");
		System.out.println("-CGView \t [facultative] generating images with CGView [false]");
		System.out.println("-ref name \t [facultative] to force a reference genome [none]");
		System.out.println("-plasmids list \t [facultative] to force plasmids to be considered e.g. Hmoins,bactec [none]");
		System.out.println("-low_cov int \t [facultative] low coverage threshold [5]");
		System.out.println("-min_contig int \t [facultative] minimum size of contigs produced in de novo assembly [500]");
		System.out.println("-min_freq double \t [facultative] minimum frequency [90%]");
		System.out.println("-plas_cov double \t [facultative] minimum coverage for a plasmid to be considered  [70%]");
		System.out.println("-bt double \t [facultative] minimum CDS coverage in blast step [80%]");
		System.out.println("-skip \t [facultative] to skip de novo and BLAST steps");
		System.out.println("-sam \t [facultative] not save the SAM files");
		System.out.println("-antibio \t [facultative] to run the antibiotic module");
		System.out.println("-drugs \"d1@d2\" \t [mandatory] the list of drugs to be tested separared by @ symbols and comprised between quotes ");
		System.out.println("-cov_db double [facultative] minimum DrugBank target coverage percentage to be considered as present [30.0]");
		System.out.println("-cov_ardb double [facultative] minimum ARDB target coverage percentage to be considered as present [30.0]");
		System.out.println("-sim_db double [facultative] minimum DrugBank target identity percentage to be considered as present [97.0]");
		System.out.println("-sim_ardb double [facultative] minimum ARDB target identity percentage to be considered as present [automatic]");

		System.out.println("-fastqc \t [facultative] to to run FastQC");
		System.out.println("-cutadapt \t [facultative] to to run cutadapt");
		System.out.println("-adapter \t [facultative] to automatically detect and trim adapters");
		System.out.println("-threshold double \t [facultative] minimum percentage of sequence detected in reads to be considered as adapter [20%]");
		System.out.println("-q int \t [facultative] quality threshold for trimming step [20]");
		System.out.println("-ml int \t [facultative] minimum size of reads to be conserved [20]");
		System.out.println("-a list_of_adapters \t [mandatory] list of adapter to be trimmed in reads (separated by coma)");
		System.out.println("-A list_of_adapters \t [mandatory] list of adapter to be trimmed in reads 2 (separated by coma)");


		System.out.println("-h \t print this help");
		
		System.exit(1);
	}


}

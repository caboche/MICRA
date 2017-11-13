import java.io.*;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.Date;

/* Main object for core pipeline */

public class Core{


	long t1=0;
	long t2=0;
	long t0=0;
	/*output directory*/
	String out_dir;
	/* sep */
	String sep;
	/* FASTQ read file*/
	String reads;
	/* directory containing genome files */
	String genomes_dir;
	/* directory containing plasmid files */
	String plasmids_dir;
	/* numbers of reads */
	int nb_reads;
	/* String array containing the list of the closer reference genome(s) l.size=1 if user does not give a reference 
	l=2 elsewhere with ref[0]=closer ref and ref[1] = ref_user */
	Genome [] ref=new Genome[1];


	/* mapper for phylo */
	Mapper mapper1;
	/* mapper for closer ref genome*/
	Mapper mapper2;
	/* mapper 1 in plasmid module*/ 
	Mapper mapper3;
	/* mapper 2 in plasmid module*/
	Mapper mapper4;

	/* congiguration file */
	String conf;
	/* option CGview*/
	boolean cgview;


	int min_cov;
	double min_freq;
	int min_cov_dip;
	/* coverage to consider a plasmid */
	double seuil_cov;
	/* BLAST patric coverage threshold for CDS */
	double seuil;
	/* feature in GFF to consider*/
	String feature;
	/* minimum size of contigs */
	int contig_size;

	LinkedList user_plasmids=new LinkedList();


	LinkedList <Genome> genomes=new LinkedList();
	LinkedList <Genome> plasmids=new LinkedList();
	LinkedList <Genome> plas_int=new LinkedList();
	String project=""; /* directory+sep*/
	String directory="";

	/* option to skip de novo assembly step */
	boolean skip;
	/* option to keep the sam file */
	boolean sam;


	Core(){}

	Core(String genomes_dir,String reads, String plasmids_dir,String out_dir,LinkedList user_plasmids,Genome [] ref,String conf,boolean cgview, int min_cov, double min_freq,int min_cov_dip, double seuil_cov,boolean skip,double seuil,String feature, int contig_size,boolean sam){
		this.genomes_dir=genomes_dir;
		this.reads=reads;
		this.plasmids_dir=plasmids_dir;
		this.out_dir=out_dir;
		this.user_plasmids=user_plasmids;
		this.ref=ref;
		this.conf=conf;
		this.cgview=cgview;
		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;
		this.seuil_cov=seuil_cov;
		this.skip=skip;
		this.seuil=seuil;
		this.feature=feature;
		this.contig_size=contig_size;
		this.sam=sam;
		try{
			init();
			check();
			phylo();
			plasmids();
			iterative();
			denovo();
			clean();
			html();
		
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}

	}

	String reads1;
	String reads2;

	Core(String genomes_dir,String reads1, String reads2, String plasmids_dir,String out_dir,LinkedList user_plasmids,Genome [] ref,String conf,boolean cgview, int min_cov, double min_freq,int min_cov_dip, double seuil_cov,boolean skip,double seuil,String feature, int contig_size,boolean sam){
		this.genomes_dir=genomes_dir;
		this.reads1=reads1;
		this.reads2=reads2;
		this.plasmids_dir=plasmids_dir;
		this.out_dir=out_dir;
		this.user_plasmids=user_plasmids;
		this.ref=ref;
		this.conf=conf;
		this.cgview=cgview;
		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;
		this.seuil_cov=seuil_cov;
		this.skip=skip;
		this.seuil=seuil;
		this.feature=feature;
		this.contig_size=contig_size;
		this.sam=sam;
		try{
			init();
			check_paired();
			phylo();
			plasmids();
			iterative();
			denovo();
			clean();
			html();
		
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}

	}




	private String s1_mapper="";
	private String s1_index="";
	private String s2_index="";
	private String s2_mapper="";
	private String s3_mapper="";
	private String s3_index="";
	private String s4_index="";
	private String s4_mapper="";
	private boolean multimap1=true;
	private boolean multimap2=true;
	private boolean multimap3=true;
	private boolean multimap4=true;
	private String dust="";
	private String deNovo="";
	private String blastPat="";



	/* initialization of directories and variables */
	void init() throws IOException{

		/* OS detection */
		String os = (String)System.getProperties().get("os.name");
		if(os.indexOf("indows")!=-1){
			sep="\\";
		}else{
			sep="/";
		}

		if(out_dir.equals("")){
			/* result directory creation */
			Date date = new Date();
			SimpleDateFormat dateFormatComp;
			dateFormatComp = new SimpleDateFormat("MM_dd_yyyy_hh:mm:ss");

			project="results_"+dateFormatComp.format(date)+sep;
			directory="results_"+dateFormatComp.format(date);

			project=project.replaceAll(":","--");
			directory=directory.replaceAll(":","--");

			new File(directory).mkdirs();
			new File(project+"variantCalling").mkdirs();
			if(!plasmids_dir.equals("")){
				new File(project+"plasmids").mkdirs();
			}
		}else{

			if(out_dir.endsWith(sep)){
				project=out_dir;
				directory=out_dir.substring(0,out_dir.length()-1);
			}else{

				project=out_dir+sep;
				directory=out_dir;

			}

			new File(directory).mkdirs();
			new File(project+"variantCalling").mkdirs();
			if(!plasmids_dir.equals("")){
				new File(project+"plasmids").mkdirs();
			}

		}


		/* Reading the configuration file to get the command lines*/
		BufferedReader in = new BufferedReader(new FileReader(conf));
		String l="";
		while ((l = in.readLine()) != null) {
			if(!l.startsWith("#")){
				if(l.indexOf("phylo_index")!=-1){
					String [] seq=l.split("@");
					if(seq.length>1){
						s1_index=seq[1];
					}
				}
				if(l.indexOf("phylo_mapper")!=-1){
					String [] seq=l.split("@");
					if(seq.length>1){
						s1_mapper=seq[1];
						multimap1=Boolean.parseBoolean(seq[2]);
					}
				}
				if(l.indexOf("ref_index")!=-1){
					String [] seq=l.split("@");
					if(seq.length>1){
						s2_index=seq[1];
					}
				}
				if(l.indexOf("ref_mapper")!=-1){
					String [] seq=l.split("@");
					if(seq.length>1){
						s2_mapper=seq[1];
						multimap2=Boolean.parseBoolean(seq[2]);
					}
				}
				if(l.indexOf("DUST")!=-1){
					String [] seq=l.split("@");
					dust=seq[1];
				}
				if(l.indexOf("de_novo")!=-1){
					String [] seq=l.split("@");
					deNovo=seq[1];
				}
				if(l.indexOf("BLAST")!=-1){
					String [] seq=l.split("@");
					blastPat=seq[1];
				}

				if(l.indexOf("plasmid1_index")!=-1){
					String [] seq=l.split("@");
					if(seq.length>1){
						s3_index=seq[1];
					}
				}
				if(l.indexOf("plasmid1_mapper")!=-1){
					String [] seq=l.split("@");
					if(seq.length>1){
						s3_mapper=seq[1];
						multimap3=Boolean.parseBoolean(seq[2]);
					}
				}

				if(l.indexOf("plasmid2_index")!=-1){
					String [] seq=l.split("@");
					if(seq.length>1){
						s4_index=seq[1];
					}
				}
				if(l.indexOf("plasmid2_mapper")!=-1){
					String [] seq=l.split("@");
					if(seq.length>1){
						s4_mapper=seq[1];
						multimap4=Boolean.parseBoolean(seq[2]);
					}
				}
			}

		}
		in.close();

		mapper1=new Mapper(s1_index,s1_mapper,multimap1);
		mapper2=new Mapper(s2_index,s2_mapper,multimap2);
		mapper3=new Mapper(s3_index,s3_mapper,multimap3);
		mapper4=new Mapper(s4_index,s4_mapper,multimap4);

		System.out.println("step 1 "+mapper1.getIndex()+" "+mapper1.getRun());
		System.out.println("step 2 "+mapper2.getIndex()+" "+mapper2.getRun());

		if(s1_mapper.equals("")){
			System.out.println("no mapper defined for step phylo");System.exit(1);
		}
		if(s2_mapper.equals("")){
			System.out.println("no mapper defined for step ref");System.exit(1);
		}
	}


	private PrintWriter out;
	private PrintWriter error;

	/* checking input files for single-end reads */
	void check() throws IOException{

		nb_reads=count(reads)/4;
		out=new PrintWriter(new FileWriter(project+"log.txt"));
		error=new PrintWriter(new FileWriter(project+"errors.txt"));

		out.println("########### PARAMETERS ####################");
		out.println("Result directory: "+directory);
		out.println("Genome directory: "+genomes_dir);
		out.println("Read file: "+reads);
		out.println("Plasmid directory: "+plasmids_dir);
		out.println("Number of reads: "+nb_reads);
		out.println("mapper1 "+mapper1.getIndex()+" "+mapper1.getRun());
		out.println("mapper2 "+mapper2.getIndex()+" "+mapper2.getRun());
		out.println("plasmid mapper1 "+mapper3.getIndex()+" "+mapper3.getRun());
		out.println("plasmid mapper2 "+mapper4.getIndex()+" "+mapper4.getRun());
		out.println("de novo assembler "+deNovo);
		out.println("Considered feature: "+feature);
		out.println("Low coverage threshold: "+min_cov);
		out.println("Minimum frequency: "+min_freq+"%");
		out.println("minimum coverage for plasmids: "+seuil_cov+"%");
		out.println("###########################################");

		t0 = System.currentTimeMillis();
		t1=t0;


		/* Checking input files */
		if(!plasmids_dir.equals("")){
			new Check(genomes_dir,plasmids_dir,sep,out);
		}else{
			new Check(genomes_dir,sep,out);
		}

		t2 = System.currentTimeMillis();
		System.out.println("checking time "+((t2-t1)/1000)+"s");
		out.println("checking time "+((t2-t1)/1000)+"s");
		t1=System.currentTimeMillis();
	}

	/* checking input files for paired-end reads */
	void check_paired() throws IOException{

		nb_reads=(count(reads1)/4)*2;

		out=new PrintWriter(new FileWriter(project+"log.txt"));
		error=new PrintWriter(new FileWriter(project+"errors.txt"));

		out.println("########### PARAMETERS ####################");
		out.println("Result directory: "+directory);
		out.println("Genome directory: "+genomes_dir);
		out.println("Read files: "+reads1+" "+reads2);
		out.println("Plasmid directory: "+plasmids_dir);
		out.println("Number of reads: "+nb_reads);
		out.println("mapper1 "+mapper1.getIndex()+" "+mapper1.getRun());
		out.println("mapper2 "+mapper2.getIndex()+" "+mapper2.getRun());
		out.println("plasmid mapper1 "+mapper3.getIndex()+" "+mapper3.getRun());
		out.println("plasmid mapper2 "+mapper4.getIndex()+" "+mapper4.getRun());
		out.println("de novo assembler "+deNovo);
		out.println("Considered feature: "+feature);
		out.println("Low coverage threshold: "+min_cov);
		out.println("Minimum frequency: "+min_freq+"%");
		out.println("minimum coverage for plasmids: "+seuil_cov+"%");
		out.println("###########################################");

		t0 = System.currentTimeMillis();
		t1=t0;

		/* Checking input files */
		if(!plasmids_dir.equals("")){
			new Check(genomes_dir,plasmids_dir,sep,out);
		}else{
			new Check(genomes_dir,sep,out);
		}

		t2 = System.currentTimeMillis();
		System.out.println("checking time "+((t2-t1)/1000)+"s");
		out.println("checking time "+((t2-t1)/1000)+"s");
		t1=System.currentTimeMillis();
	}



	private LinkedList genomes_non_ref=new LinkedList();
	private LinkedList plasmidList=new LinkedList();


	void phylo() throws IOException{
		/* phylo module */
		Phylo phyl=new Phylo();
		if(reads1!=null){
			System.out.println("PHYLO paired-end!!!!!!!!!");
			phyl=new Phylo(genomes_dir,reads1,reads2,project,sep,nb_reads,out,mapper1,mapper2,ref,dust,error,cgview,min_cov,min_freq,min_cov_dip,feature);
		}else{
			phyl=new Phylo(genomes_dir,reads,project,sep,nb_reads,out,mapper1,mapper2,ref,dust,error,cgview,min_cov,min_freq,min_cov_dip,feature);

		}
		genomes_non_ref=phyl.genomes;
		ref=phyl.tab;
		t2 = System.currentTimeMillis();
		System.out.println("Step 1 execution time "+((t2-t1)/1000)+"s");
		out.println("Step 1 execution time "+((t2-t1)/1000)+"s");
		t1=System.currentTimeMillis();
	}

	void plasmids() throws IOException{

		/* plasmid module */

		if(plasmids_dir.equals("")){
			out.println("###########################################");
			System.out.println("STEP2: SKIPPED");
			out.println("STEP2: SKIPPED");
		}else{
			Plasmid p=new Plasmid(plasmids_dir,ref[0],project,sep,out,error,mapper3,mapper4,cgview,min_cov,min_freq,min_cov_dip,dust,seuil_cov,user_plasmids,feature);
			plasmidList=p.getPlasmidList();
		}

		/* ref[0] contains the closest genome */
		t2 = System.currentTimeMillis();
		System.out.println("Step 2 execution time "+((t2-t1)/1000)+"s");
		out.println("Step 2 execution time "+((t2-t1)/1000)+"s");
		t1=System.currentTimeMillis();

	}

	void iterative() throws IOException{

		String l="";

		/* iterative module */

		if(!plasmids_dir.equals("")){
			new Iteratif(genomes_dir,ref,genomes_non_ref,plasmidList,plasmids_dir,project,sep,mapper2,out,error,min_cov,min_freq,min_cov_dip,nb_reads,feature);
		}else{
			new Iteratif(genomes_dir,ref,genomes_non_ref,project,sep,mapper2,out,error, min_cov,min_freq,min_cov_dip,nb_reads,feature);
		}
		t2 = System.currentTimeMillis();
		System.out.println("Step 3 execution time "+((t2-t1)/1000)+"s");
		out.println("Step 3 execution time "+((t2-t1)/1000)+"s");
		t1=System.currentTimeMillis();
	}

	void denovo() throws IOException{
		/* de novo assembly */
		String l="";
		if(!skip){



		File f=new File(project+"unmap.fastq");
			if(f.exists()){
				BufferedReader in = new BufferedReader(new FileReader(project+"unmap.fastq"));
				PrintWriter tmp=new PrintWriter(new FileWriter("unmapFinal.fastq"));
				int c=0;
				while ((l = in.readLine()) != null) {
					if(c%4==0){
						String newval=""+l;
						newval=newval.replaceAll("\\.","");
						tmp.println(newval);
					}else{
						tmp.println(l);
					}
					c++;
				}
				tmp.close();
				in.close();
	

			}else{
			/* paired end*/
				BufferedReader in = new BufferedReader(new FileReader(project+"unmap.1.fastq"));
				PrintWriter tmp=new PrintWriter(new FileWriter("unmapFinal.1.fastq"));
				int c=0;
				while ((l = in.readLine()) != null) {
					if(c%4==0){
						String newval=""+l;
						newval=newval.replaceAll("\\.","");
						tmp.println(newval);
					}else{
						tmp.println(l);
					}
					c++;
				}
				tmp.close();
				in.close();
				in = new BufferedReader(new FileReader(project+"unmap.2.fastq"));
				tmp=new PrintWriter(new FileWriter("unmapFinal.2.fastq"));
				c=0;
				while ((l = in.readLine()) != null) {
					if(c%4==0){
						String newval=""+l;
						newval=newval.replaceAll("\\.","");
						//System.out.println("newval "+newval+" "+l);
						tmp.println(newval);
					}else{
						tmp.println(l);
					}
					c++;
				}
				tmp.close();
				in.close();
			}

			if(deNovo.indexOf("mira")!=-1){
				new MiraInterface(project,sep,deNovo,out);
			}else{
				if(deNovo.indexOf("spades")!=-1){
					new SpadesInterface(project,sep,deNovo,out);
				}
			}

			TraitContig tc=new TraitContig(project,sep,contig_size,out);
			boolean noContig=false;



			/* */
			File f2=new File(project+"deNovo_contigs.fa");
			f2.delete();

			f2=new File(project+"denovo_contigs.fa");
			f2.renameTo(new File(project+"deNovo_contigs.fa"));


			if(f.exists()){
				File f1=new File("unmapFinal.fastq");
				f1.delete();
			}else{
				File f1=new File("unmapFinal.1.fastq");
				f1.delete();
				f1=new File("unmapFinal.2.fastq");
				f1.delete();
			}


			int end=tc.end;
			if(end<1){
				skip=true;
				System.out.println("no contigs");
				/* SEGO si pas de contigs il faut effacer les fichiers vides */
				noContig=true;

				f2=new File(project+"deNovo_contigs.fa");
				f2.delete();
			}


			t2 = System.currentTimeMillis();
			System.out.println("Step 4 execution time "+((t2-t1)/1000)+"s");
			out.println("Step 4 execution time "+((t2-t1)/1000)+"s");
			t1=System.currentTimeMillis();

		}else{

		System.out.println("###########################################");
		System.out.println("Step 4 de novo assembly was skipped");
		out.println("###########################################");
		out.println("Step 4 de novo assembly was skipped");
		}

		/* blast patric */
		if(!skip){
			blastPatric.BlastPatric bi=new blastPatric.BlastPatric(project+"deNovo_contigs.fa",blastPat,project,sep,out,error,seuil);
			new GetCDScontigs(project,sep,out);

			t2 = System.currentTimeMillis();
			System.out.println("Step 5 execution time "+((t2-t1)/1000)+"s");
			out.println("Step 5 execution time "+((t2-t1)/1000)+"s");
			t1=System.currentTimeMillis();
		}else{
	
			System.out.println("Step 5 BLAST was skipped");
			System.out.println("###########################################");
			out.println("###########################################");
			out.println("Step 5 BLAST was skipped");
		}
	}

	void clean() throws IOException{

		/* deleting XML files */

		File rep=new File(project);
		String [] listefic;
		listefic=rep.list();
		for(int i=0;i<listefic.length;i++){
			String text=listefic[i];
			if (text.endsWith("xml")){
				File f1=new File(project+text);
				f1.delete();
			}
		}


		if(!plasmids_dir.equals("")){
			File rep1=new File(project+"plasmids");
			String [] listefic1;
			listefic1=rep1.list();
			for(int i=0;i<listefic1.length;i++){
				String text1=listefic1[i];
				if (text1.endsWith(".xml")){
					File f1=new File(project+"plasmids"+sep+text1);
					f1.delete();
				}
				if (text1.endsWith("_un.fastq")||text1.endsWith("_un.1.fastq")||text1.endsWith("_un.2.fastq")){
					File f1=new File(project+"plasmids"+sep+text1);
					f1.delete();
				}
			}

		}





		/* reformat the results directory */

		String [] lf;
		/* PLASMIDS */
		if(!plasmids_dir.equals("")){
			File rep_plas=new File(project+"plasmids");
			lf=rep_plas.list();

			for(int u=0;u<lf.length;u++){
				String text1=lf[u];

				if(text1.endsWith("ORIGINAL")){
						System.out.println("DELETE "+text1.substring(0,text1.length()-8));
						System.out.println("TEXT1 "+text1);
						String sam_tmp=(text1.substring(0,text1.length()-8));
						File ff=new File(project+"plasmids/"+sam_tmp);
						ff.delete();
						ff=new File(project+"plasmids/"+text1);
						ff.renameTo(new File(project+"plasmids/"+sam_tmp));
				}
			}


			for(int i=0;i<plasmidList.size();i++){
				Genome plas=(Genome)plasmidList.get(i);
				/* creation of a directory for each plasmid */
				String dir_plas=project+"plasmids/"+plas.getName();
				dir_plas=dir_plas.replaceAll(":","--");
				new File(dir_plas).mkdirs();
				lf=rep_plas.list();
				for(int u=0;u<lf.length;u++){
					String text1=lf[u];
					if (text1.startsWith(plas.getName())){
						File f_dep=new File(project+"plasmids/"+text1);
						f_dep.renameTo(new File(dir_plas+"/"+text1.replaceAll(":","--")));
					}
				}

				if(sam){
					String var=plas.getName().replaceAll(":","--");
					File f_sam=new File(dir_plas+"/"+var+".sam");
					f_sam.delete();
				}
	

			}

	
		}


		/* REFERENCE GENOME */

		String dir_ref=project+ref[0].getName();
		dir_ref=dir_ref.replaceAll(":","--");
		new File(dir_ref).mkdirs();
		File rep_home_ref=new File(project);
		lf=rep_home_ref.list();

		for(int i=0;i<lf.length;i++){
			String text1=lf[i];
			if(text1.endsWith("ORIGINAL")){
			String sam_tmp=(text1.substring(0,text1.length()-8));
			File ff=new File(project+sam_tmp);
			ff.delete();
			ff=new File(project+text1);
			ff.renameTo(new File(project+sam_tmp));
			}	

		}

		for(int i=0;i<lf.length;i++){
			String text1=lf[i];
			if (text1.startsWith(ref[0].getName())){
				File f_dep=new File(project+text1);
				f_dep.renameTo(new File(dir_ref+"/"+text1.replaceAll(":","--")));
			}
		}

		//move the variant file
		File mut_ref=new File(project+"variantCalling/"+ref[0].getName()+"_variants.csv");
		String var=ref[0].getName();
		var=var.replaceAll(":","--");
		mut_ref.renameTo(new File(dir_ref+"/"+var+"_variants.csv"));
		if(sam){
			File f_sam=new File(dir_ref+"/"+var+".sam");
			f_sam.delete();
		}

		/* USER GENOME */

		if(ref.length==2){
			dir_ref=project+ref[1].getName();
			dir_ref=dir_ref.replaceAll(":","--");
			new File(dir_ref).mkdirs();


			rep_home_ref=new File(project);
			lf=rep_home_ref.list();
			for(int i=0;i<lf.length;i++){
				String text1=lf[i];
				if(text1.endsWith("ORIGINAL")){
					String sam_tmp=(text1.substring(0,text1.length()-8));
					File ff=new File(project+sam_tmp);
					ff.delete();
					ff=new File(project+text1);
					ff.renameTo(new File(project+sam_tmp));
				}

				if (text1.startsWith(ref[1].getName())){
					File f_dep=new File(project+text1);
					f_dep.renameTo(new File(dir_ref+"/"+text1.replaceAll(":","--")));
				}
			}
			//move the variant file
			mut_ref=new File(project+"variantCalling/"+ref[1].getName()+"_variants.csv");
			var=ref[1].getName();
			var=var.replaceAll(":","--");
			mut_ref.renameTo(new File(dir_ref+"/"+var+"_variants.csv"));
			if(sam){
				File f_sam=new File(dir_ref+"/"+var+".sam");
				f_sam.delete();
			}
		}

		/* DELETE MUTATION DIRECTORY */
		try{
		 	delete(new File(project+"variantCalling"));
		}catch(IOException e){
			e.printStackTrace();
			System.exit(0);
		}


		/* CREATION LOG*/
		String dir_log=project+"log";
		new File(dir_log).mkdirs();
		File fd=new File(project+"log.txt");
		fd.renameTo(new File(dir_log+"/log.txt"));
		fd=new File(project+"errors.txt");
		fd.renameTo(new File(dir_log+"/errors.txt"));

		/* ANNOTATION DIRECTORY */
		String dir_annot=project+"annotations";
		new File(dir_annot).mkdirs();
		fd=new File(project+"mapping_annotations.csv");
		fd.renameTo(new File(dir_annot+"/mapping_annotations.csv"));
		fd=new File(project+"mapping_annotations.fa");
		fd.renameTo(new File(dir_annot+"/mapping_annotations.fa"));
		fd=new File(project+"blast.csv");
		fd.renameTo(new File(dir_annot+"/assembly_annotations.csv"));
		fd=new File(project+"deNovo_genes.fa");
		fd.renameTo(new File(dir_annot+"/assembly_annotations.fa"));

		if(!skip){
			String dir_blast=project+"annotations/assembly_BLAST_info";
			new File(dir_blast).mkdirs();
			fd=new File(project+"blast_unmapped.html");
			fd.renameTo(new File(dir_blast+"/assembly_blast.html"));
			fd=new File(project+"blast_unmapped.txt");
			fd.renameTo(new File(dir_blast+"/assembly_blast.txt"));
		}

		/* SEQUENCE DIRECTORY */
		String dir_seq=project+"sequences";
		new File(dir_seq).mkdirs();
		fd=new File(project+"mapping_consensus.fa");
		fd.renameTo(new File(dir_seq+"/mapping_consensus.fa"));
		fd=new File(project+"deNovo_stats.txt");
		fd.renameTo(new File(dir_seq+"/assembly_stats.txt"));
		fd=new File(project+"deNovo_contigs.fa");
		fd.renameTo(new File(dir_seq+"/assembly_contigs.fa"));


		fd=new File(project+"unmap.fastq");
		if(fd.exists()){
			fd.renameTo(new File(project+"remainingReads.fastq"));
		}else{
			File f1=new File(project+"unmap.1.fastq");
			f1.renameTo(new File(project+"remainingReads.1.fastq"));
			f1=new File(project+"unmap.2.fastq");
			f1.renameTo(new File(project+"remainingReads.2.fastq"));
		}


		System.out.println("Total time "+((t2-t0)/1000)+"s");
		out.println("Total time "+((t2-t0)/1000)+"s");
		out.close();
		error.close();

	}

	void html() throws IOException{
		String l="";
		String [] lf;

		BufferedReader in = new BufferedReader(new FileReader(project+"log/log.txt"));
		/* generation the HTML summary file */
		PrintWriter html=new PrintWriter(new FileWriter(project+"summary.html"));

		html.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">");
		html.println("<html>");
		html.println("<head>");
		html.println("<meta charset='UTF-8'>");
		html.println("<style>");

		html.println("body");
		html.println("{");
		html.println("  background-color:   white;");  
		html.println("  color:              #333;");
		html.println("  margin:           0;");
		html.println("  padding:          0;");
		html.println("  border:           0;");
		html.println("  text-align:         center;");
		html.println("  font-family:        \"Trebuchet MS\", \"Bitstream Vera Sans\", verdana,arial, helvetica, sans-serif;");
		html.println("  font-size:          10pt;");
		html.println("}");

		html.println("a:hover {");
		html.println("	color:     #EF00E7;"); 
		html.println("}");

		html.println("b.info {");
		html.println("  color:        #cb3456;");
		html.println(" font-size:    10pt;");
		html.println("}");
		html.println("#page");
		html.println("{");
		html.println("  background-color: white;");
		html.println("  border:           thin solid white;");
		html.println("  width:            900px;"); 
		html.println("  text-align:	    left;"); 
		html.println("  margin:           10px auto;");
		html.println("  padding:          0px;");
		html.println("}");

		html.println("#main"); 
		html.println("{");
		html.println("  font-size:         10pt;");
		html.println("  font-family:       \"Trebuchet MS\", \"Bitstream Vera Sans\", verdana,arial, helvetica, sans-serif;");
		html.println("  padding-left:           10pt;");
		html.println("  padding-right:           10pt;");
		html.println("  padding-bottom:    20pt;");
		html.println("  background-color:  white;");
		html.println("  margin-top:	20px; ");
		html.println(" margin-left:	10px; ");
		html.println("  margin-right:	10px;");
		html.println("  text-align:	 justify;");
		html.println("}");

		html.println("#header");
		html.println("{");
		html.println("  color:            #80399A;"); 
		html.println("  padding-top:      16pt;");
		html.println("  padding-bottom:   10pt;");     
		html.println("  padding-left:     16pt;");
		html.println("  margin-bottom:    16pt;");
		html.println("  font-size:        20pt;");
		 html.println(" font-weight:      bold;");
		html.println("  text-align:       left;");
		html.println("}");

		html.println(".sub_header {");
		html.println("padding-top:      12pt;");
		html.println("  font-size:        16pt;");
		 html.println(" font-weight: normal; ");
		html.println("}");

		html.println("#header a, #header a:hover{");
		html.println("  text-decoration:   none;");    
		html.println("  color:             #fc6a03;");
		html.println("}");


		html.println("h3{");
		html.println("color:          #80399A; ");
		html.println("font-size : 12pt; ");
		html.println("}");

		html.println(".section"); 
		html.println("{");
		html.println("  padding-left:    15pt;");
		html.println("  padding-right:   15pt;");
		html.println("  margin-top:      15pt;");
		html.println("}");

		html.println(".box{");
		html.println("  	-moz-border-radius: 5px;");
		html.println("    -webkit-border-radius: 5px;");
		html.println("    background: #F6F0FB;");
		html.println("	border:            solid thin #A673D8;");
		html.println("    border-radius: 5px;");
		html.println("    cursor:pointer;");
		html.println("    font-size: 12px;");
		html.println("    height: auto;");
		html.println("   margin: 5px;");
		html.println("    padding: 5px;   ");
		html.println("}");

		html.println("</style>");
		html.println("<title>MICRA summary</title>");
		html.println("</head>");
		html.println("<body>");
		html.println("<div id=\"page\">");
		html.println("<div id=\"main\">");
		html.println("<div class=\"section\">");
		html.println("<h1> <font color=\"#80399A\">MICRA results: Summary </font></h1>");
		html.println("<h3> MICRA running parameters </h3>");
		html.println("<div class=\"box\">");
		in.readLine();
		in.readLine();
		in.readLine();
		/* line for reads */
		html.println(in.readLine()+"</br>");
		in.readLine();
		while ((l = in.readLine()) != null) {
			if(l.startsWith("########"))break;
			html.println(l+"</br>");
		}
		html.println("</div>");


		in.readLine();
		in.readLine();
		in.readLine();
		in.readLine();
		in.readLine();


		String stat1="";
		String stat2="";
		boolean ok=false;
		while ((l = in.readLine()) != null) {
		if(l.startsWith("########"))break;
		if(l.startsWith("mapping with second mapper"))ok=true;
			if(l.startsWith("Only one reference genome"))ok=true;
				if(ok){
					if(l.startsWith("size:")){
						if(stat1.equals("")){
							stat1=""+l;
						}else{
							stat2=""+l;
						}
					}
			}	
		}


		html.println("<ul>");
		html.println("<li> For <font color=\"#C726A7\"><b>more details</b></font> about the MICRA results, please see the <a target=\"_blank\" href=\"http://www.pegase-biosciences.com/MICRA/help.php\">MICRA User guide</a></li>");
		html.println("<li> <a target=\"_blank\" href=\"log/log.txt\">Log file</a></li>");
		html.println("<li> <a target=\"_blank\" href=\"log/errors.txt\">Error file</a></li>");
		html.println("</ul>");

		html.println("<h3> Identification of the closest reference </h3>");
		html.println("<div class=\"box\">");
		html.println("<ul>");

		File filetemp=new File(project+"/genomeList.html");
		if(filetemp.exists()){
			html.println("<li>List of selected genome sequences during the pre-processing step: <a target=\"_blank\" href=\"./reference_genomes/genomeList.html\">"+"HTML format"+"</a> or <a target=\"_blank\" href=\"./reference_genomes/genomeList.txt\">TXT format</a> </li>");
		}
		html.println("<li> <a target=\"_blank\" href=\"fastMapping_stats.csv\">Fast mapping statistics</a>  for reference genomes</li>");


		String var_ref=ref[0].getName();
		var_ref=var_ref.replaceAll(":","--");
		html.println("<li> Closest reference genome: <b>"+var_ref+"</b></li>");
		html.println("<ul>");
		html.println("<li>"+stat1+"</li>");


		String [] tt=stat1.split(" ");
		if(tt.length==5){
			String ttt=tt[1];
			ttt=ttt.substring(9,ttt.length());
			Double t4=Double.parseDouble(ttt);
			if(t4<=50){
				html.println("<li><font color=\"red\"><b>WARNING</b>: MICRA was not able to identify a close reference genome. You have to give your reference sequences as an ID list (see the <a href=\"http://www.pegase-biosciences.com/MICRA/help.php#advanced\">MICRA user guide</a> Section \"Selection of the reference sequences\" Part 2: \"Give your own reference sequences\" for more details)</font></li>");
			}
		}




		File rep_home=new File(project+"/"+var_ref);
		html.println("<ul>");
		html.println("<li>Corresponding sequence <a target=\"_blank\" href=\"./reference_genomes/"+var_ref+".fa\">"+"FASTA file"+"</a></li>");
		html.println("<li>Corresponding annotation <a target=\"_blank\" href=\"./reference_genomes/"+var_ref+".gff\">"+"GFF file"+"</a></li>");
		html.println("</ul>");
		lf=rep_home.list();
		System.out.println("SEGO "+rep_home);
		for(int i=0;i<lf.length;i++){
			String text1=lf[i];
				if (text1.startsWith(var_ref)){
				
					html.println("<li>"+getTitle(text1)+": <a target=\"_blank\" href=\"./"+var_ref+"/"+text1+"\">"+"file"+"</a></li>");
				}
		}

		html.println("</ul>");
	
		if(ref.length==2){

			var_ref=ref[1].getName();
			var_ref=var_ref.replaceAll(":","--");

			html.println("<li> User reference genome: <b>"+var_ref+"</b></li>");
			html.println("<ul>");
			html.println("<li>"+stat2+"</li>");
			rep_home=new File(project+"/"+var_ref);
			html.println("<ul>");
			html.println("<li>Corresponding sequence  <a target=\"_blank\" href=\"./reference_genomes/"+var_ref+".fa\">"+"FASTA file"+"</a></li>");
			html.println("<li>Corresponding annotation  <a target=\"_blank\" href=\"./reference_genomes/"+var_ref+".gff\">"+"GFF file"+"</a></li>");
			html.println("</ul>");
			lf=rep_home.list();
			for(int i=0;i<lf.length;i++){
				String text1=lf[i];
					if (text1.startsWith(var_ref)){
							html.println("<li> "+getTitle(text1)+": <a target=\"_blank\" href=\"./"+var_ref+"/"+text1+"\">"+"file"+"</a></li>");
					}
			}
			html.println("</ul>");
		}
		html.println("</ul>");
		html.print("</div>");

	LinkedList stat_plas=new LinkedList();
	while ((l = in.readLine()) != null) {
		if(l.startsWith("########"))break;
			if(l.startsWith("*")){
				stat_plas.add(l);
			}
	
	}

	if(!plasmids_dir.equals("")){
		html.println("<h3> Identification of plasmids </h3>");
		html.println("<div class=\"box\">");
		html.println("<ul>");

		filetemp=new File(project+"/plasmidList.html");
		if(filetemp.exists()){
			html.println("<li> List of selected plasmid sequences during the pre-processing step: <a target=\"_blank\" href=\"./reference_plasmids/plasmidList.html\">"+"HTML format"+"</a> or <a target=\"_blank\" href=\"./reference_plasmids/plasmidList.txt\">TXT format</a> </li>");
		}
		html.println("<li> The <a target=\"_blank\" href=\"./plasmids/PLASMIDS.csv\">Plasmids</a> statistics</li>");
		for(int i=0;i<plasmidList.size();i++){
			Genome plas=(Genome)plasmidList.get(i);
			String temp=(String)stat_plas.get(i);
	
			String s1=temp.substring(1,temp.lastIndexOf(":"));
			String s2=temp.substring((temp.lastIndexOf(":")+1),temp.length());
			html.println("<li>"+s1+"</li>");
			html.println("<ul>");
			String var_plas=plas.getName();
			var_plas=var_plas.replaceAll(":","--");
			html.println("<li>"+s2+"</li>");
			html.println("<ul>");
			html.println("<li>Corresponding sequence  <a target=\"_blank\" href=\"./reference_plasmids/"+var_plas+".fa\">"+"FASTA file"+"</a></li>");
			html.println("<li>Corresponding annotation  <a target=\"_blank\" href=\"./reference_plasmids/"+var_plas+".gff\">"+"GFF file"+"</a></li>");
			html.println("</ul>");
			File rep_plas=new File(project+"plasmids/"+var_plas);
			lf=rep_plas.list();
	
	
			for(int u=0;u<lf.length;u++){
				String text1=lf[u];
					if (text1.startsWith(var_plas)){
						html.println("<li> "+getTitle(text1)+": <a target=\"_blank\" href=\"plasmids/"+var_plas+"/"+text1+"\">"+"file"+"</a></li>");
					}
			}
			html.println("</ul>");

		}

		html.println("</div>");
		html.println("</ul>");
	}

	String nb_reads="";

	while ((l = in.readLine()) != null) {
		if(l.startsWith("########"))break;
			if(l.startsWith("There are")){
				nb_reads=""+l;
			}
	
	}



	String nb_contigs="";

	while ((l = in.readLine()) != null) {
		if(l.startsWith("########"))break;
			if(l.startsWith("deNovo contigs")){
				nb_contigs=""+l;
			}
	
	}

	String l_cds="";
	String l_cds2="";
	String total="";
	while ((l = in.readLine()) != null) {
		if(l.startsWith("########"))break;
		if(l.startsWith("number")){
			if(l_cds.equals("")){
				l_cds=""+l;
			}else{
				l_cds2=""+l;
			}
	}

	if(l.startsWith("Total time")){
		total=""+l;
	}
	
	}



	html.println("<h3> Annotations </h3>");
	html.println("<div class=\"box\">");
	html.println("<ul>");
	rep_home=new File(project+"/annotations");
	lf=rep_home.list();
	for(int i=0;i<lf.length;i++){
	
		String text1=lf[i];
		if(!text1.equals("assembly_BLAST_info")){
			html.println("<li>"+getTitle2(text1)+": <a target=\"_blank\" href=\"annotations/"+text1+"\">"+"file"+"</a></li>");
		}
	
	}

	if(!skip){
		rep_home=new File(project+"/annotations/assembly_BLAST_info");
		html.println("<li> assembly_BLAST_info </li>");
		html.println("<ul>");
		lf=rep_home.list();
		for(int i=0;i<lf.length;i++){
	
			String text1=lf[i];
			html.println("<li> <a target=\"_blank\" href=\"annotations/assembly_BLAST_info/"+text1+"\">"+text1+"</a></li>");
	
		}
	
		html.println("</ul>");
	}


	html.println("</ul>");
	html.println("</div>");
	html.println("<h3> Sequences </h3>");
	html.println("<div class=\"box\">");
	html.println("<ul>");
	rep_home=new File(project+"/sequences");
	lf=rep_home.list();
	for(int i=0;i<lf.length;i++){
		String text1=lf[i];
		html.println("<li>"+getTitle2(text1)+": <a target=\"_blank\" href=\"sequences/"+text1+"\">"+"file"+"</a></li>");
	}

	html.println("</ul>");
	html.println("</div>");

	html.println("<h3> MICRA analysis: additional data  </h3>");
	html.println("<div class=\"box\">");
	html.println("<ul> <li> After the mapping "+nb_reads+"</li>");

	File f=new File(project+"remainingReads.fastq");
	if(f.exists()){
		html.println("<li> <a target=\"_blank\" href=\"remainingReads.fastq\">Remaining reads</a> after the iterative mapping</li>");
	}else{
		html.println("<li> <a target=\"_blank\" href=\"remainingReads.1.fastq\">Remaining reads 1</a> <a target=\"_blank\" href=\"remainingReads.2.fastq\">Remaining reads 2</a> after the iterative mapping</li>");
	}

	if(!skip){

		String tc=""+contig_size;
		String ttt1=nb_contigs.substring((nb_contigs.indexOf(">"+contig_size)+(2+tc.length())),nb_contigs.indexOf("->"));
		String ttt2=nb_contigs.substring((nb_contigs.indexOf("->")+3),nb_contigs.length());

		html.println("<li>"+ttt1+"contigs produced in de novo assembly among them "+ttt2+" are > "+contig_size+"nt </li>");
		html.println("<li>"+l_cds+" (with a total "+l_cds2+")</li>");
	}
	html.println("<li> Total running time: "+total+"</li></ul>");
	html.println("</div>");



	html.println("</div>");
	html.println("</div>");
	html.println("</div>");
	html.println("</body>");
	html.println("</html>");

	html.close();
	in.close();
	}





	int count(String filename) throws IOException { 
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


	String getTitle(String in){
		if(in.endsWith("comparativeAnnot.fa"))return "Sequences for comparative annotations (FASTA)";
		if(in.endsWith("comparativeAnnot.csv"))return "Table for comparative annotations (CSV)";
		if(in.endsWith("variants.csv"))return "Table for variant calls (CSV)";
		if(in.endsWith("consensus.fa"))return "Sequences generated from mapping (FASTA)";
		if(in.endsWith(".svg"))return "Image for the comparative genomics (SVG)";
		if(in.endsWith("un.fastq"))return "Remaining reads after the mapping (FASTQ)";
		if(in.endsWith("un.1.fastq"))return "Remaining reads 1 after the mapping (FASTQ)";
		if(in.endsWith("un.2.fastq"))return "Remaining reads 2 after the mapping (FASTQ)";
		if(in.endsWith(".sam"))return "Mapping file (SAM)";
		return "";
	}


	String getTitle2(String in){
		String res="";
		if(in.startsWith("mapping")) res=res+"From iterative Mapping";
		if(in.startsWith("assembly")) res=res+"From de novo Assembly";
		if(in.endsWith(".csv"))res=res+" (Table in .CSV)";
		if(in.endsWith(".fa"))res=res+" (Sequences in .FASTA)";
		if(in.endsWith(".txt"))res=res+" (Statistics in text format)";
		return res;
	}


	int isInside(String [] tab, String s){
		for(int i=0;i<tab.length;i++){
			if(tab[i].equals(s)) return i;
		}
		return (-1);
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
}

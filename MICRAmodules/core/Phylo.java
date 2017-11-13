import java.io.*;
import java.util.*;

/* Identification of the closest reference sequence */

public class Phylo{

	String genomes_dir;
	String reads;
	String project;
	String sep;
	int nb_reads;
	PrintWriter out;
	String dust;
	Mapper mapper1;
	Mapper mapper2;
	PrintWriter error;

	int min_cov;
	double min_freq;
	int min_cov_dip;

	boolean cgview;

	Genome [] tab;
	String feature;

	LinkedList <Genome> genomes;


	Phylo(){}


	Phylo(String genomes_dir,String reads,String project,String sep,int nb_reads,PrintWriter out,Mapper mapper1, Mapper mapper2,Genome [] tab,String dust,PrintWriter error,boolean cgview,int min_cov,double min_freq,int min_cov_dip,String feature){
		this.genomes_dir=genomes_dir;
		this.reads=reads;
		this.project=project;
		this.sep=sep;
		this.nb_reads=nb_reads;
		this.dust=dust;
		this.error=error;
		this.cgview=cgview;
		this.feature=feature;

		this.mapper1=mapper1;
		this.mapper2=mapper2;

		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;


		this.tab=tab;
		this.out=out;
		try{
			run();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}


	String reads1;
	String reads2;

	Phylo(String genomes_dir,String reads1,String reads2,String project,String sep,int nb_reads,PrintWriter out,Mapper mapper1, Mapper mapper2,Genome [] tab,String dust,PrintWriter error,boolean cgview,int min_cov,double min_freq,int min_cov_dip,String feature){
		this.genomes_dir=genomes_dir;
		this.reads1=reads1;
		this.reads2=reads2;
		this.project=project;
		this.sep=sep;
		this.nb_reads=nb_reads;
		this.dust=dust;
		this.error=error;
		this.cgview=cgview;
		this.feature=feature;

		this.mapper1=mapper1;
		this.mapper2=mapper2;

		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;


		this.tab=tab;
		this.out=out;
		try{
			run_paired();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}

	void run() throws IOException{

		System.out.println("###########################################");
		System.out.println("STEP1: Computing statistics for reference genomes");
		out.println("###########################################");
		out.println("STEP1: Computing statistics for reference genomes");

		int nb_fic=0;
		File mr=new File(genomes_dir);
		File[] files = mr.listFiles();

		for(int i=0;i<files.length;i++){
			String tmp=files[i].getName().toString();
			if(tmp.endsWith(".fa"))nb_fic++;
		}
		if(nb_fic==1){
			System.out.println("Only one reference genome");
			out.println("Only one reference genome");
			mapper1=mapper2;	
		}


		ReferenceGenome rf=new ReferenceGenome(genomes_dir,reads,project,sep,nb_reads,mapper1,error,cgview,min_cov,min_freq,min_cov_dip,feature);
		genomes=rf.run();

		double max=0;
		Genome ref=new Genome();
		for(int i=0;i<genomes.size();i++){
	
			Genome g=(Genome)genomes.get(i);
			out.println("running "+g.getName());
	
			/* lowComplexity*/
			DUSTInterface di=new DUSTInterface(g,genomes_dir+"/"+g.getName()+".fa",dust,error);
			if(di.getStatus()){
	
			}else{
				System.out.println("problem with DUST: low complexity regions could not be identified in gene detection step");
				System.out.println("Please check the DUST command line in configuration file (see error.txt file)");
				out.println("problem with DUST: low complexity regions could not be identified in gene detection step");
				out.println("Please check the DUST command line in configuration file (see error.txt file)");
	
			}

			
			if (g.getCoverage()>=max){
				max=g.getCoverage();
				ref=g;
			}

		}

		if(max==0){
		System.out.println("PROBLEM WITH SNAP");
		File ref_file=new File(project+"genomeList.html");
			if(ref_file.exists()){
				BufferedReader input = new BufferedReader(new FileReader(project+"genomeList.html"));
				String l="";
				String nname="";
				int hit_max=0;
				while ((l = input.readLine()) != null) {
					if(l.startsWith("<li>accession:")){
						String tmp_line=l.substring(l.lastIndexOf("</a>")+6,l.indexOf("read matches"));
						String name_tmp=tmp_line.substring(1,tmp_line.lastIndexOf(" - "));
						String nb_tmp=tmp_line.substring(tmp_line.lastIndexOf(" - ")+3,tmp_line.length()-1);
						int hit=Integer.parseInt(nb_tmp);
						if(hit>=hit_max){
							hit_max=hit;
							nname=name_tmp;
						}
			
					}
				}
				ref=getGenome(nname,genomes);
				input.close();	
			}
		}


		System.out.println("The closer reference genome identified is "+ref.getName());
		System.out.println("size:"+ref.getSize()+" coverage:"+ref.getCoverage()+" depth:"+ref.getDepth()+ " sd:"+ref.getSd()+" mapped:"+ref.getMapped());
		out.println("The closer reference genome identified is "+ref.getName());
		out.println("size:"+ref.getSize()+" coverage:"+ref.getCoverage()+" depth:"+ref.getDepth()+ " sd:"+ref.getSd()+" mapped:"+ref.getMapped());


		if(tab.length==1){
			tab[0]=ref;
		}else{
			if((tab[1].getName()).equals(ref.getName())){
				tab=new Genome[1];
				tab[0]=ref;
				System.out.println("the reference genome given by the user is the same than the closer reference genome identified by the pipeline "+ref.getName());
				out.println("the reference genome given by the user is the same than the closer reference genome identified by the pipeline "+ref.getName());
			}else{
				tab[0]=ref;
			}
		}


		/* delete non reference genomes */

		File rep1=new File(genomes_dir);
		String [] listefic1;
		listefic1=rep1.list();
		for(int i=0;i<listefic1.length;i++){
			String text=listefic1[i];
			text=text.substring(0,text.lastIndexOf("."));
			if(!isRef(text,tab)){	
				File f1=new File(project+text+".sam");
				f1.delete();
				File f2=new File(project+text+"_un.fastq");
				f2.delete();
			}
		}

		System.out.println("computing the list of genes for reference genome(s)");

		String s1_mapper=mapper1.getRun();
		String s2_mapper=mapper2.getRun();

		/* gene file for reference genome(s)*/
		for(int i=0;i<tab.length;i++){
	
			if(!s1_mapper.equals(s2_mapper)){

				System.out.println("mapping with second mapper...");
				out.println("mapping with second mapper...");
				String s2_index=mapper2.getIndex();

				if(!s2_index.equals("")){
					new MapperIndex(genomes_dir+sep+tab[i].getName()+".fa",s2_index,error);
				}
				new MapperInterface(s2_mapper,genomes_dir+sep+tab[i].getName()+".fa",reads, project+tab[i].getName()+"_un.fastq", project+tab[i].getName()+".sam",error);
				TraitSam a=new TraitSam(genomes_dir+sep+tab[i].getName()+".fa",project+tab[i].getName()+".sam",genomes_dir+sep+tab[i].getName()+".gff",getGenome(tab[i].getName(),genomes),project,sep,mapper2.getMulti(),nb_reads,cgview,min_cov,min_freq,min_cov_dip,feature);
				String val=a.getValue();
					
				StringTokenizer tok=new StringTokenizer(val,";");
				String name=tok.nextToken();
				int s1=Integer.parseInt(tok.nextToken());
				double s2=Double.parseDouble(tok.nextToken());
				double s3=Double.parseDouble(tok.nextToken());
				double s4=Double.parseDouble(tok.nextToken());
				double s5=Double.parseDouble(tok.nextToken());
				System.out.println(name + " size:"+s1+" coverage:"+s2+" depth:"+s3+ " sd:"+s4+" mapped:"+s5);
				out.println("size:"+s1+" coverage:"+s2+" depth:"+s3+ " sd:"+s4+" mapped:"+s5);

			}else{
				System.out.println("no need to compute sam");
				out.println("no need to compute sam");
				new TraitSam(genomes_dir+sep+tab[i].getName()+".fa",project+sep+tab[i].getName()+".sam",genomes_dir+sep+tab[i].getName()+".gff",getGenome(tab[i].getName(),genomes),project,sep,mapper2.getMulti(),cgview,min_cov,min_freq,min_cov_dip,feature);
			}

		}


		/* unmapped  in correct format */
	
		for(int i=0;i<tab.length;i++){
			FastqToFastq fq1=new FastqToFastq(project+tab[i].getName()+"_un.fastq",project+"sego.fastq");
			fq1.run();
			File f1=new File(project+tab[i].getName()+"_un.fastq");
			f1.delete();
			File f=new File(project+"sego.fastq");
			f.renameTo(new File(project+tab[i].getName()+"_un.fastq"));
		}
}



	void run_paired() throws IOException{

		System.out.println("###########################################");
		System.out.println("STEP1: Computing statistics for reference genomes");
		out.println("###########################################");
		out.println("STEP1: Computing statistics for reference genomes");

		System.out.println("PAIRED");

		int nb_fic=0;
		File mr=new File(genomes_dir);
		File[] files = mr.listFiles();

		for(int i=0;i<files.length;i++){
			String tmp=files[i].getName().toString();
			if(tmp.endsWith(".fa"))nb_fic++;
		}
		
		if(nb_fic==1){
			System.out.println("Only one reference genome");
			out.println("Only one reference genome");
			mapper1=mapper2;	
		}


		ReferenceGenome rf=new ReferenceGenome(genomes_dir,reads1,reads2,project,sep,nb_reads,mapper1,error,cgview,min_cov,min_freq,min_cov_dip,feature);
		genomes=rf.run_paired();


		double max=0;
		Genome ref=new Genome();
		for(int i=0;i<genomes.size();i++){
	
			Genome g=(Genome)genomes.get(i);
			out.println("running "+g.getName());
	
			/* lowComplexity*/
			DUSTInterface di=new DUSTInterface(g,genomes_dir+"/"+g.getName()+".fa",dust,error);
			if(di.getStatus()){
			}else{
				System.out.println("problem with DUST: low complexity regions could not be identified in gene detection step");
				System.out.println("Please check the DUST command line in configuration file (see error.txt file)");
				out.println("problem with DUST: low complexity regions could not be identified in gene detection step");
				out.println("Please check the DUST command line in configuration file (see error.txt file)");
			}
			if (g.getCoverage()>=max){
				max=g.getCoverage();
				ref=g;
			}
		}


		if(max==0){
		System.out.println("PROBLEM WITH SNAP");
			File ref_file=new File(project+"genomeList.html");
			if(ref_file.exists()){
				BufferedReader input = new BufferedReader(new FileReader(project+"genomeList.html"));
				String l="";
				String nname="";
				int hit_max=0;
				while ((l = input.readLine()) != null) {
					if(l.startsWith("<li>accession:")){
						String tmp_line=l.substring(l.lastIndexOf("</a>")+6,l.indexOf("read matches"));
						String name_tmp=tmp_line.substring(1,tmp_line.lastIndexOf(" - "));
						String nb_tmp=tmp_line.substring(tmp_line.lastIndexOf(" - ")+3,tmp_line.length()-1);
						int hit=Integer.parseInt(nb_tmp);
						if(hit>=hit_max){
							hit_max=hit;
							nname=name_tmp;
						}
			
					}
				}
				ref=getGenome(nname,genomes);
				input.close();	
			}
		}


		System.out.println("The closer reference genome identified is "+ref.getName());
		System.out.println("size:"+ref.getSize()+" coverage:"+ref.getCoverage()+" depth:"+ref.getDepth()+ " sd:"+ref.getSd()+" mapped:"+ref.getMapped());
			out.println("The closer reference genome identified is "+ref.getName());
			out.println("size:"+ref.getSize()+" coverage:"+ref.getCoverage()+" depth:"+ref.getDepth()+ " sd:"+ref.getSd()+" mapped:"+ref.getMapped());


		if(tab.length==1){
			tab[0]=ref;
		}else{
			if((tab[1].getName()).equals(ref.getName())){
				tab=new Genome[1];
				tab[0]=ref;
				System.out.println("the reference genome given by the user is the same than the closer reference genome identified by the pipeline "+ref.getName());
				out.println("the reference genome given by the user is the same than the closer reference genome identified by the pipeline "+ref.getName());
			}else{
				tab[0]=ref;
			}
		}


		/* delete genome non reference */

		File rep1=new File(genomes_dir);
		String [] listefic1;
		listefic1=rep1.list();
		for(int i=0;i<listefic1.length;i++){
			String text=listefic1[i];
			text=text.substring(0,text.lastIndexOf("."));
			if(!isRef(text,tab)){
				File f1=new File(project+text+".sam");
				f1.delete();
				f1=new File(project+text+".samORIGINAL");
				f1.delete();
				File f2=new File(project+text+"_un.fastq");
				f2.delete();
			}
		}


		System.out.println("computing the list of genes for reference genome(s)");

		String s1_mapper=mapper1.getRun();
		String s2_mapper=mapper2.getRun();

		/* gene file for reference genome(s)*/
		for(int i=0;i<tab.length;i++){
			if(!s1_mapper.equals(s2_mapper)){

				System.out.println("mapping with second mapper...");
				out.println("mapping with second mapper...");
				String s2_index=mapper2.getIndex();

				if(!s2_index.equals("")){
					new MapperIndex(genomes_dir+sep+tab[i].getName()+".fa",s2_index,error);
				}
				new MapperInterface(s2_mapper,genomes_dir+sep+tab[i].getName()+".fa",reads1,reads2, project+tab[i].getName()+"_un.fastq", project+tab[i].getName()+".sam",error);
				new ParserSam(project+tab[i].getName()+".sam");
				TraitSam a=new TraitSam(genomes_dir+sep+tab[i].getName()+".fa",project+tab[i].getName()+".sam",genomes_dir+sep+tab[i].getName()+".gff",getGenome(tab[i].getName(),genomes),project,sep,mapper2.getMulti(),nb_reads,cgview,min_cov,min_freq,min_cov_dip,feature);
				String val=a.getValue();
				StringTokenizer tok=new StringTokenizer(val,";");
				String name=tok.nextToken();
				int s1=Integer.parseInt(tok.nextToken());
				double s2=Double.parseDouble(tok.nextToken());
				double s3=Double.parseDouble(tok.nextToken());
				double s4=Double.parseDouble(tok.nextToken());
				double s5=Double.parseDouble(tok.nextToken());
				System.out.println(name + " size:"+s1+" coverage:"+s2+" depth:"+s3+ " sd:"+s4+" mapped:"+s5);
				out.println("size:"+s1+" coverage:"+s2+" depth:"+s3+ " sd:"+s4+" mapped:"+s5);

			}else{
				System.out.println("no need to compute sam");
				out.println("no need to compute sam");
				new ParserSam(project+sep+tab[i].getName());
				new TraitSam(genomes_dir+sep+tab[i].getName()+".fa",project+sep+tab[i].getName()+".sam",genomes_dir+sep+tab[i].getName()+".gff",getGenome(tab[i].getName(),genomes),project,sep,mapper2.getMulti(),cgview,min_cov,min_freq,min_cov_dip,feature);
			}

		}


		/* mettre unmapped au bon format*/
	
		for(int i=0;i<tab.length;i++){

			FastqToFastq fq1=new FastqToFastq(project+tab[i].getName()+"_un.1.fastq",project+"sego.fastq");
			FastqToFastq fq2=new FastqToFastq(project+tab[i].getName()+"_un.2.fastq",project+"sego2.fastq");
			fq1.run();
			fq2.run();
			File f1=new File(project+tab[i].getName()+"_un.1.fastq");
			File f2=new File(project+tab[i].getName()+"_un.2.fastq");
			f1.delete();
			f2.delete();
			File f=new File(project+"sego.fastq");
			f.renameTo(new File(project+tab[i].getName()+"_un.1.fastq"));
			f=new File(project+"sego2.fastq");
			f.renameTo(new File(project+tab[i].getName()+"_un.2.fastq"));
		}
	}


	Genome getGenome(String name,LinkedList l){
		for(int i=0;i<l.size();i++){
			Genome g=(Genome)l.get(i);
			if(name.equals(g.getName()))return g;
		}
		return null;
	}


	boolean isRef(String s,Genome [] t){
		for(int i=0;i<tab.length;i++){
			if(s.equals(t[i].getName()))return true;
		}
		return false;

	}

}


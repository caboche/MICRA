import java.io.*;
import java.util.*;

/* Iterative mapping */

public class Iteratif{

	
	String rep;
	String repP;
	
	Genome [] reference;
	LinkedList plasmids;

	String project;
	String sep;
	
	Mapper mapper;
	PrintWriter log;
	PrintWriter error;
	int min_cov;
	double min_freq;
	int min_cov_dip;

	LinkedList genomes;

	int init_reads;
	String feature;
	
	public Iteratif(){}

	public Iteratif(String rep,Genome [] ref,LinkedList genomes,LinkedList p,String repP,String project,String sep,Mapper mapper,PrintWriter log,PrintWriter error,int min_cov,double min_freq,int min_cov_dip, int rr,String feature){
	
		this.rep=rep;
		this.repP=repP;
	
		reference=ref;
		plasmids=p;
		this.project=project;
		this.sep=sep;
		/* number of reads starting this step*/
		init_reads=rr;
		this.feature=feature;
	
		this.mapper=mapper;
		this.error=error;
		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;
		this.log=log;
		this.genomes=genomes;

		try{
			run();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	
	}

	public Iteratif(String rep,Genome [] ref,LinkedList genomes,String project, String sep,Mapper mapper,PrintWriter log,PrintWriter error,int min_cov,double min_freq,int min_cov_dip,int rr,String feature){
		this.rep=rep;
		reference=ref;
		plasmids=new LinkedList();
		this.project=project;
		this.sep=sep;
		/* number of reads starting this step*/
		init_reads=rr;
		this.feature=feature;
	
		this.mapper=mapper;
		this.error=error;
		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;
		this.log=log;
		this.genomes=genomes;

		try{
			run();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	
	}


	public void run() throws IOException{	

		System.out.println("###########################################");
		System.out.println("STEP3: Iterative computing of the gene list");
		log.println("###########################################");
		log.println("STEP3: Iterative computing of the gene list");


		int c=0;
	
		String [] listefichiers;
		PrintWriter out=new PrintWriter(new FileWriter(project+"mapping_annotations.csv"));
		PrintWriter out3=new PrintWriter(new FileWriter(project+"mapping_annotations.fa"));
		PrintWriter out4=new PrintWriter(new FileWriter(project+"mapping_consensus.fa"));
		out.println("begin;end;strand;coverage;note");

		String s2_mapper=mapper.getRun();
		String s2_index=mapper.getIndex();

		if(reference.length==2){
			System.out.println("Running "+reference[0].getName()+" ...");
			log.println("Running "+reference[0].getName()+" ...");

			TraitSam ts=new TraitSam((rep+sep+reference[0].getName()+".fa"),(project+reference[0].getName()+".sam"),(rep+sep+reference[0].getName()+".gff"),reference[0],project,sep,mapper.getMulti(),min_cov,min_freq,min_cov_dip,out,out3,out4,false,feature);

			String v=ts.getValue();
			System.out.println("number of mapped reads: "+v);
			log.println("number of mapped reads: "+v);
			File f=new File((project+reference[0].getName())+"_un.fastq");
		
			if(f.exists()){
				f.renameTo(new File(project+"nonmapped_"+c+".fastq"));
			}else{
				File f1=new File((project+reference[0].getName())+"_un.1.fastq");
				File f2=new File((project+reference[0].getName())+"_un.2.fastq");
				f1.renameTo(new File(project+"nonmapped_"+c+".1.fastq"));
				f2.renameTo(new File(project+"nonmapped_"+c+".2.fastq"));
			}
			c++;

			System.out.println("Running "+reference[1].getName()+" ...");
			log.println("Running "+reference[1].getName()+" ...");
			
			if(!s2_index.equals("")){
				new MapperIndex((rep+sep+reference[1].getName())+".fa",s2_index,error);
			}
		
			File ff=new File(project+"nonmapped_"+(c-1)+".fastq");
			if(ff.exists()){
				new MapperInterface(s2_mapper,(rep+sep+reference[1].getName())+".fa",project+"nonmapped_"+(c-1)+".fastq",(project+"nonmapped_"+c+".fastq"), (project+reference[1].getName())+"_ite.sam",error);
				
			}else{
				new MapperInterface(s2_mapper,(rep+sep+reference[1].getName())+".fa",project+"nonmapped_"+(c-1)+".1.fastq",project+"nonmapped_"+(c-1)+".2.fastq",(project+"nonmapped_"+c+".fastq"), (project+reference[1].getName())+"_ite.sam",error);
				new ParserSam((project+reference[1].getName())+"_ite.sam");
				
			}

			TraitSam ts1=new TraitSam((rep+sep+reference[1].getName()+".fa"),(project+reference[1].getName()+"_ite.sam"),(rep+sep+reference[1].getName()+".gff"),reference[1],project,sep,mapper.getMulti(),min_cov,min_freq,min_cov_dip,out,out3,out4,false,feature);

			String v1=ts1.getValue();
			System.out.println("number of mapped reads: "+v1);
			log.println("number of mapped reads: "+v1);
			c++;
		

		}else{

			System.out.println("Running "+reference[0].getName()+" ...");
			log.println("Running "+reference[0].getName()+" ...");
			TraitSam ts=new TraitSam((rep+sep+reference[0].getName()+".fa"),(project+reference[0].getName()+".sam"),(rep+sep+reference[0].getName()+".gff"),reference[0],project,sep,mapper.getMulti(),min_cov,min_freq,min_cov_dip,out,out3,out4,false,feature);

			String v=ts.getValue();
			System.out.println("number of mapped reads: "+v);
			log.println("number of mapped reads: "+v);
			File f=new File((project+reference[0].getName())+"_un.fastq");
			if(f.exists()){
				f.renameTo(new File(project+"nonmapped_"+c+".fastq"));
			}else{
				File f1=new File((project+reference[0].getName())+"_un.1.fastq");
				File f2=new File((project+reference[0].getName())+"_un.2.fastq");
				f1.renameTo(new File(project+"nonmapped_"+c+".1.fastq"));
				f2.renameTo(new File(project+"nonmapped_"+c+".2.fastq"));
			}
			c++;
		}
	

		/* PLASMIDS */
	
		if(plasmids.size()!=0){
			for (int i=0;i<plasmids.size();i++){
				Genome g=(Genome)plasmids.get(i);
				//out.println("PLASMID: "+g.getName());
				System.out.println("Running "+g.getName()+" ...");
				log.println("Running "+g.getName()+" ...");

				if(!s2_index.equals("")){
					new MapperIndex((repP+sep+g.getName())+".fa",s2_index,error);
				}
		
				File ff=new File(project+"nonmapped_"+(c-1)+".fastq");
				if(ff.exists()){
					new MapperInterface(s2_mapper,(repP+sep+g.getName())+".fa",project+"nonmapped_"+(c-1)+".fastq",(project+"nonmapped_"+c+".fastq"), (project+g.getName())+"_ite.sam",error);
				
				}
				else{
					new MapperInterface(s2_mapper,(repP+sep+g.getName())+".fa",project+"nonmapped_"+(c-1)+".1.fastq",project+"nonmapped_"+(c-1)+".2.fastq",(project+"nonmapped_"+c+".fastq"), (project+g.getName())+"_ite.sam",error);
					new ParserSam((project+g.getName())+"_ite.sam");


				}
				TraitSam ts=new TraitSam((repP+sep+g.getName()+".fa"),(project+g.getName()+"_ite.sam"),(repP+sep+g.getName()+".gff"),g,project,sep,mapper.getMulti(),min_cov,min_freq,min_cov_dip,out,out3,out4,true,feature);

				String v=ts.getValue();
				System.out.println("number of mapped reads: "+v);
				log.println("number of mapped reads: "+v);
				c++;
			}
		}

		/* other genomes */

		for(int i=0;i<genomes.size();i++){
			Genome g=(Genome)genomes.get(i);
			if(!isRef(g.getName(),reference)){


				System.out.println("Running "+g.getName()+" ...");
				log.println("Running "+g.getName()+" ...");
			

				if(!s2_index.equals("")){
					new MapperIndex((rep+sep+g.getName())+".fa",s2_index,error);
				}
		
				File ff=new File(project+"nonmapped_"+(c-1)+".fastq");
				if(ff.exists()){
					new MapperInterface(s2_mapper,(rep+sep+g.getName())+".fa",project+"nonmapped_"+(c-1)+".fastq",(project+"nonmapped_"+c+".fastq"), (project+g.getName())+"_ite.sam",error);
				
				}else{
					new MapperInterface(s2_mapper,(rep+sep+g.getName())+".fa",project+"nonmapped_"+(c-1)+".1.fastq",project+"nonmapped_"+(c-1)+".2.fastq",(project+"nonmapped_"+c+".fastq"), (project+g.getName())+"_ite.sam",error);
					new ParserSam((project+g.getName())+"_ite.sam");

				}

				TraitSam ts=new TraitSam((rep+sep+g.getName()+".fa"),(project+g.getName()+"_ite.sam"),(rep+sep+g.getName()+".gff"),g,project,sep,mapper.getMulti(),min_cov,min_freq,min_cov_dip,out,out3,out4,false,feature);
				String v=ts.getValue();
				System.out.println("number of mapped reads: "+v);
				log.println("number of mapped reads: "+v);
				c++;
			}

		}
	

		int nb=0;
		File ff=new File(project+("nonmapped_"+(c-1)+".fastq"));
		if(ff.exists()){
			FastqToFastq fq=new FastqToFastq(project+("nonmapped_"+(c-1)+".fastq"),project+"unmap.fastq");
			nb=fq.run();
		}
		else{
			FastqToFastq fq1=new FastqToFastq(project+("nonmapped_"+(c-1)+".1.fastq"),project+"unmap.1.fastq");
			FastqToFastq fq2=new FastqToFastq(project+("nonmapped_"+(c-1)+".2.fastq"),project+"unmap.2.fastq");
			nb=fq1.run()+fq2.run();
		}
		System.out.println("There are "+nb+" reads still unmapped");
		log.println("There are "+nb+" reads still unmapped");

		out.close();
		out3.close();
		out4.close();

		/* delete _ite.sam and nonmapped*/
		File repertoire=new File(project);		
		listefichiers=repertoire.list();
			for(int i=0;i<listefichiers.length;i++){ 
				if((listefichiers[i].endsWith("_ite.sam"))||(listefichiers[i].startsWith("nonmapped_"))||(listefichiers[i].endsWith("_ite.samORIGINAL"))){
					if(listefichiers[i].startsWith("nonmapped_0")){
						File f=new File(project+"nonmapped_0.fastq");
						if(f.exists()){
							if(reference.length==2){
								f.renameTo(new File(project+reference[1].getName()+"_un.fastq"));
							}else{
								f.renameTo(new File(project+reference[0].getName()+"_un.fastq"));
							}
						}else{
							if(reference.length==2){
								File f1=new File(project+"nonmapped_0.1.fastq");
								f1.renameTo(new File(project+reference[1].getName()+"_un.1.fastq"));
								File f2=new File(project+"nonmapped_0.2.fastq");
								f2.renameTo(new File(project+reference[1].getName()+"_un.2.fastq"));
							}else{
								File f1=new File(project+"nonmapped_0.1.fastq");
								f1.renameTo(new File(project+reference[0].getName()+"_un.1.fastq"));
								File f2=new File(project+"nonmapped_0.2.fastq");
								f2.renameTo(new File(project+reference[0].getName()+"_un.2.fastq"));
							}
						
						}
					}else{
						File f1=new File(project+listefichiers[i]);
						f1.delete();
					}	
				}
			
			}

	}


	boolean isRef(String s,Genome [] t){
		for(int i=0;i<t.length;i++){
			if(s.equals(t[i].getName()))return true;
		}
		return false;

	}
}

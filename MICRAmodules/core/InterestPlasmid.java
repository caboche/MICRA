import java.io.*;
import java.util.*;

public class InterestPlasmid{

	File repertoire;
	String rep;
	Genome ref;
	String project;
	String sep;
	PrintWriter out;
	PrintWriter error;
	Mapper mapper;
	int min_cov;
	double seuil_cov;
	LinkedList not_conserved;
	String feature;
	
	public InterestPlasmid(){}

	public InterestPlasmid(String rep,Genome ref,String project,String sep,PrintWriter out,PrintWriter error,Mapper mapper,int min_cov,double seuil_cov,String feature){
		repertoire=new File(rep);
		this.rep=rep;
		this.ref=ref;
		this.project=project;
		this.sep=sep;
		this.out=out;
		this.error=error;
		this.mapper=mapper;
		this.min_cov=min_cov;
		this.seuil_cov=seuil_cov;
		not_conserved=new LinkedList();
		this.feature=feature;
	}


	public LinkedList run() throws IOException{	

		try{
			LinkedList <Genome> genomes= new LinkedList();
			String [] listefichiers;
			//PrintWriter out=new PrintWriter(new FileWriter("results/PLASMID.txt"));
			int nbReads=0;
			/* count number of reads in unmapped reference reads */
			File f_reads=new File(project+ref.getName()+"_un.fastq");
			if(f_reads.exists()){
				FastqToFastq fq1=new FastqToFastq(project+ref.getName()+"_un.fastq",project+"sego.fastq");
				nbReads=fq1.run();
				File f=new File(project+"sego.fastq");
				f.delete();
			}else{
				System.out.println("PAIRED-END unmap");
				FastqToFastq fq1=new FastqToFastq(project+ref.getName()+"_un.1.fastq",project+"sego.fastq");
				nbReads=fq1.run()*2;
				File f=new File(project+"sego.fastq");
				f.delete();
			}
	

	
			listefichiers=repertoire.list();
			for(int i=0;i<listefichiers.length;i++){ 
				if(listefichiers[i].endsWith(".fa")){
					String name=rep+sep+listefichiers[i];
					String sortie=name.substring(name.lastIndexOf(sep)+1,name.lastIndexOf("."));
					System.out.println("running "+sortie);		
				
					String s1_index=mapper.getIndex();
					if(!s1_index.equals("")){
						new MapperIndex(name,s1_index,error);
					}
					if(f_reads.exists()){
						new MapperInterface(mapper.getRun(),name,project+ref.getName()+"_un.fastq", project+sortie+"_un.fastq", project+sortie+".sam",error);
					}else{
						System.out.println("READS "+project+ref.getName()+"_un.1.fastq");
						new MapperInterface(mapper.getRun(),name,project+ref.getName()+"_un.1.fastq",project+ref.getName()+"_un.2.fastq",project+sortie+"_un.fastq", project+sortie+".sam",error);
						new ParserSam(project+sortie+".sam");

					}
				
					TraitSam a=new TraitSam(name,project+sortie+".sam",project,sep,mapper.getMulti(),nbReads,min_cov,feature);

					String val=a.getValue();
					StringTokenizer tok=new StringTokenizer(val,";");
					Genome g=new Genome(tok.nextToken());
					g.addSize(Integer.parseInt(tok.nextToken()));
					g.addCoverage(Double.parseDouble(tok.nextToken()));
					g.addDepth(Double.parseDouble(tok.nextToken()));
					g.addSd(Double.parseDouble(tok.nextToken()));
					g.addMapped(Double.parseDouble(tok.nextToken()));
					String tmp=tok.nextToken();
					g.addMin(Integer.parseInt(tmp.substring(0,tmp.indexOf("|"))));
					g.addMax(Integer.parseInt(tmp.substring(tmp.indexOf("|")+1,tmp.length())));
					System.out.println("1: "+val);
					if(g.getCoverage()>=seuil_cov){
						genomes.add(g);
						System.out.println(val);
					}
						not_conserved.add(g);
				}
			}
		return genomes;
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	
		return(new LinkedList());
	}	
}

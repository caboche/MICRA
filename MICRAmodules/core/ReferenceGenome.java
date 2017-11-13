import java.io.*;
import java.util.*;

public class ReferenceGenome
{

	File repertoire;
	String rep;
	String reads;
	String project;
	String sep;
	int nbReads;

	Mapper mapper;
	PrintWriter error;

	int min_cov;
	double min_freq;
	int min_cov_dip;

	boolean cgview;
	String feature;

	public ReferenceGenome(){}

	public ReferenceGenome(String rep,String r,String project,String sep,int nbReads,Mapper mapper,PrintWriter error,boolean cgview,int min_cov,double min_freq,int min_cov_dip,String feature){
	
		repertoire=new File(rep);
	
		this.rep=rep;
	
		reads=r;
		this.project=project;
		this.sep=sep;
		this.nbReads=nbReads;
		this.mapper=mapper;
		this.error=error;
		this.cgview=cgview;
		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;
		this.feature=feature;
	}


	String reads1;
	String reads2;

	public ReferenceGenome(String rep,String r1,String r2,String project,String sep,int nbReads,Mapper mapper,PrintWriter error,boolean cgview,int min_cov,double min_freq,int min_cov_dip,String feature){
	
		repertoire=new File(rep);
	
		this.rep=rep;
	
		reads1=r1;
		reads2=r2;
		
		this.project=project;
		this.sep=sep;
		this.nbReads=nbReads;
		this.mapper=mapper;
		this.error=error;
		this.cgview=cgview;
		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;
		this.feature=feature;
	}



	public LinkedList run() throws IOException{	

		try{
			LinkedList <Genome> genomes= new LinkedList();
			String [] listFiles;
			PrintWriter out=new PrintWriter(new FileWriter(project+"fastMapping_stats.csv"));

			out.println("genome;size(bp);coverage(%);mean depth;sd;%mapped;min,max;#SNVs;#SNVs_CDS;#SNVs_change;#INDELs;#INDELs_CDS");
			listFiles=repertoire.list();
	
			for(int i=0;i<listFiles.length;i++){ 
				if(listFiles[i].endsWith(".fa")){
					String name=rep+sep+listFiles[i];
					String sortie=name.substring(name.lastIndexOf(sep)+1,name.lastIndexOf("."));
					System.out.println("running "+sortie);

					String s1_index=mapper.getIndex();
					if(!s1_index.equals("")){
						new MapperIndex(name,s1_index,error);
					}
					new MapperInterface(mapper.getRun(),name,reads, project+sortie+"_un.fastq", project+sortie+".sam",error);
				
				
					String gff=name.substring(0,name.lastIndexOf("."))+".gff";
					TraitSam a=new TraitSam(name,project+sortie+".sam",gff,project,sep,mapper.getMulti(),nbReads,cgview,min_cov,min_freq,min_cov_dip,feature);
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
					g.addSnp(Integer.parseInt(tok.nextToken()));
					g.addSnp_cds(Integer.parseInt(tok.nextToken()));
					g.addSnp_change(Integer.parseInt(tok.nextToken()));
					g.addDip(Integer.parseInt(tok.nextToken()));
					g.addDip_cds(Integer.parseInt(tok.nextToken()));
					out.println(val);
					genomes.add(g);
				}
			}
		out.close();
		return genomes;
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	
		return(new LinkedList());
	}


	public LinkedList run_paired() throws IOException{	

		try{
			LinkedList <Genome> genomes= new LinkedList();
			String [] listFiles;
			PrintWriter out=new PrintWriter(new FileWriter(project+"fastMapping_stats.csv"));

			out.println("genome;size(bp);coverage(%);mean depth;sd;%mapped;min,max;#SNVs;#SNVs_CDS;#SNVs_change;#INDELs;#INDELs_CDS");
			listFiles=repertoire.list();
	
			for(int i=0;i<listFiles.length;i++){ 
				if(listFiles[i].endsWith(".fa")){
					String name=rep+sep+listFiles[i];
					String sortie=name.substring(name.lastIndexOf(sep)+1,name.lastIndexOf("."));
					System.out.println("running "+sortie);

					String s1_index=mapper.getIndex();
					if(!s1_index.equals("")){
						new MapperIndex(name,s1_index,error);
					}
					new MapperInterface(mapper.getRun(),name,reads1,reads2, project+sortie+"_un.fastq", project+sortie+".sam",error);
				
					/* Parser sam */

					new ParserSam(project+sortie+".sam");

				
					String gff=name.substring(0,name.lastIndexOf("."))+".gff";
					TraitSam a=new TraitSam(name,project+sortie+".sam",gff,project,sep,mapper.getMulti(),nbReads,cgview,min_cov,min_freq,min_cov_dip,feature);
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
					g.addSnp(Integer.parseInt(tok.nextToken()));
					g.addSnp_cds(Integer.parseInt(tok.nextToken()));
					g.addSnp_change(Integer.parseInt(tok.nextToken()));
					g.addDip(Integer.parseInt(tok.nextToken()));
					g.addDip_cds(Integer.parseInt(tok.nextToken()));
					out.println(val);
					genomes.add(g);
				}
			}
		out.close();
		return genomes;
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	
		return(new LinkedList());
	}

}

import java.io.*;
import java.util.*;

public class InterestPlasmid2
{
	String rep;
	Genome p_ref;
	LinkedList <Genome> list;
	String project;
	String sep;
	Mapper mapper;
	PrintWriter error;
	PrintWriter out;
	int min_cov;
	double seuil_cov;
	String feature;

	public InterestPlasmid2(){}

	public InterestPlasmid2(String rep,LinkedList <Genome> l, Genome p_ref,String project,String sep,PrintWriter out,PrintWriter error,Mapper mapper,int min_cov,double seuil_cov,String feature){
		this.rep=rep;
		this.p_ref=p_ref;
		list=l;
		this.project=project;
		this.sep=sep;
		this.mapper=mapper;
		this.out=out;
		this.error=error;
		this.min_cov=min_cov;
		this.seuil_cov=seuil_cov;
		this.feature=feature;
	}


	public LinkedList run() throws IOException{	

		try{
		LinkedList <Genome> genomes= new LinkedList();
		String [] listefichiers;
			

		int nbReads=0;
		/* count number of reads in unmapped reference reads */
		File f_reads=new File(project+p_ref.getName()+"_un.fastq");
		if(f_reads.exists()){
			FastqToFastq fq1=new FastqToFastq(project+p_ref.getName()+"_un.fastq",project+"sego.fastq");
			nbReads=fq1.run();
			File f=new File(project+"sego.fastq");
			f.delete();
		}else{
			System.out.println("PAIRED-END unmap");
			FastqToFastq fq1=new FastqToFastq(project+p_ref.getName()+"_un.1.fastq",project+"sego.fastq");
			nbReads=fq1.run()*2;
			File f=new File(project+"sego.fastq");
			f.delete();
		}
	
	
		for(int i=0;i<list.size();i++){ 
			Genome genome=(Genome)list.get(i);
			String name=rep+sep+genome.getName()+".fa";
			String sortie=name.substring(name.lastIndexOf(sep)+1,name.lastIndexOf("."));
			String s1_index=mapper.getIndex();
			if(!s1_index.equals("")){
				new MapperIndex(name,s1_index,error);
			}

			if(f_reads.exists()){
				new MapperInterface(mapper.getRun(),name,project+p_ref.getName()+"_un.fastq", project+sortie+"_un.fastq", project+sortie+".sam",error);
			}else{
				new MapperInterface(mapper.getRun(),name,project+p_ref.getName()+"_un.1.fastq",project+p_ref.getName()+"_un.2.fastq", project+sortie+"_un.fastq", project+sortie+".sam",error);
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
			if(g.getCoverage()>=seuil_cov){
				genomes.add(g);
			}	
		}
		return genomes;
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	
		return(new LinkedList());
	}


	public int count(String filename) throws IOException { 
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
}

import java.io.*;
import java.util.*;

public class Plasmid{

	String rep;
	Genome ref;
	String project;
	String sep;
	PrintWriter out;
	PrintWriter error;
	Mapper mapper1;
	Mapper mapper2;
	boolean cgview;
	int min_cov;
	double min_freq;
	int min_cov_dip;
	String dust;

	LinkedList user_plasmids;

	LinkedList not_conserved;
	String feature;

	double seuil_cov;

	LinkedList plasmids;
	LinkedList plas_int;

	Plasmid(){}


	Plasmid(String rep,Genome ref,String project,String sep,PrintWriter out,PrintWriter error,Mapper mapper1,Mapper mapper2,boolean cgview,int min_cov,double min_freq,int min_cov_dip,String dust,double seuil_cov, LinkedList user_plasmids,String feature){

		this.rep=rep;
		this.ref=ref;
		this.project=project;
		this.sep=sep;
		this.out=out;
		this.error=error;
		this.mapper1=mapper1;
		this.mapper2=mapper2;
		this.cgview=cgview;
		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;
		this.dust=dust;
		this.seuil_cov=seuil_cov;
		this.user_plasmids=user_plasmids;
		this.feature=feature;
		plasmids=new LinkedList();
		not_conserved=new LinkedList();

		try{
			run();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}	
}

	void run()throws IOException{
		System.out.println("###########################################");
		System.out.println("STEP2: Computing statistics for plasmids");
		out.println("###########################################");
		out.println("STEP2: Computing statistics for plasmids");

		InterestPlasmid plas=new InterestPlasmid(rep,ref,project,sep,out,error,mapper1,min_cov,seuil_cov,feature);
		plasmids=plas.run();
		not_conserved=plas.not_conserved;
		System.out.println("not_conserved "+not_conserved);

		if(plasmids.size()==0){
			boolean todo=false;
			for(int i=0;i<not_conserved.size();i++){
				Genome gg=(Genome)not_conserved.get(i);
				System.out.println("ENTRY "+gg.getName()+" "+gg.getCoverage()+" "+gg.getMax());
				if(gg.getMax() > 0){
				System.out.println("ATTENTION "+gg.getName()+" "+gg.getCoverage());
					todo=false;
					break;	
				}		
			}
			if(todo){

			File ref_file=new File(project+"plasmidList.html");
				if(ref_file.exists()){
					BufferedReader input = new BufferedReader(new FileReader(project+"plasmidList.html"));
					String l="";
					while ((l = input.readLine()) != null) {
						if(l.startsWith("<li>accession:")){
							String tmp_line=l.substring(l.lastIndexOf("</a>")+6,l.indexOf("read matches"));
							String name_tmp=tmp_line.substring(1,tmp_line.lastIndexOf(" - "));
							String nb_tmp=tmp_line.substring(tmp_line.lastIndexOf(" - ")+3,tmp_line.length()-1);
							int hit=Integer.parseInt(nb_tmp);
							if(hit>=30){
								plasmids.add(getGenome(name_tmp,not_conserved));
								not_conserved.remove(getGenome(name_tmp,not_conserved));
							}
			
						}
					}
					input.close();	
				}
			}
		}


		System.out.println("Plasmids before filtering step: ");
		out.println("Plasmids before filtering step: ");
		for(int i=0;i<plasmids.size();i++){
			Genome p=(Genome)plasmids.get(i);
			System.out.print(p.getName()+";");
			out.print(p.getName()+";");
		}


		System.out.println("");
		out.println("");

		System.out.println("Filtering step...");

		plas_int=new LinkedList();
		while(plasmids.size()!=0){

			Genome pr=getMax(plasmids);
			String s1_index=mapper2.getIndex();
			if(!s1_index.equals("")){
				new MapperIndex(rep+sep+pr.getName()+".fa",s1_index,error);
			}
			File f_reads=new File(project+ref.getName()+"_un.fastq");
			if(f_reads.exists()){
				new MapperInterface(mapper2.getRun(),rep+sep+pr.getName()+".fa",project+ref.getName()+"_un.fastq", project+pr.getName()+"_un.fastq", project+pr.getName()+".sam",error);
			}else{
				new MapperInterface(mapper2.getRun(),rep+sep+pr.getName()+".fa",project+ref.getName()+"_un.1.fastq",project+ref.getName()+"_un.2.fastq", project+pr.getName()+"_un.fastq", project+pr.getName()+".sam",error);
			}
			plas_int.add(pr);
			plasmids.remove(pr);

			if(f_reads.exists()){
				FastqToFastq fq1=new FastqToFastq(project+pr.getName()+"_un.fastq",project+"sego.fastq");
				fq1.run();
				File f1=new File(project+pr.getName()+"_un.fastq");
				f1.delete();
				File f=new File(project+"sego.fastq");
				f.renameTo(new File(project+pr.getName()+"_un.fastq"));
			}else{
				FastqToFastq fq1=new FastqToFastq(project+pr.getName()+"_un.1.fastq",project+"sego.fastq");
				FastqToFastq fq2=new FastqToFastq(project+pr.getName()+"_un.2.fastq",project+"sego2.fastq");
				fq1.run();
				fq2.run();
				File f1=new File(project+pr.getName()+"_un.1.fastq");
				File f2=new File(project+pr.getName()+"_un.2.fastq");
				f1.delete();
				f2.delete();
				File f=new File(project+"sego.fastq");
				File ff=new File(project+"sego2.fastq");
				f.renameTo(new File(project+pr.getName()+"_un.1.fastq"));
				ff.renameTo(new File(project+pr.getName()+"_un.2.fastq"));
			}

			InterestPlasmid2 ip=new InterestPlasmid2(rep,plasmids,pr,project,sep,out,error,mapper2,min_cov,seuil_cov,feature);
			plasmids=ip.run();
		}

		System.out.println("plasmids after filtering step");
		out.println("plasmids after filtering step");
		for(int i=0;i<plas_int.size();i++){
			Genome p=(Genome)plas_int.get(i);
			System.out.print(p.name+";");
			out.print(p.name+";");
		}
		System.out.println("");
		out.println("");

		/* on efface les fichiers plasmide sans interet */

		File repertoire=new File(rep);
		String [] listefic;
		listefic=repertoire.list();
		for(int i=0;i<listefic.length;i++){
			if(!isInside(listefic[i],new LinkedList())){	
			String text=listefic[i];
				File f1=new File(project+text.substring(0,text.lastIndexOf("."))+".sam");
				f1.delete();
				System.out.println("on supprime "+text);
				File fff=new File(project+text.substring(0,text.lastIndexOf("."))+"_un.fastq");
				if(fff.exists()){
					File f2=new File(project+text.substring(0,text.lastIndexOf("."))+"_un.fastq");
					f2.delete();
				}else{
					File f2=new File(project+text.substring(0,text.lastIndexOf("."))+"_un.1.fastq");
					f2.delete();
					f2=new File(project+text.substring(0,text.lastIndexOf("."))+"_un.2.fastq");
					f2.delete();
					f2=new File(project+text.substring(0,text.lastIndexOf("."))+".samORIGINAL");
					f2.delete();
				}
			}
		}

	
		File f_tmp=new File(project+ref.getName()+"_un.fastq");

		/* number of reads in ref_unmapped*/
		int nb1=0;
		FastqToFastq fq1;
		File f;

		if(f_tmp.exists()){
			fq1=new FastqToFastq(project+ref.getName()+"_un.fastq",project+"sego.fastq");
			nb1=fq1.run();
			f=new File(project+"sego.fastq");
			f.delete();
		}else{
			fq1=new FastqToFastq(project+ref.getName()+"_un.1.fastq",project+"sego.fastq");
			nb1=(fq1.run())*2;
			f=new File(project+"sego.fastq");
			f.delete();
		}


		/* potential adding of user plasmids */

		if(user_plasmids.size()!=0){
			for(int i=0;i<user_plasmids.size();i++){
				String s=(String)user_plasmids.get(i);
				if(!isInside2(s,plas_int)){
					plas_int.add(getGenome(s,not_conserved));
				}
			}
		}



		/* third phase */
		System.out.println("computing snp and genes for identified plasmids");
		out.println("computing snp and genes for identified plasmids");
		PrintWriter outt=new PrintWriter(new FileWriter(project+"plasmids"+sep+"PLASMIDS.csv"));
		outt.println("PLASMID;size(bp);coverage(%);mean depth;sd;%mapped;min,max;#SNVs;#SNVs_CDS;#SNVs_change;#INDELs;#INDELs_CDS");

		for(int i=0;i<plas_int.size();i++){
			Genome p=(Genome)plas_int.get(i);

			/* lowComplexity*/
			DUSTInterface di=new DUSTInterface(p,rep+sep+p.getName()+".fa",dust,error);
			if(di.getStatus()){
			}else{
				System.out.println("problem with DUST: low complexity regions could not be identified in gene detection step");
				System.out.println("Please check the DUST command line in configuration file (see error.txt file)");
				out.println("problem with DUST: low complexity regions could not be identified in gene detection step");
				out.println("Please check the DUST command line in configuration file (see error.txt file)");
	
			}
			String s2_index=mapper2.getIndex();
			TraitSam a;

			if(!s2_index.equals("")){
				new MapperIndex(rep+sep+p.getName()+".fa",s2_index,error);
			}
			if(f_tmp.exists()){
				new MapperInterface(mapper2.getRun(),rep+sep+p.getName()+".fa",project+ref.getName()+"_un.fastq", project+"plasmids"+sep+p.getName()+"_un.fastq", project+"plasmids"+sep+p.getName()+".sam",error);
				fq1=new FastqToFastq(project+"plasmids"+sep+p.getName()+"_un.fastq",project+"sego.fastq");
				fq1.run();
				File f1=new File(project+"plasmids"+sep+p.getName()+"_un.fastq");
				f1.delete();
				f=new File(project+"sego.fastq");
				f.renameTo(new File(project+"plasmids"+sep+p.getName()+"_un.fastq"));
				a=new TraitSam(rep+sep+p.getName()+".fa",project+"plasmids"+sep+p.getName()+".sam",rep+sep+p.getName()+".gff",p,project,sep,mapper2.getMulti(),nb1,cgview,min_cov,min_freq,min_cov_dip,true,feature);
			}else{
				new MapperInterface(mapper2.getRun(),rep+sep+p.getName()+".fa",project+ref.getName()+"_un.1.fastq",project+ref.getName()+"_un.2.fastq",project+"plasmids"+sep+p.getName()+"_un.fastq", project+"plasmids"+sep+p.getName()+".sam",error);
				fq1=new FastqToFastq(project+"plasmids"+sep+p.getName()+"_un.1.fastq",project+"sego.fastq");
				fq1.run();
				File f1=new File(project+"plasmids"+sep+p.getName()+"_un.1.fastq");
				f1.delete();
				f=new File(project+"sego.fastq");
				f.renameTo(new File(project+"plasmids"+sep+p.getName()+"_un.1.fastq"));
	
				fq1=new FastqToFastq(project+"plasmids"+sep+p.getName()+"_un.2.fastq",project+"sego.fastq");
				fq1.run();
				f1=new File(project+"plasmids"+sep+p.getName()+"_un.2.fastq");
				f1.delete();
				f=new File(project+"sego.fastq");
				f.renameTo(new File(project+"plasmids"+sep+p.getName()+"_un.2.fastq"));

				new ParserSam(project+"plasmids"+sep+p.getName()+".sam");
				a=new TraitSam(rep+sep+p.getName()+".fa",project+"plasmids"+sep+p.getName()+".sam",rep+sep+p.getName()+".gff",p,project,sep,mapper2.getMulti(),nb1,cgview,min_cov,min_freq,min_cov_dip,true,feature);
			}

			/* value update */

			String val=a.getValue();
			StringTokenizer tok=new StringTokenizer(val,";");
			tok.nextToken();
			p.addSize(Integer.parseInt(tok.nextToken()));
			p.addCoverage(Double.parseDouble(tok.nextToken()));
			p.addDepth(Double.parseDouble(tok.nextToken()));
			p.addSd(Double.parseDouble(tok.nextToken()));
			p.addMapped(Double.parseDouble(tok.nextToken()));
			String tmp=tok.nextToken();
			p.addMin(Integer.parseInt(tmp.substring(0,tmp.indexOf("|"))));
			p.addMax(Integer.parseInt(tmp.substring(tmp.indexOf("|")+1,tmp.length())));
					
			outt.println(a.getValue());
			System.out.println("*"+p.name+": coverage="+p.coverage+" mean depth="+p.depth);
			out.println("*"+p.name+": coverage="+p.coverage+" mean depth="+p.depth);
		}
		outt.close();

	}


	Genome getMax(LinkedList <Genome> l){
		Genome plasmid_ref=null;
		double maxcov=-1.0;

		for(int i=0;i<l.size();i++){
			Genome p=(Genome)l.get(i);
			if(p.getCoverage()>maxcov){
				plasmid_ref=p;
				maxcov=p.getCoverage();
			}
		}
		return plasmid_ref;
	}

	boolean isInside2(String name, LinkedList l){
		for(int i=0;i<l.size();i++) {
			Genome g=(Genome)l.get(i);
			if (name.equals(g.getName())) return true;	
		}
		return false;
	}


	boolean isInside(String fichier, LinkedList l){

		String text=fichier.substring(0,fichier.lastIndexOf("."));

		for(int i=0;i<l.size();i++) {
			Genome g=(Genome)l.get(i);
			if (text.equals(g.getName())) return true;	

		}
		return false;
	}

	Genome getGenome(String s,LinkedList l){
		for(int i=0;i<l.size();i++){
			Genome g=(Genome)l.get(i);
			if(s.equals(g.getName()))return g;
		}
		return null;
	}


	LinkedList getPlasmidList(){
		return plas_int;
	}


}

import java.io.*;
import java.util.*;
import java.text.*; 


public class TraitSam{


	String fasta;
	String sam;
	String gff;
	int tailleGenome;
	Cell [] tab;
	int nb_reads;
	int total_reads;
	LinkedList annotations;
	int snp_tot;
	int snp_cds;
	int snp_change;
	int dip;
	int dip_cds;
	String value;
	String project;
	String sep;
	String feature;
	int window=50;
	int min_contig_size=50;
	boolean plasmid=false;
	String mapperCommand;
	Genome genome;
	int wordSize;
	String name;
	boolean cgview;
	boolean multimap;
	int min_cov;
	double min_freq;
	int min_cov_dip;

	public TraitSam(){}

	/* genes +SNP */
	public TraitSam(String fasta,String sam,String gff,Genome genome,String project,String sep,boolean multimap,int nbreads,boolean cgview,int min_cov,double min_freq,int min_cov_dip,String feature){
	
		this.fasta=fasta;
		this.sam=sam;
		this.gff=gff;
		this.project=project;
		this.sep=sep;
		this.genome=genome;
		this.cgview=cgview;
		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;
		this.feature=feature;
	
		value="";
		this.multimap=multimap;
		annotations=new LinkedList();
		mapperCommand="";

		snp_tot=0;
		snp_cds=0;
		snp_change=0;
		dip=0;
		dip_cds=0;

		total_reads=nbreads;
		wordSize=22;

		name=fasta.substring((fasta.lastIndexOf(sep)+1),fasta.lastIndexOf("."));

		try{setTaille();
		}catch (IOException e) {
	       		System.out.println("ERROR: get Taille");
		} 
		try{getMapperCommand();
		}catch (IOException e) {
	       		System.out.println("ERROR: mapper command");
		} 
		tab=new Cell[tailleGenome+1];
		tab[0]=new Cell('_');


		try{init();}
		catch (IOException e) {
			System.out.println("ERROR: initialisation");
		} 

		try{
			if(multimap){
				trait();
			}else{
				traitEasy();
			}
		}
		catch (IOException e) {
	       	 System.out.println("ERROR: sam file");
		} 

	
		/* SNP*/
		try{setAnnot();}
		catch (IOException e) {
	       		System.out.println("ERROR: gff file");
		} 



		try{
			value=SNP();
		}
		catch (IOException e) {
	     	   System.out.println("ERROR: SNP");
		} 
	
		try{getGenes();}
		catch (IOException e) {
	      	  System.out.println("ERROR: getting list of genes");
		} 


		try{
	
		getContigs();
		}
		catch (IOException e) {
	      	  System.out.println("ERROR: getting contigs");
		} 

	}

	/* genes +SNP PLASMIDS*/
	public TraitSam(String fasta,String sam,String gff,Genome genome,String project,String sep,boolean multimap,int nbreads,boolean cgview,int min_cov,double min_freq,int min_cov_dip,boolean plasmid,String feature){

		this.fasta=fasta;
		this.sam=sam;
		this.gff=gff;
		this.project=project;
		this.sep=sep;
		this.genome=genome;
		this.cgview=cgview;
		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;
		this.plasmid=plasmid;
		this.feature=feature;
	
		value="";
		this.multimap=multimap;
		annotations=new LinkedList();
		mapperCommand="";

		snp_tot=0;
		snp_cds=0;
		snp_change=0;
		dip=0;
		dip_cds=0;

		total_reads=nbreads;

		wordSize=22;

		name=fasta.substring((fasta.lastIndexOf(sep)+1),fasta.lastIndexOf("."));

		try{setTaille();
		}catch (IOException e) {
	       		System.out.println("ERROR: get Taille");
		} 
		try{getMapperCommand();
		}catch (IOException e) {
	       		System.out.println("ERROR: mapper command");
		} 
		tab=new Cell[tailleGenome+1];
		tab[0]=new Cell('_');


		try{init();}
		catch (IOException e) {
			System.out.println("ERROR: initialisation");
		}

		try{
			if(multimap){
				trait();
			}else{
				traitEasy();
			}
		}
		catch (IOException e) {
	       	 System.out.println("ERROR: sam file");
		} 

		/* SNP*/
		try{setAnnot();}
		catch (IOException e) {
	       		System.out.println("ERROR: gff file");
		} 

		try{

			value=SNP();
		}
		catch (IOException e) {
	     	   System.out.println("ERROR: SNP");
		} 
	
		try{getGenes();}
		catch (IOException e) {
	      	  System.out.println("ERROR: getting list of genes");
		} 

		/* plamid contigs */
		try{
	
		getContigs();
		}
		catch (IOException e) {
	      	  System.out.println("ERROR: getting contigs");
		} 
	}

	/* genes uses in Phylo.javaif the mapper is the same */
	public TraitSam(String fasta,String sam,String gff,Genome genome,String project,String sep,boolean multimap,boolean cgview,int min_cov,double min_freq,int min_cov_dip,String feature){

		this.fasta=fasta;
		this.sam=sam;
		this.gff=gff;
		this.project=project;
		this.sep=sep;
		this.genome=genome;
		this.cgview=cgview;
		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;
		this.feature=feature;
	
		value="";
		this.multimap=multimap;
		annotations=new LinkedList();
		mapperCommand="";

		snp_tot=0;
		snp_cds=0;
		snp_change=0;
		dip=0;
		dip_cds=0;

		wordSize=22;

		name=fasta.substring((fasta.lastIndexOf(sep)+1),fasta.lastIndexOf("."));

		try{setTaille();
		}catch (IOException e) {
	       		System.out.println("ERROR: fasta file");
		} 
		try{getMapperCommand();
		}catch (IOException e) {
	       		System.out.println("ERROR: mapper command");
		} 
		tab=new Cell[tailleGenome+1];
		tab[0]=new Cell('_');


		try{init();}
		catch (IOException e) {
			System.out.println("ERROR: initialisation");
		} 

		try{
			if(multimap){
				trait();
			}else{
				traitEasy();
			}
		}
		catch (IOException e) {
	       	 System.out.println("ERROR: sam file");
		} 

		try{getGenes();}
		catch (IOException e) {
	      	  System.out.println("ERROR: getting list of genes");
		} 
	}

	PrintWriter out;
	PrintWriter out3;

	/* genes iteratif */

	public TraitSam(String fasta,String sam,String gff,Genome genome,String project,String sep,boolean multimap,int min_cov,double min_freq,int min_cov_dip,PrintWriter out,PrintWriter out3,PrintWriter out4,boolean plasmid,String feature){
	
		this.fasta=fasta;
		this.sam=sam;
		this.gff=gff;
		this.project=project;
		this.sep=sep;
		this.genome=genome;
		this.cgview=cgview;
		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;
		this.out=out;
		this.out3=out3;
		this.plasmid=plasmid;
		this.feature=feature;
	
		value="";
		this.multimap=multimap;
		annotations=new LinkedList();
		mapperCommand="";

		snp_tot=0;
		snp_cds=0;
		snp_change=0;
		dip=0;
		dip_cds=0;

		wordSize=22;

		name=fasta.substring((fasta.lastIndexOf(sep)+1),fasta.lastIndexOf("."));

		try{setTaille();
		}catch (IOException e) {
	       		System.out.println("ERROR: fasta file");
		} 

		if(tailleGenome!=0){
			try{getMapperCommand();
			}catch (IOException e) {
		       		System.out.println("ERROR: mapper command");
			} 

			tab=new Cell[tailleGenome+1];
			tab[0]=new Cell('_');


			try{init();}
			catch (IOException e) {
				System.out.println("ERROR: initialisation");
			} 
			try{
				if(multimap){
					trait();
				}else{
					traitEasy();
				}
			}
			catch (IOException e) {
		       	 System.out.println("ERROR: sam file");
			} 

			value=""+nb_reads;

			try{getGenesIteratif();}
			catch (IOException e) {
		      	  System.out.println("ERROR: getting iterative list of genes");
			} 


			try{getContigs(out4);}
			catch (IOException e) {
		      	  System.out.println("ERROR: getting iterative list of contigs");
			} 

		}

	}



	/* SNP */
	public TraitSam(String fasta,String sam,String gff,String project,String sep,boolean multimap,int nbreads,boolean cgview,int min_cov,double min_freq,int min_cov_dip,String feature){
	
		this.fasta=fasta;
		this.sam=sam;
		this.gff=gff;
		this.project=project;
		this.sep=sep;
		this.genome=genome;
		this.cgview=cgview;
		this.min_cov=min_cov;
		this.min_freq=min_freq;
		this.min_cov_dip=min_cov_dip;
		this.feature=feature;
	
		value="";
		this.multimap=multimap;
		annotations=new LinkedList();
		mapperCommand="";

		snp_tot=0;
		snp_cds=0;
		snp_change=0;
		dip=0;
		dip_cds=0;

		total_reads=nbreads;

		wordSize=22;

		name=fasta.substring((fasta.lastIndexOf(sep)+1),fasta.lastIndexOf("."));

		try{setTaille();
		}catch (IOException e) {
	       		System.out.println("ERROR: fasta file");
		} 
		try{getMapperCommand();
		}catch (IOException e) {
	       		System.out.println("ERROR: mapper command");
		} 
		tab=new Cell[tailleGenome+1];
		tab[0]=new Cell('_');

	
		try{init();}
		catch (IOException e) {
			System.out.println("ERROR: initialisation");
		} 
		try{
			if(multimap){
				trait();
			}else{
				traitEasy();
			}
		}
		catch (IOException e) {
	       	 System.out.println("ERROR: sam file");
		} 
	
		/* SNP*/
		try{setAnnot();}
		catch (IOException e) {
	       		System.out.println("ERROR: gff file");
		} 

		try{

			value=SNP();
		}
		catch (IOException e) {
	     	   System.out.println("ERROR: SNP");
		} 
	}


	/* stat */
	public TraitSam(String fasta,String sam,String project,String sep,boolean multimap,int nbreads,int min_cov,String feature){

		this.fasta=fasta;
		this.sam=sam;
		this.project=project;
		this.sep=sep;
		this.genome=genome;
		this.min_cov=min_cov;	
		this.feature=feature;
	
		value="";
		this.multimap=multimap;
		annotations=new LinkedList();
		mapperCommand="";
		total_reads=nbreads;


		name=fasta.substring((fasta.lastIndexOf(sep)+1),fasta.lastIndexOf("."));

		try{setTaille();
		}catch (IOException e) {
	       		System.out.println("ERROR: fasta file");
		} 
		try{getMapperCommand();
		}catch (IOException e) {
	       		System.out.println("ERROR: mapper command");
		} 
		tab=new Cell[tailleGenome+1];
		tab[0]=new Cell('_');


		try{init();}
		catch (IOException e) {
			System.out.println("ERROR: initialisation");
		} 

		try{
			if(multimap){
				trait();
			}else{
				traitEasy();
			}
		}
		catch (IOException e) {
	       	 System.out.println("ERROR: sam file");
		} 

		try{
			value=stat();
		}
		catch (IOException e) {
	     	   System.out.println("ERROR: SNP");
		} 
	}


	String getValue(){
		return value;
	}




	/* to get the genome size */
	void setTaille() throws IOException {
		
		BufferedReader input = new BufferedReader(new FileReader(sam));

		String l="";
		String [] seq;
		
		while ((l = input.readLine()) != null) {
				if (l.indexOf("@SQ")!=-1){
					seq=l.split("\t");
					String tmp=seq[2].substring((seq[2].indexOf(":")+1),seq[2].length());
					tailleGenome=Integer.parseInt(tmp);
					break;

				}
		}
		input.close();
		System.out.println("TAILLE "+sam+" "+tailleGenome);
	}

	/* to get the genome size */
	void getMapperCommand() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(sam));

		String l="";
		String [] seq;
		
		while ((l = input.readLine()) != null) {
			if (l.indexOf("@PG")!=-1){
				seq=l.split("\t");
				for(int u=0;u<seq.length;u++){
					if(seq[u].indexOf("ID:")!=-1){
						String tmp=seq[u].substring((seq[u].indexOf(":")+1),seq[u].length());
						mapperCommand=mapperCommand+"mapper: "+tmp;
					}
					if(seq[u].indexOf("CL:")!=-1){
						String tmp=seq[u].substring((seq[u].indexOf(":")+1),seq[u].length());
						mapperCommand=mapperCommand+" command line "+tmp;
					}
				}
				break;

				}
		}
		input.close();
	}

	/* to init the array with the reference sequence */
	void init() throws IOException {

		BufferedReader input = new BufferedReader(new FileReader(fasta));
		System.out.println("fasta "+fasta+" "+tab.length);
		input.readLine();
		String l="";
		int ct=1;
		while ((l = input.readLine()) != null) {

			for(int i=0;i<l.length();i++){
				tab[ct]=new Cell(l.charAt(i));
				ct++;
			}
		}

		input.close();
	}



	/* read traitment */
	void trait() throws IOException {
	
		BufferedReader input = new BufferedReader(new FileReader(sam));
		String l="";
		String [] line;
		int pos;
		String cigar;
		String seq;
		String quality;

		HashMap reads = new HashMap();

		while ((l = input.readLine()) != null) {
			if(l.charAt(0)!='@'){
		
				line=l.split("\t");
				if(!line[1].equals("4")){

					if(reads.get(line[0])==null){
						reads.put(line[0],1);
					}else{
						int ch=(Integer)reads.get(line[0]);
						ch=ch+1;
						reads.put(line[0],ch);
					}
				}

			}
		}
		input.close();

		nb_reads=reads.size();

		input = new BufferedReader(new FileReader(sam));
		while ((l = input.readLine()) != null) {
			if(l.charAt(0)!='@'){
		
				line=l.split("\t");
				pos=Integer.parseInt(line[3]);
				cigar=line[5];
				seq=line[9];
				if(line.length>10){
					quality=line[10];
				}else{System.out.println(l);
					quality="";}
		

				if(reads.get(line[0])==null){
				}else{

						int ch=(Integer)reads.get(line[0]);
						if(ch==0){
						}
						Integer tt=new Integer(ch);
						double tot=tt.doubleValue();
						update(pos,cigar,seq,(1.0/(tot)),quality);
				
				}

			}
		}
		input.close();
	}




	/* no hashMap creation - no multimap reads */
	void traitEasy() throws IOException {

		BufferedReader input = new BufferedReader(new FileReader(sam));

		String l="";
		String [] line;

		int pos;
		String cigar;
		String seq;
		String quality;

		int c=0;

		while ((l = input.readLine()) != null) {
			if(l.charAt(0)!='@'){
		
				line=l.split("\t");
				if(!line[4].equals("4")){
				pos=Integer.parseInt(line[3]);
				cigar=line[5];
				seq=line[9];
				quality=line[10];
				if(getSizeCigar(cigar)==seq.length()){
					update(pos,cigar,seq,1.0,quality);
				}else{
					System.out.println("PROBLEM CIGAR "+pos+" "+cigar+" "+seq);
				}
				c++;
				}
			}
		}
		input.close();

		nb_reads=c;
	}


	int getSizeCigar(String s){
		int res=0;
			for(int i=0;i<s.length();i++) {

				if ((s.charAt(i)=='M')||(s.charAt(i)=='I')||(s.charAt(i)=='S')||(s.charAt(i)=='=')||(s.charAt(i)=='X')){
					int n=nombre(i,s);
					res=res+n;
				}
			}
		return res;
	}

	void update(int pos,String s,String seq,double nb,String qual){

		int c_seq=0;
		int c_gen=pos;
		/* 
		0 false
		1 snp
		2 autre
		*/
		int is_mut=0;
		String mut="";
		int p=pos;
		double qual_mut=0;

		for(int i=0;i<s.length();i++) {
			if (s.charAt(i)=='M'){
				int n=nombre(i,s);
			
				for(int j=0;j<n;j++){
				
					tab[c_gen].addRead(nb);
					tab[c_gen].addRead2(nb);
					if(c_seq==(seq.length()-1)){tab[c_gen].addEnd(nb);}
					if(baseSim(tab[c_gen].base,seq.charAt(c_seq))){

						tab[c_gen].addDefaut(((int)qual.charAt(c_seq)-33)*nb);
						if(is_mut!=0){
							Mut mutation=new Mut(nb,mut);
							tab[p].addMut(mutation,qual_mut);
						}
						p=c_gen;
						mut=""+seq.charAt(c_seq);
						qual_mut=((int)qual.charAt(c_seq)-33)*nb;
						is_mut=0;
					
					}else{
						if(is_mut==0){
							p=c_gen;
							mut=""+seq.charAt(c_seq);
							qual_mut=((int)qual.charAt(c_seq)-33)*nb;
						}else{
							if(is_mut==1){
								Mut mutation=new Mut(nb,mut);
								tab[p].addMut(mutation,qual_mut);
								p=c_gen;
								mut=""+seq.charAt(c_seq);
								qual_mut=((int)qual.charAt(c_seq)-33)*nb;
							}
							else{
							mut=mut+seq.charAt(c_seq);
							qual_mut=qual_mut+((int)qual.charAt(c_seq)-33)*nb;

							}
						}
					
					is_mut=1;
					}


					c_seq++;
					c_gen++;

					if(c_gen>tailleGenome){
					return;}
				}
		
			}
			if (s.charAt(i)=='I'){
				
				int n=nombre(i,s);

				for(int j=0;j<n;j++){
					String tmp=""+seq.charAt(c_seq);
					tmp=tmp.toLowerCase();
					mut=mut+tmp;
					qual_mut=qual_mut+((int)qual.charAt(c_seq)-33)*nb;
					is_mut=2;
					c_seq++;
				}	
			}

			if (s.charAt(i)=='D'){
				int n=nombre(i,s);
				tab[c_gen].addRead2(nb);
				for(int j=0;j<n;j++){
					mut=mut+"*";
					is_mut=2;

					c_gen++;
					if(c_gen>tailleGenome){
					return;}
				}
			}
		
			if (s.charAt(i)=='='){
				int n=nombre(i,s);
			
				for(int j=0;j<n;j++){
					tab[c_gen].addRead(nb);
					tab[c_gen].addRead2(nb);
					tab[c_gen].addDefaut(((int)qual.charAt(c_seq)-33)*nb);

					if(is_mut!=0){
							Mut mutation=new Mut(nb,mut);
							tab[p].addMut(mutation,qual_mut);
						}
							p=c_gen;
							mut=""+seq.charAt(c_seq);
							qual_mut=((int)qual.charAt(c_seq)-33)*nb;
							is_mut=0;
					c_seq++;
					c_gen++;
					if(c_gen>tailleGenome){
					return;}
				}
			
			}
			if (s.charAt(i)=='X'){
				int n=nombre(i,s);
			
				for(int j=0;j<n;j++){
					tab[c_gen].addRead(nb);
					tab[c_gen].addRead2(nb);
					if(is_mut==0){
							p=c_gen;
							mut=""+seq.charAt(c_seq);
							qual_mut=((int)qual.charAt(c_seq)-33)*nb;
						}else{
							if(is_mut==1){
								Mut mutation=new Mut(nb,mut);
								tab[p].addMut(mutation,qual_mut);
								p=c_gen;
								mut=""+seq.charAt(c_seq);
								qual_mut=((int)qual.charAt(c_seq)-33)*nb;
							}
							else{
							mut=mut+seq.charAt(c_seq);
							qual_mut=qual_mut+((int)qual.charAt(c_seq)-33)*nb;
							}
						}
						is_mut=1;
					c_seq++;
					c_gen++;
					if(c_gen>tailleGenome){
						return;}
				}
			
			}

			if (s.charAt(i)=='S'){
				int n=nombre(i,s);
			
				for(int j=0;j<n;j++){
					c_seq++;
				}
			}

			if (s.charAt(i)=='H'){
				/* nothing*/
			}

			if (s.charAt(i)=='P'){
				/* nothing*/
			}
			if (s.charAt(i)=='N'){
				/* nothing*/
			}
		}


		if(is_mut!=0){
			Mut mutation=new Mut(nb,mut);
			tab[p].addMut(mutation,qual_mut);
		}
	}

	/* a= ref genome base */
	boolean baseSim(char a, char b){
	if((b=='N')||(b=='n'))return true;
	switch(a){
			case 'A': if((b=='A')||(b=='a'))return true;else return false;
			case 'a': if((b=='a')||(b=='A'))return true;else return false;
			case 'T': if((b=='T')||(b=='t'))return true;else return false;
			case 't': if((b=='t')||(b=='T'))return true;else return false;
			case 'G': if((b=='G')||(b=='g'))return true;else return false;
			case 'g': if((b=='g')||(b=='G'))return true;else return false;
			case 'C': if((b=='C')||(b=='c'))return true;else return false;
			case 'c': if((b=='c')||(b=='C'))return true;else return false;
			case 'N': return true;
			case 'n': return true;
			case 'R': if((b=='A')||(b=='a')||(b=='G')||(b=='g'))return true;else return false;
			case 'r': if((b=='A')||(b=='a')||(b=='G')||(b=='g'))return true;else return false;
			case 'Y': if((b=='C')||(b=='c')||(b=='T')||(b=='t'))return true;else return false;
			case 'y': if((b=='C')||(b=='c')||(b=='T')||(b=='t'))return true;else return false;
			case 'M': if((b=='A')||(b=='a')||(b=='C')||(b=='c'))return true;else return false;
			case 'm': if((b=='A')||(b=='a')||(b=='C')||(b=='c'))return true;else return false;
			case 'K': if((b=='T')||(b=='t')||(b=='G')||(b=='g'))return true;else return false;
			case 'k': if((b=='T')||(b=='t')||(b=='G')||(b=='g'))return true;else return false;
			case 'S': if((b=='C')||(b=='c')||(b=='G')||(b=='g'))return true;else return false;
			case 's': if((b=='C')||(b=='c')||(b=='G')||(b=='g'))return true;else return false;
			case 'W': if((b=='A')||(b=='a')||(b=='T')||(b=='t'))return true;else return false;
			case 'w': if((b=='A')||(b=='a')||(b=='T')||(b=='t'))return true;else return false;
			case 'B': if((b=='C')||(b=='c')||(b=='G')||(b=='g')||(b=='T')||(b=='t'))return true;else return false;
			case 'b': if((b=='C')||(b=='c')||(b=='G')||(b=='g')||(b=='T')||(b=='t'))return true;else return false;
			case 'D': if((b=='A')||(b=='a')||(b=='G')||(b=='g')||(b=='T')||(b=='t'))return true;else return false;
			case 'd': if((b=='A')||(b=='a')||(b=='G')||(b=='g')||(b=='T')||(b=='t'))return true;else return false;
			case 'H': if((b=='C')||(b=='c')||(b=='A')||(b=='a')||(b=='T')||(b=='t'))return true;else return false;
			case 'h': if((b=='C')||(b=='c')||(b=='A')||(b=='a')||(b=='T')||(b=='t'))return true;else return false;
			case 'V': if((b=='C')||(b=='c')||(b=='G')||(b=='g')||(b=='A')||(b=='a'))return true;else return false;
			case 'v': if((b=='C')||(b=='c')||(b=='G')||(b=='g')||(b=='A')||(b=='a'))return true;else return false;
		}

	return false;
	}


	void getGenesIteratif() throws IOException {
	/* gff */
		BufferedReader in = new BufferedReader(new FileReader(gff));
		createMut();
		out.println("## "+name);

		String line="";
		String [] seq2;
		int deb=0;
		int fin=0;
		int id=1;
		
		Couple succ=new Couple();
		while ((line = in.readLine()) != null) {
			if (line.charAt(0)!='#'){
				seq2=line.split("\t");
				
				
				if((!seq2[2].equals("source"))&& (!seq2[2].equals("stop_codon"))&&(!seq2[2].equals("start_codon"))){
					deb=Integer.parseInt(seq2[3]);
					fin=Integer.parseInt(seq2[4]);

					String res="";

					double r=-1.0;
					if(seq2[2].equals(feature)){
					if((deb<=tailleGenome)&&(deb>=0)&&(fin<=tailleGenome)&&(fin>=0)){
					r= getStatut(deb,fin);
					}
					if(r!=-1){
					if(r<10) {	
							
					}
					else{
						if(r<80) {
							
						}
						else {
								res=res+deb+";"+fin+";"+seq2[6]+";";
								res=res+r+";";
								String res_bis=""+res;
								/* annotation */
								if(seq2.length==9){
									String ttt=seq2[8];
									ttt=ttt.replaceAll(";","&");
									res=res+ttt;
								}
					
					String [] parse=res.split(";");
					if(parse.length>4){
							String [] annot=parse[4].split("&");
							String locus="";
							String product="";
							String name="";
							for(int u=0;u<annot.length;u++){
								

								if(annot[u].indexOf("Name=")!=-1){
									name=annot[u].substring(5,annot[u].length());
								}
								if(annot[u].indexOf("product=")!=-1){
									product=annot[u].substring(8,annot[u].length());
								}
								if(annot[u].indexOf("locus_tag=")!=-1){
									locus=annot[u].substring(10,annot[u].length());
								}
							}
					
							String st="";
							if(!locus.equals(""))st=st+"locus="+locus+",";
							if(!name.equals(""))st=st+"name="+name+",";
							if(!product.equals(""))st=st+"product="+product;
							if(!st.equals("")){
								out.println(res_bis+st);
							}else{out.println(res);}
						}

						DecimalFormat f = new DecimalFormat("##.00");
						out3.println("> "+name+" "+parse[0]+"-"+parse[1]+" ("+parse[2]+") "+" cov:"+f.format(Double.parseDouble(parse[3]))+"% "+" ");	
						getConsensus(Integer.parseInt(parse[0]),Integer.parseInt(parse[1]),out3);
						out3.println("");
						id++;
					}}

					

					}
					}
				}

			}

		}
		
		in.close();	
	}


	boolean getLowComplexity(int deb,int fin,double rr){
		double somme=0;
		for(int i=deb;i<=fin;i++){
			if (genome.lowComplexity[i-1]==1){
				somme=somme+1;
			}
		}
		double r=somme/(fin-deb+1)*100.0;
		if (r > ((100-rr)-10)) return true;
		else return false;	
	}



	int getIndex(char a){
		if(a=='A') return 0;
		if(a=='T') return 1;
		if(a=='G') return 2;
		if(a=='C') return 3;
		if(a=='a') return 0;
		if(a=='t') return 1;
		if(a=='g') return 2;
		if(a=='c') return 3;
		if(a=='*') return 4;
		return -1;
	}

	double getStatut(int deb,int fin){

		double somme=0;
	
		for(int i=deb;i<=fin;i++){

		Cell c=tab[i];
		double p=c.getProf();

		

			if ((p>=min_cov)){
				somme=somme+1;
			}

		}
		double r=somme/(fin-deb+1)*100.0;
		return r;
		
	}

	int getStatut(int i){
		Cell c=tab[i];
		double p=c.getProf();


		if ((p>=min_cov)){
			return 1;
		}
		return 0;
	}

	String getChar(int n){
		if (n==0) return "A";
		if (n==1) return "T";
		if (n==2) return "G";
		if (n==3) return "C";
		if (n==4) return "*";
		return "";
	}

	
	Couple getSucc(int deb,int fin){
		double somme=0;
		int nb=0;
		double statut=getStatut(deb);
		int c=0;
		if (statut==1) {nb++;}
		
		for(int i=deb+1;i<=fin;i++){

			if(getStatut(i)!=statut){
				if(statut==1){
					if(nb>=wordSize) {somme=somme+nb;c++;}
				nb=0;
				}else{
				nb++;
				}
			statut=getStatut(i);
			}
			else{
				if(statut==1) nb++;
			}
			

		}	
		
		if(statut==1) {if(nb>=wordSize) {somme=somme+nb;c++;}}
		double r=somme/(fin-deb+1)*100.0;
		return new Couple(r,c);
		
	}

	int nombre(int pos,String s){
		String tmp="";
		if (pos!=-1){
			int i=pos-1;
			while (i>=0){
				if (Character.isDigit(s.charAt(i))){
					tmp=s.charAt(i)+tmp;
					i--;
				}else{i=-2;}
			}
		}
	
	return Integer.parseInt(tmp);

	}



	String SNP() throws IOException{


		double nbZero=0;
		double meanCoverage=0;
		double min=1000000000;
		double max=-1;
		double somme=0;

		PrintWriter out;
		if(plasmid){
			out=new PrintWriter(new FileWriter(project+"plasmids"+sep+name+"_variants"+".csv"));
		}else{
			out=new PrintWriter(new FileWriter(project+"variantCalling"+sep+name+"_variants"+".csv"));
		}
		out.println(mapperCommand);
		out.println("ref. position;type;ref. base;variant;counts;frequency;depth;CDS;aa change");
		for(int i=1;i<tab.length;i++){
			Cell c=tab[i];
			if(c.getProf()>=min_cov){
				LinkedList list=c.getList(min_freq);			
				for(int u=0;u<list.size();u++){
					Mut m=(Mut)list.get(u);
					String alt=m.alt;
			
					if(alt.length()==1){
						out.println(i+";SNV;"+c.getBase()+";"+m.alt+";"+m.getNb()+";"+(m.getNb()/c.getProf()*100)+";"+c.getProf()+";"+getCDS(i,""+alt.charAt(0)));
						snp_tot++;
					}else{

						String ins=""+alt.charAt(0);
						for(int j=0;j<alt.length();j++){
				
						if(alt.charAt(j)=='*'){
							if(((c.getProf()-c.getEnd())>=min_cov)&&(m.getNb()/(c.getProf()-c.getEnd())*100)>=min_freq){
								if(ins.length()>1){
									out.println((i)+";INDEL;"+c.getBase()+";"+ins+";"+m.getNb()+";"+(m.getNb()/(c.getProf()-c.getEnd())*100)+";"+(c.getProf()-c.getEnd())+";"+getCDS(i,(ins.length()-1)));
									dip++;
									ins="";
								}

								out.println((i+j)+";INDEL;"+tab[i+j].getBase()+";"+"*"+";"+m.getNb()+";"+(m.getNb()/(c.getProf()-c.getEnd())*100)+";"+(c.getProf()-c.getEnd())+";"+getCDS(i));
								dip++;
							}
					
							}else{

								if(Character.isLowerCase(alt.charAt(j))){
						
									ins=ins+alt.charAt(j);						
								}else{
									if(alt.charAt(j)!=tab[i+(j-ins.length()+1)].getBase()){
							
										if(((c.getProf()-c.getEnd())>=min_cov)&&(m.getNb()/(c.getProf()-c.getEnd())*100)>=min_freq){
										if(ins.length()>1){
							
										out.println((i)+";INDEL;"+c.getBase()+";"+ins+";"+m.getNb()+";"+(m.getNb()/(c.getProf()-c.getEnd())*100)+";"+(c.getProf()-c.getEnd())+";"+getCDS(i,(ins.length()-1)));
									dip++;

										out.println((i+j-ins.length()+1)+";SNV;"+tab[i+(j-ins.length()+1)].getBase()+";"+alt.charAt(j)+";"+m.getNb()+";"+(m.getNb()/c.getProf()*100)+";"+c.getProf()+";"+getCDS(i,""+alt.charAt(j)));
										snp_tot++;


										ins="";
										}else{

										out.println((i+j-ins.length()+1)+";SNV;"+tab[i+(j-ins.length()+1)].getBase()+";"+alt.charAt(j)+";"+m.getNb()+";"+(m.getNb()/c.getProf()*100)+";"+c.getProf()+";"+getCDS(i,""+alt.charAt(j)));
										snp_tot++;
										}
									}
							
								}
						
							}
				
						}
				}
				if(ins.length()>1){
					if(((c.getProf()-c.getEnd())>=min_cov)&&(m.getNb()/(c.getProf()-c.getEnd())*100)>=min_freq){
					out.println((i)+";INDEL;"+c.getBase()+";"+ins+";"+m.getNb()+";"+(m.getNb()/(c.getProf()-c.getEnd())*100)+";"+(c.getProf()-c.getEnd())+";"+getCDS(i,(ins.length()-1)));
					dip=dip+(ins.length()-1);
					}
				}
			}
		}


	}

		if(c.getProf() < min_cov){
				nbZero=nbZero+1;
			
			}

		if(c.getProf()>max){
				max=c.getProf();
			}
		if(c.getProf()<min){
				min=c.getProf();
			}

		somme=somme+c.getProf();
	}
	String res="";
	meanCoverage=somme/(tab.length-1);


	/* calcul de la deviation standart*/
	double sd=0;
	double tmp=0;

	for(int i=0;i<tab.length;i++){
		Cell c=tab[i];
		tmp=tmp+((c.getProf()-meanCoverage)*(c.getProf()-meanCoverage));
	}
	sd=Math.sqrt(tmp/(tab.length-1));
	DecimalFormat df4 = new DecimalFormat("########.0000");
	DecimalFormat df2 = new DecimalFormat("########.00");
	DecimalFormat df0 = new DecimalFormat("########");

	Integer nb=new Integer(nb_reads);
	double tot=nb.doubleValue();
	Integer tt=new Integer(total_reads);
	double tot2=tt.doubleValue();
	res=res+(fasta.substring(fasta.lastIndexOf("/")+1,fasta.lastIndexOf("."))+";");
	res=res+(tailleGenome+";");
	res=res+(df4.format(((tailleGenome-nbZero)/tailleGenome*100.0))+";");
	res=res+(df2.format(meanCoverage)+";");
	res=res+(df2.format(sd)+";");
	res=res+(df2.format(tot/tot2*100.0)+";");
	res=res+(df0.format(min)+"|"+df0.format(max));
	Integer i1=new Integer(snp_tot);
	double d1=i1.doubleValue();
	Integer i2=new Integer(dip);
	double d2=i2.doubleValue();
	Integer i3=new Integer(tailleGenome);
	double d3=i3.doubleValue();
	double coverage=((i3-nbZero)/i3*100.0);
	double mr=(d1+d2)/((coverage/100)*d3);

	res=res+";"+snp_tot+";"+snp_cds+";"+snp_change+";"+dip+";"+dip_cds;
	res=res.replaceAll(",",".");
	out.close();
	return res;
	}




	String stat() throws IOException{


		double nbZero=0;
		double meanCoverage=0;
		double min=1000000000;
		double max=-1;
		double somme=0;

		for(int i=1;i<tab.length;i++){
	
			Cell c=tab[i];
			if(c.getProf() < min_cov){
					nbZero=nbZero+1;
			
			}

			if(c.getProf()>max){
					max=c.getProf();
				}
			if(c.getProf()<min){
					min=c.getProf();
				}

			somme=somme+c.getProf();
		}
		String res="";
		meanCoverage=somme/(tab.length-1);


		/* calcul de la deviation standart*/
		double sd=0;
		double tmp=0;

		for(int i=0;i<tab.length;i++){
			Cell c=tab[i];
			tmp=tmp+((c.getProf()-meanCoverage)*(c.getProf()-meanCoverage));

		}
		
		sd=Math.sqrt(tmp/(tab.length-1));

		
		DecimalFormat df4 = new DecimalFormat("########.0000");
		DecimalFormat df2 = new DecimalFormat("########.00");
		DecimalFormat df0 = new DecimalFormat("########");

		Integer nb=new Integer(nb_reads);
		double tot=nb.doubleValue();
		Integer tt=new Integer(total_reads);
		double tot2=tt.doubleValue();

		res=res+(fasta.substring(fasta.lastIndexOf("/")+1,fasta.lastIndexOf("."))+";");
		res=res+(tailleGenome+";");
		res=res+(df4.format(((tailleGenome-nbZero)/tailleGenome*100.0))+";");
		res=res+(df2.format(meanCoverage)+";");
		res=res+(df2.format(sd)+";");
		res=res+(df2.format(tot/tot2*100.0)+";");
		res=res+(df0.format(min)+"|"+df0.format(max));

		Integer i1=new Integer(snp_tot);
		double d1=i1.doubleValue();
		Integer i2=new Integer(dip);
		double d2=i2.doubleValue();
		Integer i3=new Integer(tailleGenome);
		double d3=i3.doubleValue();
		double coverage=((i3-nbZero)/i3*100.0);
		double mr=(d1+d2)/((coverage/100)*d3);


		res=res.replaceAll(",",".");

		return res;
	}




	/* Annotation traitement */
	void setAnnot() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(gff));

		String line="";
		String [] seq;
		int deb,fin,phase;
		while ((line = input.readLine()) != null) {
			if (line.charAt(0)!='#'){
				seq=line.split("\t");

				if(seq[2].equals(feature)){

					deb=Integer.parseInt(seq[3]);
					fin=Integer.parseInt(seq[4]);
					phase=1;
					Annot a=new Annot(deb,fin,seq[2],seq[6],phase,seq[8]);
					annotations.add(a);
				}
			}
		}
		input.close();
	}


	int last=0;


	String getCDS(int pos,String c){

		String codon="";
		String codon_init="";


		Iterator itr = annotations.iterator();
		int i=0;
		while(itr.hasNext()){
		Annot a = (Annot)itr.next();

		if((pos>=a.getDeb())&&(pos<=a.getFin())) {

			if(i!=0){last=i-1;}
			if(a.getStrand().equals("-")){
				int coor=((a.getFin()-a.getPhase()+1)-pos);
				int val=coor%3;
				int num=(coor/3)+1;
				switch(val){
					case 2: codon=codon+getComp(tab[pos+2].getBase())+getComp(tab[pos+1].getBase())+getComp(c.charAt(0));
					codon_init=codon_init+getComp(tab[pos+2].getBase())+getComp(tab[pos+1].getBase())+getComp(tab[pos].getBase());
					break;
					case 0: codon=codon+getComp(c.charAt(0))+getComp(tab[pos-1].getBase())+getComp(tab[pos-2].getBase());
					codon_init=codon_init+getComp(tab[pos].getBase())+getComp(tab[pos-1].getBase())+getComp(tab[pos-2].getBase());
					break;
					case 1: codon=codon+getComp(tab[pos+1].getBase())+getComp(c.charAt(0))+getComp(tab[pos-1].getBase());
					codon_init=codon_init+getComp(tab[pos+1].getBase())+getComp(tab[pos].getBase())+getComp(tab[pos-1].getBase());
					break;
				}
				if(!getAA(codon).equals(getAA(codon_init))) {snp_cds++;snp_change++;return (a.getAnnot()+";"+getAA(codon_init)+"("+num+")"+"->"+getAA(codon));}
				else {snp_cds++;return (a.getAnnot()+";");}
			}else{
			int coor=(pos-(a.getDeb()+a.getPhase()-1))+1;
			int val=coor%3;
			int num=((coor-1)/3)+1;
	
				switch(val){
					case 1: codon=codon+c+tab[pos+1].getBase()+tab[pos+2].getBase();
					codon_init=codon_init+tab[pos].getBase()+tab[pos+1].getBase()+tab[pos+2].getBase();
					break;
					case 2: codon=codon+tab[pos-1].getBase()+c+tab[pos+1].getBase();
					codon_init=codon_init+tab[pos-1].getBase()+tab[pos].getBase()+tab[pos+1].getBase();
					break;
					case 0: codon=codon+tab[pos-2].getBase()+tab[pos-1].getBase()+c;
					codon_init=codon_init+tab[pos-2].getBase()+tab[pos-1].getBase()+tab[pos].getBase();
					break;
				}
				if(!getAA(codon).equals(getAA(codon_init))){snp_cds++;snp_change++;return (a.getAnnot()+";"+getAA(codon_init)+"("+num+")"+"->"+getAA(codon));}
				else {snp_cds++;return (a.getAnnot()+";");}
			}
		}

		if(pos<a.getDeb()) {if(i!=0){last=i-1;}return (";;");}
		i++;
		}
		return (";;");
	}






	void getGenes() throws IOException {
	/*  gff */
		BufferedReader in = new BufferedReader(new FileReader(gff));
		createMut();

		PrintWriter out;
		PrintWriter out2;
		PrintWriter out3;
		if(plasmid){

			out=new PrintWriter(new FileWriter(project+"plasmids"+sep+name+"_comparativeAnnot.csv"));
			out2=new PrintWriter(new FileWriter(project+"plasmids"+sep+name+"_genes.xml"));
			out3=new PrintWriter(new FileWriter(project+"plasmids"+sep+name+"_comparativeAnnot.fa"));
		}else{
			out=new PrintWriter(new FileWriter(project+name+"_comparativeAnnot.csv"));
			out2=new PrintWriter(new FileWriter(project+name+"_genes.xml"));
			out3=new PrintWriter(new FileWriter(project+name+"_comparativeAnnot.fa"));
		}

		out.println("## "+name);
		out.println("begin;end;type;strand;statut;coverage;succ;note");
		out2.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		out2.println("<cgview backboneRadius=\"320\" sequenceLength=\""+tailleGenome+"\" height=\"800\" width=\"800\">");

		String line="";
		String [] seq2;
		int deb=0;
		int fin=0;
		int id=1;
		LinkedList plus=new LinkedList();
		LinkedList moins=new LinkedList();
		Couple succ=new Couple();
		while ((line = in.readLine()) != null) {
			if (line.charAt(0)!='#'){
				seq2=line.split("\t");
				
				
				if((!seq2[2].equals("source"))&& (!seq2[2].equals("stop_codon"))&&(!seq2[2].equals("start_codon"))){
					deb=Integer.parseInt(seq2[3]);
					fin=Integer.parseInt(seq2[4]);

					String res="";
					res=res+deb+";"+fin+";"+seq2[2]+";"+seq2[6]+";";
					double r=-1.0;
					if((deb<=tailleGenome)&&(deb>=0)&&(fin<=tailleGenome)&&(fin>=0)){
					r= getStatut(deb,fin);
					}
					if(r!=-1){
					if(r<10) {	
							res=res+"1;";
							res=res+r+";ND;";
							if(seq2[2].equals(feature)){
								String deco="";
								if(seq2[6].equals("-")){
									deco="<feature color=\"red\" decoration=\"arc\" >";
									deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
									deco=deco+"</feature>";
									moins.add(deco);
								}else{
									deco="<feature color=\"red\" decoration=\"arc\" >";
									deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
									deco=deco+"</feature>";
									plus.add(deco);
								}
							}
					}
					else{
						if(r<80) {
							/* test low complexity */
							boolean lc=getLowComplexity(deb,fin,r);
							if(lc) {

								res=res+"3;";
								res=res+r+";ND;";
								if(seq2[2].equals(feature)){
								String deco="";
								if(seq2[6].equals("-")){
									deco="<feature color=\"grey\" decoration=\"arc\" >";
									deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
									deco=deco+"</feature>";
									moins.add(deco);
								}else{
									deco="<feature color=\"grey\" decoration=\"arc\" >";
									deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
									deco=deco+"</feature>";
									plus.add(deco);
								}
								}

							}
							else  {
								/* blocs*/
								succ=getSucc(deb,fin);
								if((succ.getValue() >= 40)&&((succ.getN()==1)||(succ.getN()==2))){
								res=res+"4;";
								res=res+r+";("+succ.getValue()+","+succ.getN()+");";
									if(seq2[2].equals(feature)){
									String deco="";
									if(seq2[6].equals("-")){
										deco="<feature color=\"purple\" decoration=\"arc\" >";
										deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
										deco=deco+"</feature>";
										moins.add(deco);
									}else{
										deco="<feature color=\"purple\" decoration=\"arc\" >";
										deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
										deco=deco+"</feature>";
										plus.add(deco);
									}
									}
								}else{
								res=res+"5;";
								res=res+r+";("+succ.getValue()+","+succ.getN()+");";
									if(seq2[2].equals(feature)){
									String deco="";
									if(seq2[6].equals("-")){
										deco="<feature color=\"yellow\" decoration=\"arc\" >";
										deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
										deco=deco+"</feature>";
										moins.add(deco);
									}else{
										deco="<feature color=\"yellow\" decoration=\"arc\" >";
										deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
										deco=deco+"</feature>";
										plus.add(deco);
									}
									}
								}
								}
						}
						else {

							res=res+"2;";
							res=res+r+";ND;";
							if(seq2[2].equals(feature)){
							String deco="";
							if(seq2[6].equals("-")){
								deco="<feature color=\"green\" decoration=\"arc\" >";
								deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
								deco=deco+"</feature>";
								moins.add(deco);
							}else{
								deco="<feature color=\"green\" decoration=\"arc\" >";
								deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
								deco=deco+"</feature>";
								plus.add(deco);
							}
							}
						}
					}
					
					/* annotation */
					if(seq2.length==9){
					
					String ttt=seq2[8];
					ttt=ttt.replaceAll(";","&");
					res=res+ttt;
					
					}
					out.println(res);
					String [] parse=res.split(";");
					
					if(parse[2].equals(feature)){

						
						/* consensus */

						DecimalFormat f = new DecimalFormat("##.00");
						out3.print(">"+parse[0]+"-"+parse[1]+" ("+parse[3]+") status:"+parse[4]+" cov:"+f.format(Double.parseDouble(parse[5]))+"% "+parse[6]+" ");
						if(parse.length>7){
							
							String [] annot=parse[7].split("&");
							String name="";
							String product="";
							String locus="";
							for(int u=0;u<annot.length;u++){
								if(annot[u].indexOf("Name=")!=-1){
									name=annot[u].substring(5,annot[u].length());	
								}
								if(annot[u].indexOf("product=")!=-1){
									product=annot[u].substring(8,annot[u].length());
								}
								if(annot[u].indexOf("locus_tag=")!=-1){
									locus=annot[u].substring(10,annot[u].length());
								}
							}
					
					
							out3.println("locus="+locus+",name="+name);
							
						}
					
						getConsensus(Integer.parseInt(parse[0]),Integer.parseInt(parse[1]),out3);
						out3.println("");
						id++;
					}

					
					}
				}

			}

		}

		out2.println("<featureSlot strand=\"direct\">");
		for(int i=0;i<plus.size();i++){
			out2.println((String)plus.get(i));
		}
		out2.println("</featureSlot>");


		out2.println("<featureSlot strand=\"reverse\">");
		for(int i=0;i<moins.size();i++){
			out2.println((String)moins.get(i));
		}
		out2.println("</featureSlot>");
		
		out2.println("<legend position=\"upper-center\">"); 
		out2.println("<legendItem text=\""+name+"\" font=\"Monospaced,plain,20\" textAlignment=\"center\" />"); 
		out2.println("</legend>");


		out2.println("<legend position=\"upper-right\" font=\"SansSerif,plain,16\">");
		out2.println(" <legendItem text=\"Absent (1)\" drawSwatch=\"true\" swatchColor=\"red\" />");
		out2.println(" <legendItem text=\"Present (2)\" drawSwatch=\"true\" swatchColor=\"green\" />");
		out2.println(" <legendItem text=\"Low comp (3)\" drawSwatch=\"true\" swatchColor=\"grey\" />");
		out2.println(" <legendItem text=\"Partial (4)\" drawSwatch=\"true\" swatchColor=\"purple\" />");
		out2.println(" <legendItem text=\"Divergent (5)\" drawSwatch=\"true\" swatchColor=\"yellow\" />");
		out2.println("</legend>");
	
		out2.println("</cgview>");


		in.close();
		out.close();
		out2.close();
		out3.close();
		
		if(cgview){
			if(plasmid){
				new CGviewInterface(project+"plasmids"+sep+name+"_genes.xml",project+"plasmids"+sep,name);
			}else{
				new CGviewInterface(project+name+"_genes.xml",project,name);
			}
		}
	}




	void getGenesHTML() throws IOException {
	/* gff */
		BufferedReader in = new BufferedReader(new FileReader(gff));
		createMut();

		PrintWriter out;
		PrintWriter out2;
		PrintWriter out3;
		if(plasmid){
			out=new PrintWriter(new FileWriter(project+"plasmids"+sep+name+"_genes.csv"));
			out2=new PrintWriter(new FileWriter(project+"plasmids"+sep+name+"_genes.xml"));
			out3=new PrintWriter(new FileWriter(project+"plasmids"+sep+name+"_genes.html"));
		}else{
			out=new PrintWriter(new FileWriter(project+name+"_genes.csv"));
			out2=new PrintWriter(new FileWriter(project+name+"_genes.xml"));
			out3=new PrintWriter(new FileWriter(project+name+"_genes.html"));
		}

		out.println("## "+name);
		out.println("begin;end;type;strand;statut;coverage;succ;note");


		out2.println("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		out2.println("<cgview backboneRadius=\"320\" sequenceLength=\""+tailleGenome+"\" height=\"800\" width=\"800\">");


		out3.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">");
		out3.println("<html>");
		out3.println("<head>");
		  out3.println("<title>CDS List</title>");
		out3.println("</head>");

		out3.println("<body>");
		out3.println("<h1>List of CDS for "+name+" genome</h1>");
		
		String line="";
		String [] seq2;
		int deb=0;
		int fin=0;
		int id=1;
		LinkedList plus=new LinkedList();
		LinkedList moins=new LinkedList();
		Couple succ=new Couple();
		while ((line = in.readLine()) != null) {
			if (line.charAt(0)!='#'){
				seq2=line.split("\t");
				
				if((!seq2[2].equals("source"))&& (!seq2[2].equals("stop_codon"))&&(!seq2[2].equals("start_codon"))){
					deb=Integer.parseInt(seq2[3]);
					fin=Integer.parseInt(seq2[4]);

					String res="";
					res=res+deb+";"+fin+";"+seq2[2]+";"+seq2[6]+";";
					double r=-1.0;
					if((deb<=tailleGenome)&&(deb>=0)&&(fin<=tailleGenome)&&(fin>=0)){
					r= getStatut(deb,fin);
					}
					if(r!=-1){
					if(r<10) {	

							res=res+"1;";
							res=res+r+";ND;";
							if(seq2[2].equals(feature)){
								String deco="";
								if(seq2[6].equals("-")){
									deco="<feature color=\"red\" decoration=\"arc\" >";
									deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
									deco=deco+"</feature>";
									moins.add(deco);
								}else{
									deco="<feature color=\"red\" decoration=\"arc\" >";
									deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
									deco=deco+"</feature>";
									plus.add(deco);
								}
							}
					}
					else{
						if(r<80) {
							/* low complexity */
							boolean lc=getLowComplexity(deb,fin,r);
							if(lc) {
								res=res+"3;";
								res=res+r+";ND;";
								if(seq2[2].equals(feature)){
								String deco="";
								if(seq2[6].equals("-")){
									deco="<feature color=\"grey\" decoration=\"arc\" >";
									deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
									deco=deco+"</feature>";
									moins.add(deco);
								}else{
									deco="<feature color=\"grey\" decoration=\"arc\" >";
									deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
									deco=deco+"</feature>";
									plus.add(deco);
								}
								}

							}
							else  {
								/* blocs*/
								succ=getSucc(deb,fin);
								if((succ.getValue() >= 40)&&((succ.getN()==1)||(succ.getN()==2))){
								res=res+"4;";
								res=res+r+";("+succ.getValue()+","+succ.getN()+");";
									if(seq2[2].equals(feature)){
									String deco="";
									if(seq2[6].equals("-")){
										deco="<feature color=\"purple\" decoration=\"arc\" >";
										deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
										deco=deco+"</feature>";
										moins.add(deco);
									}else{
										deco="<feature color=\"purple\" decoration=\"arc\" >";
										deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
										deco=deco+"</feature>";
										plus.add(deco);
									}
									}
								}else{
								res=res+"5;";
								res=res+r+";("+succ.getValue()+","+succ.getN()+");";
									if(seq2[2].equals(feature)){
									String deco="";
									if(seq2[6].equals("-")){
										deco="<feature color=\"yellow\" decoration=\"arc\" >";
										deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
										deco=deco+"</feature>";
										moins.add(deco);
									}else{
										deco="<feature color=\"yellow\" decoration=\"arc\" >";
										deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
										deco=deco+"</feature>";
										plus.add(deco);
									}
									}
								}
								}
						}
						else {
							res=res+"2;";
							res=res+r+";ND;";
							if(seq2[2].equals(feature)){
							String deco="";
							if(seq2[6].equals("-")){
								deco="<feature color=\"green\" decoration=\"arc\" >";
								deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
								deco=deco+"</feature>";
								moins.add(deco);
							}else{
								deco="<feature color=\"green\" decoration=\"arc\" >";
								deco=deco+"<featureRange start=\""+deb+"\" stop=\""+fin+"\" />";
								deco=deco+"</feature>";
								plus.add(deco);
							}
							}
						}
					}
					
					/* annotation */
					if(seq2.length==9){
					String ttt=seq2[8];
					ttt=ttt.replaceAll(";","&");
					res=res+ttt;
					}
					

					out.println(res);


					String [] parse=res.split(";");
					
					if(parse[2].equals(feature)){

						/* consensus */

						DecimalFormat f = new DecimalFormat("##.00");
					


						out3.println("<b>CDS</b>: "+parse[0]+"-"+parse[1]+" ("+parse[3]+") status:"+parse[4]+" cov:"+f.format(Double.parseDouble(parse[5]))+"% "+parse[6]+"<br>");
						if(parse.length>7){
							String [] annot=parse[7].split("&");
							String GI="";
							String product="";
							for(int u=0;u<annot.length;u++){
								if(annot[u].indexOf("GI:")!=-1){
									GI=annot[u].substring((annot[u].indexOf("GI:")+3),annot[u].length());
								}
								if(annot[u].indexOf("product=")!=-1){
									product=annot[u].substring(8,annot[u].length());
								}
							}
					
					
							out3.println("<a href=\"http://www.ncbi.nlm.nih.gov/protein/"+GI+"\" target=\"_blank\">"+GI+"</a>: "+product);
						}
					
						out3.println(getConsensusHTML(Integer.parseInt(parse[0]),Integer.parseInt(parse[1])));
						id++;
					}

					}
				}

			}

		}

		out2.println("<featureSlot strand=\"direct\">");
		for(int i=0;i<plus.size();i++){
			out2.println((String)plus.get(i));
		}
		out2.println("</featureSlot>");


		out2.println("<featureSlot strand=\"reverse\">");
		for(int i=0;i<moins.size();i++){
			out2.println((String)moins.get(i));
		}
		out2.println("</featureSlot>");
		
		out2.println("<legend position=\"upper-center\">"); 
		out2.println("<legendItem text=\""+name+"\" font=\"Monospaced,plain,20\" textAlignment=\"center\" />"); 
		out2.println("</legend>");


		out2.println("<legend position=\"upper-right\" font=\"SansSerif,plain,16\">");
		out2.println(" <legendItem text=\"Absent (1)\" drawSwatch=\"true\" swatchColor=\"red\" />");
		out2.println(" <legendItem text=\"Present (2)\" drawSwatch=\"true\" swatchColor=\"green\" />");
		out2.println(" <legendItem text=\"Low comp (3)\" drawSwatch=\"true\" swatchColor=\"grey\" />");
		out2.println(" <legendItem text=\"Partial (4)\" drawSwatch=\"true\" swatchColor=\"purple\" />");
		out2.println(" <legendItem text=\"Divergent (5)\" drawSwatch=\"true\" swatchColor=\"yellow\" />");
		out2.println("</legend>");
	
		out2.println("</cgview>");

		out3.println("</body>");
		out3.println("</html>");

		in.close();
		out.close();
		out2.close();
		out3.close();
		
		if(cgview){
			if(plasmid){
				new CGviewInterface(project+"plasmids"+sep+name+"_genes.xml",project+"plasmids"+sep,name);
			}else{
				new CGviewInterface(project+name+"_genes.xml",project,name);
			}
		}
	}

	HashMap muts; 
	void createMut() throws IOException{
		muts = new HashMap();
		BufferedReader input;
		if(plasmid){
			input=new BufferedReader(new FileReader(project+"plasmids"+sep+name+"_variants"+".csv"));
		}else{
			input=new BufferedReader(new FileReader(project+"variantCalling"+sep+name+"_variants"+".csv"));
		}

		String l="";
		String [] line;

		int pos;
		l = input.readLine();
		l = input.readLine();


		while ((l = input.readLine()) != null) {
	
		
			line=l.split(";");
			pos=Integer.parseInt(line[0]);
		
			muts.put(pos,new Mut(Double.parseDouble(line[4]),line[3]));

		}
		input.close();
	}



	Mut getMut(int i) {
		Mut res=null;
		if(muts.get(i)!=null)
			return ((Mut)muts.get(i));
		return res;
	}


	Mut getMut1(int i) throws IOException{

		Mut res=null;

		Cell c=tab[i];

		BufferedReader input;
		if(plasmid){
			input=new BufferedReader(new FileReader(project+"plasmids"+sep+name+"_variants"+".csv"));
		}else{
			input=new BufferedReader(new FileReader(project+"variantCalling"+sep+name+"_variants"+".csv"));
		}

		String l="";
		String [] line;

		int pos;
		l = input.readLine();
		l = input.readLine();


		while ((l = input.readLine()) != null) {
	
		
				line=l.split(";");
				pos=Integer.parseInt(line[0]);
		
				if(i==pos){res=new Mut(Double.parseDouble(line[4]),line[3]);input.close();return res;}

		}
		input.close();
		System.out.println("endmut");
		return res;
	}



	void getConsensus(int deb, int fin, PrintWriter out) throws IOException{

	
		String contig="";
		int ct=0;
		boolean lc=false;
	

		for(int i=deb;i<=fin;i++){
			Cell c=tab[i];
		
			Mut m=getMut(i);
			lc=false;
			if(m!=null){

				for(int u=0;u<m.alt.length();u++){

					if(m.alt.charAt(u)!='*'){
					contig=contig+m.alt.charAt(u);
					ct++;
					if((ct % 80)==0){out.println(contig);contig="";}
					}else{if(m.alt.length()>1)i++;}
				}

			}else{
				if(c.getProf()<min_cov){
					contig=contig+"N";
					ct++;
					if((ct % 80)==0){out.println(contig);contig="";}
				}else{

				contig=contig+c.getBase();
				ct++;
				if((ct % 80)==0){out.println(contig);contig="";}
				}
				
			}


		

		}
		if(!contig.equals("")){
		out.println(contig);
		}

	}

	String getConsensusHTML(int deb,int fin) throws IOException{
		String res="<pre width=30>";
		int ct=0;
		DecimalFormat df2 = new DecimalFormat("########.00");
	
		for(int i=deb;i<=fin;i++){
			Cell c=tab[i];

			if(c.getProf()<=min_cov){
				res=res+"N";
				ct++;
				if((ct % 80)==0)res=res+"<br>";
			}else{

			Mut b=c.getBaseMax();
			String base=b.alt;
			if(base.equals(""+c.getBase())){
				res=res+base;
				ct++;
				if((ct % 80)==0)res=res+"<br>";
			}else{
				Mut m=getMut(i);
				if(m!=null){

				if(base.equals(m.alt)){
					if(base.indexOf("*")!=-1){
					/* deletion */
					res=res+base.charAt(0);
					ct++;
					if((ct % 80)==0)res=res+"<br>";
					for(int u=1;u<base.length();u++){
					if(i+u<=fin){
						Cell c2=tab[i+u];
								if(c2.getProf()<=min_cov){
											res=res+"N";
											ct++;
											i++;
											if((ct % 80)==0)res=res+"<br>";
								}else{
								if(base.charAt(u)=='*'){
						
							
							
								res=res+"<span  style=\"background:yellow\" title=\" "+c2.getBase()+";"+df2.format(c2.getProf())+";"+df2.format((b.getNb()/c2.getProf()*100))+"\"><b>"+"*"+"</b></span>";
							
								ct++;
								i++;
								if((ct % 80)==0)res=res+"<br>";
						
							}else{
							
								res=res+"<font color=\"red\"><span  style=\"background:yellow\" title=\" "+c2.getBase()+";"+df2.format(c2.getProf())+";"+df2.format((b.getNb()/c.getProf()*100))+"\"><b>"+base.charAt(u)+"</b></span></font>";
							
								ct++;
								i++;
								if((ct % 80)==0)res=res+"<br>";
							}
						}
					}}
					}else{
						/* snp*/
						if(base.length()==1){
						res=res+"<font color=\"red\"><span style=\"background:yellow\" title=\" "+c.getBase()+";"+df2.format(c.getProf())+";"+df2.format((m.getNb()/c.getProf()*100))+"\"><b>"+base+"</b></span></font>";
								ct++;
								if((ct % 80)==0)res=res+"<br>";
						}else{
							/* insertion */
							String ins=base.substring(1,base.length());
							res=res+base.charAt(0);
							ct++;
							if((ct % 80)==0)res=res+"<br>";
							for(int w=0;w<ins.length();w++){
								if(Character.isLowerCase(ins.charAt(w))){
								res=res+"<font color=\"blue\"><span style=\"background:yellow\" title=\" "+df2.format(c.getProf())+";"+df2.format(b.getNb()/c.getProf()*100)+"\"><b>"+ins.charAt(w)+"</b></span></font>";
									ct++;
									if((ct % 80)==0)res=res+"<br>";
								}else{
									if(i+1<=fin){
										i++;
										c=tab[i];
										if(c.getProf()<=min_cov){
											res=res+"N";
											ct++;
											if((ct % 80)==0)res=res+"<br>";
										}else{
										res=res+"<font color=\"red\"><span style=\"background:yellow\" title=\" "+c.getBase()+";"+df2.format(c.getProf())+";"+df2.format(m.getNb()/c.getProf()*100)+"\"><b>"+ins.charAt(w)+"</b></span></font>";
										ct++;
										if((ct % 80)==0)res=res+"<br>";
										}
									}

								}
							}

						}
							

					}
					

				}
				}else{
					if(base.indexOf("*")!=-1){
					/* deletion */
					res=res+base.charAt(0);
					ct++;
					if((ct % 80)==0)res=res+"<br>";
					for(int u=1;u<base.length();u++){
						if(i+u<=fin){

						Cell c2=tab[i+u];
						if(c2.getProf()<=min_cov){
							res=res+"N";
							ct++;
							i++;
							if((ct % 80)==0)res=res+"<br>";
						}else{

	
							if(base.charAt(u)=='*'){
						
							
								res=res+"<span  title=\" "+c2.getBase()+";"+df2.format(c2.getProf())+";"+df2.format(b.getNb()/c2.getProf()*100)+"\"><b>"+"*"+"</b></span>";
								ct++;
								i++;
								if((ct % 80)==0)res=res+"<br>";
						
							}else{

								res=res+"<font color=\"red\"><span  title=\" "+c2.getBase()+";"+df2.format(c2.getProf())+";"+df2.format(b.getNb()/c.getProf()*100)+"\"><b>"+base.charAt(u)+"</b></span></font>";
								ct++;
								i++;
								if((ct % 80)==0)res=res+"<br>";
							}
						}}
					}
					}else{
						/* snp*/
						if(base.length()==1){
						res=res+"<font color=\"red\"><span  title=\" "+c.getBase()+";"+df2.format(c.getProf())+";"+df2.format(b.getNb()/c.getProf()*100)+"\"><b>"+base+"</b></span></font>";
								ct++;
								if((ct % 80)==0)res=res+"<br>";
						}else{
							/* insertion */
							String ins=base.substring(1,base.length());
							res=res+base.charAt(0);
							ct++;
							if((ct % 80)==0)res=res+"<br>";
							for(int w=0;w<ins.length();w++){
								if(Character.isLowerCase(ins.charAt(w))){
								res=res+"<font color=\"blue\"><span  title=\" "+df2.format(c.getProf())+";"+df2.format(b.getNb()/c.getProf()*100)+"\"><b>"+ins.charAt(w)+"</b></span></font>";
									ct++;
									if((ct % 80)==0)res=res+"<br>";
								}else{
									if(i+1<=fin){
								
										i++;
										c=tab[i];
										if(c.getProf()<=min_cov){
											res=res+"N";
										}else{
										res=res+"<font color=\"red\"><span title=\" "+c.getBase()+";"+df2.format(c.getProf())+";"+df2.format(b.getNb()/c.getProf()*100)+"\"><b>"+ins.charAt(w)+"</b></span></font>";							}
										ct++;
										if((ct % 80)==0)res=res+"<br>";
									}

								}
							}

						}
							

					}
				

				}
			

			}
		

			}

		}


		res=res+"<br></pre>";
		return res;

	}





	char getComp(char c){
		switch(c){
		case 'A': return 'T'; 
		case 'T': return 'A';
		case 'C': return 'G';
		case 'G': return 'C';
		}
		return '-';
	}

	String getCDS(int pos){

		String res="";
		Iterator itr = annotations.iterator();
		int i=0;
		while(itr.hasNext()){
			Annot a = (Annot)itr.next();
			if((pos>=a.getDeb())&&(pos<=a.getFin())) {if(i!=0){last=i-1;}dip_cds++;return (a.getAnnot()+";");}
			if(pos<a.getDeb()) {if(i!=0){last=i-1;}return (";;");}
			i++;
		}
		return res;
	}

	String getCDS(int pos,int ins){
		String res="";
		Iterator itr = annotations.iterator();
		int i=0;
		while(itr.hasNext()){
			Annot a = (Annot)itr.next();
			if((pos>=a.getDeb())&&(pos<=a.getFin())) {if(i!=0){last=i-1;}dip_cds=dip_cds+ins;return (a.getAnnot()+";");}
			if(pos<a.getDeb()) {if(i!=0){last=i-1;}return (";;");}
			i++;
		}
		return res;
	}


	String getAA(String c){
		if((c.equals("TTT"))||(c.equals("TTC")))  return "Phe";
		if((c.equals("TCT"))||(c.equals("TCC"))||(c.equals("TCG"))||(c.equals("TCA"))||(c.equals("AGT"))||(c.equals("AGC")))  return "Ser";
		if((c.equals("TAT"))||(c.equals("TAC")))  return "Tyr";
		if((c.equals("TGT"))||(c.equals("TGC")))  return "Cys";
		if((c.equals("TTA"))||(c.equals("TTG"))||(c.equals("CTT"))||(c.equals("CTC"))||(c.equals("CTA"))||(c.equals("CTG")))  return "Leu";
		if((c.equals("TAA"))||(c.equals("TAG"))||(c.equals("TGA")))  return "Ter";
		if((c.equals("CCT"))||(c.equals("CCC"))||(c.equals("CCA"))||(c.equals("CCG")))  return "Pro";
		if((c.equals("CAT"))||(c.equals("CAC")))  return "His";
		if((c.equals("CGT"))||(c.equals("CGC"))||(c.equals("CGA"))||(c.equals("CGG"))||(c.equals("AGA"))||(c.equals("AGG")))  return "Arg";
		if(c.equals("TGG"))  return "Trp";
		if((c.equals("CAA"))||(c.equals("CAG")))  return "Gln";
		if((c.equals("ATT"))||(c.equals("ATC"))||(c.equals("ATA")))  return "Ile";
		if((c.equals("ACT"))||(c.equals("ACC"))||(c.equals("ACA"))||(c.equals("ACG")))  return "Thr";
		if((c.equals("AAT"))||(c.equals("AAC")))  return "Asn";
		if(c.equals("ATG"))  return "Met";
		if((c.equals("AAA"))||(c.equals("AAG")))  return "Lys";
		if((c.equals("GTT"))||(c.equals("GTC"))||(c.equals("GTA"))||(c.equals("GTG")))  return "Val";
		if((c.equals("GCT"))||(c.equals("GCC"))||(c.equals("GCA"))||(c.equals("GCG")))  return "Ala";
		if((c.equals("GGT"))||(c.equals("GGC"))||(c.equals("GGA"))||(c.equals("GGG")))  return "Gly";
		if((c.equals("GAT"))||(c.equals("GAC")))  return "Asp";
		if((c.equals("GAA"))||(c.equals("GAG")))  return "Glu";
		return(c);
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




	void getDepth () throws IOException{
		PrintWriter out;
		out=new PrintWriter(new FileWriter(project+sep+name+"_depth.txt"));
		for(int i=1;i<tab.length;i++){
			Cell c=tab[i];
			out.println(i+";"+c.getProf());

		}

		out.close();
	}

	void getContigs () throws IOException{
	
	
		PrintWriter out;
		if(plasmid){
			out=new PrintWriter(new FileWriter(project+"plasmids"+sep+name+"_consensus.fa"));
		}else{
			out=new PrintWriter(new FileWriter(project+sep+name+"_consensus.fa"));
		}
	
	
		updateLowCov();
		updateLowCov2();
	
		String contig="";
		int ct=0;
		int n=0;
		boolean lc=false;
		String last="";


		if(tab[1].getLowCov())lc=true;
		
			n++;
			out.println(">contig_"+n);
			last=(">"+name+"_contig_"+n+"\n");
		
		for(int i=1;i<tab.length;i++){
			Cell c=tab[i];
			if(c.getLowCov()){
				if(!lc){
				n++;
				contig=deleteEnd(contig);
				out.println(contig+"\n");
				out.println(">contig_"+n);
				last=">contig_"+n+"\n";
				contig="";
				ct=0;
				lc=true;
				}else{}
				}
			else{
				Mut m=getMut(i);
				lc=false;
				if(m!=null){
					for(int u=0;u<m.alt.length();u++){
						if(m.alt.charAt(u)!='*'){
						contig=contig+m.alt.charAt(u);
						ct++;
						if(last.startsWith(">")){
								if((ct % 80)==0){
									String tmp=deleteDeb(contig);
									if(contig.length()==tmp.length()){
										out.println(contig);last=contig+"\n";contig="";
									}else{
										ct=ct-(contig.length()-tmp.length());
										contig=tmp;
									}
								}
							}else{
								if((ct % 80)==0){out.println(contig);last=contig+"\n";contig="";}
							}
						}else{if(m.alt.length()>1)i++;}
					}

				}else{
					if(c.getProf()<min_cov){
						contig=contig+"N";
						ct++;
						if(last.startsWith(">")){
								if((ct % 80)==0){
									String tmp=deleteDeb(contig);
									if(contig.length()==tmp.length()){
										out.println(contig);last=contig+"\n";contig="";
									}else{
										ct=ct-(contig.length()-tmp.length());
										contig=tmp;
									}
								}
							}else{
								if((ct % 80)==0){out.println(contig);last=contig+"\n";contig="";}
							}
					}else{

					contig=contig+c.getBase();
					ct++;
					if(last.startsWith(">")){
								if((ct % 80)==0){
									String tmp=deleteDeb(contig);
									if(contig.length()==tmp.length()){
										out.println(contig);last=contig+"\n";contig="";
									}else{
										ct=ct-(contig.length()-tmp.length());
										contig=tmp;
									}
								}
							}else{
								if((ct % 80)==0){out.println(contig);last=contig+"\n";contig="";}
							}
					}
				
				}


			}

		}
			if(!contig.equals("")){
			contig=deleteEnd(contig);

			out.println(contig);
			last=contig+"\n";
			}

		out.close();

		if(last.indexOf(">contig_")!=-1){
			 try{

			 RandomAccessFile file;

			if(plasmid){
			file= new RandomAccessFile(project+"plasmids"+sep+name+"_consensus.fa","rw");
		
		}else{
			file= new RandomAccessFile(project+sep+name+"_consensus.fa","rw");
		
		}
		     long length = file.length();
		     file.setLength(length-last.length());
		     file.close();


		     }catch(Exception ex){
	 
	 		 System.out.println("ERROR : "+ex);
		 }
		}else{

		}

	}



	void getContigs (PrintWriter output) throws IOException{

	
		PrintWriter out=new PrintWriter(new FileWriter("tmp_contigs.fa"));
		updateLowCov();
		updateLowCov2();
		String contig="";
		int ct=0;
		int n=0;
		boolean lc=false;
		String last="";


		if(tab[1].getLowCov())lc=true;
		n++;
		out.println(">"+name+"_contig_"+n);
		last=(">"+name+"_contig_"+n+"\n");

		for(int i=1;i<tab.length;i++){
			Cell c=tab[i];
			if(c.getLowCov()){
				if(!lc){
				n++;			
				contig=deleteEnd(contig);
			

				out.println(contig+"\n");
				out.println(">"+name+"_contig_"+n);
				last=(">"+name+"_contig_"+n+"\n");
				contig="";
				ct=0;
				lc=true;
				}else{}
				}
			else{
				Mut m=getMut(i);
				lc=false;
				if(m!=null){
					for(int u=0;u<m.alt.length();u++){

						if(m.alt.charAt(u)!='*'){
							contig=contig+m.alt.charAt(u);
							ct++;
							if(last.startsWith(">")){
								if((ct % 80)==0){
									String tmp=deleteDeb(contig);
									if(contig.length()==tmp.length()){
										out.println(contig);last=contig+"\n";contig="";
									}else{
										ct=ct-(contig.length()-tmp.length());
										contig=tmp;
									}
								}
							}else{
								if((ct % 80)==0){out.println(contig);last=contig+"\n";contig="";}
							}
						}else{
							if(m.alt.length()>1)i++;
						}
					}

				}else{

					if(c.getProf()<min_cov){
						contig=contig+"N";
						ct++;

						if(last.startsWith(">")){
								if((ct % 80)==0){
									String tmp=deleteDeb(contig);
									if(contig.length()==tmp.length()){
										out.println(contig);last=contig+"\n";contig="";
									}else{
										ct=ct-(contig.length()-tmp.length());
										contig=tmp;
									}
								}
							}else{
								if((ct % 80)==0){out.println(contig);last=contig+"\n";contig="";}
							}
					}else{

					contig=contig+c.getBase();
					ct++;

						if(last.startsWith(">")){
								if((ct % 80)==0){
									String tmp=deleteDeb(contig);
									if(contig.length()==tmp.length()){
										out.println(contig);last=contig+"\n";contig="";
									}else{
										ct=ct-(contig.length()-tmp.length());
										contig=tmp;
									}
								}
							}else{
								if((ct % 80)==0){out.println(contig);last=contig+"\n";contig="";}
							}
					}
				
				}


			}

		}
			if(!contig.equals("")){
		
			contig=deleteEnd(contig);		

			out.println(contig);
			last=contig+"\n";
			}

		out.close();
		if(last.indexOf("contig")!=-1){
			 try{


		     RandomAccessFile file= new RandomAccessFile("tmp_contigs.fa","rw");
		     long length = file.length();
		     file.setLength(length-last.length());
		     file.close();


		     }catch(Exception ex){
	 
	 		 System.out.println("ERROR : "+ex);
		 }
		}else{

		}

	
		BufferedReader in = new BufferedReader(new FileReader("tmp_contigs.fa"));
		String l="";
		while ((l = in.readLine()) != null) {
			output.println(l);
		}

		/* delete temp file*/
		File f1=new File("tmp_contigs.fa");
		f1.delete();

	
	}


	String deleteDeb(String s){
		String res="";
		int i=0;
		if(s.startsWith("N")){
			while(s.charAt(i)=='N'){
				i++;
				if(i>=s.length())break;
			}	
		
		}
	

		res=s.substring(i,(s.length()));
		return res;
	}

	String deleteEnd(String s){
		String res="";
	
		int j=0;
		if(s.endsWith("N")){
		
			while(s.charAt(s.length()-1-j)=='N'){
				j++;
				if(j>=s.length())break;
			}	
		
		}
		res=s.substring(0,(s.length()-j));
	
		return res;
	}



	void updateLowCov2(){
		int s=0;
		int current=0;

		for(int i=0;i<tab.length;i++){
			Cell c=tab[i];
			if(c.getLowCov()){
				if(s<min_contig_size){
					for(int j=current;j<i;j++){
						tab[j].setLowCov();
					}
				}
				current=i;
				s=0;

			}else{
				s++;
			}
		}
	}


	void updateLowCov(){
		int n=0;
		for(int i=0;i<tab.length;i++){
			Cell c=tab[i];
			if(getLowCov(i)){c.setLowCov();n++;}
		}
	}

	boolean getLowCov(int i){
		int somme=0;
		int ct=0;
	
		if(i-window/2<0){
			for(int j=i;j>=0;j--){
				Cell c=tab[j];
				if(c.getProf()<min_cov){
					somme++;
				}
				ct++;
			}
			for(int j=i+1;j<=i+(window+1-ct);j++){
				Cell c=tab[j];
				if(c.getProf()<min_cov){
					somme++;
				}
			}
			if(somme>window/2)return true;
			return false;
		}

		if(i+window/2>tab.length){
			for(int j=i;j<tab.length;j++){
				Cell c=tab[j];
				if(c.getProf()<min_cov){
					somme++;
				}
				ct++;
			}
			for(int j=i-1;j>i-(window+1-ct);j--){
				Cell c=tab[j];
				if(c.getProf()<min_cov){
					somme++;
				}
			}
			if(somme>window/2)return true;
			return false;

		}

		for(int j=i-window/2;j<i+window/2;j++){
			Cell c=tab[j];
			if(c.getProf()<min_cov){
				somme++;
			}

		}
		if(somme>window/2)return true;

		return false;
	}


}

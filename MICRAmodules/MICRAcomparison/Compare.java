import java.io.*;
import java.util.*;


/*
Compare dir1 dir2 dir3...
dirN: must contain mapping_annotations.csv and (optional) assembly_annotations.csv
*/

public class Compare

{

	/* List of input directory */
	LinkedList files;
	/* output directory */
	String path;
	/* -all option considers all the reference sequences (i.e. union) 
	else considers only common references in comparison (i.e. intersection mode)
	*/
	boolean all;



	Compare(boolean all,String path,LinkedList files){

		this.path=path;
		this.all=all;
		this.files=files;

		try{
			run();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}


	

	void run() throws IOException{

		int number=files.size();

		System.out.println("number "+number);
		PrintWriter out=new PrintWriter(new FileWriter("sortie.R"));

		/* For each strain compute the list of interest genome and plasmid*/
		LinkedList [] souches=new LinkedList[number];

		for(int i=0;i<number;i++){
			LinkedList l=new LinkedList();
			File f=new File((String)files.get(i)+"/"+"mapping_annotations.csv");
			if(f.exists()){
			BufferedReader input = new BufferedReader(new FileReader(f));
			String line="";
			while ((line = input.readLine()) != null) {
				if(line.startsWith("##")){
					String s=line.substring(3,line.length());
					l.add(s);
				}
			}
				
			input.close();
			}

			/* denovo CDS file */
			File ff=new File((String)files.get(i)+"/"+"assembly_annotations.csv");		
			if(ff.exists()){
				l.add("deNovo");
			}

			souches[i]=l;
		}
	
		for(int i=0;i<souches.length;i++){
			System.out.println("souche "+i+" "+souches[i]);
		}


		/* computing the list of genomes */
		LinkedList genomes=new LinkedList();


		if(all){

			genomes=union(souches[0],souches[1]);
			System.out.println("genome "+genomes);

			for(int i=2;i<souches.length;i++){
		
				LinkedList res=union(genomes,souches[i]);
				genomes=copyList(res);
			}


		}else{
			genomes=intersect(souches[0],souches[1]);
			System.out.println("genome "+genomes);

			for(int i=2;i<souches.length;i++){
		
				LinkedList res=intersect(genomes,souches[i]);
				genomes=copyList(res);
			}

	
		
		}

		System.out.println("liste des genomes consideres: "+genomes);


		/* for each strain, computing the gene list */
		
		String [] tab_name= new String [number];
		String [] tab_name2= new String [number];
		for(int i=0;i<number;i++){
			String f=(String)files.get(i)+"/"+"mapping_annotations.csv";
			BufferedReader input = new BufferedReader(new FileReader(f));
			PrintWriter outt=new PrintWriter(new FileWriter(i+".tmp"));

			String ref="";
			String [] seq;
			String line="";
			String name="";

			line =input.readLine();
			String temp=(String)files.get(i);
			name=temp.substring(0,(temp.length()));
			String [] seq2=name.split("/");
			name=seq2[seq2.length-1];

			tab_name[i]=name;
			tab_name2[i]="table_"+i+"$souche_"+i;
			outt.println("souche_"+i);
			int ct=0;

			while ((line = input.readLine()) != null) {

					if(line.indexOf("##")!=-1){
					/* reference change */
						ref=line.substring(3,line.length());
					}
		
					else{			
						if(genomes.indexOf(ref)!=-1){		
							if (line.indexOf(";")!=-1){
								seq=line.split(";");
								String tmp="";
								tmp=tmp+ref;

								tmp=tmp+";"+seq[0]+";"+seq[1]+";"+seq[2];
								if(!seq[4].equals("")){
									String product=getProduct(seq[4]);
									product=product.replaceAll("\'","");
									if(!product.equals("")) tmp=tmp+";"+product;
								}
			
								outt.println(tmp);
								ct++;

							}
			}
					}

		
		}
		input.close();
	
		/* deNovo*/

		File ff=new File((String)files.get(i)+"/"+"assembly_annotations.csv");		
		if(ff.exists()){
			f=(String)files.get(i)+"/"+"assembly_annotations.csv";
			input = new BufferedReader(new FileReader(f));
			while ((line = input.readLine()) != null) {

				String [] tab=line.split(";");
				if(tab.length==6){
					if((tab[3].equals("hypothetical protein"))||(tab[3].equals("Mobile element protein"))){
						outt.println("deNovo;0;0;+;"+tab[3]+":"+tab[5]);
					}else{
						outt.println("deNovo;0;0;+;"+tab[3]);
					}
				}

			}
			input.close();
		}
	
		/*********/
	
		outt.close();
		out.println("table_"+i+ " <- read.table(\""+i+".tmp\", sep=\"\\t\",header=TRUE)");
	}

	if(number<=4){
	
		out.println("library(VennDiagram);");

		out.println("venn.diagram(");
		out.print("x = list(");
		for(int i=0;i<tab_name.length;i++){
			if(i!=0){
			out.print(",");
			}
			out.print(tab_name[i]+"=table_"+i+"$souche_"+i);
		}
		out.println("),");


		String colors="";
		if(number==2){
			colors="fill = c(\"cornflowerblue\", \"darkorchid1\"),";
		}else{
			if(number==3){
				colors="fill = c(\"red\", \"blue\", \"green\"),";
			}else{
				colors="fill = c(\"cornflowerblue\", \"green\", \"yellow\", \"darkorchid1\"),";
			}

		}
	

		out.println("filename = \""+path+"/output.tiff\",");
		out.println(colors);
		out.println(");");

	}else{
		System.out.println("The number of files is superior to 4: Venn diagram cannot be drawn");
	}

	LinkedList [] combi=combinaisons(tab_name2);
	
	for(int i=0;i<combi.length;i++){
		LinkedList lt=combi[i];
		if(lt.size()==0){
		}else{
			if(lt.size()==number){
			/* intersection*/
				System.out.println(inter(lt));
				out.println("vect <- "+inter(lt)+";");
				out.println("con <- file(\""+path+"/all.txt\", open = \"w\");");
				out.println("writeLines(vect, con = con);");
				out.println("close(con);");
			}
			else{

				if(lt.size()!=0){
					String nf="";	
					for(int j=0;j<lt.size();j++){
						String tt=(String)lt.get(j);
						int e=Integer.parseInt(tt.substring(tt.lastIndexOf("_")+1,tt.length()));
						nf=nf+tab_name[e];
					}
					nf=nf+".txt";
					out.println("vect <- "+"setdiff("+inter(lt)+","+union(seconde(combi,lt))+");");
					out.println("con <- file(\""+path+"/"+nf+"\", open = \"w\");");
					out.println("writeLines(vect, con = con);");
					out.println("close(con);");
				}

			}

		}
	}


	out.close();

	/* Running R script */
	RInterface ri=new RInterface("sortie.R");
	ri.run();

	}



	static LinkedList copyList(LinkedList l){
		LinkedList res=new LinkedList();
		for(int i=0;i<l.size();i++){
			res.add(l.get(i));	
		}
		return res;
	}


	

	static String inter(LinkedList l){
		if(l.size()==0){
			return "";
		}
		if(l.size()==1){
			return (String)l.get(0);
		}
		else{
			String t="intersect("+ l.get(0) +","+ l.get(1) +")";
			LinkedList ll=new LinkedList();
			ll.add(t);
			for(int i=2;i<l.size();i++){
				ll.add(l.get(i));
			}
			return inter(ll);
		}
	}

	static String getProduct(String s){
		String seq []=s.split("&");
		for(int i=0;i<seq.length;i++){
			if(seq[i].indexOf("product=")!=-1){
				String res=seq[i].replaceAll("%","");
				return res;
			}
		}
		return "";
	}


	static String union(LinkedList l){
		if(l.size()==0){
			return "";
		}
		if(l.size()==1){
			return (String)l.get(0);
		}
		else{
			String t="union("+ l.get(0) +","+ l.get(1) +")";
			LinkedList ll=new LinkedList();
			ll.add(t);
			for(int i=2;i<l.size();i++){
				ll.add(l.get(i));
			}
			return union(ll);	
		}
	}


	static LinkedList seconde(LinkedList [] comb,LinkedList groupe){
	LinkedList res=new LinkedList();

		for(int i=0;i<comb.length;i++){
			LinkedList ll=comb[i];
			if ((ll.size()==groupe.size()+1)&&(isInsideGroupe(ll,groupe))){
				res.add("("+inter(ll)+")");
			}	
		}
		return res;
	}


	static boolean isInside(LinkedList l, String s){
		for(int i=0;i<l.size();i++){
			String t=(String)l.get(i);
			
			if(s.equals(t)){return true;}
		}
		return false;
	}

	static boolean isInsideGroupe(LinkedList l, LinkedList s){
		int c=0;
		for(int i=0;i<s.size();i++){
			String t=(String)s.get(i);
			if(isInside(l,t)){c++;}
		}
		return (c==s.size());
	}

	static LinkedList intersect(LinkedList l1,LinkedList l2){
		LinkedList res=new LinkedList();
		for(int i=0;i<l1.size();i++){
			if(l2.indexOf(l1.get(i))!=-1){

				res.add(l1.get(i));
			}
		}
		return res;
	}


	static LinkedList union(LinkedList l1,LinkedList l2){
		System.out.println(l1+";"+l2);
		LinkedList res=new LinkedList();

		for(int i=0;i<l1.size();i++){
			res.add(l1.get(i));
		}

		LinkedList res2=copyList(res);

		for(int i=0;i<l2.size();i++){
			if(res.indexOf(l2.get(i))==-1){
				res2.add(l2.get(i));
			}
		}
		return res2;

	}


	static LinkedList [] combinaisons(String [] mot){
    	int longueur=mot.length;
 	int nbr=(int)Math.pow(2,longueur);//number of combinatorial compararison
    	LinkedList [] comb=new LinkedList [nbr];
    	int k=0;
     		for(int i=0;i<nbr;i++){
			comb[i]=new LinkedList();
      			k=i;
      			for(int j=longueur-1;j>=0;j--){
      				 if(k%2==0){
       					 k/=2;
      				 }
      				 else{
       					 comb[i].add(mot[j]);
       					 k/=2;
       				}
      			}
     		}
     	return comb;
   	}



	static void afficher(LinkedList [] a){
     		for(int i=0;i<a.length;i++){
      		 	System.out.println(a[i]);
      		}
     
   	}


	static int isInside(String [] tab, String s){
		for(int i=0;i<tab.length;i++){
			if(tab[i].equals(s)) return i;
		}
		return (-1);
	}


	static void help(){
		System.out.println("Usage: java Compare directory1 <directory2> <...> [-all] [-o output directory]");
		System.out.println("-all \t [facultative] this option considers all the reference sequences (i.e. union) else only the common reference sequences (i.e. intersection)");
		System.out.println("-o \t [facultative] output directory [compa_cds]");
		System.out.println("-h \t print this help");
		
		System.exit(1);
	}


}	


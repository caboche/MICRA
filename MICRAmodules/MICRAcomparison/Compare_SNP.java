import java.io.*;
import java.util.*;


/*
Class to compare list of variant calls
*/

public class Compare_SNP{

	/*
	mode 0 SNP+DIP
	mode 1 SNP
	mode 2 DIP
	*/

	int mode;
	String path;
	LinkedList files;

	Compare_SNP(){}

	Compare_SNP(int mode,String path,LinkedList files){
		this.mode=mode;
		this.path=path;
		this.files=files;

		new File(path).mkdirs();

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
	

		/* number of compared strains*/
	
		PrintWriter out=new PrintWriter(new FileWriter("sortie.R"));

		String [] tab_name= new String [number];
		String [] tab_name2= new String [number];


		for(int i=0;i<number;i++){
			
			String f=(String)files.get(i);
		
			BufferedReader input = new BufferedReader(new FileReader(f));
			PrintWriter outt=new PrintWriter(new FileWriter(i+".tmp"));

			String [] seq;
			String line="";
			String name="";

			/* first line */
			line =input.readLine();
			String temp=(String)files.get(i);
			name=temp.substring(0,temp.lastIndexOf("."));
			input.readLine();

			String [] seq2=name.split("/");
			name=seq2[seq2.length-1];

			tab_name[i]=name;
			tab_name2[i]="table_"+i+"$souche_"+i;
			outt.println("souche_"+i);
			int ct=0;


			HashMap reads = new HashMap();

			while ((line = input.readLine()) != null) {

				seq=line.split(";");
	
		
				if(mode==0){

					if(reads.get(seq[0])==null){
						Champ c=new Champ(seq[1],seq[3]);
							if(seq.length==9){
								c.addAnnot(seq[7]);
								c.addChange(seq[8]);	
							}
							if(seq.length==8){
								c.addAnnot(seq[7]);	
							}
						reads.put(seq[0],c);
					}else{

						Champ ch=(Champ)reads.get(seq[0]);
						if(seq[1].equals("INDEL")){ch.addAllele(seq[7]);}
						else {System.out.println("position problem "+seq[0]);}
					}

				}

				if(mode==1){
					if(seq[1].equals("SNV")){
						if(reads.get(seq[0])==null){
						Champ c=new Champ(seq[1],seq[3]);
				
							if(seq.length==9){
								c.addAnnot(seq[7]);
								c.addChange(seq[8]);	
							}
							if(seq.length==8){
								c.addAnnot(seq[7]);	
							}
							reads.put(seq[0],c);
						}else{

							Champ ch=(Champ)reads.get(seq[0]);
								if(seq[1].equals("INDEL")){ch.addAllele(seq[7]);}
								else {System.out.println("position problem "+seq[0]);}
							}
						}
				}


				if(mode==2){
					if(seq[1].equals("INDEL")){
						if(reads.get(seq[0])==null){
				
							Champ c=new Champ(seq[1],seq[3]);
				
							if(seq.length==9){
								c.addAnnot(seq[7]);
								c.addChange(seq[8]);	
							}
							if(seq.length==8){
								c.addAnnot(seq[7]);	
							}
							reads.put(seq[0],c);
						}else{

							Champ ch=(Champ)reads.get(seq[0]);
							if(seq[1].equals("INDEL")){ch.addAllele(seq[7]);}
							else {System.out.println("probleme position "+seq[0]);}
						}
					}
				}


			ct++;

		
		}

		for(int p=0;p<tab_name.length;p++){
			System.out.println(tab_name[p]+" "+tab_name2[p]);		
		}


		input.close();

		int nb_reads=reads.size();

		Set cles = reads.keySet();
		Iterator it = cles.iterator();
		while (it.hasNext()){
	   		String cle = (String)it.next(); 
	   		Champ valeur = (Champ)reads.get(cle); 
			outt.print(cle+";"+valeur.getType()+";"+valeur.getAllele());
			String tmp=valeur.getAnnot();
			tmp=tmp.replaceAll("\'","");
			if(!valeur.getAnnot().equals("")){outt.print(";"+(tmp));}	
			if(!valeur.getChange().equals("")){outt.print(";"+valeur.getChange());}
			outt.println("");				
		
	
		}
	



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
			/* intersection */
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
	RInterface_SNP ri=new RInterface_SNP("sortie.R");
	ri.run();
	}






	LinkedList copyList(LinkedList l){
		LinkedList res=new LinkedList();
		for(int i=0;i<l.size();i++){
			res.add(l.get(i));	
		}
		return res;
	}


	String inter(LinkedList l){
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


	String union(LinkedList l){
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


	LinkedList seconde(LinkedList [] comb,LinkedList groupe){
	LinkedList res=new LinkedList();

	for(int i=0;i<comb.length;i++){
		LinkedList ll=comb[i];
		if ((ll.size()==groupe.size()+1)&&(isInsideGroupe(ll,groupe))){
			res.add("("+inter(ll)+")");
		}	
	}
	return res;
	}


	boolean isInside(LinkedList l, String s){
		
		for(int i=0;i<l.size();i++){
			String t=(String)l.get(i);
			
			if(s.equals(t)){return true;}
		}
		return false;
	}

	boolean isInsideGroupe(LinkedList l, LinkedList s){
		int c=0;
		for(int i=0;i<s.size();i++){
			String t=(String)s.get(i);
			
			if(isInside(l,t)){c++;}
		}
		return (c==s.size());
	}


	

	LinkedList intersect(LinkedList l1,LinkedList l2){
		LinkedList res=new LinkedList();

		for(int i=0;i<l1.size();i++){
			if(l2.indexOf(l1.get(i))!=-1){

				res.add(l1.get(i));
			}

		}

		return res;

	}

	static LinkedList [] combinaisons(String [] mot){
	    	int longueur=mot.length;
	 	int nbr=(int)Math.pow(2,longueur);//nbr=nombre de combinaisons
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
}	


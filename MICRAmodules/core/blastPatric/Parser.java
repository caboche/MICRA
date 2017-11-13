package blastPatric;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Parser{


	String in;
	String output;
	double seuil;

	Parser(){}

	Parser(String in,String output,double seuil){
		this.in=in;
		this.output=output;
		this.seuil=seuil;
		try{
			init();
		}catch (IOException e) {
			System.out.println("ERROR: Parser1");
		} 
	}





	void init() throws IOException{	

		
		LinkedList interest=new LinkedList();
		BufferedReader input = new BufferedReader(new FileReader(in));	
		PrintWriter out=new PrintWriter(new FileWriter(output));		
		HashMap cds = new HashMap();
		String l="";

		String fid="";
		String desc="";
		String deb="";
		String fin="";
		String id="";
		double cov=0;

		String [] seq;
		String [] seq2;
		String contig="";
		String length="";
		
		while ((l = input.readLine()) != null) {
			if(!l.startsWith("#")){
				if(l.startsWith("@")){
					if(!contig.equals("")){
				
						LinkedList ll=sort(cds);
						if(ll.size()!=0){
							out.println(contig);	
				
							for(int i=0;i<ll.size();i++){
								String cle=(String)ll.get(i);
								CDS c=(CDS)cds.get(cle);
								out.println(c.print());
							}

						}

					}

					contig=l;
					cds=new HashMap();
				
				}else{

					if(l.startsWith("fid")){
						seq=l.split(" ");
						seq2=seq[0].split("\\|");
						fid=seq2[3];
						desc=getDesc(l);
					}

				
					if(l.startsWith("HSP")){
						seq=l.split("\t");
						deb=seq[1].substring(8,seq[1].length());
						fin=seq[2].substring(6,seq[2].length());
						id=deb+";"+fin;


						if(cov>=seuil){
							if(cds.get(id)!=null){
								CDS c=(CDS)cds.get(id);
								c.addId(fid);
								c.setCoverage(cov);
								cds.put(id,c);
							}else{
								CDS c=new CDS(Integer.parseInt(deb),Integer.parseInt(fin),desc);
								c.addId(fid);
								c.setCoverage(cov);
								cds.put(id,c);	

							}
						}
					
					}
				

					if(l.startsWith("CDS_coverage")){
					
						String temp=l.substring(14,l.length()-1);
						cov=Double.parseDouble(temp);

					}
				

				}

			
			
		
			}
		}

	LinkedList ll=sort(cds);
		if(ll.size()!=0){
			out.println(contig);
			for(int i=0;i<ll.size();i++){
				String cle=(String)ll.get(i);
				CDS c=(CDS)cds.get(cle);
				out.println(c.print());
			}
		}		

		
	input.close();	
	out.close();

	}


	String getDesc(String l){
		String tmp=l.substring(l.lastIndexOf("|")+1,l.indexOf("["));
		tmp=tmp.trim();
		return tmp;
	}


	LinkedList sort(HashMap h){
		LinkedList res=new LinkedList();
		Set cles = h.keySet();
		Iterator it = cles.iterator();
		while (it.hasNext()){
		   	String cle = (String)it.next(); 
			addElt(cle,res);

		}
		return res;
	}



	void addElt(String s,LinkedList l){

		if(l.size()==0){
			l.add(s);
			return;
		}

		String [] seq=s.split(";");
		int deb1=Integer.parseInt(seq[0]);
		int fin1=Integer.parseInt(seq[1]);
		int index=0;

		if(l.size()==1){
			String s2=(String)l.get(0);
			String [] seq2=s2.split(";");
			int deb2=Integer.parseInt(seq2[0]);
			int fin2=Integer.parseInt(seq2[1]);
			if(deb1<deb2){
				l.add(0,s);	return;
			}else{l.add(s);return;}
		}


		int last=0;
		int last2=0;
		for(int i=0;i<l.size();i++){
			String s2=(String)l.get(i);
			String [] seq2=s2.split(";");
			int deb2=Integer.parseInt(seq2[0]);
			int fin2=Integer.parseInt(seq2[1]);
			if(deb1>deb2){
				index=i;
			}else{
				if(deb1==deb2){
					if(fin1>fin2){
						index=i;
				
					}else{
						index=i;
						last=deb2;
						last2=fin2;
						break;
				
					}
				}else{
					index=i;
					last=deb2;
					last2=fin2;
					break;
				}

			}


		}

		if (index==0){l.addFirst(s);return;}
		if (index!=(l.size()-1))l.add(index,s);
		else {
			if(deb1<last){l.add(index,s);}
			else{
				if(deb1==last){
					if(fin1<last2){
						l.add(index,s);
					}	else{
				l.addLast(s);
				}
			
				}else{
				l.addLast(s);
				}
		}	}

	}





	
}


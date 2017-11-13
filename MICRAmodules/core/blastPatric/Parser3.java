package blastPatric;

import java.io.*;
import java.util.*;
import java.util.regex.*;

public class Parser3
{

	String in;
	String output;
	String csv;

	Parser3(){}


	Parser3(String in,String output,String csv){
		this.in=in;
		this.output=output;
		this.csv=csv;
		try{
			init();
		}catch (IOException e) {
        		System.out.println("ERROR: ParserHTML");
		} 

	}



	void init() throws IOException 
	{	

		LinkedList interest=new LinkedList();
		
		BufferedReader input = new BufferedReader(new FileReader(in));
		
		
		PrintWriter out=new PrintWriter(new FileWriter(output));
		PrintWriter outcsv=new PrintWriter(new FileWriter(csv));
		PrintWriter out2=new PrintWriter(new FileWriter("tmp.html"));
		
		out.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">");
		out.println("<html>");
		out.println("<head>");
		out.println("<title>BLAST results</title>");
		out.println("</head>");
		out.println("<body>");
		out.println("BLAST results with <i>de novo</i> generated contigs.");
		
		String l="";
		String contig="";

		String [] seq;
		LinkedList cds=new LinkedList();
		int unique=0;
		
		boolean first=false;

		
		while ((l = input.readLine()) != null) {
			if(l.startsWith("@")){
				if(first){
					if(cds.size()==0){
						out.println("no hit found");
						outcsv.println(contig);
					}else{
						out.println("<table BORDER=\"1\">");
						out.println("<TR>");
		 				out.println("<TH> begin </TH>"); 
						out.println("<TH> end </TH>"); 
						out.println("<TH> length </TH>"); 
						out.println("<TH> #CDS </TH>"); 
						out.println("<TH> description </TH>"); 
						out.println("<TH> ID </TH>");
						out.println("<TH> coverage (%) </TH>");
		 				out.println("</TR>"); 


						for(int i=0;i<cds.size();i++){
							CDS cc=(CDS)cds.get(i);
							/* */
							out.println(cc.printHTML(unique));
							outcsv.println(contig+";"+cc.printCsv(unique));
							LinkedList ll=cc.annotation;
					
								out2.println("<balise id=\""+unique+"\">");
								out2.println("<h3><font color=\"#046380\"> contig <i>"+contig+"</i> positions: "+cc.beg+"-"+cc.end+"</font></h3>");
								out2.println("<table BORDER=\"1\">");
								out2.println("<TR>");
		 						out2.println("<TH> begin </TH>"); 
								out2.println("<TH> end </TH>"); 
								out2.println("<TH> length </TH>"); 
								out2.println("<TH> coverage (%)</TH>"); 
								out2.println("<TH> description </TH>"); 
								out2.println("<TH> ID </TH>"); 
		 						out2.println("</TR>"); 
								out2.println(cc.printAnnot());
								if(ll.size()!=0){
						
								for(int j=0;j<ll.size();j++){
									CDS ccc=(CDS)ll.get(j);
							
									out2.println(ccc.printAnnot());
								}
						
								}
								out2.println("</table>");
								out2.println("</balise>");
						
								unique++;
					
						}
						out.println("</table>");
				
						cds=new LinkedList();
					}
				}
			
				out.println("<h2><font color=\"#046380\">"+l.substring(1,l.length())+"</font></h2>");	
				first=true;
				contig=l.substring(1,l.length());
			

				}else{
					seq=l.split("\t");
					String tmp=seq[3];
					tmp=tmp.replace(',','.');
				
					CDS c=new CDS(Integer.parseInt(seq[0]),Integer.parseInt(seq[1]),seq[4],Double.parseDouble(tmp),seq[5].substring(1,seq[5].length()-1));
					update(cds,c);
				}
			}

				if(cds.size()==0){
					out.println("no hit found");
					outcsv.println(contig);
				}else{
					out.println("<table BORDER=\"1\">");
					out.println("<TR>");
			 		out.println("<TH> begin </TH>"); 
					out.println("<TH> end </TH>"); 
					out.println("<TH> length </TH>"); 
					out.println("<TH> #CDS </TH>"); 
					out.println("<TH> description </TH>"); 
					out.println("<TH> IDs </TH>"); 
					out.println("<TH> coverage (%) </TH>");
			 		out.println("</TR>"); 


					for(int i=0;i<cds.size();i++){
						CDS cc=(CDS)cds.get(i);

						out.println(cc.printHTML(unique));
						outcsv.println(contig+";"+cc.printCsv(unique));
						LinkedList ll=cc.annotation;
					
							out2.println("<balise id=\""+unique+"\">");
							out2.println("<h3> <font color=\"#046380\">contig <i>"+contig+"</i> positions: "+cc.beg+"-"+cc.end+"</font></h3>");
							out2.println("<table BORDER=\"1\">");
							out2.println("<TR>");
	 						out2.println("<TH> begin </TH>"); 
							out2.println("<TH> end </TH>"); 
							out2.println("<TH> length </TH>"); 
							out2.println("<TH> coverage (%) </TH>"); 
							out2.println("<TH> description </TH>"); 
							out2.println("<TH> ID </TH>"); 
	 						out2.println("</TR>"); 
							out2.println(cc.printAnnot());
							if(ll.size()!=0){
						
							for(int j=0;j<ll.size();j++){
								CDS ccc=(CDS)ll.get(j);
							
								out2.println(ccc.printAnnot());
							}
						
							}
							out2.println("</table>");
							out2.println("</balise>");
						
							unique++;
					
					}
					out.println("</table>");
				
					cds=new LinkedList();
			}
			
		
		
		
			for(int i=0;i<cds.size();i++){
				CDS cc=(CDS)cds.get(i);
				out.println(cc.print2());
			}

		
		input.close();
		out2.close();

		/*copy of tmp.html in the principal file */

		BufferedReader in = new BufferedReader(new FileReader("tmp.html"));
		out.println("<br><br> ********************************************************************************************************** <br><br>");
		while ((l = in.readLine()) != null) {
			out.println(l);
		}
		
		in.close();

		File f=new File("tmp.html");
		f.delete();


		out.println("</body>");
		out.println("</html>");
		
		out.close();
		outcsv.close();
	
	}


	static void update(LinkedList l,CDS c){


		if(l.size()==0){l.add(c);return;}
		boolean b=false;
		for(int i=0;i<l.size();i++){
			CDS cds=(CDS)l.get(i);
			if(c.estIncluse(cds)){
		
				cds.setBeg(c.beg);
				cds.setEnd(c.end);
				cds.addAnnot(c.desc,c);
				cds.setDesc(c.desc);
		
		
				b=true;
			}else{
				if(cds.estIncluse(c)){
					cds.addAnnot(c.desc,c);
					b=true;
				}else{

					/* overlap */
					if(cds.overlap(c)){
						if(c.coverage>cds.coverage){
							cds.setBeg(c.beg);
							cds.setEnd(c.end);
							cds.addAnnot(c.desc,c);
							cds.setDesc(c.desc);
						}else{
							cds.addAnnot(c.desc,c);
						}
						b=true;
					}
			
				}
			}
		}
		if(!b)l.add(c);

	}




	
}


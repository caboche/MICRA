import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.DecimalFormat;

public class Parser{


	String in;
	PrintWriter out;
	String title;
	double seuil;

	LinkedList antibio;
	LinkedList resume;

	HashMap DB;
	HashMap ARDB;
	HashMap arbg;
	HashMap require;
	HashMap origin;
	PrintWriter log;


	double cov_db;
	double cov_ardb;
	double iden_db;
	double iden_ardb;


	LinkedList id_ardb;
	String requirement;

	String path;

	Parser(){}

	Parser(String in,PrintWriter out,String title,double seuil,LinkedList antibio,LinkedList resume,double cov_db,double cov_ardb,double iden_db,double iden_ardb,PrintWriter log,String path){
	
		this.in=in;
		this.out=out;
		this.title=title;
		this.seuil=seuil;
		this.antibio=antibio;
		DB=new HashMap();
		ARDB=new HashMap();
		arbg=new HashMap();
		require=new HashMap();
		origin=new HashMap();
		id_ardb=new LinkedList();
		this.resume=resume;

		this.cov_db=cov_db;
		this.cov_ardb=cov_ardb;
		this.iden_db=iden_db;
		this.iden_ardb=iden_ardb;
		this.log=log;
		this.path=path;


		if(title.indexOf("ARDB")!=-1){
		requirement="<ul>";
		}else{
			requirement="";
		}
	
		try{
			initHashMap();
			if(title.indexOf("ARDB")!=-1){
				initHashMap2();
				initHashMap3();
				initHashMap4();
			
			}
			init();
			resume();
		
		
		}catch (IOException e) {
			System.out.println("ERROR: Parser");
		} 
	}

	void initHashMap() throws IOException {
			BufferedReader input = new BufferedReader(new FileReader("antibio_files/drugBank_ids.txt"));
			String l="";
			input.readLine();
			while ((l = input.readLine()) != null) {
				String [] seq=l.split("@");
				DB.put(seq[0],seq[1].toLowerCase());
			}

			input.close();
			input = new BufferedReader(new FileReader("antibio_files/ARDB_ids_V4.txt"));
			while ((l = input.readLine()) != null) {
				String [] seq=l.split("@");
				ARDB.put(seq[0],seq[1]);
			}
			input.close();

	}


	void initHashMap2() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader("antibio_files/abrg.tab"));
		String l="";
		while ((l = input.readLine()) != null) {
			String [] seq=l.split("\t");
			if(arbg.get(seq[0])!=null){
				System.out.println("deja dedans "+seq[0]);
			}
			arbg.put(seq[0],seq[1].toLowerCase());
		}

		input.close();
	}


	void initHashMap3() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader("antibio_files/type2rq.tab"));
		String l="";
		while ((l = input.readLine()) != null) {
			String [] seq=l.split("\t");
			String id=seq[0].toLowerCase();
			if(require.get(id)==null){
				require.put(id,seq[1].toLowerCase());
			}else{
				String tmp=(String)require.get(id);
				tmp=tmp+";"+seq[1].toLowerCase();
				require.put(id,tmp);

			}
			
		}

		input.close();

	}


	void initHashMap4() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader("antibio_files/origin_type.tab"));
		String l="";
		while ((l = input.readLine()) != null) {
			String [] seq=l.split("\t");
			String id=seq[0].toLowerCase();
			if(origin.get(id)==null){
				origin.put(id,Integer.parseInt(seq[3]));
			}else{
				System.out.println("doublon "+id);

			}
			
		}

		input.close();

	}


	


	boolean getRes(LinkedList l){
		
		boolean res=false;
		for(int i=0;i<l.size();i++){
			String tmp="<li>";
			String r=(String)l.get(i);
			
			int s_ardb=90;

				if((Integer)origin.get(res)!=null){
					s_ardb=(Integer)origin.get(r);
				}

			double iden_tmp=0;
			if(iden_ardb==-1){
				iden_tmp=s_ardb;
			}else{
				iden_tmp=iden_ardb;
			}
			
			if(require.get(r)==null){res=true;
				tmp=tmp+"<a href=\"http://ardb.cbcb.umd.edu/cgi/ssquery.cgi?db=T&gn="+r+"\"target=_blank>"+r+"</a> "+"</a> ("+ iden_tmp+"%)  <img src=\"true.png\" width=\"30\" \\> "+" (no requirement) ";
			}
		
			else{
				String tmpe=(String)require.get(r);
				tmp=tmp+"<a href=\"http://ardb.cbcb.umd.edu/cgi/ssquery.cgi?db=T&gn="+r+"\"target=_blank>"+r+"</a>"+ "</a> ("+ iden_tmp +"%) <img src=\"true.png\" width=\"30\" \\> "+" requires ";
				String [] tab=tmpe.split(";");
				int ct=0;
				for(int j=0;j<tab.length;j++){
					

					int s_ardb2=90;
					if((Integer)origin.get(tab[j])!=null){
						s_ardb2=(Integer)origin.get(tab[j]);
					}
					

					double iden_tmp2=0;
					if(iden_ardb==-1){
						iden_tmp2=s_ardb2;
					}else{
						iden_tmp2=iden_ardb;
					}


			
					if(l.indexOf(tab[j])!=-1){
						ct++;
						tmp= tmp+"<a href=\"http://ardb.cbcb.umd.edu/cgi/ssquery.cgi?db=T&gn="+tab[j]+"\"target=_blank>"+tab[j]+"</a> ("+ iden_tmp2+"%) <img src=\"true.png\" width=\"30\" \\> ";
					}else{
						tmp= tmp+"<a href=\"http://ardb.cbcb.umd.edu/cgi/ssquery.cgi?db=T&gn="+tab[j]+"\"target=_blank>"+tab[j]+"</a>("+ iden_tmp2+"%) <img src=\"false.png\" width=\"30\" \\> ";
					}

				}
				if(ct==tab.length)res=true;
			}
		tmp=tmp+"</li>";
		
		requirement=requirement+"\n"+tmp;
		}
		requirement=requirement+"</ul>";
		return res;
	}



	void resume() throws IOException{

		/* update resitances */
		if(title.indexOf("ARDB")!=-1){
			for(int i=0;i<antibio.size();i++){
				requirement="<ul>";
				Couple c=(Couple)resume.get(i);
			
				c.ardb=getRes(c.resistance);
			
				c.setRequirement(requirement);

			}
		}


		File f1=new File("antibio_files/false.png");
		File f2=new File(path+"false.png");
		copyFile(f1,f2);

		f1=new File("antibio_files/true.png");
		f2=new File(path+"true.png");
		copyFile(f1,f2);

		f1=new File("antibio_files/nd.png");
		f2=new File(path+"nd.png");
		copyFile(f1,f2);
		

		PrintWriter output=new PrintWriter(new FileWriter(path+"antibiogram.html"));
		output.println("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01//EN\">");
		output.println("<html>");
		output.println("<head>");
		output.println("<title>In silico antibiogram</title>");
		output.println("</head>");
		output.println("<body>");


		output.println("<h1> <i>In silico</i> drug susceptibility prediction  </h1>");
		output.println("<table BORDER=\"1\">");
		output.println("<tr><td>drug</td><td> DrugBank match </td><td>ARDB match</td><td>prediction</td><td> ARDB Resistance types</td></tr>");
		
		for(int i=0;i<antibio.size();i++){
			String a=(String)antibio.get(i);
			Couple c=(Couple)resume.get(i);
			boolean db=c.db;
			boolean ardb=c.ardb;
			String sensi="Not sensitive";
			String image_db="nd.png";
			String image_ardb="nd.png";

			if((c.nb_db==0)||(c.nb_ardb==0)){
				if(c.nb_db!=0){image_db=db+".png";sensi="Not defined";}
				else{
					if(c.nb_ardb!=0){image_ardb=ardb+".png";sensi="Not defined";}
					else{sensi="Not defined";}				
				}
			}else{

			
			if((db==true)&&(ardb==true)){image_db="true.png";image_ardb="true.png";sensi="Resistant";}
			if((db==true)&&(ardb==false)){image_db="true.png";image_ardb="false.png";sensi="Sensitive";}
			if((db==false)&&(ardb==true)){image_db="false.png";image_ardb="true.png";sensi="Not sensitive";}
			if((db==false)&&(ardb==false)){image_db="false.png";image_ardb="false.png";sensi="Not sensitive";}
			}
			

			output.println("<tr><td>"+a+"</td><td><img src=\""+image_db+"\" width=\"90\" \\></td>"+"<td><img src=\""+image_ardb+"\" width=\"90\" \\></td><td>"+sensi+"</td><td>"+ c.requirement+"</td></tr>");

		}


		output.println("</table>");
		output.println("</body>");
		output.println("</html>");


		output.close();

	}



	void init() throws IOException {	

		
		LinkedList interest=new LinkedList();
		
		BufferedReader input = new BufferedReader(new FileReader(in));		
		HashMap prot = new HashMap();

		out.println("<balise id=\""+title+"\">");
		out.println("<H1>"+title+"</H1>");
		
		String l="";
		
		String fid="";
		String desc="";
		
		double cov=0;
		int len=0;

		String [] seq;
		String [] seq2;
		String name="";

		LinkedList hsp=new LinkedList();
	
		while ((l = input.readLine()) != null) {
			if(!l.startsWith("#")){
			if(l.startsWith("@")){
				name=l;	
				fid="";				
			}else{
				if(l.startsWith("********")){


					HitBis hit=new HitBis(name,cov,hsp);
					if(prot.get(fid)!=null){
						Prot p=(Prot)prot.get(fid);
						p.addHit(hit);
						prot.put(fid,p);
						
					}else{
						Prot p=new Prot(desc,len);
						p.addHit(hit);
						prot.put(fid,p);
						

					}
				
				}
				if(l.startsWith("id:")){

					if(title.indexOf("T3DB")!=-1){
						l=input.readLine();
						seq=l.split(" ");
					
					
						seq2=seq[0].split("\\|");
						fid=seq2[1];
						desc=getDescT3DB(l);

					}

					if(title.indexOf("DrugBank")!=-1){
						l=input.readLine();
						
						seq=l.split("\\|");
						fid=seq[0];
						
						desc=getDescDB(l);

					}

					if(title.indexOf("ARDB")!=-1){
						fid=l.substring(l.indexOf(":")+1,l.length());
						l=input.readLine();

						desc=getDescARDB(l);
					}
					
					
					
					hsp=new LinkedList();
				}

				



				if(l.startsWith("HSP")){
					
					hsp.add(new Hsp(l));

					
				}
				

				if(l.startsWith("prot_coverage")){
					
					String temp=l.substring(14,l.length()-1);
					cov=Double.parseDouble(temp);

				}
				if(l.startsWith("prot_length:")){
					
					String temp=l.substring(12,l.length());
					len=Integer.parseInt(temp);

				}
			
			}
		
			}
		}

		if(!fid.equals("")){
		HitBis hit=new HitBis(name,cov,hsp);
					if(prot.get(fid)!=null){
						Prot p=(Prot)prot.get(fid);
						p.addHit(hit);
						prot.put(fid,p);
						
					}else{
						Prot p=new Prot(desc,len);
						p.addHit(hit);
						prot.put(fid,p);
						

					}		

		}		

		out.println("<table BORDER=\"1\">");
		out.println("<tr><td>contig name</td><td> target coverage </td><td>positions</td><td>%iden</td><td>%sim</td></tr>");
		Set cles = prot.keySet();
		Iterator it = cles.iterator();

		int c=0;
		while (it.hasNext()){
   			String cle = (String)it.next(); 
			Prot p=(Prot)prot.get(cle);
			
			c++;
			if(title.indexOf("T3DB")!=-1){
			
			out.println("<TR><td  bgcolor=\"#EAEAEA\" align=\"center\"><b>"+p.desc+" [target_id:"+cle+"] L:"+p.len+"</b></td>");
			}
			if(title.indexOf("ARDB")!=-1){

			String id=getId(p.desc);
			String res=(String)arbg.get(id);
			
			
			int s_ardb=90;
			
			if((Integer)origin.get(res)!=null){
				s_ardb=(Integer)origin.get(res);
			}

			double iden_tmp=0;
			if(iden_ardb==-1){
				iden_tmp=s_ardb;
			}else{
				iden_tmp=iden_ardb;
			}

			String match=p.print2(seuil,cov_ardb,iden_tmp);
			
			
			if(!match.equals("")){
				
					out.println("<TR><td  colspan=\"5\" bgcolor=\"#EAEAEA\" align=\"center\"><FONT size= \"2\">"+p.desc+" L:"+p.len+"</FONT></td>");
					out.print(match);
				
				if(p.is_resume){
					
					
					setResume(p.desc,"ardb");
				
					id_ardb.add(getId(p.desc));
				}
				}
			

			}
			if(title.indexOf("DrugBank")!=-1){
			
			String match=p.print2(seuil,cov_db,iden_db);
			if(!match.equals("")){
				out.println("<TR><td  colspan=\"5\" bgcolor=\"#EAEAEA\" align=\"center\"><b>"+p.desc+" L:"+p.len+"</b></td>");
				out.print(match);
				if(p.is_resume){setResume(p.desc,"drugbank");}
				}
			}	

		}
		out.println("</table>");
		out.println("#match "+c);
		
		input.close();	
	
	}


	String getId(String l){
		String res="";

		res=l.substring(l.indexOf("target=_blank>")+14,l.indexOf("</a>"));


		return res;
	}


	void setResume(String l,String db){
		String [] tmp=l.split("<b>");

		String [] seq=new String[tmp.length-1];
		for(int i=1;i<tmp.length;i++){
			seq[i-1]=tmp[i];
		}
		for(int i=0;i<seq.length;i++){

			seq[i]=seq[i].substring(0,seq[i].indexOf("</b>"));
		}

		for(int i=0;i<antibio.size();i++){
			String a=(String)antibio.get(i);
			if(isInTab(a,seq)){

				Couple c=(Couple)resume.get(i);
				if(db.equals("drugbank")){
					c.setDB();
			
				}
				if(db.equals("ardb")){
					c.setARDB();
					String id=getId(l);
					String res=(String)arbg.get(id);
					if(res!=null){
						c.addRes(res);
					}
			
			
	
				}
				resume.set(i,c);
			}

		}

	}

	boolean isInTab(String s,String [] tab){
		for(int i=0;i<tab.length;i++){



			String a=tab[i];
			String tmp="";
			if(a.endsWith("e")){
				tmp=a.substring(0,a.length()-1);
			}else{
				tmp=a+"e";
			}
			if(a.equalsIgnoreCase(s))return true;
			if(tmp.equalsIgnoreCase(s))return true;




		}
		return false;
	}


	String getDescT3DB(String l){
		String res="";
		String tmp=l.substring(l.lastIndexOf("|")+1,l.length());
		tmp=tmp.trim();
		res=res+l.substring(l.indexOf(" ")+1,l.indexOf("("))+" (";
		tmp=l.substring(l.lastIndexOf("(")+1,l.lastIndexOf(")"));
		String [] t=tmp.split(";");
		for(int i=0;i<t.length;i++){
		String val=t[i].trim();
		val="<a href=\"http://www.t3db.org/toxins/"+val+"\"  target=_blank>"+val+"</a>";
		res=res+val+" ";
		}
		res=res+" )";
		return res;

	}


	String getDescDB(String l){

		String [] seq=l.split("\\|");
		String res="";
		String tmp=l.substring(l.lastIndexOf("|")+1,l.length());
		tmp=tmp.trim();

		res="<a href=\"http://www.ncbi.nlm.nih.gov/protein/"+seq[0]+"\" target=_blank>"+seq[0]+"</a>";
		res=res+"|"+seq[1]+"|"+seq[2]+"|";

		String [] t=tmp.split(";");
		for(int i=0;i<t.length;i++){
		String val=t[i].trim();
		String comp=(String)DB.get(val);


		if(comp==null){
		val="<a href=\"http://www.drugbank.ca/drugs/"+val+"\"  target=_blank>"+val+"</a>";
		res=res+val+" ";
		}else{
			if(isInside(comp)){
				val="<a href=\"http://www.drugbank.ca/drugs/"+val+"\"  target=_blank><font color=\"red\"><b>"+comp+"</b></font></a> ";
			}else{
				val="<a href=\"http://www.drugbank.ca/drugs/"+val+"\"  target=_blank>"+comp+"</a> ";
			}
		res=res+val+" ";
		}
		}

		return res;

	}


	String getDescARDB(String l){

		String [] seq=l.split("\\|");
		String res="";
		String tmp=l.substring(l.lastIndexOf("|")+1,l.length());
		tmp=tmp.trim();

		res="<a href=\"http://www.ncbi.nlm.nih.gov/protein/"+seq[0]+"\" target=_blank>"+seq[0]+"</a>";
		res=res+"|"+seq[1]+"|"+seq[2]+"|";

		String [] v=seq[3].split(";");
		for(int i=0;i<v.length;i++){

			if(isInside(v[i])){
				res=res+"<a href=\"http://ardb.cbcb.umd.edu/cgi/search.cgi?db=R&and0=O&submit=Search&term="+seq[0]+"&field=af&\" target=_blank><font color=\"red\"><b>"+v[i]+"</b></font></a> ";
			}else{
				res=res+"<a href=\"http://ardb.cbcb.umd.edu/cgi/search.cgi?db=R&and0=O&submit=Search&term="+seq[0]+"&field=af&\" target=_blank>"+v[i]+"</a> ";
			}
		}


		return res;

	}


	public static boolean copyFile(File source, File dest){
		try{
			java.io.FileInputStream sourceFile = new java.io.FileInputStream(source);
	 
			try{
				java.io.FileOutputStream destinationFile = null;
	 
				try{
					destinationFile = new FileOutputStream(dest);
	 
				
					byte buffer[] = new byte[512 * 1024];
					int nbLecture;
	 
					while ((nbLecture = sourceFile.read(buffer)) != -1){
						destinationFile.write(buffer, 0, nbLecture);
					}
				} finally {
					destinationFile.close();
				}
			} finally {
				sourceFile.close();
			}
		} catch (IOException e){
			e.printStackTrace();
			return false; 
		}
	 
		return true; 
	}


	boolean isInside(String l){
		for(int i=0;i<antibio.size();i++){
			String a=(String)antibio.get(i);
			String tmp="";
			if(a.endsWith("e")){
				tmp=a.substring(0,a.length()-1);
			}else{
				tmp=a+"e";
			}
			if(a.equalsIgnoreCase(l))return true;
			if(tmp.equalsIgnoreCase(l))return true;
		}
		return false;
	}


	LinkedList sort(HashMap h){

		LinkedList res=new LinkedList();
		Set cles = h.keySet();
		Iterator it = cles.iterator();
		while (it.hasNext()){
		   	String cle = (String)it.next(); 
	
			res.add(cle);

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


import java.io.*;
import java.util.*;

public class Data
{

/*
Download missing sequences
*/

String project;
String sep;
String file;
String path;

Data(){}

Data(String file,String path){
this.file=file;
this.path=path;
project="";
sep="/";

try{
		run();
	}
	catch(IOException ioe) {
		ioe.printStackTrace();
	}

}



	void  run ()	throws IOException{

		LinkedList ll=new LinkedList();
		LinkedList lll=new LinkedList();
		BufferedReader input = new BufferedReader(new FileReader(file));
		String l="";
		String [] seq;

		while ((l = input.readLine()) != null) {
			System.out.println(l);
			seq=l.split(";");
			Couple c=new Couple(seq[0],seq[1]);
			if(seq[2].equals("genome"))project=path+"reference_genomes";
			if(seq[2].equals("plasmid"))project=path+"reference_plasmids";

			File rep=new File(project);
					if(!rep.exists()){
						rep.mkdirs();
					}

			PerlWGS p=new PerlWGS(seq[0]);
			String id=p.run();		
		
			if(!id.equals("")){

				System.out.println("INIT id "+id);
				if(id.indexOf("_")!=-1){
					id=id.substring(id.indexOf("_")+1,id.length());
				
				
				
				}

				String nb="1";
				if(id.indexOf(".")!=-1){
					nb=id.substring(id.indexOf(".")+1,id.length());
				}
				id=id.substring(0,6);
				if (id.charAt(5)=='0'){

					id=id.substring(0,5)+nb;
				}
			
				id=id+".1";			
				System.out.println("WGS");
				System.out.println("new WGS ID "+id);
				new WGS(id,seq[1],project,sep);
			}
			else{
				ll.add(c);
				lll.add(project);
			}
		}

		input.close();

		Couple [] tab=new Couple[ll.size()];
		String [] tab2=new String[lll.size()];
		for(int i=0;i<tab.length;i++){
			tab[i]=(Couple)ll.get(i);
			tab2[i]=(String)lll.get(i);

		}
		new Entry(tab,tab2,sep,path);

	}



}

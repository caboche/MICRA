import java.io.*;
import java.util.*;
import java.text.*; 

public class TraitContig{

	String project;
	String sep;
	PrintWriter log;
	int size;
	int end;

	TraitContig(){}

	TraitContig(String project,String sep,int size,PrintWriter log){
		this.project=project;
		this.sep=sep;
		this.size=size;
		this.log=log;

		end=0;

		try{
			trait();
		}catch (IOException e) {
		       	System.out.println("ERROR: TraitContigs");
		} 

	}

	void trait() throws IOException {
		BufferedReader input = new BufferedReader(new FileReader(project+"deNovo_contigs.fa"));
		PrintWriter out=new PrintWriter(new FileWriter(project+"denovo_contigs.fa"));

		String l="";
		String tmp="";

		int total=0;

		int s=0;

		while ((l = input.readLine()) != null) {
			if(l.startsWith(">")){

				if(!l.startsWith(">unmapped")){
					String [] seq=l.split("_");
					if(seq[0].equals(">NODE")){
						l=">unmapped_c"+seq[1];
					}else{
						l=">unmapped_c"+seq[seq.length-1];
					}
				}

				total++;
				if(!tmp.equals("")){
					if(s>=size){
						out.print(tmp);
						end++;
			
					}
				}
				tmp=l+"\n";
				s=0;

			}else{
				tmp=tmp+l+"\n";
				s=s+l.length();
			}
		}


		if(!tmp.equals("")){
			if(s>=size){
				out.print(tmp);
				end++;
			}
		}


		System.out.println("deNovo contigs >"+size+" "+total+" -> "+end);
		log.println("deNovo contigs >"+size+" "+total+" -> "+end);

		input.close();
		out.close();


	}


}

import java.io.*;
import java.util.*;

public class Gff{

/*
from the gbff file containing all the WGS genbank files
convert gbff in temporary.gff
from the tempory.gff create the final gff
*/

	String file;
	String rep;
	String sep;
	String name;
	LinkedList sizes;

	Gff(){}

	Gff(String file,String name,String rep,String sep,LinkedList sizes){
		this.file=file;
		this.name=name;
		this.rep=rep;
		this.sep=sep;
		this.sizes=sizes;

		try{
			run();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}


	}


	void run() throws IOException{


		String l="";
		BufferedReader in = new BufferedReader(new FileReader(file));
		PrintWriter output=new PrintWriter(new FileWriter("temp.gb"));
		PrintWriter outfile=new PrintWriter(new FileWriter("tempory.gff"));

		while ((l = in.readLine()) != null) {
			if(l.equals("//")){
				output.close();
				PerlInterface4 pi4=new PerlInterface4("temp.gb",outfile);
				pi4.run();
				output=new PrintWriter(new FileWriter("temp.gb"));
	
			}else{
				output.println(l);

			}
		}


		in.close();
		output.close();
		outfile.close();

		BufferedReader input = new BufferedReader(new FileReader("tempory.gff"));
		PrintWriter out=new PrintWriter(new FileWriter(rep+sep+name+".gff"));

		String [] seq;
		String line="";

		out.println("# Input: "+file);
		out.println("##gff-version 3");

		int c=0;
		int pred=0;
		int length=0;
		boolean change=false;

		while ((line = input.readLine()) != null) {

			if (line.charAt(0)!='#'){
				String tmp="";
				seq=line.split("\t");

				if(change){
					pred=pred+length;

					if(c<sizes.size()){
						length=(Integer)sizes.get(c);
					}

					change=false;
					c++;
				}

				tmp=tmp+name+"\t";
				tmp=tmp+seq[1]+"\t";
				tmp=tmp+seq[2]+"\t";
				int d=Integer.parseInt(seq[3]);
				tmp=tmp+(d+pred)+"\t";
				int f=Integer.parseInt(seq[4]);
				tmp=tmp+(f+pred);
				for(int i=5;i<seq.length;i++){
					tmp=tmp+"\t"+seq[i];
				}
				out.println(tmp);
			}
			else{change=true;}
		}

		input.close();
		out.println("# GFF3 saved to stdout");
		out.close();

		/* delete temp files */
		File fi=new File("temp.gb");
		fi.delete();
		fi=new File("tempory.gff");
		fi.delete();


	}
}

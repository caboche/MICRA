import java.io.*;
import java.util.*;

public class Fasta{

	String file;
	String rep;
	String sep;
	String name;
	LinkedList sizes;

	Fasta(){
	}

	Fasta(String file,String name,String rep,String sep){
	this.file=file;
	this.rep=rep;
	this.sep=sep;
	this.name=name;
	sizes=new LinkedList();
		try{
			run();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}

	}

	void run()throws IOException{


		BufferedReader input = new BufferedReader(new FileReader(file));
		PrintWriter out=new PrintWriter(new FileWriter(rep+sep+name+".fa"));
		out.println(">"+name);

		String l="";
		int c=0;
		int len=0;

		while ((l = input.readLine()) != null) {
			if(l.charAt(0)!='>'){
				out.println(l);
				len=len+l.length();
			}else{
				c++;
				if(len!=0){
					sizes.add(len);
				}
				len=0;
				}
		
		}

		input.close();
		out.close();
	}

}

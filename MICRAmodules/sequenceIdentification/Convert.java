import java.io.*;
import java.util.*;

public class Convert
{

/*
Converting fasta files used in WGS class
*/

	String rep;
	String sep;
	String name;



	Convert(){}

	Convert(String name,String rep,String sep){
	
	
		this.name=name;
		this.rep=rep;
		this.sep=sep;
	

		try{
			run();
		}
		catch(IOException ioe) {
			ioe.printStackTrace();
		}


	}


	void run() throws IOException{

		File fi=new File(rep+sep+name+".fa");
		fi.delete();
		String l="";

		BufferedReader in = new BufferedReader(new FileReader(rep+sep+name+".fa.fasta"));
		PrintWriter out=new PrintWriter(new FileWriter(rep+sep+name+".fa"));

		while ((l = in.readLine()) != null) {

			if(l.startsWith(">")){
				out.println(">"+name);
			}else{
				out.println(l);
			}
		}


		in.close();
		out.close();

		fi=new File(rep+sep+name+".fa.fasta");
		fi.delete();


	}
}

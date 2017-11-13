import java.io.*;
import java.util.*;
import java.text.*; 

/* Detection of adapters from FastQC results */

public class Parser
{

	String file;
	String adapters="";
	PrintWriter log;

	double threshold;


	Parser(){}

		Parser(String file,PrintWriter log,double threshold){
		this.file=file;
		this.log=log;
	
		this.threshold=threshold;

		try{
			System.out.println("Detection of adaptater(s)");
			log.println("Detection of adaptater(s)");	
			parse();
		
		}
		catch (IOException e) {
	       		System.out.println("ERROR: parsing FASTQC file");
		} 
	}

	void parse() throws IOException {

		BufferedReader input = new BufferedReader(new FileReader(file));
	
		String l="";
	
		while ((l = input.readLine()) != null) {
			if(l.startsWith(">>Overrepresented sequences")){
				if(l.indexOf("pass")==-1){
					l=input.readLine();
					l=input.readLine();
					do{
				
						String [] seq=l.split("\t");
					
					
						double percent=Double.parseDouble(seq[2]);
						if((percent>=threshold)&&(!seq[3].equals("No Hit"))){
							adapters=adapters+seq[0]+",";
							log.println(l);
							System.out.println(l);
						}
						l=input.readLine();
					}while(!l.startsWith(">>END_MODULE"));
		
				}
			}
		
		}
		if(adapters!=""){
			adapters=adapters.substring(0,adapters.length()-1);
		}
		input.close();
	
	}




}

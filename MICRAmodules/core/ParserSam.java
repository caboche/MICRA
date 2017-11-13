import java.io.*;
import java.util.*;
import java.text.*; 

public class ParserSam{

	String sam;

	ParserSam(){}

	ParserSam(String sam){
		this.sam=sam;
		try{
			parse();	
		}
		catch (IOException e) {
	       		System.out.println("ERROR: parsing sam file");
		} 
	}

	void parse() throws IOException {

		BufferedReader input = new BufferedReader(new FileReader(sam));
		PrintWriter write=new PrintWriter(new FileWriter("res.sam"));
		String l="";
		String [] line;
		while ((l = input.readLine()) != null) {
			if(l.charAt(0)!='@'){
			/* discarded discordant pairs*/
			if(l.indexOf("YT:Z:DP")==-1){
				line=l.split("\t");
				/* not unmapped reads */
				if(!flagSet(Integer.parseInt(line[1]),0x4)){
					/* broken paired are discarded */
					if(!flagSet(Integer.parseInt(line[1]),0x8)){
							/* First of pair */
							if(flagSet(Integer.parseInt(line[1]),0x40)){				
								line[0]=line[0]+".R1";
							}
							/* Second of pair */
							if(flagSet(Integer.parseInt(line[1]),0x80)){				
								line[0]=line[0]+".R2";
							}
							/* Reverse strand */
							if(flagSet(Integer.parseInt(line[1]),0x10)){				
								line[1]="16";
							}else{
								line[1]="0";
							}
							write.println(getString(line));
						
						
					}
				}
			}
			}else{
				/* print the headlines */
				write.println(l);
			}
		}

		input.close();
		write.close();

		File f=new File("res.sam");
		File ff=new File(sam);
		ff.renameTo(new File(sam+"ORIGINAL"));
		f.renameTo(new File(sam));
	}

	/* return true if flag is set in val */
	boolean flagSet(int val,int flag){
	 	return ((val & flag)!=0); 
	}

	String getString(String [] line){
		String res="";
		for (String s : line) {
		  res=res+s+"\t";
		}
		res=res.substring(0,res.length()-1);
		return res;
	}


}

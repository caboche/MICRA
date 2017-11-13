import java.io.*;
import java.util.*;

public class FastqToFastq{

	String input;
	String output;


	FastqToFastq(){}

	FastqToFastq(String input,String output){
		this.input=input;
		this.output=output;
	}


	public int run()throws IOException{	
		BufferedReader in = new BufferedReader(new FileReader(input));
		PrintWriter out=new PrintWriter(new FileWriter(output));

		int somme=0;		
		int stage=1;
		String l="";
		String seq="";
		String score="";
		int c=0;
		String id="";
		
		while ((l = in.readLine()) != null) {
			
			if(stage==1){
				out.println(l);
				id=l.substring(1,l.length());
				stage=2;
				seq="";
				score="";
			}else{
				if(stage==2){
					
					if((l.equals("+"))||(l.startsWith("+"+id))){
						out.println(seq);
						out.println(l);
						c++;
						stage=3;
					}
					else{seq=seq+l;}
				}
				else{
					if(stage==3){
					score=score+l;
					if (score.length()==seq.length()){
						out.println(score);
						stage=1;
					}


					}



				}



			}
			
		
		}
		
		in.close();			
		out.close();
		return (c);
	
	}
	
}


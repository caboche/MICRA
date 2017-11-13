import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.DecimalFormat;

public class GetIds{


	LinkedList antibio;
	LinkedList orga;
	String db;

	LinkedList ids;

	GetIds(){}

	GetIds(String db,LinkedList antibio,LinkedList orga){

		this.antibio=antibio;
		this.orga=orga;
		this.db=db;
		ids=new LinkedList();
	
		try{
			init();
		}catch (IOException e) {
			System.out.println("ERROR: get IDs");
		} 
	}


	void init() throws IOException {

		String file="";
		if(db.equals("drugBank")){
			file="antibio_files/drugBank_ids.txt";
		}
		if(db.equals("ARDB")){
			file="antibio_files/ARDB_ids_V4.txt";
		}
		BufferedReader input = new BufferedReader(new FileReader(file));
		String l="";

		input.readLine();

		while ((l = input.readLine()) != null) {
			String [] seq=l.split("@");
			if(isInside(seq[1])){
				ids.add(seq[0]);
			}
			
		}

		input.close();
	}



	boolean isInside(String l){
		for(int i=0;i<antibio.size();i++){
			String a=(String)antibio.get(i);
			String [] seq=l.split(";");
			if(seq.length==0)System.out.println("SEQ0 "+l);
			for(int j=0;j<seq.length;j++){
				String tmp="";
				if(a.endsWith("e")){
					tmp=a.substring(0,a.length()-1);
				}else{
					tmp=a+"e";
				}
				if(a.equalsIgnoreCase(seq[j]))return true;
				if(tmp.equalsIgnoreCase(seq[j])){return true;}
			}
		}
		return false;
	}
}


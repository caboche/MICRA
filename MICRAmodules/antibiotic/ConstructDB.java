import java.io.*;
import java.util.*;
import java.util.regex.*;
import java.text.DecimalFormat;

public class ConstructDB{


	LinkedList antibio;
	LinkedList orga;
	String db;

	HashMap DB;
	HashMap ARDB;

	LinkedList resume;
	LinkedList name;
	PrintWriter log;
	int val;

	ConstructDB(){}

	ConstructDB(String db,LinkedList antibio,LinkedList orga,LinkedList name,LinkedList resume,PrintWriter log){
	
		this.antibio=antibio;
		this.orga=orga;
		this.db=db;
		this.resume=resume;
		this.name=name;
		DB=new HashMap();
		ARDB=new HashMap();
		this.log=log;
		val=-1;
		
		try{
		
			initHashMap();
			val=init();
		
			if(val!=0){
				new MakeDB();
			}
		
		}catch (IOException e) {
			System.out.println("ERROR: Constructing DB");
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


	int init() throws IOException {	
		int c=0;
		String file="";
		if(db.equals("drugBank")){
			file="antibio_files/drugBank_targets_NEW.fasta";
		}
		if(db.equals("ARDB")){
			file="antibio_files/ARDB_perso_V4.fasta";
		}



		BufferedReader input=new BufferedReader(new FileReader(file));
		PrintWriter out=new PrintWriter(new FileWriter("db_temp.fasta"));
		String l="";
		boolean write=false;
		while ((l = input.readLine()) != null) {
			if(l.startsWith(">")){
				if(isInside2(db,l)){
					out.println(l);
					write=true;
					c++;
				}else{
					write=false;
				}
			}else{
				if(write)out.println(l);
			}
		}

		input.close();		
		out.close();



		System.out.println("number of considered "+db+" sequences in databank "+c);	
		log.println("number of considered "+db+" sequences in databank "+c);	
		return c;
	}


	boolean isInside2(String db,String l){

		boolean res=false;
		
		String [] seq=l.split("\\|");
		if(db.equals("drugBank")){
			String [] tab=seq[3].split(";");
			for(int j=0;j<tab.length;j++){

				String id_tmp=tab[j];
				if(id_tmp.charAt(0)==' '){
					id_tmp=id_tmp.substring(1,id_tmp.length());
				}
			
				int i=antibio.indexOf(id_tmp);
				if(i!=-1){
				String a=(String)antibio.get(i);
					int index=isInList((String)DB.get(a));
					if(index!=-1){
						Couple c=(Couple)resume.get(index);
						c.increaseDB();
					resume.set(index,c);
					}
					res=true;
				}
			}
		}
		if(db.equals("ARDB")){
			String id_tmp=seq[0].substring(1,seq[0].length());
			int i=antibio.indexOf(id_tmp);
				
			if(i!=-1){
		
				String a=(String)antibio.get(i);
				String tmp=(String)ARDB.get(a);
				
				String [] tab=tmp.split(";");
				for(int j=0;j<tab.length;j++){
					int index=isInList(tab[j]);
					if(index!=-1){
						Couple c=(Couple)resume.get(index);
						c.increaseARDB();
						resume.set(index,c);
					}
				}
				res=true;
			
			}
		}
		return res;
	}


	int isInList(String s){

		for(int i=0;i<name.size();i++){
			String a=(String)name.get(i);
			String tmp="";
			if(a.endsWith("e")){
				tmp=a.substring(0,a.length()-1);
			}else{
				tmp=a+"e";
			}
	
			if(a.equalsIgnoreCase(s)){return i;}
			if(tmp.equalsIgnoreCase(s)){return i;}
		}
		return -1;
	}

	boolean isInside(String db,String l){
		for(int i=0;i<antibio.size();i++){
			String a=(String)antibio.get(i);
			String [] seq=l.split("\\|");
			if(db.equals("drugBank")){
				if(seq[3].indexOf(a)!=-1){
					int index=isInList((String)DB.get(a));
					if(index!=-1){
						Couple c=(Couple)resume.get(index);
						c.increaseDB();
					resume.set(index,c);
					}
				
					return true;
				}
			}
			if(db.equals("ARDB")){
				if(seq[0].indexOf(a)!=-1){
					String tmp=(String)ARDB.get(a);
					String [] tab=tmp.split(";");
					for(int j=0;j<tab.length;j++){
					int index=isInList(tab[j]);
						if(index!=-1){
						Couple c=(Couple)resume.get(index);
						c.increaseARDB();
						resume.set(index,c);
						}
					}
					return true;
				}
			}
		}
		return false;
	}

}


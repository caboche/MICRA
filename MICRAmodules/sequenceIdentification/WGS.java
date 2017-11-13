import java.io.*;
import java.util.*;

public class WGS{

	String id;
	String rep;
	String sep;
	String name;

	WGS(){}

	WGS(String id,String name,String rep,String sep){
		this.id=id;
		this.rep=rep;
		this.sep=sep;
		this.name=name;

		/* download file from NCBI */
		Download d=new Download(id,".gbff.gz");
		d=new Download(id,".fsa_nt.gz");

		/* unzip files */
		Unzip u=new Unzip(id+".gbff.gz");
		u=new Unzip(id+".fsa_nt.gz");

		Fasta f=new Fasta(id+".fsa_nt",name,rep,sep);
		new Gff(id+".gbff",name,rep,sep,f.sizes);

		new ReadSeq(name,rep,sep);
		new Convert(name,rep,sep);

		/* delete gbff and fsa files */
		File fi=new File(id+".gbff");
		fi.delete();
		fi=new File(id+".fsa_nt.gz");
		fi.delete();
		fi=new File(id+".fsa_nt");
		fi.delete();

	}

}

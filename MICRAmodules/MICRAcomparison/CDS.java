import java.io.*;
import java.util.*;

/*
Annotation object
*/

public class CDS{
	/* CDS id*/
	String id;
	/* description */
	String annot;

	CDS(){}

	CDS(String id,String annot){
		this.id=id;
		this.annot=annot;
	}

	void setId(String s){
		id=s;
	}

	void setAnnot(String s){
		annot=s;
	}

	String getId(){
		return id;
	}

	String getAnnot(){
		return annot;
	}

	boolean equals(CDS cds){
		return (id.equals(cds.getId()));
	}


	void print(){
		System.out.println(annot);
	}
}

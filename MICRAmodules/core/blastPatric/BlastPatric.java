package blastPatric;

import java.io.*;
import java.util.*;
import java.text.*; 


public class BlastPatric
{
	
	String file;
	String command;
	PrintWriter error;
	PrintWriter log;
	String project;
	String sep;
	double seuil;
	
	public BlastPatric(){

	}

	public BlastPatric(String file,String command,String project,String sep,PrintWriter log,PrintWriter error,double seuil){
		System.out.println("###########################################");
		System.out.println("STEP5: BLAST unmapped reads with Patric CDS database");
		log.println("###########################################");
		log.println("STEP5: BLAST unmapped reads with Patric CDS database");
		this.file=file;	
		this.command=command;
		this.log=log;
		this.error=error;
		this.sep=sep;
		this.project=project;
		this.seuil=seuil;
		new BlastInterfacePatric(file,project+"blast_unmapped.txt",command);
		new Parser(project+"blast_unmapped.txt","res.txt",seuil);
		new Parser3("res.txt",project+"blast_unmapped.html",project+"blast.csv");
		File f1=new File("res.txt");
		f1.delete();
	}



}

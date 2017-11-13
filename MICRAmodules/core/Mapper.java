/*package MICRA.core;*/

import java.io.*;
import java.util.*;

/* 
Class for a mapper
*/

public class Mapper{

	/* command line for indexing reference sequence*/
	String index;
	/* command line for mapping reads*/
	String run;
	/* consider several position dor a read*/
	boolean multimap;


	public Mapper(){}

	Mapper(String index, String run,boolean multimap){
		this.index=index;
		this.run=run;
		this.multimap=multimap;
	}

	String getIndex(){
		return index;
	}

	String getRun(){
		return run;
	}

	boolean getMulti(){
		return multimap;
	}
}

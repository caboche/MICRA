import java.io.*;
import java.util.*;
import java.text.*;

/* 

*/

class CDScontig{

	int beg;
	int end;
	String desc;
	double coverage;


	public CDScontig(){}

	public CDScontig(int beg,int end,String desc,double coverage){
		this.beg=beg;
		this.end=end;
		this.desc=desc;
		this.coverage=coverage;
	}

}

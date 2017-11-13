The main class is Run.

type java Run -h to see all the parameters and options. 

The configuration files are:
- conf.txt => ion torrent
- confIllumina.txt => single-end illumina
- confMulti.txt => repeat content
- confPaired.txt => paired-end illumina

These configuration files contains the command line for the mappers,
the de novo assembler, BLAST and dustmasker.
You can easily change the command lines from the configuration file.

- The manifest.txt is the input file for MIRA assembler

REQUIREMENTS:

- Directory "lib" containing:
	- batik-awt-util.jar
	- batik-dom.jar
	- batik-svggen.jar
	- batik-util.jar
	- batik-xml.jar
	- commons-lang-2.0.jar
	- jargs.jar
	- xercesImpl.jar
- directory bowtie2-2.2.9 
- directory MIRA-4.0.2
- directory SHRiMP-2.2.3
- directory snap-0.15
- directory SPAdes-3.9.0-Linux

- zip must be installed
- requires cgview.jar
- dustmasker as an executable
- a directory "BLAST_db" containing the BLAST patric_cds database


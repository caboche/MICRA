MICRA
=========

MICRA is a pipeline for the Microbial Identification and Characterization through Reads Analysis. Contrary 
to the current approaches often based on de novo assembly and contig annotation, MICRA is based on read 
mapping and comparative genomics, exploiting the growing knowledge in databases. 
Due to its concept, MICRA is fast, requiring only few minutes to run. 

MICRA is available as a web interface at http://www.pegase-biosciences.com/MICRA

Here you will find the MICRA source codes and jar files available under the terms of the GNU GPLv3 license.

# Table of Contents
* [MICRA organization](#micra-organization)
* [External tools](#external-tools)
* [Databases](#databases)
* [Content description](#content-description)
* [Support](#support)
* [Citation](#citation)

# MICRA organization

MICRA is divided into 4 main parts.
		
1- The pre-processing part, which is optional, allows read quality check and trimming.

2- The sequence identification part aims to obtain the reference sequences used in the core part of MICRA.

3- The core part identifies the closest reference genome and the plamids, produces the 
sequences and annotations from iterative mapping and performs 
de novo assembly with the remaining unmapped reads to generate additionnal annotations.

4- The post-analysis part is able to predict the antibiotic susceptibility and resistance 
profile from the MICRA results and to compare several samples
with the MICRA comparison tool.


In addition to the reads analysis pipeline, an other tool called MICRA Comparison is available 
to easily compare several result files generated with MICRA.

For more details about the MICRA pipeline, please see the associated [publication](#citation). 

  
# External tools

* [BLAST](http://blast.ncbi.nlm.nih.gov/Blast.cgi)
* [Bowtie2](http://bowtie-bio.sourceforge.net/bowtie2/index.shtml)
* [CGView](http://wishart.biology.ualberta.ca/cgview/) 
* [cutadapt](https://github.com/marcelm/cutadapt) 
* [dustmasker](https://www.ncbi.nlm.nih.gov/IEB/ToolBox/CPP_DOC/lxr/source/src/app/dustmasker/)
* [FastQC](https://www.bioinformatics.babraham.ac.uk/projects/fastqc/)
* [genbank2gff3](https://github.com/bioperl/bioperl-live/blob/master/scripts/Bio-DB-GFF/bp_genbank2gff3.pl)
* [MIRA](https://sourceforge.net/projects/mira-assembler/)
* [Readseq](http://iubio.bio.indiana.edu/soft/molbio/readseq/java/)
* [SHRiMP2](http://compbio.cs.toronto.edu/shrimp/)
* [SNAP](http://snap.cs.berkeley.edu/)
* [SPAdes](http://bioinf.spbau.ru/en/spades)

The MICRA Comparison module uses [R](https://www.r-project.org/) with the [VennDiagram package](https://www.ncbi.nlm.nih.gov/pmc/articles/PMC3041657/).
		
# Databases

* [ARDB](https://ardb.cbcb.umd.edu/index.html)
* [Drugbank](http://www.drugbank.ca/) 
* [NCBI](https://www.ncbi.nlm.nih.gov/)
* [PATRIC](https://www.patricbrc.org/)

# Content description

Here you will find the MICRA source codes and jar files available under the terms of the GNU GPLv3 license.
* MICRApipeline directory contains the source codes for the MICRA pipeline linking all the modules together and organize
the result directory
* MICRAmodules directory contains:
	- preProcessing directory containing the source codes for the pre-processing module
	- sequenceIdentification containing the source codes for the reference sequence Identification module
	- core directory containing the source codes for the core module
	- antibiotic drirectory containing the source codes for the antibiotic susceptibility and resistance prediction
	- MICRAcomparison containing the source codes for the comparison module

* All directories contains a ReadMe.txt file describing the requirements of each module.


# Support
For questions and comments, please contact us at micra(AT)pasteur-lille.fr.

# Citation

A manuscript describing MICRA is under publication. If you make use of the MICRA pipeline, please cite us:
Caboche et al., Fast characterization of microbial genomes from high-throughput sequencing data (submited). 



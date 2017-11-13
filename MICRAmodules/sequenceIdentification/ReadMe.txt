The main class is Run.

type java Run -h to see all the parameters and options. 


In the directory external scripts:
- annot.pl => get the description of an entry from accession
- bioPerl.pl => get the genbank file from accession
- bp_genbank2gff3 => convert genbank to gff3 TO BE DOWNLOADED FROM 
https://github.com/bioperl/bioperl-live/blob/master/scripts/Bio-DB-GFF/bp_genbank2gff3.pl
- dict_genomes.txt gives the correspondance between gi and NCBI accessions for genome sequences (gi;accession)
- dict_plasmids.txt gives the correspondance between gi and NCBI accessions for plasmid sequences (gi;accession)
- sampling_random.sh => randomly get n reads from a fastq file (REQUIREMENT fastq-sample executable) 

REQUIREMENTS:
- blastn must be installed
- gunzip must be installed
- the fastq-sample executable
- readseq.jar
- a directory BLAST_db containing the BLAST databases:
	- ncbi.fa
	- plasmids.fasta
- A directory "allGenomes" containing all the fasta and gff files for genome sequences in the BLAST database
- A directory "allPlasmids" containing all the fasta and gff files for plasmid sequences in the BLAST database


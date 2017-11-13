#!/bin/bash

# sub-sampling of the fastq file
# using fastq-sample from fastq-tools-0.8

# $1 fastq file
# $2 nb reads to keep

random=$[($RANDOM % ($[100000 - 1] + 1)) + 1]


./fastq-sample -s $random -n $2 -o temp $1
paste - - - - <  temp.fastq | cut -f 1,2 | sed 's/^@/>/' | tr "\t" "\n" > sample.fa

rm temp.fastq

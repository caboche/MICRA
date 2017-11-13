use Bio::SeqIO ;
use Bio::DB::GenBank ;

my $usage = "bioPerl.pl Accession name project sep\n";
$acc = shift or die $usage;
$name= shift or die $usage;
$project = shift or die $usage;
$sep = shift or die $usage;

$genbank = new Bio::DB::GenBank ;
$sequence = $genbank->get_Seq_by_version($acc) ;
print $sequence->display_id(), "\n" ;


my $outfileformat = 'Fasta';
my $outfile = $project.$sep.$name.'.fa';
my $outfile3 = $project.$sep.'temp.fa';
my $outfile2 = $project.$sep.$name.'.gb';
my $seq_out = Bio::SeqIO->new('-file' => ">$outfile3",'-format' => $outfileformat);
my $in  = Bio::SeqIO->new('-file' => ">$outfile2" , '-format' => 'genbank');
$seq_out->write_seq($sequence);
$in->write_seq($sequence);



open INFILE, "<$outfile3";
open OUTFILE, ">$outfile";
<INFILE>; #discards first line
print OUTFILE ">".$sequence->display_id()."\n";
print OUTFILE $_ while (<INFILE>);
close OUTFILE;
close INFILE;

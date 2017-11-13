use Bio::SeqIO ;
use Bio::DB::GenBank ;

my $usage = "annot.pl Accession\n";
$acc = shift or die $usage;

$genbank = new Bio::DB::GenBank ;
$sequence = $genbank->get_Seq_by_version($acc) ;
print $sequence->accession_number(),".",$sequence->version(), "\n" ;


my $anno_collection = $sequence->annotation;
my @annotations = $anno_collection->get_Annotations('keywords');


 print ($anno_collection->display_text());


 


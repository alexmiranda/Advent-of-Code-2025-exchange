use strict;


my $totalscore = 0;

my @seq = ();

while (<>) {
	chop();
    if ( $_ =~ /[,\S]+/ ) {
        @seq = split(/,/, $_);
    } 
}

print "@seq\n";

foreach my $step ( @seq ) {
    my $val = 0;
    foreach my $ch ( split(//, $step) ) {
        $val += ord($ch);
        $val *= 17;
        $val %= 256;
    }
    print "$step: $val\n";
    $totalscore += $val;
}



print "\n== $totalscore\n";

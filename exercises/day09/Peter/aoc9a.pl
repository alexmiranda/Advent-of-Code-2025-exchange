use List::Util qw(max);
use List::Util qw(min);

my $totalscore = 0;

my @reportLst = ();

while (<>) {
	chop();
    if ( $_ =~ /(\d+)/ ) {
        push(@reportLst, [ [ split(/ +/, $_) ] ] );
    } 
}


for my $r ( 0 .. $#reportLst ) {
    my $i=0;
    while ( max(@{$reportLst[$r][$i]}) > 0 or min(@{$reportLst[$r][$i]}) < 0) {
print "[@{$reportLst[$r][$i]}]\n";
        for my $j ( 1 .. $#{$reportLst[$r][$i]} ) {
            $reportLst[$r][$i+1][$j-1] = $reportLst[$r][$i][$j] - $reportLst[$r][$i][$j-1];
        }
        $i++;
    }
    $reportLst[$r][$i][$#{$reportLst[$r][$i]}+1] = 0;
    for ( $i=$#{$reportLst[$r]} ; $i >= 1 ; $i-- ) {
        my $j = $#{$reportLst[$r][$i]};
#print "[@{$reportLst[$r][$i]}]\n";
        $reportLst[$r][$i-1][$j+1] = $reportLst[$r][$i][$j] + $reportLst[$r][$i-1][$j];
#print "$i,$j: $reportLst[$r][$i-1][$j+1] = $reportLst[$r][$i][$j] + $reportLst[$r][$i-1][$j]\n";
    }
    $totalscore += $reportLst[$r][0][$#{$reportLst[$r][0]}];

}


print "\n== $totalscore\n";

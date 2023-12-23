use strict;
use List::MoreUtils 'first_index'; 

my $totalscore = 0;

my @seq = ();

while (<>) {
	chop();
    if ( $_ =~ /[,\S]+/ ) {
        @seq = split(/,/, $_);
    } 
}

my @box = ();
foreach my $step ( @seq ) {
    $step =~ /(\w+)([-=])([\d]?)/;
    my $pos = 0;
    my $lb = $1;
    my $op = $2;
    my $fl = $3;
    foreach my $ch ( split(//, $lb) ) {
        $pos += ord($ch);
        $pos *= 17;
        $pos %= 256;
    }
    print "$step: $pos\n";
    if ($op eq '=') {
        if ((my $id = first_index {/\[$lb / } @{$box[$pos]}) > -1) {
            ${$box[$pos]}[$id] = "[$lb $fl]";
        } else {
            push(@{$box[$pos]}, "[$lb $fl]");
        }
    } else {
        if ((my $id = first_index {/\[$lb / } @{$box[$pos]}) > -1) {
            splice(@{$box[$pos]}, $id, 1);
        }
    }
#    print "Box $pos: @{$box[$pos]}\n";
}

foreach my $i (0 .. 255) {
    if ($box[$i]) {
        foreach my $j (0 .. $#{$box[$i]}) {
            $box[$i][$j] =~ /(\d)/;
            my $fpow = ($i+1) * ($j+1) * $1;
#print "\n$box[$i][$j]: ".($i+1)." ".($j+1)." $1 -> $fpow\n";
            $totalscore += $fpow;
        }
    }
}

print "\n== $totalscore\n";

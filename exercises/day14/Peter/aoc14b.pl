use strict;
use Data::Dumper qw(Dumper);


my $score = 0;

my @map = ();
my $lenX = 0;

while (<>) {
	chop();
    if ( $_ =~ /[\.O#]+/ ) {
        push(@map, [ split(//, $_) ] );
        $lenX or ($lenX = length($_)-1);
    } 
}

my $cycle = 0;
my $lenY = $#map;
my %mapHist = {};
while ($cycle < 1000000000) {
    $cycle++;
    foreach my $dir ('n', 'w', 's', 'e') {
        my @newMap = ();
        for my $y (0 .. $lenY) {
            for my $x (0 .. $lenX) {
                if ($y > 0 and $map[$y][$x] eq 'O' and $newMap[$x][$lenY-$y+1] eq '.') {
                    my $yy = $y-1;
                    while ($yy > 0 and $newMap[$x][$lenY-$yy+1] eq '.') {
                        $yy--;
                    }
                    $newMap[$x][$lenY-$yy] = $map[$y][$x];
                    $newMap[$x][$lenY-$y] = '.';
                } else {
                    $newMap[$x][$lenY-$y] = $map[$y][$x];
                }

            }
        }
        @map = @newMap;
    }
    $score = 0;
    my $weigth = $lenY+1;
    foreach my $row (@map) {
#        print "@{$row} \n";
        $score += $weigth * grep(/O/, @{$row});
        $weigth--;
    }
    if (!$mapHist{$score}) {
        $mapHist{$score} = { dump => Dumper(\@map), cycl => $cycle };
    } else {
        my $match = 1;
        if ($mapHist{$score}{dump} eq Dumper(\@map)) {
            my $repeat = $cycle - $mapHist{$score}{cycl};
#            print "- $score: $cycle $mapHist{$score}{cycl}\n";
            $cycle += int((1000000000-$cycle) / $repeat) * $repeat;
        }
    }
    print "$cycle: $score \n";
}

print "\n== $score\n";

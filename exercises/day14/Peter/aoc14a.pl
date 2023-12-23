use strict;
use List::Util qw(min);


my $totalscore = 0;

my @map = ();
my $lenX = 0;

while (<>) {
	chop();
    if ( $_ =~ /[\.O#]+/ ) {
        push(@map, [ split(//, $_) ] );
        $lenX or ($lenX = length($_)-1);
    } 
}

my $change = 1;
my $lenY = $#map;
while ($change) {
    $change = 0;
    for my $y (1 .. $lenY) {
        for my $x (0 .. $lenX) {
            if ($map[$y][$x] eq 'O' and $map[$y-1][$x] eq '.') {
                $map[$y-1][$x] = $map[$y][$x];
                $map[$y][$x] = '.';
                $change++;
            }
        }
    }
}

my $weigth = $lenY+1;
foreach my $row (@map) {
    print "@{$row} : $weigth *".grep(/O/, @{$row})."\n";
    $totalscore += $weigth * grep(/O/, @{$row});
    $weigth--;
}


print "\n== $totalscore\n";

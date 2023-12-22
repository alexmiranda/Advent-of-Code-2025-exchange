use strict;

my $totalscore = 0;

my @map = ();
my @galPos = ();
my $galNum = 0;

while (<>) {
	chop();
    if ( $_ =~ /[\.#]+/ ) {
        while ((my $pos = index($_, '#')) >=0) {
            push(@galPos, { x => $pos, y => $#map+1, p => $#galPos+2});
            $_ =~ s/#/$#galPos/;
        }
        push(@map, [ split(//, $_) ] );
    } 
}

my @neX = ();
my @neY = ();
foreach my $g ( @galPos ) {
    grep($$g{x} == $_, @neX) or push(@neX,$$g{x});
    grep($$g{y} == $_, @neY) or push(@neY,$$g{y});
}
foreach my $i ( 0 .. $#map ) {
    grep($i == $_, @neX) or print "$i ";
}
print "\n";
foreach my $i ( 0 .. $#map ) {
    grep($i == $_, @neY) or print "$i ";
}
print "\n";


foreach my $i ( 0 .. $#galPos ) {
    print "$i: ";
    foreach my $j ( $i+1 .. $#galPos ) {
    print ".";
        my %pos = %{$galPos[$i]};
        my $step = 0;
        while ( ((my $dx = $pos{x} - $galPos[$j]{x}) !=0) or ((my $dy = $pos{y} - $galPos[$j]{y}) !=0) ) {
            if (abs($dx) > abs($dy))  {
                $pos{x} -= abs($dx)/$dx;
                $step += (grep($pos{x} == $_, @neX) ? 1 : 2);
if ($galPos[$j]{p} == 439) {
    print (grep($pos{x} == $_, @neX) ? 1 : 2)."\n";
}
            } else {
                $pos{y} -= abs($dy)/$dy;
                $step += (grep($pos{y} == $_, @neY) ? 1 : 2);
if ($galPos[$j]{p} == 439) {
    print (grep($pos{y} == $_, @neY) ? 1 : 2)."\n";
}
            }
        }
    print "$pos{p} -> $galPos[$j]{p}: $step\n";
        $totalscore += $step;
    }
    print "\n";exit;
}





print "\n== $totalscore\n";

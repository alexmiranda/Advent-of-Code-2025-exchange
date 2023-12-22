use strict;
use List::MoreUtils qw(uniq);
use List::Util qw(max);

my $totalscore = 0;
my @table = ();

sub qsort (\@) {_qsort($_[0], 0, $#{$_[0]})}

sub _qsort {
    my ($array, $low, $high) = @_;
    if ($low < $high) {
        my $mid = partition($array, $low, $high);
        _qsort($array, $low,     $mid - 1);
        _qsort($array, $mid + 1, $high   );
    }
}

sub partition {
    my ($array, $low, $high) = @_;
    my $x = $$array[$high];
    my $i = $low - 1;
    for my $j ($low .. $high - 1) {
        if ($$array[$j]{score} < $$x{score} or ($$array[$j]{score} == $$x{score} and $$array[$j]{handtr} le $$x{handtr}) ) {
#        if ($$array[$j] <= $x) {
            $i++;
            @$array[$i, $j] = @$array[$j, $i];
        } 
    }
    $i++;
    @$array[$i, $high] = @$array[$high, $i];
    return $i;
}



while (<>) {
	chop();
    if ( $_ =~ /(\w+) +(\d+)/ ) {
        my $tr = $1;
        $tr =~ tr/AKQJT/EDC.A/;
        push(@table, { hand => $1, bid => $2, handtr => $tr } );
    } 
}


for (my $h=0 ; $h<=$#table ; $h++) {
    my @countLst = (); 
    my $countJ = 0;
#    foreach my $card ( @{$table[$h][]}  ) {
    my @cardLst = split(//,$table[$h]{hand});
    foreach my $card ( uniq(@cardLst)  ) {
        my @same = grep (/$card/, @cardLst);
        if ($card eq 'J') {
            $countJ = $#same + 1;
        } else {
            ($#same > 0) and push( @countLst, $#same + 1 );
        }
    }
    my $maxSame = max(@countLst);
    $maxSame = ($maxSame ? $maxSame + $countJ: ($countJ == 5) ? $countJ : $countJ + 1);
#print "$table[$h]{hand} $maxSame $countJ\n";
    if ($maxSame > 3) {
        $table[$h]{score} = $maxSame + 1;
    } elsif ($maxSame == 3 && $#countLst  > 0) {
        $table[$h]{score} = 4;
    } elsif ($maxSame == 3) {
        $table[$h]{score} = 3;
    } elsif ($maxSame == 2 and $#countLst  > 0) {
        $table[$h]{score} = 2;
    } elsif ($maxSame == 2) {
        $table[$h]{score} = 1;
    } else {
        $table[$h]{score} = 0;
    }
}

qsort(@table);

my $rank = 1;
foreach my $t (@table) {
    print "$$t{hand} ($$t{score})\n";
    $totalscore += $rank * $$t{bid};
    $rank++;
}


print "\n== $totalscore\n";

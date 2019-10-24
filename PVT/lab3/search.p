#!/usr/bin/gnuplot -persist

set terminal png size 1000,600 linewidth 4 font "Verdana,14"
set termoption dash
set output "experiments.png"

set grid x y
set key right bottom
unset log
set xlabel "Process"
set ylabel "Speedup"
set xrange [8 : 64]
set yrange [0 : 70]

plot "28k.txt" using 1:2 with linespoints linecolor 5 title "m=n=28000","45k.txt" usin 1:2 with linespoints linecolor 2 title "m=n=45000", "linear.txt" usin 1:2 with linespoints linecolor 3 title "Linear speedup"

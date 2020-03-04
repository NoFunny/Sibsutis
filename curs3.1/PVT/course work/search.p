#!/usr/bin/gnuplot -persist

set terminal png size 1000,600 linewidth 4 font "Verdana,14"
set termoption dash
set output "search.svg"

set grid x y
set key right bottom
unset log
set xlabel "Number of threads"
set ylabel "Speedup"
set xrange [2 : 17]
set yrange [0 : 17]

plot "exp.txt" using 1:2 with linespoints linecolor 5 title "Parallel speedup", "Linear.txt" using 1:2 with linespoints linecolor 6 title "Linear speedup"

#!/usr/bin/gnuplot -persist

set terminal png size 1000,600 linewidth 4 font "Verdana,14"
set termoption dash
set output "overhead.png"

set grid x y
set key right bottom
unset log
set xlabel "Process"
set ylabel "Speedup"
set xrange [4 : 13]
set yrange [0 : 1]

plot "overhead(500x500).txt" using 1:2 with linespoints linecolor 5 title "500x500","overhead(700x700).txt" usin 1:2 with linespoints linecolor 2 title "700x700"

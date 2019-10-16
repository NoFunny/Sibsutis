#!/usr/bin/gnuplot -persist

set terminal png size 1000,600 linewidth 4 font "Verdana,14"
set termoption dash
set output "Monte.png"

set grid x y
set key right bottom
unset log
set xlabel "Кол-во запусков"
set ylabel "Время"
set xrange [1 : 70]
set yrange [0 : 0.3]

plot "monte.txt" using 1:2 with linespoints linecolor 5 title "MONTE-CARLO METHOD"

#!/usr/bin/gnuplot -persist

set terminal png size 1000,600 linewidth 4 font "Verdana,14"
set termoption dash
set output "Dgemm.png"

set grid x y
set key right bottom
unset log
set xlabel "Кол-во входных данных"
set ylabel "Время"
set xrange [10 : 2000]
set yrange [0 : 150000]

plot "dgemm.txt" using 1:2 with linespoints linecolor 6 title "Dgemm BLAS"

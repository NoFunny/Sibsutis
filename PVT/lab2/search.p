#!/usr/bin/gnuplot -persist

set terminal png size 1000,600 linewidth 4 font "Verdana,14"
set termoption dash
set output "Monte.png"

set grid x y
set key right bottom
unset log
set xlabel "Кол-во процессов"
set ylabel "Ускорение"
set xrange [1 : 64]
set yrange [0 : 70]

plot "monte.txt" using 1:2 with linespoints linecolor 5 title "MONTE-KARLO METHOD", "linear.txt" usin 1:2 with linespoints linecolor 3 title "Линейное ускорение"

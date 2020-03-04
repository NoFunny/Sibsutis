#!/usr/bin/gnuplot -persist

set terminal png size 1000,600 linewidth 4 font "Verdana,14"
set termoption dash
set output "search1.png"

set grid x y
set key right top
unset log
set xlabel "Количество нитей"
set ylabel "Время,с"
set xrange [0 : 300]
set yrange [0 : 0.03]

plot "1.txt" using 1:2 with linespoints linecolor 5 title "Нечётное количество нитей", "2.txt" using 1:2 with linespoints linecolor 3 title "Чётное количество нитей"

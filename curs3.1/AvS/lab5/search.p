#!/usr/bin/gnuplot -persist

set terminal png size 1000,600 linewidth 4 font "Verdana,14"
set termoption dash
set output "Dgemv.png"

set grid x y
set key right bottom
unset log
set xlabel "Threads"
set ylabel "Speedup"
set xrange [0 : 10]
set yrange [0 : 10]

plot "linear.txt" using 1:2 with linespoints linecolor 6 title "Linear speedup", "5000x5000.txt" using 1:2 with linespoints linecolor 5 title "m=n=5000","10000x10000.txt" using 1:2 with linespoints linecolor 4 title "m=n=10000","20000x20000.txt" using 1:2 with linespoints linecolor 3 title "m=n=20000"

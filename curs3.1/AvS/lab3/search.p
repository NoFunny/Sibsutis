#!/usr/bin/gnuplot -persist

set terminal png size 1000,600 linewidth 4 font "Verdana,14"
set termoption dash
set output "GetTime.png"

set grid x y
set key right bottom
unset log
set xlabel "Кол-во запусков"
set ylabel "Время"
set xrange [1 : 10]
set yrange [0 : 0.05]

plot "gettime.txt" using 1:2 with linespoints linecolor 6 title "Get_Time_of_Day method"

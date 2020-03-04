#!/usr/bin/gnuplot -persist

set terminal png size 1000,600 linewidth 4 font "Verdana,14"
set termoption dash
set output "search1.png"

set grid x y
set key right top
unset log
set xlabel "Время,ч"
set ylabel "Вероятность решения задачи"
set xrange [0 : 2000]
set yrange [0 : 1]

plot "os.txt" using 1:2 smooth csplines title "Функция осуществимости" with lines linecolor 4 

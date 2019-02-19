#!/usr/bin/gnuplot
# Plotting data of file timeVStilesize.dat
# Author: Nathan Nard

reset

# svg
set terminal svg size 600,400 enhanced font 'Verdana,14'
set output 'timeVStilesize.svg'
set title "Effect of Tile Size on Run Time"

set border linewidth 1.5
set xlabel "Tile Size"
set ylabel "Time (ms)"

# Set line style
set style line 1 \
    linecolor rgb '#0060ad' \
    linetype 1 linewidth 2 \
    pointtype 7 pointsize 1.5

unset key

plot '16timeVStilesize.dat' using 1:2 with linespoints linestyle 1

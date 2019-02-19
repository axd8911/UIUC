#!/usr/bin/gnuplot
# Plotting data of file timeVStilesize.dat
# Author: Nathan Nard

reset

# svg
set terminal svg size 600,400 enhanced font 'Verdana,14'
set output 'timeVsMatrixSize.svg'
set title "Comparing Tile Sizes 8 and 16 for Matrices of Varying Sizes"

set border linewidth 1.5
set xlabel "Matrix Size"
set ylabel "Time (ms)"

# Set line style
set style line 1 \
    linecolor rgb '#0060ad' \
    linetype 1 linewidth 2 \
    pointtype 7 pointsize 1.5
set style line 2 \
    linecolor rgb '#dd181f' \
    linetype 1 linewidth 2 \
    pointtype 5 pointsize 1.5

set key inside top left

plot 'timeVsMatrixSize.dat' index 0 with  linespoints linestyle 1 title "Tile size of 8", \
     ''                     index 1 with  linespoints linestyle 2 title "Tile size of 16"

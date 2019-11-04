# You can uncomment the following lines to produce a png figure
set terminal png enhanced
set output 'plot1.png'

set title "Average Clustering Coefficient"
set xlabel "cycles"
set ylabel "clustering coefficient (log)"
set key right top
set logscale y 
plot "clustering-Random-cache30.txt" title 'Random Graph c = 30' with lines, \
	"clustering-Shuffle-cache30.txt" title 'Shuffle c = 30' with lines, \
	"clustering-Random-cache50.txt" title 'Random Graph c = 50' with lines, \
	"clustering-Shuffle-cache50.txt" title 'Shuffle c = 50' with lines
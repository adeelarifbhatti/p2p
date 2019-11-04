set terminal png enhanced
set output 'plot3.png'
set title "Average Path Length"
set xlabel "cycles"
set ylabel "average path length (log)"
set xrange [0:890]
set yrange [0:800]
set key right top
set logscale y
plot "length-Shuffle-cache30.txt" title 'Basic Shuffle c = 30' with lines, \
	"length-Shuffle-cache50.txt" title 'Basic Shuffle c = 50' with lines, \
	"length-Random-cache30.txt" title 'Random c = 30' with lines, \
	"length-Random-cache50.txt" title 'Random c = 50' with lines

set terminal png enhanced
set output 'InDegree.png'

set title "In-degree distribution"
set xlabel "in-degree"
set ylabel "number of nodes"
set xrange [0:100]
set yrange [0:3000]
set key right top
plot "indegree-Shuffle-cache30.txt" title 'Basic Shuffle c = 30' with histeps, \
	"indegree-Shuffle-cache50.txt" title 'Basic Shuffle c = 50' with histeps, \
	"indegree-Random-cache30.txt" title 'Random Graph c = 30' with histeps, \
	"indegree-Random-cache50.txt" title 'Random Graph c = 50' with histeps
	

package example.gossip;


import java.io.FileNotFoundException;
import java.io.PrintWriter;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.util.IncrementalStats;

public class AvgLengthPrint extends GraphStatsPrint {

	/**
	 * The number of nodes to use for sampling average path length. Statistics
	 * are printed over a set of node pairs. To create the set of pairs, we
	 * select the given number of different nodes first, and then pair all these
	 * nodes with every other node in the network. If zero is given, then no
	 * statistics will be printed about path length. If a negative value is
	 * given then the value is the full size of the graph. Defaults to zero.
	 */
	private static final String PAR_NL = "nl";

	private final int nl;

	/**
	 * Standard constructor that reads the configuration parameters. Invoked by
	 * the simulation engine.
	 * 
	 * @param name
	 *            the configuration prefix for this class
	 * @throws FileNotFoundException
	 */
	public AvgLengthPrint(String name) throws FileNotFoundException {
		super(name);

		nl = Configuration.getInt(name + "." + PAR_NL, 0);
		
		init("length-" + type + "-cache" + cacheSize + ".txt");
	}

	/**
	 * This algorithm was taken from Peersim's {@code GraphStats} class.
	 * 
	 * @return
	 */
	private double getAveragePathLength() {
		IncrementalStats stats = new IncrementalStats();

		final int n = nl < 0 ? g.size() : nl;
		outerloop: for (int i = 0; i < n && i < g.size(); ++i) {
			ga.dist(g, i);
			for (int j = 0; j < g.size(); ++j) {
				if (j == i)
					continue;
				if (ga.d[j] == -1) {
					stats.add(Double.POSITIVE_INFINITY);
					break outerloop;
				} else
					stats.add(ga.d[j]);
			}
		}

		return stats.getAverage();
	}

	@Override
	protected boolean execute(PrintWriter writer) {
		int cycle = CommonState.getIntTime() / step;

		updateGraph();

		if (nl != 0) {
			double avg = getAveragePathLength();
			writer.println(cycle + "\t" + avg);
		}

		return false;
	}
}
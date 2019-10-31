package example.gossip;

import java.io.FileNotFoundException;
import java.io.PrintWriter;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.graph.GraphAlgorithms;
import peersim.util.IncrementalStats;

public class AvgClusterPrint extends GraphStatsPrint {

	/**
	 * The number of nodes to use to sample average clustering. If zero is
	 * given, then no statistics will be printed about clustering. If a negative
	 * value is given then the value is the full size of the graph. Defaults to
	 * zero.
	 */
	private static final String PAR_NC = "nc";

	private final int nc;

	/**
	 * Standard constructor that reads the configuration parameters. Invoked by
	 * the simulation engine.
	 * 
	 * @param name
	 *            the configuration prefix for this class
	 * @throws FileNotFoundException
	 */
	public AvgClusterPrint(String name) throws FileNotFoundException {
		super(name);

		nc = Configuration.getInt(name + "." + PAR_NC, 0);
		
		init("clustering-" + type + "-cache" + cacheSize + ".txt");
	}

	/**
	 * This algorithm was taken from Peersim's {@code GraphStats} class.
	 * 
	 * @return
	 */
	private double getAverageClusteringCoefficient() {
		IncrementalStats stats = new IncrementalStats();

		final int n = nc < 0 ? g.size() : nc;
		for (int i = 0; i < n && i < g.size(); ++i) {
			stats.add(GraphAlgorithms.clustering(g, i));
		}

		return stats.getAverage();
	}

	@Override
	protected boolean execute(PrintWriter writer) {
		int cycle = CommonState.getIntTime() / step;

		updateGraph();

		if (nc != 0) {
			double avg = getAverageClusteringCoefficient();
			writer.println(cycle + "\t" + avg);
		}

		return false;
	}
}
package example.reports;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;

import peersim.config.Configuration;
import peersim.core.CommonState;
import peersim.core.Control;
import peersim.core.Linkable;
import peersim.core.Network;
import peersim.core.Node;

/**
 * @author Lucas Provensi
 * 
 * Report the in-degree distribution of the network.
 * Uses the list of neighbors obtained from a linkable protocol.
 *
 */
public class InDegreeObserver implements Control
{
	private final static String PAR_PID = "protocol";
	private static final String PAR_START_PROTOCOL = "starttime";
	private static final String PAR_END_PROTOCOL = "endtime";
	private static final String OUTPUT_LOCATION = "plot/";
	private final int step;
	private static final String PAR_STEP = "step";
	private static final String PAR_CACHESIZE = "cacheSize";

	private static final String PAR_TYPE = "type";

	private final int pid;
	private final long startTime;
	private final long endTime;
	private PrintWriter writer;
	protected final int cacheSize;

	protected final String type;

	public InDegreeObserver(String prefix) throws FileNotFoundException
	{
		this.pid = Configuration.getPid(prefix + "." + PAR_PID);
		this.startTime = Configuration.getLong(prefix + "." + PAR_START_PROTOCOL, Long.MIN_VALUE);
		this.endTime = Configuration.getLong(prefix + "." + PAR_END_PROTOCOL, Long.MAX_VALUE);
		cacheSize = Configuration.getInt(prefix + PAR_CACHESIZE);
		type = Configuration.getString(prefix + PAR_TYPE);
		step = Configuration.getInt(prefix + "." + PAR_STEP, 0);
		init("indegree-" + type + "-cache" + cacheSize + ".txt");
	}
	public boolean execute(PrintWriter writer) {
	if (!lastCycle()) {
		return false;
	}

	// Map of all nodes and their in-degree count
	Map<Long, Integer> degreeCount = new HashMap<Long, Integer>();

	for (int i = 0; i < Network.size(); i++) {
		// Get all the nodes in the network
		Node n = Network.get(i);

		if (n.isUp()) {
			// Get the linkable protocol for all the running nodes
			Linkable linkable = (Linkable) n.getProtocol(pid);
			// Go through the neighbor list and update the degrees in the
			// map
			for (int j = 0; j < linkable.degree(); j++) {
				Long nodeId = linkable.getNeighbor(j).getID();
				Integer count = degreeCount.get(nodeId);
				if (count == null) {
					degreeCount.put(nodeId, 1);
				} else {
					degreeCount.put(nodeId, count + 1);
				}
			}
		}
	}

	// Map of the in-degree distribution. The key is the in-degree and the
	// entry is the number of nodes having this distribution
	Map<Integer, Integer> dist = new HashMap<Integer, Integer>();

	// Fill the map with the in-degree distribution of each node
	for (int i = 0; i < Network.size(); i++) {
		Long nodeId = Network.get(i).getID();
		Integer degree = degreeCount.get(nodeId);
		int value = 1;

		if (dist.containsKey(degree)) {
			value = dist.get(degree) + 1;
		}
		dist.put(degree, value);
	}
	dist.remove(null);
	// Sort the distribution and print the result
	SortedSet<Integer> sortedKeys = new TreeSet<Integer>(dist.keySet());
	for (int i = 0; i <= sortedKeys.last(); i++) {
		Integer value;

		if (sortedKeys.contains(i)) {
			value = dist.get(i);
		} else {
			value = 0;
		}

		writer.println(i + "\t" + value);
	}

	return false;
}
	protected void init(String filename) throws FileNotFoundException {
		try {
			writer = new PrintWriter(OUTPUT_LOCATION + filename, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			// Not possible
		}
	}
	protected boolean lastCycle() {
		return CommonState.getTime() + step >= CommonState.getEndTime();
	}

	
	public boolean execute() {
		boolean result = execute(writer);

		// Check if this is the last cycle
		if (lastCycle()) {
			writer.close();
		}

		return result;
	}

}

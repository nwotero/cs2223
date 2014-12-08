/**
 * Donald Bourque, Nicholas Otero
 * project3
 * 12/8/2014
 */

package project3;

/**
 *  <i>Edge</i>. This class defines the data and methods for a weighted edge on a graph
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 */

public class Edge implements Comparable<Edge> {
	private final int v;
	// one vertex
	private final int w;
	// the other vertex
	private final double weight;
	private boolean visited = false;

	// edge weight
	public Edge(int v, int w, double weight) {
		this.v = v;
		this.w = w;
		this.weight = weight;
	}

	public double weight() {
		return weight;
	}

	public int either() {
		return v;
	}

	public int other(int vertex) {
		if (vertex == v)
			return w;
		else if (vertex == w)
			return v;
		else
			throw new RuntimeException("Inconsistent edge");
	}

	public int compareTo(Edge that) {
		if (this.weight() < that.weight())
			return -1;
		else if (this.weight() > that.weight())
			return +1;
		else
			return 0;
	}

	public String toString() {
		return String.format("%d-%d %.2f", v, w, weight);
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.visited = visited;
	}
}

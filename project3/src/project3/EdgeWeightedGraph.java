package project3;

import java.util.ArrayList;

import std.Bag;
import std.In;

public class EdgeWeightedGraph {
	private final int V;// number of vertices
	private int E;// number of edges
	private Bag<Edge>[] adj;// adjacency lists

	public EdgeWeightedGraph(int V) {
		this.V = V;
		this.E = 0;
		adj = (Bag<Edge>[]) new Bag[V];
		for (int v = 0; v < V; v++)
			adj[v] = new Bag<Edge>();
	}

	public EdgeWeightedGraph(In in)
	{
		this(in.readInt());
		int E = in.readInt();
		for (int i = 0; i < E; i++)
		{ // Add an edge.
			int v = in.readInt();
			int w = in.readInt();
			double weight = in.readDouble();
			Edge newEdge = new Edge(v, w, weight);
			addEdge(newEdge);
		}
	}


	public int V() {
		return V;
	}

	public int E() {
		return E;
	}

	public void addEdge(Edge e) {
		int v = e.either(), w = e.other(v);
		adj[v].add(e);
		adj[w].add(e);
		E++;
	}
	
	public Edge getEdge(int v, int w){
		Bag<Edge> b = adj[v];
		for (Edge e : b){
			if (e.either() == v && e.other(e.either()) == w
					||
					e.either() == w && e.other(e.either()) == v)
			{
				return e;
			}
		}
		return null;
	}
	
	public double getEdgeWeight(int v, int w){
		Bag<Edge> b = adj[v];
		for (Edge e : b){
			if (e.either() == v && e.other(e.either()) == w
					||
					e.either() == w && e.other(e.either()) == v)
			{
				return e.weight();
			}
		}
		return 99;
	}

	public ArrayList<Integer> adj(int v) {
		ArrayList<Integer> returnList = new ArrayList<Integer>();
		for (Edge e : adj[v]){
			returnList.add(e.other(v));
		}
		return returnList;
	}

	public Iterable<Edge> edges() {
		Bag<Edge> b = new Bag<Edge>();
		for (int v = 0; v < V; v++)
			for (Edge e : adj[v])
				if (e.other(v) > v)
					b.add(e);
		return b;
	}

}

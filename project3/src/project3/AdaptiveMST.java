package project3;

/*************************************************************************
 *  Compilation:  javac AdaptiveMST.java 
 *           or   javac-algs4  AdaptiveMST.java
 *  Execution:    java AdaptiveMST <filename>
 *          or    java-algs4  AdaptiveMST <filename>
 *  Dependencies: StdIn from the Sedgewick and Wayne stdlib library
 *                EdgeWeightedGraph 
 *
 *  uses code by  Robert Sedgewick and Kevin Wayne
 *
 * @author:  Dan Dougherty, for CS 2223 WPI,  November 2014 
 *
 * For project 3 your job is to write the method treeChanges, along with whatever 
 * auxilliary methods you'd like to write.
 *
 * The main procedure should be ready to use, you needn't worry about
 * chaninging that code.
 *
 *************************************************************************/

import java.io.*;
import java.util.*;

import std.Bag;
import std.Cycle;
import std.In;

public class AdaptiveMST {
  

    /**
      REQUIRES: T is a minimum spaning tree for G, e is an edge over the vertices of G,
         interpreted as a modification of G.
         other assumption are in force (see program spec)
      EFFECTS: writes to standard output the changes in T due to the modifiation e
    **/

    // STUB VERSION:  right now this doesn't do anything but echo the input.
    //  Get rid of these printlns !
	//TODO
    public static void treeChanges(EdgeWeightedGraph G, PrimMST t,
                                   Edge e) {
    	boolean updatedEdge = false; //True if this edge already exists and is being updated
        Edge original = null;			//The original edge, before being updated
    	
        //Search for the given edge in the given graph
        for (Edge edge : G.edges()){
    		int v = edge.either();			//One node on the edge
    		int w = edge.other(v);			//The other node on the edge
    		
    		//Are these edges the same? (Note, cannot use weight trick here because weight is
    		//what is being updated
        	if (((v == e.either())
        			&&
        			(w == e.other(v)))
        			||
        			((v == e.other(e.either()))
        			&&
        			(w == e.either())))
        	{
        		updatedEdge = true;
        		original = edge;
        		break;
        	}
        }
    	
        //If the edge exists in the graph, process either Case 1-4
    	if (updatedEdge){
    		//Decide which case the change belongs to
    		
    		//Is the edge in the given minimum spanning tree?
    		boolean isInMST = false;
    		for (Edge edge : t.edges()){
    			if (((edge.either() == original.either())
    					&&
    					(edge.other(edge.either()) == original.other(original.either())))
    					||
    					((edge.either() == original.other(original.either()))
    					&&
    					(edge.other(edge.either()) == original.either())
    					))
    			{
    				isInMST = true;
    				break;
    			}
    		}
    		
    		//If the edge is not in the MST and the weight has increased
    		if (!isInMST && ((e.weight() - original.weight()) > 0)){
    			System.out.println("No change in the tree");
    		}
    		//If the edge is not in the MST and the weight has decreased
    		else if (!isInMST && ((e.weight() - original.weight()) < 0))
    		{
    			//Case 3, create cycle and delete maximum weighted edge
    			Edge removed = findRemovedEdge(G, t, e);
    			
    			//Ensure the removed edge is not the updated edge
    			if (removed.compareTo(e) != 0)
    			{
    				int vr = removed.either();
    				int wr = removed.other(vr);
    				int va = e.either();
    				int wa = e.other(va);
    				System.out.println("the result is to remove tree edge {" + vr + ", " + wr + "} and "
    						+ "replace it by edge {" + va + ", " + wa + "}");
    			}
    			else{
    				System.out.println("No change in the tree");
    			}
    		}
    		//If the edge is in the MST and the weight has decreased
    		else if (isInMST && ((e.weight() - original.weight()) < 0))
    		{
    			System.out.println("No change in the tree");
    		}
    		//If the edge is in the MST and the weight has increased
    		else if (isInMST && ((e.weight() - original.weight()) > 0))
    		{
    			//Case 4: consider the two trees obtained by removing edge from t
    			// find minimum-weight crossing edge and swap if less
    			Edge minCrossingEdge = findMinCrossing(G, t, e);
    			//if Edge is e
    			if (minCrossingEdge.compareTo(e) == 0){
    				System.out.println("No change in the tree");
    			}
    			else{
    				System.out.println("The result is to remove tree edge {" + e.either() + ", " + e.other(e.either()) + "} and "
    						+ "replace it with edge {" + minCrossingEdge.either() + ", " + minCrossingEdge.other(minCrossingEdge.either()) + "}");
    			}
    		}
    		
    	}
    	else //new weighted edge
    	{
    		//Case 5: Add edge, consider cycle, remove maximum.
    		Edge removed = findRemovedEdge(G, t, e);
    		
    		//Ensure the removed edge is not the new edge
			if (removed.compareTo(e) != 0)
			{
				int vr = removed.either();
				int wr = removed.other(vr);
				int va = e.either();
				int wa = e.other(va);
				System.out.println("the result is to remove tree edge {" + vr + ", " + wr + "} and "
						+ "replace it by edge {" + va + ", " + wa + "}");
			}
			else{
				System.out.println("No change in the tree");
			}
    	}     
    }

    
    /** 
     * This function adds the given edge to the given minimum spanning tree.  This forms a cycle within
     * the tree.  The function then finds this cycle and removes the edge within the cycle with
     * the largest weighted edge. It then returns this removed edge.
    **/
    private static Edge findRemovedEdge(EdgeWeightedGraph G, PrimMST t, Edge e) {
		//Create a temporary edge weighted graph using the minimum spanning tree
    	EdgeWeightedGraph temp = t.toEdgeWeightGraph();
		temp.addEdge(e);	//Add the given edge to create a cycle
		
		//These variables will be used to detect the cycles in the graph
		Cycle cycleDetector = new Cycle(temp);
		Iterable<Integer> cycle = cycleDetector.cycle();
		ArrayList<Edge> cycleEdges = new ArrayList<Edge>();
		Iterator<Integer> cycleIterator = cycle.iterator();
		
		//Convert the nodes in the detected cycle to edges and add them to a list
		int previous = cycleIterator.next();
		while (cycleIterator.hasNext()){
			int w = cycleIterator.next();
			Edge ed = G.getEdge(previous, w);
			if (ed == null ||			//null case is found in Case 5
					(((ed.either() == e.either())
					&&
					(ed.other(ed.either()) == e.other(e.either())))
					||
					((ed.either() == e.other(e.either()))
					&&
					(ed.other(ed.either()) == e.either()))))
			{
				cycleEdges.add(e);	//Add the updated edge, not the one found in the given graph
			}
			else{
				cycleEdges.add(ed);
			}
			
			previous = w;
		}
		
		//Find the maximum weighted edge in the cycle and return it.
		Edge maxEdge = cycleEdges.get(0);
		for (Edge ed : cycleEdges){
			if (ed.weight() > maxEdge.weight()){
				maxEdge = ed;
			}
		}
		return maxEdge;
	}

	/**
	 * REQUIRES: a string consisting of 3 tokens representing two ints and a double.
	 * RETURNS:  a weighted edge built from this data.
	 * EFFECTS: in addition to number format exceptions, can throw an
	 * IllegalArgumentException if input is not 3 tokens,
    **/
    public static Edge makeEdge(String inline) 
        throws IllegalArgumentException, NumberFormatException
    {
        // whitespace will separate strings; have to trim leading/trailing whitespace first
        String delims = "[ ]+";
        String[] input_strings = inline.trim().split(delims);

        if (input_strings.length != 3) 
            throw new IllegalArgumentException("Expect 3 numbers per input line");
        int x = Integer.parseInt(input_strings[0]);
        int y = Integer.parseInt(input_strings[1]);
        double weight = Double.parseDouble(input_strings[2]);
        return new Edge(x,y, weight);
    }
    
    /**
     * 
     * @param G is the original EdgeWeightedGraph
     * @param t is the original MST
     * @param e is the updated (increased) edge within the MST
     * @return the least weighted connection edge after e is updated
     */
    public static Edge findMinCrossing(EdgeWeightedGraph G, PrimMST t, Edge e){
    	ArrayList<Edge> t1 = new ArrayList<Edge>();
    	ArrayList<Edge> t2 = new ArrayList<Edge>();
    	ArrayList<Edge> tWithoutE = new ArrayList<Edge>();
    	ArrayList<Edge> remainingEdges = new ArrayList<Edge>();

    	//removed e from MST
    	for (Edge edge : t.edges()){
    		if (edge.either() != e.either() || edge.other(edge.either()) != e.other(e.either())){
    			tWithoutE.add(edge);
    		}
    	}
    	
    	//dfs to determine edges in trees
    	//determine tree1
    	t1 = dfs(tWithoutE, tWithoutE.get(0));
    	//determine tree2
    	t2 = getDiff(tWithoutE, t1);
    	
    	//size of t2 could be 0
    	//size of t1 can never be zero due to how it is produced
    	if (t2.size() == 0){
    		System.out.println("t2: " + t2.size());
    		//find lonely vertice
    		boolean eVertice1Lonely = true;
    		boolean eVertice2Lonely = true;
    		for (Edge edgeT1 : t1){
    			if (edgeT1.either() == e.either() || edgeT1.other(edgeT1.either()) == e.either()){
    				//this vertice is not lonely
    				eVertice1Lonely = false;
    			}
    			if (edgeT1.either() == e.other(e.either()) || edgeT1.other(edgeT1.either()) == e.other(e.either())){
    				//this vertice is not lonely
    				eVertice2Lonely = false;
    			}    				
    		}
    		if (eVertice1Lonely){
    			System.out.println(e.either() + " is lonely");
    			return findMinConnection(G, e, e.either());
    		}
    		else {
    			System.out.println(e.other(e.either()) + " is lonely");
    			return findMinConnection(G, e, e.other(e.either()));
    		}
    	}
    	
    	//create list of remaining edges
    	remainingEdges = getRemaining(G, t1, t2, e);

    	//determine minimum crossing edge
    	Edge tempShortest = null;
    	for (Edge edge : remainingEdges){ 
    		for (Edge edgeT1 : t1){
    			for (Edge edgeT2 : t2){
    				//if edge is a crossing edge
    				if (((edge.either() == edgeT1.other(edgeT1.either()) || edge.either() == edgeT1.either()) && (edge.other(edge.either()) == edgeT2.either() || edge.other(edge.either()) == edgeT2.other(edgeT2.either())) || 
    						((edge.either() == edgeT2.other(edgeT2.either()) || edge.either() == edgeT2.either()) && (edge.other(edge.either()) == edgeT1.either() || edge.other(edge.either()) == edgeT1.other(edgeT1.either()))))) {
    					//maintain shortest crossing edge estimate
    					if(tempShortest == null){
    						//if edgeG is edge e
    						if ((edge.either() == e.either() && edge.other(edge.either()) == e.other(e.either())) || (edge.either() == e.other(e.either()) && edge.other(edge.either()) == e.either())){
        						tempShortest = e;
    						}
    						else{
        						tempShortest = edge;
    						}
    					}
    					//tempShortest is an edge
    					else{
    						//if edge is edge e
    						if ((edge.either() == e.either() && edge.other(edge.either()) == e.other(e.either())) || (edge.either() == e.other(e.either()) && edge.other(edge.either()) == e.either())){
    							if(tempShortest.compareTo(e) > 0){
        							tempShortest = e;
        						}
    						}
    						else{
        						if(tempShortest.compareTo(edge) > 0){
        							tempShortest = edge;
        						}
    						}

    					}
    				}
    			}
    		}
    	}
    	return tempShortest;
    	

    }
    
    /**
     * 
     * @param g is the original EdgeWeightedGraph
     * @param e is the updated (increased) edge within the MST
     * @param vertice is a lonely vertice, abandoned when edge e was removed from the MST
     * @return
     */
    //find the minimum connector edge from lonely vertice to rest
    private static Edge findMinConnection(EdgeWeightedGraph g, Edge e, int vertice) {
		//temp variable for update minEdge
    	Edge minEdge = null;
    	for (Edge edgeG : g.edges()){
    		//check if the edge connects to the lonely vertice 
			if (edgeG.either() == vertice || edgeG.other(edgeG.either()) == vertice){
				//if edgeG is e
				if ((edgeG.either() == e.either() && edgeG.other(edgeG.either()) == e.other(e.either())) 
						|| (edgeG.other(edgeG.either()) == e.either() && edgeG.either() == e.other(e.either()))){
					if (minEdge == null){
						//edgeG is e and minEdge was null
						minEdge = e;
					}
					else{
						if(minEdge.compareTo(e) > 0){
							//edgeG is e and e is shortest
							minEdge = e;
						}
					}
				}
				else{
					//edgeG is not e
					if (minEdge == null){
						minEdge = edgeG;
					}
					else{
						if(minEdge.compareTo(edgeG) > 0){
							minEdge = edgeG;
						}
					}
				}
			}
		}
    	return minEdge;
	}

    /**
     * 
     * @param G is the original list of EdgeWeightedGraph
     * @param t1 is the list of edges pertaining to tree1
     * @param t2 is the list of edges pertaining to tree2
     * @param e is the removed edge from the MST, causing the formation of t1 and t2
     * @return the remaining edges in G after removing t1, t2, and e
     */
	private static ArrayList<Edge> getRemaining(EdgeWeightedGraph G, ArrayList<Edge> t1, ArrayList<Edge> t2, Edge e) {
    	ArrayList<Edge> remaining = new ArrayList<Edge>();
    	boolean breakFlag = false;
    	for (Edge edgeG : G.edges()){
    		for (Edge edgeT1 : t1){
    			//if edgeT is in t1
    			if (edgeG.compareTo(edgeT1) == 0){
    				breakFlag = true;
    				break;
    			}
    		}
    		//check flag
    		if (breakFlag == true){
    			breakFlag = false;
    			continue;
    		}
    		
    		for (Edge edgeT2 : t2){
    			//if edgeT is in t2
    			if (edgeG.compareTo(edgeT2) == 0){
    				breakFlag = true;
    				break;
    			}
    		}
    		//check flag
    		if (breakFlag == true){
    			breakFlag = false;
    			continue;
    		}
    		
    		//if at this point, edgeT is not in t1 or t2
    		remaining.add(edgeG);
    	}
    	
		return remaining;
    	
	}

	/**
	 * 
	 * @param full is an ArrayList of edges
	 * @param partial is a subset of the full ArrayList
	 * @return an ArrayList comprised of the difference between the full and partial ArrayLists
	 */
	//this method takes in an ArrayList of edges, and a sub-ArrayList of the other ArrayList, and returns the difference
    private static ArrayList<Edge> getDiff(ArrayList<Edge> full, ArrayList<Edge> partial) {
		ArrayList<Edge> tDiff = new ArrayList<Edge>();
		boolean alreadyInPartial = false;
		
		for (Edge edgeFromFull : full){
			for (Edge edgeFromPartial : partial){
				if (edgeFromFull.compareTo(edgeFromPartial) == 0){
					alreadyInPartial = true;
					break;
				}
			}
			if(!alreadyInPartial){
				tDiff.add(edgeFromFull);
			}
			
			alreadyInPartial = false;
		}
		return tDiff;
	}

    /**
     * 
     * @param tWithoutE is the remaining trees after removing edge e from MST t
     * @param currentE is the current edge being examined in a tree
     * @return an ArrayList of edges that make up one of the trees in tWithoutE
     */
	public static ArrayList<Edge> dfs(ArrayList<Edge> tWithoutE, Edge currentE){
    	ArrayList<Edge> tPrime = new ArrayList<Edge>();
    	currentE.setVisited(true);
    	//find parents
    	for (Edge edge : tWithoutE){
    		//if connected to current edge
    		if (edge.either() == currentE.either() || edge.either() == currentE.other(currentE.either()) 
    				|| edge.other(edge.either()) == currentE.either() || edge.other(edge.either()) == currentE.other(currentE.either())){
    			//if unvisited
    			if (!edge.isVisited()){
        			tPrime.addAll(dfs(tWithoutE, edge));
    			}
    		}
    	}
    	tPrime.add(currentE);
    	return tPrime;
    }

	/**
	 * main
	 * @throws IOException
	 * @throws IllegalArgumentException
	 */
    public static void main(String[] args) throws IOException, IllegalArgumentException {

        // Read original graph from a file
        // Here is where we use Sedgewick and Wayne stdlib library
        In graph_in = new In(args[0]);
        EdgeWeightedGraph G = new EdgeWeightedGraph(graph_in);

        // Now compute an MST for G
        // Here we just make T something dumb
        PrimMST T = new PrimMST(G);
        int numEdges = 0;
        for (Edge edge : T.edges()){
        	numEdges++;
        }
        //WARNING: THIS IS MOST LIKELY WRONG!!!!!!
        if (numEdges < 50)
        {
        	System.out.println(T.toString());
        }

        // Read the user input, line by line
        // This is all stock Java
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String prompt = "Please enter a weighted edge, or a blank line to exit";
        String input_line;

        System.out.println(prompt);
        while ( (input_line = in.readLine()) !=null && input_line.length() != 0 ) {
            
            try{
                Edge current_edge = makeEdge(input_line);
                treeChanges(G, T, current_edge);
            }
            catch (IllegalArgumentException e) {
                System.out.println(e);
                System.out.println("Try again...");
            }
                System.out.println(prompt);
        }
        System.out.println("bye");
    }
}

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
  

    /*
      REQUIRES: T is a minimum spaning tree for G, e is an edge over the vertices of G,
         interpreted as a modification of G.
         other assumption are in force (see program spec)
      EFFECTS: writes to standard output the changes in T due to the modifiation e
    */

    // STUB VERSION:  right now this doesn't do anything but echo the input.
    //  Get rid of these printlns !
	//TODO
    public static void treeChanges(EdgeWeightedGraph G, PrimMST t,
                                   Edge e) {
    	boolean updatedEdge = false;
        Edge original = null;
    	for (Edge edge : G.edges()){
    		int v = edge.either();
    		int w = edge.other(v);
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
    	
    	if (updatedEdge){
    		//Decide what the change is
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
    			System.out.println("Case 4");
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

    private static Edge findRemovedEdge(EdgeWeightedGraph G, PrimMST t, Edge e) {
		EdgeWeightedGraph temp = t.toEdgeWeightGraph();
		temp.addEdge(e);
		
		Cycle cycleDetector = new Cycle(temp);
		Iterable<Integer> cycle = cycleDetector.cycle();
		ArrayList<Edge> cycleEdges = new ArrayList<Edge>();
		Iterator<Integer> cycleIterator = cycle.iterator();
		
		int previous = cycleIterator.next();
		while (cycleIterator.hasNext()){
			int w = cycleIterator.next();
			Edge ed = G.getEdge(previous, w);
			if (ed == null ||
					(((ed.either() == e.either())
					&&
					(ed.other(ed.either()) == e.other(e.either())))
					||
					((ed.either() == e.other(e.either()))
					&&
					(ed.other(ed.either()) == e.either()))))
			{
				cycleEdges.add(e);
			}
			else{
				cycleEdges.add(ed);
			}
			
			previous = w;
		}
		
		Edge maxEdge = cycleEdges.get(0);
		for (Edge ed : cycleEdges){
			if (ed.weight() > maxEdge.weight()){
				maxEdge = ed;
			}
		}
		return maxEdge;
	}

	/*
      REQUIRES: a string consisting of 3 tokens representing two ints and a double.
      RETURNS:  a weighted edge built from this data.
      EFFECTS: in addition to number format exceptions, can throw an
          IllegalArgumentException if input is not 3 tokens,
    */
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
    
    public static Edge findMinCrossing(EdgeWeightedGraph G, PrimMST t, Edge e){
    	ArrayList<Edge> t1 = new ArrayList<Edge>();
    	ArrayList<Edge> t2 = new ArrayList<Edge>();
    	ArrayList<Edge> tWithoutE = new ArrayList<Edge>();
    	ArrayList<Edge> remainingEdges = new ArrayList<Edge>();
    	
    	//dfs to determine edges in trees
    	
    	for (Edge edge : t.edges()){
    		if (edge.either() != e.either() || edge.other(edge.either()) != e.other(e.either())){
    			tWithoutE.add(edge);
    		}
    	}
    	//determine tree1
    	t1 = dfs(tWithoutE, tWithoutE.get(0));
    	//determine tree2
    	t2 = getDiff(tWithoutE, t1);
    	//create list of remaining edges
    	remainingEdges = getRemaining(G, t1, t2);

    	//determine minimum crossing edge
    	Edge tempShortest = null;
    	/**
    	System.out.println();
    	System.out.println("remaining edges:");
    	for (Edge edge: remainingEdges){
    		System.out.println("edge: {" + edge.either() + ", " + edge.other(edge.either()) + "} " + edge.weight());
    	}
    	System.out.println();
    	System.out.println("t1 edges:");
    	for (Edge edge: t1){
    		System.out.println("t1 edges: {" + edge.either() + ", " + edge.other(edge.either()) + "} " + edge.weight());
    	}
    	System.out.println();
    	System.out.println("t2 edges:");
    	for (Edge edge: t2){
    		System.out.println("t2 edges: {" + edge.either() + ", " + edge.other(edge.either()) + "} " + edge.weight());
    	}
    	**/
    	for (Edge edge : remainingEdges){ 
    		for (Edge edgeT1 : t1){
    			for (Edge edgeT2 : t2){
    				/**
    				System.out.println("edge: {" + edge.either() + ", " + edge.other(edge.either()) + "} " + edge.weight());
    				System.out.println("T1: {" + edgeT1.either() + ", " + edgeT1.other(edgeT1.either()) + "} " + edgeT1.weight());
    				System.out.println("T2: {" + edgeT2.either() + ", " + edgeT2.other(edgeT2.either()) + "} " + edgeT2.weight());
    				**/
    				//if edge is a crossing edge
    				if (((edge.either() == edgeT1.other(edgeT1.either()) || edge.either() == edgeT1.either()) && (edge.other(edge.either()) == edgeT2.either() || edge.other(edge.either()) == edgeT2.other(edgeT2.either())) || 
    						((edge.either() == edgeT2.other(edgeT2.either()) || edge.either() == edgeT2.either()) && (edge.other(edge.either()) == edgeT1.either() || edge.other(edge.either()) == edgeT1.other(edgeT1.either()))))) {
    					/** System.out.println("edgeG is a crossing edge!"); **/
    					//maintain shortest crossing edge estimate
    					if(tempShortest == null){
    						//if edgeG is edge e
    						if ((edge.either() == e.either() && edge.other(edge.either()) == e.other(e.either())) || (edge.either() == e.other(e.either()) && edge.other(edge.either()) == e.either())){
        						tempShortest = e;
        						/**
        						System.out.println("tempShortest was null and is now e");
        						System.out.println(e.weight());
        						**/
    						}
    						else{
        						tempShortest = edge;
        						/**
        						System.out.println("tempShortest was null and is now edge");
        						System.out.println(e.weight());
        						**/
    						}
    					}
    					//tempShortest is an edge
    					else{
    						//if edge is edge e
    						if ((edge.either() == e.either() && edge.other(edge.either()) == e.other(e.either())) || (edge.either() == e.other(e.either()) && edge.other(edge.either()) == e.either())){
    							if(tempShortest.compareTo(e) > 0){
        							tempShortest = e;
        							/**
        							System.out.println("tempShortest has been update to e");
        							System.out.println(e.weight());
        							**/
        						}
    						}
    						else{
        						if(tempShortest.compareTo(edge) > 0){
        							tempShortest = edge;
        							/**
        							System.out.println("tempShortest has been update to edge");
        							System.out.println(e.weight());
        							**/
        						}
    						}

    					}
    				}
    			}
    		}
    	}
    	return tempShortest;
    	

    }
    
    private static ArrayList<Edge> getRemaining(EdgeWeightedGraph G, ArrayList<Edge> t1, ArrayList<Edge> t2) {
    	ArrayList<Edge> remaining = new ArrayList<Edge>();
    	boolean breakFlag = false;
    	//size of t2 may be 0
    	//size of t1 can never be zero due to how it is produced
    	if (t2.size() == 0){
    		System.out.println("t2: " + t2.size());
    	}
    	else{
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
    	}	
		return remaining;
    	
	}

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

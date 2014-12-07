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
    	boolean validEdge = false;
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
        		validEdge = true;
        		original = edge;
        		break;
        	}
        }
    	
    	if (validEdge){
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
    		else if (isInMST && ((e.weight() - original.weight()) < 0))
    		{
    			System.out.println("Case 4");
    			//Case 4: Remove the edge, consider minimum crossing edge, place minimum in tree
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

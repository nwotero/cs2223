/*
 * Donald Bourque, Nicholas Otero
 * Algorithms - CS 2223 B'14
 * Date: 11/8/2014
 * 
 * Testing.java
 */

import java.util.Collections;
import java.util.PriorityQueue;

public class Trading 
{
	PriorityQueue<Bid> sellOrders;
	PriorityQueue<Bid> buyOrders;
	
	private Trading(String[] orders)
	{
		sellOrders = new PriorityQueue<Bid>(20,Collections.reverseOrder()); //maxPQ
		buyOrders = new PriorityQueue<Bid>(20); //minPQ
	}
	
	public static void main(String[] args)
	{
		new Trading(args);
	}

}

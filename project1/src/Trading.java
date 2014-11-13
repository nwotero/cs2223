/*
 * Donald Bourque, Nicholas Otero
 * Algorithms - CS 2223 B'14
 * Date: 11/8/2014
 * 
 * Testing.java
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/*This class simulates the stock market model specified by the CS 2223
 * project 1 requirements. It reads buy and sell orders through standard input
 * and performs trades until the market reaches equilibrium
 */
public class Trading {
	String[] orders;
	PriorityQueue<Bid> sellOrders;
	PriorityQueue<Bid> buyOrders;
	List<Transaction> sales;

	/* This is the constructor for the trading class.  It does the work 
	 * of setting up the buy and sell max and min priority
	 * queues, converting the string orders into Bids, and placing them 
	 * on the queue.
	 * 
	 * These operations are done in a for loop with a call to process
	 * the market after each insertion of a bid to a queue.  This simulates
	 * the "as they come in" procedure of the market.
	 * 
	 * Once all of the inputed strings have been added to queues and processed
	 * this function makes a call to printResults to output the results
	 */
	private Trading(String[] input) {
		orders = input;
		buyOrders = new PriorityQueue<Bid>(20, Collections.reverseOrder()); // maxPQ
		sellOrders = new PriorityQueue<Bid>(20); // minPQ
		sales = new ArrayList<Transaction>(50);

		for (String bid : orders) {
			Bid newBid = bidFromString(bid);

			if (newBid.getType().equals("buy")) {
				buyOrders.add(newBid);
			} else if (newBid.getType().equals("sell")) {
				sellOrders.add(newBid);
			} else {
				System.out.println("Bad input: cannot have bid of type "
						+ newBid.getType());
				return;
			}
			processMarket();
		}
		printResults();
	}

	/*
	 * This function accepts a bid in a string format, processes it
	 * to get it into a readable form, and then creates a Bid object
	 * from it. This object is then returned.
	 */
	private Bid bidFromString(String bid) {
		//Process the string to make sure weird input
		//does not cause the program to fail
		bid = bid.replace("(", "").replace("(", "")
				.replace(")", "").replace(",", "")
				.trim().replaceAll(" +", " ");
		
		//Split the input by the space to get the type, price, and quantity
		String[] typePriceQuant = bid.split(" ");

		//Try to use the split pieces of the bid string to create
		//a Bid object. If this fails, the input was bad.
		Bid newBid;
		try {
			newBid = new Bid(typePriceQuant[0],
					Integer.parseInt(typePriceQuant[1]),
					Integer.parseInt(typePriceQuant[2]));
		} catch (Exception e) {
			System.out.println("Bad input: Exiting due to exception: "
					+ e.toString());
			System.exit(0);
			return null;
		}
		return newBid;
	}

	/*
	 * This function simulates the trading of the market for a single
	 * evolution cycle.  It tries to make trades between the the lowest
	 * asking price bid and the highest bidding price bid. If it can, it
	 * makes the trade and tries again. If it cannot, the market is in
	 * equilibrium and the method exits.
	 */
	private void processMarket() {
		//Loop until equilibrium
		while (true) {
			//Make sure there are bids to trade
			if (buyOrders.isEmpty() || sellOrders.isEmpty()) {
				return;
			}

			//Get information about the minimum asking bid
			//and the maximum bidding bid
			int biddingPrice = buyOrders.peek().getPrice();
			int biddingQuant = buyOrders.peek().getQuant();
			int askingPrice = sellOrders.peek().getPrice();
			int askingQuant = sellOrders.peek().getQuant();

			//If a trade is possible
			if (biddingPrice >= askingPrice) {
				//Remove the bids from their queues
				Bid buyOrder = buyOrders.poll();
				Bid sellOrder = sellOrders.poll();

				int quant = biddingQuant;	//The quantity to be traded
				
				//Determine how much stock is going to be traded
				if (biddingQuant < askingQuant) {
					sellOrder.setQuant(askingQuant - quant);
					sellOrders.add(sellOrder);
				} else if (biddingQuant > askingQuant) {
					quant = askingQuant;
					buyOrder.setQuant(biddingQuant - quant);
					buyOrders.add(buyOrder);
				}
				//Add the trade to the list of transactions
				sales.add(new Transaction(askingPrice, quant));
			} else {
				return;
			}
		}
	}
	
	/*
	 * This function prints the results of the trading after the market has
	 * reached its final equilibrium
	 */
	private void printResults(){
		//Print input
		if (orders.length > 0)
			System.out.println("on input: " + Arrays.toString(orders));
		
		//Print sequence of sales
		System.out.print("the sequence of sales is: [");
		for (int i = 0; i < sales.size(); i++) {
			System.out.print(sales.get(i).toString());
			if (i != (sales.size() - 1)) {
				System.out.print(", ");
			}
		}
		System.out.println("]");

		//Print outstanding sell orders
		System.out.print("the outstanding sell orders are: [");
		int numSell = sellOrders.size();
		for (int i = 0; i < numSell; i++) {
			System.out.print(sellOrders.poll().toString());
			if (i != (numSell - 1)) {
				System.out.print(", ");
			}
		}
		System.out.println("]");

		//Print outstanding buy orders
		System.out.print("the outstanding buy orders are: [");
		int numBuy = buyOrders.size();
		for (int i = 0; i < numBuy; i++) {
			System.out.print(buyOrders.poll().toString());
			if (i != (numBuy - 1)) {
				System.out.print(", ");
			}
		}
		System.out.println("]");
	}

	/*
	 * Main function. 'nuff said.
	 */
	public static void main(String[] args) {
		String[] inputLines = StdIn.readAllLines();
		new Trading(inputLines);
	}

}

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

public class Trading {
	PriorityQueue<Bid> sellOrders;
	PriorityQueue<Bid> buyOrders;
	List<Transaction> sales;

	private Trading(String[] orders) {
		sellOrders = new PriorityQueue<Bid>(20, Collections.reverseOrder()); // maxPQ
		buyOrders = new PriorityQueue<Bid>(20); // minPQ
		sales = new ArrayList<Transaction>(50);

		for (String bid : orders) {
			bid = bid.replace(" ", "").replace("(", "").replace("(", "").replace(")", "");
			String[] typePriceQuant = bid.split(",");
			
			Bid newBid = new Bid(typePriceQuant[0],
					Integer.parseInt(typePriceQuant[1]),
					Integer.parseInt(typePriceQuant[2]));
			
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
		
		if (orders.length > 0)
		System.out.println("on input: " + Arrays.toString(orders));
		System.out.print("the sequence of sales is: [");
		for (int i = 0; i < sales.size(); i++){
			System.out.print(sales.get(i).toString());
			if (i != (sales.size() - 1)){
				System.out.print(", ");
			}
		}
		System.out.println("]");
		
		System.out.print("the outstanding sell orders are: [");
		int numSell = sellOrders.size();
		for (int i = 0; i < numSell; i++){
			System.out.print(sellOrders.poll().toString());
			if (i != (numSell - 1)){
				System.out.print(", ");
			}
		}
		System.out.println("]");
		
		System.out.print("the outstanding buy orders are: [");
		int numBuy = buyOrders.size();
		for (int i = 0; i < numBuy; i++){
			System.out.print(buyOrders.poll().toString());
			if (i != (numBuy - 1)){
				System.out.print(", ");
			}
		}
		System.out.println("]");
	}

	void processMarket(){
		while (true){
			if(buyOrders.isEmpty() || sellOrders.isEmpty()){
				return;
			}
			
			int biddingPrice = buyOrders.peek().getPrice();
			int biddingQuant = buyOrders.peek().getQuant();
			int askingPrice = sellOrders.peek().getPrice();
			int askingQuant = sellOrders.peek().getQuant();
			
			if (biddingPrice >= askingPrice){
				Bid buyOrder = buyOrders.poll();
				Bid sellOrder = sellOrders.poll();
				
				int quant = biddingQuant;
				if (biddingQuant < askingQuant){
					sellOrder.setQuant(askingPrice - quant);
					sellOrders.add(sellOrder);
				}
				else if (biddingQuant > askingQuant){
					quant = askingQuant;
					buyOrder.setQuant(biddingPrice - quant);
					buyOrders.add(buyOrder);
				}
				sales.add(new Transaction(askingPrice, quant));
			}
			else {
				return;
			}
		}
	}

	public static void main(String[] args) {
		System.out.println(Arrays.toString(args));
		new Trading(args);
	}

}

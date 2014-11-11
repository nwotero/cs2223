/*
 * Donald Bourque, Nicholas Otero
 * Algorithms - CS 2223 B'14
 * Date: 11/8/2014
 * 
 * Transaction.java
 */

public class Transaction {
	private int price;
	private int quant;

	public Transaction(int price, int quant) {
		this.price = price;
		this.quant = quant;
	}

	@Override
	public String toString() {
		return "(" + Integer.toString(price) + ", " + Integer.toString(quant)
				+ ")";
	}

}

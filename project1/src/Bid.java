/*
 * Donald Bourque, Nicholas Otero
 * Algorithms - CS 2223 B'14
 * Date: 11/8/2014
 * 
 * Bid.java
 */

public class Bid implements Comparable<Bid>{
	private String type;
	private int price;
	private int quant;

	public Bid(String type, int price, int quant) {
		this.type = type;
		this.price = price;
		this.quant = quant;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getQuant() {
		return quant;
	}

	public void setQuant(int quant) {
		this.quant = quant;
	}

	public String toString() {
		return "(" + type + ", " + Integer.toString(price) + ", "
				+ Integer.toString(quant) + ")";
	}

	@Override
	public int compareTo(Bid that) {
		if (this.getPrice() > that.getPrice()){
			return 1;
		}
		else if (this.getPrice() < that.getPrice()){
			return -1;
		} else {
			return 0;
		}
	}
}

/*
 * Donald Bourque, Nicholas Otero
 * Algorithms - CS 2223 B'14
 * Date: 11/8/2014
 * 
 * Bid.java
 */

//This class models the Buy and Sell bids performed on the Stock Market.
//Each bid consists of a price per share and a quantity of shares.
public class Bid implements Comparable<Bid>{
 private String type;
 private int price;
 private int quant;
 
 //Constructor for the class Bid
 public Bid(String type, int price, int quant) {
  this.type = type;
  this.price = price;
  this.quant = quant;
 }
 
 //Returns whether the bid is of type buy or sell
 public String getType() {
  return type;
 }

 //Sets the bid to type buy or sell
 public void setType(String type) {
  this.type = type;
 }

 //Returns the price per share of the bid
 public int getPrice() {
  return price;
 }

 //Sets the price per share of the bid
 public void setPrice(int price) {
  this.price = price;
 }

 //Returns the bid's quantity of shares
 public int getQuant() {
  return quant;
 }

 //Sets the bid's quantity of shares
 public void setQuant(int quant) {
  this.quant = quant;
 }

 //This method prints out the information for a bid in the form (type,price,quantity).
 public String toString() {
  return "(" + type + ", " + Integer.toString(price) + ", "
    + Integer.toString(quant) + ")";
 }

 /**
 This compareTo() method compares two bids and returns: 
      1 if this bid price per share is greater,
      0 if the bid prices per share are equal,
      -1 if this bid price per share is less than the bid being compared to.
 **/
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

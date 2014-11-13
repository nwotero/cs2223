/*
 * Donald Bourque, Nicholas Otero
 * Algorithms - CS 2223 B'14
 * Date: 11/8/2014
 * 
 * Transaction.java
 */

//This class models a transactions made on the Stock Market.
//Each transaction consists of the number and price of the shares sold.
public class Transaction {
 private int price;
 private int quant;

 //Cronstructor for the class Transaction
 public Transaction(int price, int quant) {
  this.price = price;
  this.quant = quant;
 }

 //This method prints out the information for a transaction in the form (price,quantity).
 @Override
 public String toString() {
  return "(" + Integer.toString(price) + ", " + Integer.toString(quant)
    + ")";
 }

}

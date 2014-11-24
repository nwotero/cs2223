package project2;

public class Node {
	private Node left = null;
	private Node right = null;
	private String data;

	//get the left child
	public Node getLeft() {
		return left;
	}

	//set the left child
	public void setLeft(Node left) {
		this.left = left;
	}

	//get the right child
	public Node getRight() {
		return right;
	}

	//set the right child
	public void setRight(Node right) {
		this.right = right;
	}

	//get the data
	public String getData() {
		return data;
	}

	//set the data
	public void setData(String data) {
		this.data = data;
	}
}

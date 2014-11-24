package project2;

public class Node {
	private Node left = null;
	private Node right = null;
	private int data;

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
	public int getData() {
		return data;
	}

	//set the data
	public void setData(int data) {
		this.data = data;
	}
}

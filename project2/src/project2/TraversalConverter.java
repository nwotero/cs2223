package project2;
import java.util.ArrayList;

public class TraversalConverter {

	//takes in the preorder of a bst in the form String[] and converts to the postorder 
	public static String[] search_pre_to_post(String[] preorder) {
		//variable declarations
		int size = preorder.length;
		String[] postorder = new String[size];
		Node pre[] = new Node[size];
		ArrayList<String> list = new ArrayList<>();
		ArrayList<String> post = new ArrayList<>();
		
		//if empty input
		if (preorder.length == 0){
			System.out.println("Input is null.");
			return new String[0];
		}
		//if single element array
		if (preorder.length == 1){
			return preorder;
		}
		
		// Check to make sure the given array is actually a preorder of a binary search tree
		int count = 0;
		for (String s : preorder){
			list.add(s);	//parse int data from String
			count++;
		}
		try{
			if(isValid(list)){
				System.out.println("Error in search_pre_to_post. Input not a BST.");
			}	
		}
		catch (Exception e){
		}
		
		
		//create array of Nodes for each String
		count = 0;
		for (String s : preorder){
			Node node = new Node();	//create a new node
			node.setData(s);	//parse int data from String
			pre[count] = node;
			count++;
		}
		
		//create bst from preorder
		buildbst(pre[0],pre,1,new String("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz")); //we deserve to not get the edge case that purposely tries to beat this 
		//generate postorder of bst
		post = generatePostOrder(pre[0]);
		
		//convert int back to Strings
		count = 0;
		for (String s : post){
			postorder[count] = s;
			count++;
		}
		
		return postorder;
		
	}
	
	//check if valid bst
	public static boolean isValid(ArrayList<String> arr) throws Exception{
		boolean state = false;
		try {
			state = recValidCheck(arr);
		} catch (Exception e) {
			System.out.println("Error in search_pre_to_post");
		}
		return state;
	}
	
	public static boolean recValidCheck(ArrayList<String> list){
		ArrayList<String> newListLeft = new ArrayList<>();
		ArrayList<String> newListRight = new ArrayList<>();
		int edge = -1;
		
		//base case
		if (list.size() == 1){
			return true;
		}
		
		//find edge
		for (int count = 1; count < list.size(); count++){
			if(list.get(0).compareTo(list.get(count)) < 0){
				edge = count;
				break;
			}
		}
		
		if (edge == -1){
			//then no edge
			//check for greater than and recurse
			for (int j = 1; j < list.size(); j++){
				newListLeft.add(list.get(j));
			}
			for(int j = 0; j < newListLeft.size(); j++){
				if (newListLeft.get(j).compareTo(list.get(0)) > 0){
					return false;
				}
			}
			return recValidCheck(newListLeft);
			
		}
		//there is an edge
		else{
			//create new lists
			for (int j = 1; j < edge; j++){
				newListLeft.add(list.get(j));
			}
			for (int k = edge; k < list.size(); k++){
				newListLeft.add(list.get(k));
			}
			
			//check left for greater than
			for(int j = 0; j < newListLeft.size(); j++){
				if (newListLeft.get(j).compareTo(list.get(0)) > 0 ){
					return false;
				}
			}
			//check right for less than
			for(int j = 0; j < newListRight.size(); j++){
				if (newListLeft.get(j).compareTo(list.get(0)) < 0){
					return false;
				}
			}
			//recurse
			return (recValidCheck(newListLeft) && recValidCheck(newListRight));
		}
	
	}
	
	
    //create a bst from the preorder
	public static int buildbst(Node current, Node[] arr, int i, String biggestSoFar)
	{
		//base case
	    if (i == arr.length) return i;

	    // recurse left
	    if (arr[i].getData().compareTo(current.getData()) < 0)
	    {
	      current.setLeft(arr[i++]);
	      i = buildbst(current.getLeft(), arr, i, current.getData());
	    }

	    // recurse right
	    if (i < arr.length && arr[i].getData().compareTo(biggestSoFar) < 0)
	    {
	      current.setRight(arr[i++]);
	      i = buildbst(current.getRight(), arr, i, biggestSoFar);
	    }

	    return i;
	}
	
	//takes a bst and generates the postorder
    public static ArrayList<String> generatePostOrder(Node Root){
    	//variable declaration
    	ArrayList<String> post = new ArrayList<String>();
    	//if node has a left child
        if(Root.getLeft() != null){
        	//recurse down left subtree
        	
            post.addAll(generatePostOrder(Root.getLeft()));
        }
        
        //if node has a right child
        if(Root.getRight() != null){
        	//recurse down right subtree
            post.addAll(generatePostOrder(Root.getRight()));
        }
        
        //add current node
        post.add(Root.getData());
        
        return post;
    }

	public static String[] pre_in_to_post(String[] preorder, String[] inorder) {
		try{
			String[] returnArray = pre_in_to_post_helper(preorder, inorder);
			return returnArray;
		}catch (Exception e){
			System.out.println("Given arrays cannot be the preorder "
					+ "and inorder traversals of the same tree");
			return new String[0];
		}
		
	}
	
	public static String[] pre_in_to_post_helper(String[] preorder, String[] inorder) 
	throws Exception {
		// Check to make sure the given arrays are actually a preorder and
		// inorder of a binary
		// search tree
		if (preorder.length != inorder.length) {
			throw new Exception();
		} else if (preorder.length == 0) {
			System.out.println("Preorder list is empty!");
			return new String[0];
		} else if ((preorder.length + 1) % 2 != 0){
			System.out.println("The given lists do not encode full binary trees!");
			return new String[0];
		}

		// Base case for recursive algorithm
		if (preorder.length == 1) {
			if (preorder[0] == inorder[0]) {
				String[] baseArray = { preorder[0] };
				return baseArray;
			} else {
				throw new Exception();
			}
		}

		// Partition the inorder array about the first entry in preorder
		String root = preorder[0];
		String[][] partitionedInOrder;
		try {
			partitionedInOrder = partitionArray(root, inorder);
		} catch (Exception e) {
			throw new Exception();
		}
		String[] leftSideIn = trimArray(partitionedInOrder[0]);
		String[] rightSideIn = trimArray(partitionedInOrder[1]);

		// Partition the preorder array using the partitioned inorder array
		String[] leftSidePre = new String[leftSideIn.length];
		String[] rightSidePre = new String[rightSideIn.length];

		for (int j = 0; j < leftSidePre.length; j++) {
			leftSidePre[j] = preorder[j + 1];
		}

		for (int k = 0; k < rightSidePre.length; k++) {
			rightSidePre[k] = preorder[leftSidePre.length + k + 1];
		}

		// Use recursion to convert the partitioned arrays into postorder arrays
		String[] leftSidePost = pre_in_to_post_helper(leftSidePre, leftSideIn);
		String[] rightSidePost = pre_in_to_post_helper(rightSidePre, rightSideIn);
		String[] rootArray = { root };

		// Concatenate the postorder arrays and the root.
		String[] tempArray = concatenateArray(leftSidePost, rightSidePost);
		String[] returnArray = concatenateArray(tempArray, rootArray);

		return returnArray;
	}

	public static String[] pre_post_to_in(String[] preorder, String[] postorder) {
		if ((preorder.length + 1) % 2 != 0){
			System.out.println("The given lists do not encode full binary trees!");
			return new String[0];
		}
		try{
			String[] returnArray = pre_post_to_in_helper(preorder, postorder);
			return returnArray;
		}catch (Exception e){
			System.out.println("Given arrays cannot be the preorder "
					+ "and postorder traversals of the same tree");
			return new String[0];
		}
		
	}
	
	public static String[] pre_post_to_in_helper(String[] preorder, String[] postorder) 
	throws Exception{
		// Check to make sure the given arrays are actually a preorder and
		// postorder of a binary
		// search tree

		if (preorder.length != postorder.length) {
			throw new Exception();
		} else if (preorder.length == 0) {
			System.out.println("Preorder list is empty!");
			return new String[0];
		} 

		// Base case for recursive algorithm
		if (preorder.length == 1) {
			if (preorder[0].equals(postorder[0])) {
				String[] baseArray = {preorder[0]};
				return baseArray;
			} else {
				throw new Exception();
			}
		}

		// Partition the inorder array about the first entry in preorder
		String root = preorder[0];
		String[][] partitionedPostOrder;
		try {
			partitionedPostOrder = partitionArray(root, postorder);
		} catch (Exception e) {
			throw new Exception();
		}
		String[] leftSidePost = trimArray(partitionedPostOrder[0]);
		String[] rightSidePost = trimArray(partitionedPostOrder[1]);

		// Partition the preorder array using the partitioned inorder array
		String[] leftSidePre = new String[leftSidePost.length];
		String[] rightSidePre = new String[rightSidePost.length];

		for (int j = 0; j < leftSidePre.length; j++) {
			leftSidePre[j] = preorder[j + 1];
		}

		for (int k = 0; k < rightSidePre.length; k++) {
			rightSidePre[k] = preorder[leftSidePre.length + k + 1];
		}
		
		String[] rootArray = { root };
		String[] nullArray = {null};
		String[] returnArray = new String[leftSidePre.length + rightSidePre.length + 1];
		if (rightSidePost.length == 0){
			String[] leftSideIn = pre_post_to_in_helper(leftSidePre, leftSidePost);
			boolean inserted = false;
			for (int i = 0; i < leftSideIn.length; i++){
				if (leftSideIn[i] == null){
					leftSideIn[i] = root;
					inserted = true;
				}
			}
			if(!inserted){
				throw new Exception();
			}
			return leftSideIn;
		} else if (leftSidePost.length == 0){
			String[] rightSideIn = pre_post_to_in_helper(rightSidePre, rightSidePost);
			String[] tempArray = concatenateArray(rootArray, nullArray);
			returnArray = concatenateArray(tempArray, rightSideIn);
			return returnArray;
		}else{
			String[] leftSideIn = pre_post_to_in_helper(leftSidePre, leftSidePost);
			String[] rightSideIn = pre_post_to_in_helper(rightSidePre, rightSidePost);
			boolean inserted = false;
			for (int i = 0; i < leftSideIn.length; i++){
				if (leftSideIn[i] == null){
					leftSideIn[i] = root;
					inserted = true;
				}
			}
			if (!inserted){
				throw new Exception();
			}
			String[] tempArray = concatenateArray(leftSideIn, nullArray);
			returnArray = concatenateArray(tempArray, rightSideIn);
			return returnArray;
		}
	}

	private static String[][] partitionArray(String pivot, String[] array)
			throws Exception {
		// Partition the inorder array about the first entry in preorder
		String[] leftSide = new String[array.length - 1];
		int leftIndex = 0;
		String[] rightSide = new String[array.length - 1];
		int rightIndex = 0;
		boolean onLeft = true;

		for (int i = 0; i < array.length; i++) {
			if (array[i].equals(pivot)) {
				onLeft = false;
			} else if (onLeft) {
				leftSide[leftIndex] = array[i];
				leftIndex++;
			} else {
				rightSide[rightIndex] = array[i];
				rightIndex++;
			}
		}
		if (onLeft) {
			throw new Exception();
		}

		String[][] returnArray = new String[2][array.length - 1];
		returnArray[0] = leftSide;
		returnArray[1] = rightSide;
		return returnArray;
	}

	private static String[] concatenateArray(String[] array1, String[] array2) {
		int newLength = array1.length + array2.length;
		String[] returnArray = new String[newLength];

		for (int i = 0; i < array1.length; i++) {
			returnArray[i] = array1[i];
		}

		for (int j = 0; j < array2.length; j++) {
			returnArray[j + array1.length] = array2[j];
		}

		return returnArray;
	}
	
	private static String[] trimArray(String[] input){
		int counter = 0;
		for (String bit : input){
			if (bit == null){
				break;
			}
			else{
				counter++;
			}
		}
		String[] returnArray = new String[counter];
		for (int i = 0; i < counter; i++){
			returnArray[i] = input[i];
		}
		
		return returnArray;
	}
}

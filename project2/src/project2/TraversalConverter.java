package project2;
import java.util.ArrayList;

public class TraversalConverter {

	//takes in the preorder of a bst in the form String[] and converts to the postorder 
	public static String[] search_pre_to_post(String[] preorder) {
		//variable declarations
		int size = preorder.length;
		String[] postorder = new String[size];
		Node pre[] = new Node[size];
		ArrayList<Integer> list = new ArrayList<Integer>();
		ArrayList<Integer> post = new ArrayList<Integer>();
		
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
			list.add(Integer.parseInt(s));	//parse int data from String
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
			node.setData(Integer.parseInt(s));	//parse int data from String
			pre[count] = node;
			count++;
		}
		
		//create bst from preorder
		buildbst(pre[0],pre,1,Integer.MAX_VALUE);
		
		//generate postorder of bst
		post = generatePostOrder(pre[0]);
		
		//convert int back to Strings
		count = 0;
		for (Integer num : post){
			postorder[count] = num.toString();
			count++;
		}
		
		return postorder;
		
	}
	
	//check if valid bst
	public static boolean isValid(ArrayList<Integer> arr) throws Exception{
		boolean state = false;
		try {
			state = recValidCheck(arr);
		} catch (Exception e) {
			System.out.println("Error in search_pre_to_post");
		}
		return state;
	}
	
	public static boolean recValidCheck(ArrayList<Integer> list){
		ArrayList<Integer> newListLeft = new ArrayList<Integer>();
		ArrayList<Integer> newListRight = new ArrayList<Integer>();
		int edge = -1;
		
		//base case
		if (list.size() == 1){
			return true;
		}
		
		//find edge
		for (int count = 1; count < list.size(); count++){
			if(list.get(count) > list.get(0)){
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
				if (newListLeft.get(j) > list.get(0)){
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
				if (newListLeft.get(j) > list.get(0)){
					return false;
				}
			}
			//check right for less than
			for(int j = 0; j < newListRight.size(); j++){
				if (newListLeft.get(j) < list.get(0)){
					return false;
				}
			}
			//recurse
			return (recValidCheck(newListLeft) && recValidCheck(newListRight));
		}
	
	}
	
	
    //create a bst from the preorder
	public static int buildbst(Node current, Node[] arr, int i, int biggestSoFar)
	{
		//base case
	    if (i == arr.length) return i;

	    // recurse left
	    if (arr[i].getData() < current.getData())
	    {
	      current.setLeft(arr[i++]);
	      i = buildbst(current.getLeft(), arr, i, current.getData());
	    }

	    // recurse right
	    if (i < arr.length && arr[i].getData() < biggestSoFar)
	    {
	      current.setRight(arr[i++]);
	      i = buildbst(current.getRight(), arr, i, biggestSoFar);
	    }

	    return i;
	}
	
	//takes a bst and generates the postorder
    public static ArrayList<Integer> generatePostOrder(Node Root){
    	//variable declaration
    	ArrayList<Integer> post = new ArrayList<Integer>();
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
		// Check to make sure the given arrays are actually a preorder and
		// inorder of a binary
		// search tree
		if (preorder.length != inorder.length) {
			printError("pre_in_to_post");
			return new String[0];
		} else if (preorder.length == 0) {
			printError("pre_in_to_post");
		}

		// Base case for recursive algorithm
		if (preorder.length == 1) {
			if (preorder[0] == inorder[0]) {
				String[] baseArray = { preorder[0] };
				return baseArray;
			} else {
				printError("pre_in_to_post");
				return new String[0];
			}
		}

		// Partition the inorder array about the first entry in preorder
		String root = preorder[0];
		String[][] partitionedInOrder;
		try {
			partitionedInOrder = partitionArray(root, inorder);
		} catch (Exception e) {
			printError("pre_in_to_post");
			return new String[0];
		}
		String[] leftSideIn = partitionedInOrder[0];
		String[] rightSideIn = partitionedInOrder[1];

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
		String[] leftSidePost = pre_in_to_post(leftSidePre, leftSideIn);
		String[] rightSidePost = pre_in_to_post(rightSidePre, rightSideIn);
		String[] rootArray = { root };

		// Concatenate the postorder arrays and the root.
		String[] tempArray = concatenateArray(leftSidePost, rightSidePost);
		String[] returnArray = concatenateArray(tempArray, rootArray);

		return returnArray;
	}

	public static String[] pre_post_to_in(String[] preorder, String[] postorder) {
		// Check to make sure the given arrays are actually a preorder and
		// postorder of a binary
		// search tree

		if (preorder.length != postorder.length) {
			printError("pre_post_to_in");
			return new String[0];
		} else if (preorder.length == 0) {
			printError("pre_in_to_post");
		}

		// Base case for recursive algorithm
		if (preorder.length == 1) {
			if (preorder[0] == postorder[0]) {
				String[] baseArray = {preorder[0]};
				return baseArray;
			} else {
				printError("pre_post_to_in");
				return new String[0];
			}
		}

		// Partition the inorder array about the first entry in preorder
		String root = preorder[0];
		String[][] partitionedPostOrder;
		try {
			partitionedPostOrder = partitionArray(root, postorder);
		} catch (Exception e) {
			printError("pre_post_to_in");
			return new String[0];
		}
		String[] leftSidePost = partitionedPostOrder[0];
		String[] rightSidePost = partitionedPostOrder[1];

		// Partition the preorder array using the partitioned inorder array
		String[] leftSidePre = new String[leftSidePost.length];
		String[] rightSidePre = new String[rightSidePost.length];

		for (int j = 0; j < leftSidePre.length; j++) {
			leftSidePre[j] = preorder[j + 1];
		}

		for (int k = 0; k < rightSidePre.length; k++) {
			rightSidePre[k] = preorder[leftSidePre.length + k + 1];
		}

		// Use recursion to convert the partitioned arrays into postorder arrays
		String[] leftSideIn = pre_post_to_in(leftSidePre, leftSidePost);
		String[] rightSideIn = pre_post_to_in(rightSidePre, rightSidePost);
		String[] rootArray = { root };
		String[] returnArray = new String[leftSideIn.length + rightSideIn.length + 1];
		
		if (leftSideIn.length == 0){	//If this node has no children
			String[] nullArray = {null};
			String[] tempArray = concatenateArray(rootArray, nullArray);
			returnArray = concatenateArray(tempArray, rightSideIn);
		}else if (rightSideIn.length == 0){ //If this node has no neighbors
			returnArray = concatenateArray(leftSideIn, rootArray);
		}else{ //If this node has children and neighbors
			//Iterate through children of root, insert root at null point
			for (int i = 0; i < leftSideIn.length; i++){
				if (leftSideIn[i] == null){
					leftSideIn[i] = root;
					returnArray = concatenateArray(leftSideIn, rightSideIn); 
					break;
				}
			}
		}
		return returnArray;
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

	private static void printError(String funcName) {
		System.out.println("Error in method " + funcName
				+ ": given arrays cannot"
				+ "be the preorder and inorder traversals of the same tree");
	}
}

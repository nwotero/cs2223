
public class TraversalConverter {

	public static String[] search_pre_to_post(String[] preorder){
		//Check to make sure the given array is actually a preorder of a binary
		//search tree
		
		//Convert the preorder to a postorder
		return null;
	}
	
	public static String[] pre_in_to_post(String[] preorder, String[] inorder){
		//Check to make sure the given arrays are actually a preorder and inorder of a binary
		//search tree
		if (preorder.length != inorder.length){
			printError("pre_in_to_post");
			return new String[0];
		}
		else if(preorder.length == 0){
			printError("pre_in_to_post");
		}
		
		//Base case for recursive algorithm
		if (preorder.length == 1){
			if (preorder[0] == inorder[0])
			{
				String[] baseArray = {preorder[0]};
				return baseArray;
			}
			else{
				printError("pre_in_to_post");
				return new String[0];
			}
		}
		
		//Partition the inorder array about the first entry in preorder
		String root = preorder[0];
		String[] leftSideIn = new String[preorder.length - 1];
		int leftIndex = 0;
		String[] rightSideIn = new String[preorder.length - 1];
		int rightIndex = 0;
		boolean onLeft = true;
		
		for (int i = 0; i < preorder.length; i++){
			if (inorder[i].equals(root))
			{
				onLeft = false;
			}
			else if (onLeft){
				leftSideIn[leftIndex] = inorder[i];
				leftIndex++;
			}
			else{
				rightSideIn[rightIndex] = inorder[i];
				rightIndex++;
			}
		}
		
		if (onLeft){
			printError("pre_in_to_post");
			return new String[0];
		}
		
		//Partition the preorder array using the partitioned inorder array
		String[] leftSidePre = new String[leftSideIn.length];
		String[] rightSidePre = new String[rightSideIn.length];
		
		for (int j = 0; j < leftSidePre.length; j++){
			leftSidePre[j] = preorder[j+1]; 
		}
		
		for (int k = 0; k < rightSidePre.length; k++){
			rightSidePre[k] = preorder[leftSidePre.length + k + 1];
		}
		
		//Use recursion to convert the partitioned arrays into postorder arrays
		String[] leftSidePost = pre_in_to_post(leftSidePre, leftSideIn);
		String[] rightSidePost = pre_in_to_post(rightSidePre, rightSideIn);
		String[] rootArray = {root};
		
		//Concatenate the postorder arrays and the root.
		String[] tempArray = concatenateArray(leftSidePost, rightSidePost);
		String[] returnArray = concatenateArray(tempArray, rootArray);
		
		return returnArray;
	}
	
	public static String[] pre_post_to_in(String[] preorder, String[] postorder){
		//Check to make sure the given arrays are actually a preorder and postorder of a binary
		//search tree
		
		//Convert the preorder and postorder to an inorder
		return null;
	}
	
	private static String[] concatenateArray(String[] array1, String[] array2){
		int newLength = array1.length + array2.length;
		String[] returnArray = new String[newLength];
		
		for (int i = 0; i < array1.length; i++){
			returnArray[i] = array1[i];
		}
		
		for (int j = 0; j < array2.length; j++){
			returnArray[j + array1.length] = array2[j];
		}
		
		return returnArray;
	}
	
	private static void printError(String funcName){
		System.out.println("Error in method " + funcName + ": given arrays cannot"
				+ "be the preorder and inorder traversals of the same tree");
	}
}

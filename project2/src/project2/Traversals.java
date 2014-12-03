/*
 * Donald Bourque, Nicholas Otero
 * CS 2223 - Project 2
 * 11/24/2014
 */

package project2;
/*************************************************************************
Sample code for CS 2223  B term 2014 WPI
@author Dan Dougherty

Reads one or two lines of input from standard input, splits them into
strings separated by whitespace, and reads them in to one or two
String arrays.

Should be helpful in project 2, on tree traversals.

*************************************************************************/
import java.io.*;

public class Traversals {

    public static void main(String[] args) throws IOException {
        
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        String[] traversal1 = new String[100];
        String[] traversal2 = new String[100];
        boolean readAtLeastOneLine   = false;
        boolean readTwoLines = false;

        // whitespace will separate our strings in a given input line
        // this doesn't work for leading/trailing whitespace, so we use trim() below.
        String delims = "[ ]+";

        String s1 = (in.readLine()).trim();
        if  (s1 !=null && s1.length() != 0) {
            traversal1 = s1.split(delims);
            readAtLeastOneLine = true;
        }
        /* at this point the first line of input has been read into an array of Strings, namely 
           traversal1 */

        String s2 = (in.readLine()).trim();
        if  (s2 !=null && s2.length() != 0) {
            traversal2 = s2.split(delims);
            readTwoLines = true;
        }

        /* at this point one or two lines of input have been read into an array or two of Strings, namely 
           traversal1  and traversal2.  The boolean variables readAtLeastOneLine and readTwoLines
           capture how much was read.
        */

        
        /* Testing */
        System.out.println(readAtLeastOneLine);
        System.out.println(readTwoLines); 
        if (readAtLeastOneLine){
        	System.out.println("traversal1 length was : " + traversal1.length);
        	if(readTwoLines){
        		System.out.println("traversal2 length was : " + traversal2.length);
        		for (String s : TraversalConverter.pre_in_to_post(traversal1, traversal2)){
        			System.out.print(s + " ");
        		}
        		System.out.println("");
        		for (String s : TraversalConverter.pre_post_to_in(traversal1, traversal2)){
        			System.out.print(s + " ");
        		}
        		System.out.println("");
        	}
        	else{
        		for (String s : TraversalConverter.search_pre_to_post(traversal1)){
        			System.out.print(s + " ");
        		}
        		System.out.println("");
        	}
        }
    } //main
} // class Traversals

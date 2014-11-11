
public class InputDemo {

    /* Here's how to have your program get input from "stdin"
       (This is the way to write project 1)
    */
    public static void main(String[] args) {

        // get the input lines into an array
        String[] inputLines = StdIn.readAllLines();
        int numLines = inputLines.length;

        // now do interesting stuff with the array inputLines

        System.out.println("number of lines in stdin was " + numLines);
    }
}
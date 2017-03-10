import java.io.PrintStream;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

/**
 * Created by curti_000 on 3/7/2017.
 */
public class HuffmanCode {

    private HuffmanNode root;

    //Makes a new HuffmanCode object from the given frequencies.
    public HuffmanCode(int[] frequencies) {
        Queue<HuffmanNode> order = addLetters(frequencies);
        while(order.size() != 1) {
            HuffmanNode node1 = order.remove();
            HuffmanNode node2 = order.remove();
            HuffmanNode combined = new HuffmanNode('?', node1, node2);
            combined.frequency = node1.frequency + node2.frequency;
            order.add(combined);
        }
        this.root = order.remove();
    }

    // pre: Given file exists in standard format
    // post: Initializes HuffmanCode object
    public HuffmanCode(Scanner input) {
        while(input.hasNextLine()) {
            int ASCII = Integer.parseInt(input.nextLine());
            char letter = (char)ASCII;
            String code = input.nextLine();
            this.root = readCode(this.root, letter, code);
        }
    }

    // post: Stores the current huffman codes to the given output
    public void save(PrintStream output) {
        writeTree(this.root, output, "");
    }

    // pre: Input file contains a legal encoding of characters.
    // post: Takes individual bits from the input file and composes the output file.
    public void translate(BitInputStream input, PrintStream output) {
        HuffmanNode t = this.root;
        while(input.hasNextBit()) {
            int bit = input.nextBit();
            if(bit == 0) {
                t = t.left;
            } else {
                t = t.right;
            }
            if(t.left == null && t.right == null) {
                output.print(t.dataLetter);
                t = this.root;
            }
        }
    }

    // pre: Takes in a letter and the code
    // post: builds a tree of till it finishes reading the given code.
    // Returns the tree
    private HuffmanNode readCode(HuffmanNode current, char letter, String code) {
        if(code.length() == 0) {
            current = new HuffmanNode(letter);
        } else {
            if(current == null && code.length() >= 1) {
                current = new HuffmanNode('?');
            } else if(current == null && code.length() == 1) {
                current = new HuffmanNode(letter);
            }
            if(code.charAt(0) == '0') {
                current.left = readCode(current.left, letter, code.substring(1));
            } else {
                current.right = readCode(current.right, letter, code.substring(1));
            }
        }
        return current;
    }

    // post: Reads through the tree. When it reaches a letter, it prints to
    //       output the ASCII value of that letter + code.
    private void writeTree(HuffmanNode current, PrintStream output, String code) {
        if(current.left == null && current.right == null) {
            output.println((int)current.dataLetter);
            output.println(code);
        } else {
            writeTree(current.left, output, code + 0);
            writeTree(current.right, output, code + 1);
        }
    }

    // post: Uses the given frequencies to add their respective objects with
    //       characters to the queue.
    private Queue<HuffmanNode> addLetters(int[] frequencies) {
        Queue<HuffmanNode> order = new PriorityQueue<HuffmanNode>();
        for(int i = 0; i < frequencies.length; i ++) {
            int freq = frequencies[i];
            if(freq != 0) {
                char letter = (char)i;
                HuffmanNode node = new HuffmanNode(letter);
                node.frequency = freq;
                order.add(node);
            }
        }
        return order;
    }
    private static class HuffmanNode implements Comparable<HuffmanNode> {

        public char dataLetter;
        public int frequency;
        public HuffmanNode left;
        public HuffmanNode right;

        // post: Initializes the letter of the object.
        //       Intializes the left and right objects for the current object
        public HuffmanNode(char letter, HuffmanNode left, HuffmanNode right) {
            this.dataLetter = letter;
            this.left = left;
            this.right = right;
        }

        // post: Initializes a HuffmanNode object and it's character

        public HuffmanNode(char letter) {
            this(letter, null, null);
        }

        // post: Allows comparison between the current and other HuffmanNode.
        public int compareTo(HuffmanNode other) {
            return ((Integer)this.frequency).compareTo(other.frequency);
        }
    }
}

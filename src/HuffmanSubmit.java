/* Name: Yoo (Daniel) Choi
 * Net ID: ychoi42
 * Assignment Name: CSC 172 Project 2 Huffman Coding
 * Lab Section: MW 2:00 - 3:15
 * I did not collaborate with anyone on this assignment.
 */

/* This class contains the encode and decode methods, along with other methods used internally. 
 */

import java.util.*;
import java.io.*;

public class HuffmanSubmit implements Huffman {
	
	static class Node {
		Character value = null;   //properties of a node
		Integer freq = null;
		Node left = null; Node right = null;
		public Node(char value, int freq) {  //used in the priority queue
			this.value = value;
			this.freq = freq;
		}
		public Node(Character value, Integer freq, Node left, Node right) {  //used in creating binary tree
			this.value = value;
			this.freq = freq;
			this.left = left;
			this.right = right;
		}
	}
	public void encode(String inputFile, String outputFile, String freqFile) {
		//initial conversion
		BinaryIn in = new BinaryIn(inputFile);
		ArrayList<Character> initialChars = new ArrayList<Character>();    //converts the input file into chars
		while (!in.isEmpty()) {  //read file, add chars into initialChars
			char temp = in.readChar();
			initialChars.add(temp);
		}
		
		//hashmap of character and its frequency in integers
		HashMap<Character, Integer> intFreq = new HashMap<Character, Integer>();
		for (Character c : initialChars) {
			if (intFreq.containsKey(c))
				intFreq.replace(c, intFreq.get(c) + 1);   //if the map already has c, add 1 to count
			else
				intFreq.put(c, 1);   //if new char, insert it with a 1
		}
		
		//create huffman tree and frequency file
		Node root = createBinaryTree(createPriorityQueue(intFreq));
		System.out.println("----------------PRINTING TREE IN ENCODE---------------");
		printTree(root);
		try {
			printFrequencyFile(intFreq, freqFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		//create hashmap with char to binary string based on huffman tree, for the encoded file
		HashMap<Character, String> huffmanMap = new HashMap<Character, String>();
		buildHuffmanMap(root, huffmanMap, "");
		System.out.println("--------------PRINTING HUFFMANMAP------------");
		printHuffmanMap(huffmanMap);
		
		//print the encoded file
		try {
			printEncFile(outputFile, initialChars, huffmanMap);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


   public void decode(String inputFile, String outputFile, String freqFile){
	   BinaryIn in = new BinaryIn(inputFile);  //.enc file
	   BinaryOut out = new BinaryOut(outputFile);
	   
	   //create intFreq like in encode(), but this time we create intFreq from the freqFile not the actual file
	   HashMap<Character, Integer> intFreq = new HashMap<Character, Integer>();
	   Scanner scan = null;
	   String s;  //temporary functional string
	   try {
		scan = new Scanner(new File(freqFile));
	   } catch (FileNotFoundException e) {
		e.printStackTrace();
	   }
	   while (scan.hasNext()) {
		   s = scan.nextLine();
		   String[] charAndInt = s.split(":");
		   char c = (char) Integer.parseInt(charAndInt[0], 2);  //convert binary string back to char
		   int i = Integer.parseInt(charAndInt[1]);
		   intFreq.put(c, i);
	   }
	   scan.close();
	   
	   //create huffman tree
	   Node root = createBinaryTree(createPriorityQueue(intFreq));
	   System.out.println("----------------PRINTING TREE IN DECODE---------------");
	   printTree(root);
	   
	   //decoding the .enc file using the huffman tree as a reference
	   Node position = root;
	   while (!in.isEmpty()) {
		   //depending on boolean, move down the huffman tree to locate the char 
		   boolean b = in.readBoolean();
		   if (b) 
			   position = position.right;
		   else
			   position = position.left;
		  
		   //if we find a leaf
		   if (position.value != null) {
			   out.write(position.value);
			   out.flush();
			   position = root;   //reset the position to the root 
		   }  //note that the root is always null (internal node) so it's okay to move left/right BEFORE we check whether the node is a leaf
	   }
	   out.flush();
	   
   }
   
   //priority queue based on frequency integer
   private static PriorityQueue<Node> createPriorityQueue(Map<Character, Integer> map){
	   PriorityQueue<Node> q = new PriorityQueue<Node>(2, new Comparator<Node>(){  //initial size of 2, Node
		 //nodes in the queue are compared by values of freq
		   @Override
		   public int compare(Node n1, Node n2) { 
			   return (n1.freq.compareTo(n2.freq));
		   }
	   });
	   
	   //add nodes one by one into the priority queue, from the intFreq hashmap
	   for (Map.Entry<Character, Integer> entry : map.entrySet()) {
		   Node temp = new Node(entry.getKey(), entry.getValue());
		   q.add(temp);
	   }
	   return q;
   }

   //create binary tree from the priority queue
   private static Node createBinaryTree(PriorityQueue<Node> q) {
	   //keep combining the two minimum values in the queue with an internal node with the sum of their freqs
	   while(q.size() >= 2) {  //note that this runs when there are two nodes left --> when this ends there is one root
		   Node min1 = q.poll();
		   Node min2 = q.poll();
		   Node parent = new Node(null, min1.freq + min2.freq, min1, min2);
		   q.add(parent);
	   }
	   return q.poll();    //return the final single root
   }
   
   //prints the tree in pre-order traversal
   private static void printTree(Node root) {
	   if (root.value != null)
		   System.out.println(root.value + ":" + root.freq);
	   if (root.left != null)
		   printTree(root.left);
	   if (root.right != null)
		   printTree(root.right);
   }
   
   //creates the frequency file
   private static void printFrequencyFile(HashMap<Character, Integer> map, String freqFile) throws IOException{
	   BufferedWriter pr = new BufferedWriter(new FileWriter(freqFile));
	   pr.flush();
	   
	   for (Map.Entry<Character, Integer> entry : map.entrySet()) {
		   String toBeAdded = Integer.toBinaryString(entry.getKey());
		   
		   //if the binary string is less than 8 digits (as the method doesn't add leading zeroes for us), add 0's until it does
		   if (Integer.toBinaryString(entry.getKey()).length() < 8) {
			   while (toBeAdded.length() < 8)
				   toBeAdded = "0" + toBeAdded;
		   }
		   
		   //write on a line in the file for each character in the hashmap in the form of "11001010:40"
		   pr.write(toBeAdded + ":" + entry.getValue());
		   pr.newLine();
	   }
	   pr.close();
   }
   
   //building a hashmap with the character pointing to its huffman binary string
   private static void buildHuffmanMap(Node root, HashMap<Character, String> huffmanMap, String binary) {
	   //if the node is a leaf and has a value, put an entry with the node's character with its path down represented as a binary string
	   if (root.value != null)
		   huffmanMap.put(root.value, binary);
	   else {  //otherwise go left and right, adding a 0 or 1 appropriately to 'binary'
		   //note that since this a full binary tree, all internal nodes will have 2 children, always eventually getting to a leaf
		   buildHuffmanMap(root.left, huffmanMap, binary + "0");
		   buildHuffmanMap(root.right, huffmanMap, binary + "1");
	   }
   }
   
   //prints the huffman hashmap
   private static void printHuffmanMap(HashMap<Character, String> map) {
	   for (Map.Entry<Character, String> entry : map.entrySet()) {
		   System.out.println(entry.getKey() + " : " + entry.getValue());
	   }
   }
   
   //create the encoded file
   private static void printEncFile(String outputFile, ArrayList<Character> initialChars, HashMap<Character, String> huffmanMap) throws Exception {
	   BinaryOut out = new BinaryOut(outputFile);
	   
	   for (Character c : initialChars) {
		   String binary = huffmanMap.get(c);   //for each char in the map, get the huffman binary, and work with each 0 or 1
		   String[] zeroOrOne = binary.split("");
		   for (String s : zeroOrOne) {
			   if (s.equals("0"))
				   out.write(false);
			   if (s.equals("1"))
				   out.write(true);
		   }
	   }
	   
	   out.flush();
   }
   
   public static void main(String[] args) {
      Huffman huffman = new HuffmanSubmit();
		huffman.encode("ur.jpg", "ur.enc", "freq.txt");
		huffman.decode("ur.enc", "ur_dec.jpg", "freq.txt");
		
		// After decoding, both ur.jpg and ur_dec.jpg should be the same. 
		// On linux and mac, you can use `diff' command to check if they are the same. 
   }

}

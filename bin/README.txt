/* Name: Yoo (Daniel) Choi
 * Net ID: ychoi42
 * Assignment Name: CSC 172 Project 2: Huffman Coding
 * Lab Section: MW 2:00 - 3:15
 * I did not collaborate with anyone on this assignment.
 */

HuffmanSubmit

For my Node class, I have four properties: the character value, its frequency, left, and right. 

For my encode method, I first create a HashMap<Character, Integer> to store the frequencies in integer form. I then use this hashmap
to create the huffman tree and the frequency file. Then, I create a HashMap<Character, String> where the string is a binary string representation
of the integer. This is what I use to create the encoded file.

For my decode method, I first create the same HashMap<Character, Integer> for integer frequencies from the freqFile. Then I recreate the
huffman tree. Using these two data structures, I can decode the encoded file and produce a decoded copy of the original file.

To run:
javac HuffmanSubmit.java
java HuffmanSubmit

To test for difference:
fc file1 file2


I referred to oddguan's directory on Github. However, all of the code was handwritten by myself. 
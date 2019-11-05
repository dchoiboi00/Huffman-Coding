# Huffman-Coding
Uses Huffman encoding to implement lossless compression. Can compress files of any type.

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

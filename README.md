# zci65
**Fast, secure, efficent stream cipher**

Zci65 uses a 32-byte key and does not use a Initialization Vector (IV); the algorithm use a internal 256 length array s[] of 32 bit integer and other 2 internal 32 bit fields, k and c; in total the status size is 1088 bytes.
The internal state is continuously changed by the source content to be encrypted.
The code is very simple, the main functions are:
```java	
public byte encipher(byte b) {
	int p = ((k >>> 24) ^ (k >>> 16) ^ (k >>> 8) ^ k) & 0xFF;
	int r = s[p];
	s[p] = k ^ (r * 5);
	k = s[(b + c++) & 0xFF] ^ (k * 5);
	return (byte)((r >>> 24) ^ (r >>> 16) ^ (r >>> 8) ^ r ^ b);
}

public byte decipher(byte b) {
	int p = ((k >>> 24) ^ (k >>> 16) ^ (k >>> 8) ^ k) & 0xFF;
	int t = s[p];
	s[p] = k ^ (t * 5);
	int r = ((t >>> 24) ^ (t >>> 16) ^ (t >>> 8) ^ t ^ b) & 0xFF;
	k = s[(r + c++) & 0xFF] ^ (k * 5);
	return (byte)r;
}
```
The main properties of a good encryption algorithm are:

1. Produce an output indistinguishable from a random sequence of bytes 
2. Do it in the shortest possible time (and this is especially true for a stream cipher) 
3. Make it impossible to predict the output sequence starting from the data already produced.

Point 1 can be verified with statistical analyses; point 2 by measuring the times and comparing them with benchmark values, while point 3 can only be verified by an independent cryptographic analysis and therefore I invite the researchers to consider zci65 and confirm or deny this feature!

### Entropy of output
This tool provides visible feedback on the entropy level of a sequence of bytes.

Input: Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris a quam et sem facilisis elementum. Cras tristique, tellus nec tincidunt commodo, elit ex rutrum ipsum, suscipit tempor lacus ex vel arcu. Proin commodo, nisl id facilisis sodales, tortor felis ultricies lacus, at suscipit lectus quam ac sapien. Sed mollis orci eu lectus pretium, vitae ultrices sapien feugiat. In eget posuere libero, sit amet commodo ex. Donec ac pharetra mauris. Ut tristique sodales dolor, sed lacinia metus placerat ut. Phasellus vitae eleifend erat. | ![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/lorem_ipsum_txt.png.png)


### The output stream produced by the encryption has a uniformly random distribution
To compare the quality of the random distribution of the output, the encryption output streams of two algorithms were analyzed and compared: Salsa20 and SecRnd (a simple, but slow algorithm that generates a secure random sequence based on SHA-256 which puts exclusive with the input stream)



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

### Entropy of encryption
This tool provides visible feedback on the entropy of the zci65 encryped stream

|Input/Output|Byte Map|
|:---|:---:|
|**Input:** Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris a quam et sem facilisis elementum. Cras tristique, tellus nec tincidunt commodo, elit ex rutrum ipsum, suscipit tempor lacus ex vel arcu. Proin commodo, nisl id facilisis sodales, tortor felis ultricies lacus, at suscipit lectus quam ac sapien. Sed mollis orci eu lectus pretium, vitae ultrices sapien feugiat. In eget posuere libero, sit amet commodo ex. Donec ac pharetra mauris. Ut tristique sodales dolor, sed lacinia metus placerat ut. Phasellus vitae eleifend erat. | ![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/lorem_ipsum_txt.png)|
|**Output:** 128AC5879E1EBAB1041E2EA8ECA23A6BB5A77E9214955F4BFD293BFD5DBE9635BE26C95302B22BE2487A26 70DC4003C0128623793CA125EA8774C5B0DFECEFD6E65205B87D3856F1C6E3018140EF569FAA0F78386674 E2D0F628A5D804F55C76E5AD45E9B079DD065E79FD87615669FCBB2A889C3D6770A5ECA41D67F0DCB07B99 FA0325A40BC72FBAE6D6B73704140341F1B1A64A7FDBDFF5CFBC19A8D3C6482273290F2B81E7E6F04F0688 E02EE0AD587446F26B013CC88B9889CB6E79A9D1E0E9D5BD4E4BE98DCA6143389F73495D43BB1744DB0EA2 17929FB4952290E65217057340F02AA3DB6B500F60F6209E953DBEEB5B17C55BD2AA5EA419B5417F112F10 AB7523C0920673F4F8AD1EB3BA9DCBB31FADA7A4705FB027DFEB1A26BE40559FA47DC4E4BB01E8E3380DDA F882B5DBA4688E64280FF8EF4C925B4D83CB18708E5F01EF976DEE60ABC53412A3CF6FA1D05393EFB7A464 626F164D0635F1541D085B568C7989C37677A30924B3712053ABEF838EB09272F67F1D79571BF09B9EFA37 18BAD5365D74ABBE3BADC1498F268B74949F2CC01FAF6E3DF18222BE943EE5F973955724D9A63103B6323D 93EC2F213CDF436C78E419884680CB2428F5FD9BAE86BE1B97D0FF32A525F8E89C5E86B0A820EB0B936436 4F3A90CA008951E5D624E2FD69A60750B32556D4517A09AFDFA91473C45D835ED917839919D0316BA01D71 B859350CF58DBD5465F469E73FA3BEE725386C 
 |![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/lorem_ipsum_zci65.png)|


### The output stream produced by the encryption has a uniformly random distribution
To compare the quality of the random distribution of the output, the encryption output streams of two algorithms were analyzed and compared: Salsa20 and SecRnd (a simple, but slow algorithm that generates a secure random sequence based on SHA-256 which puts exclusive with the input stream)



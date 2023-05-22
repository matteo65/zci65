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
This tool shows the byte distribution of a file on a square window displaying a 16x16 matrix (one element for each byte). 
The least frequent byte is displayed in white, the most frequent byte in black, the others are proportionally distributed in shades of gray (total 256 shades)
|lorem_ipsum.txt|lorem_ipsum.txt.zci65|
|:--- |:--- |
|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/lorem_ipsum_txt.png)|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/lorem_ipsum_zci65.png)|
|Length = 2982 bytes                |Length = 2982 bytes              |
|Min Frequency = 0 (214 instances)  |Min Frequency = 4 (12 instances) |
|Max Frequency = 441 (13 instances) |Max Frequency = 22 (14 instances)|
|Average Frequency μ = 11.65        |Average Frequency μ = 11.65        |
|Variance = 2318.26                 |Variance = 10.95                 |
|Deviation Standard σ = 48.15       |Deviation Standard σ = 3.31        |
|Chi squared 𝛘2 = 2318.26            |Chi squared 𝛘2= 10.95              |
|**Coefficient of Variation σ/μ = 413.35%**|**Coefficient of Variation σ/μ = 28.4%**|

|shakespeare_romeo-and-juliet.pdf|shakespeare_romeo-and-juliet.pdf.zci65|
|:--- |:--- |
|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/shakespeare_romeo-and-juliet.pdf.png)|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/shakespeare_romeo-and-juliet.pdf.zci65.png)|
|Length = 1064366 bytes             |Length = 1064366 bytes            |
|Min Frequency = 2634 (7 instances) |Min Frequency = 3904 (4 instances)|
|Max Frequency = 31436 (4 instances)|Max Frequency = 4313 (7 instances)|
|Average Frequency μ = 4157.68      |Average Frequency μ = 4157.68       |
|Variance = 5780361.23              |Variance = 4280.4                 |
|Deviation Standard σ = 2404.24     |Deviation Standard σ = 65.42        |
|Chi squared 𝛘2 = 5780361.23         |Chi squared 𝛘2 = 4280.4              |
|**Coefficient of Variation σ/μ = 57.83%**|**Coefficient of Variation σ/μ = 1.57%**|



**Input:** File [shakespeare_romeo-and-juliet.pdf](Resource/shakespeare_romeo-and-juliet.pdf)

![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/shakespeare_romeo-and-juliet.pdf.png)

**Output:** File [shakespeare_romeo-and-juliet.pdf.zci65](Resource/shakespeare_romeo-and-juliet.pdf.zci65)

![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/shakespeare_romeo-and-juliet.pdf.zci65.png)

### The output stream produced by the encryption has a uniformly random distribution
To compare the quality of the random distribution of the output, the encryption output streams of two algorithms were analyzed and compared: Salsa20 and SecRnd (a simple, but slow algorithm that generates a secure random sequence based on SHA-256 which puts exclusive with the input stream)



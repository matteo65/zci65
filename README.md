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

**Input:** Lorem ipsum dolor sit amet, consectetur adipiscing elit. Mauris a quam et sem facilisis elementum. Cras tristique, tellus nec tincidunt commodo, elit ex rutrum ipsum, suscipit tempor lacus ex vel arcu. Proin commodo, nisl id facilisis sodales, tortor felis ultricies lacus, at suscipit lectus quam ac sapien. Sed mollis orci eu lectus pretium, vitae ultrices sapien feugiat. In eget posuere libero, sit amet commodo ex. Donec ac pharetra mauris. Ut tristique sodales dolor, sed lacinia metus placerat ut. Phasellus vitae eleifend erat.

![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/lorem_ipsum_txt.png)

**Output:** 12 8A C5 87 9E 1E BA B1 04 1E 2E A8 EC A2 3A 6B B5 A7 7E 92 14 95 5F 4B FD 29 3B FD 5D BE 96 35 BE 26 C9 53 02 B2 2B E2 48 7A 26 70 DC 40 03 C0 12 86 23 79 3C A1 25 EA 87 74 C5 B0 DF EC EF D6 E6 52 05 B8 7D 38 56 F1 C6 E3 01 81 40 EF 56 9F AA 0F 78 38 66 74 E2 D0 F6 28 A5 D8 04 F5 5C 76 E5 AD 45 E9 B0 79 DD 06 5E 79 FD 87 61 56 69 FC BB 2A 88 9C 3D 67 70 A5 EC A4 1D 67 F0 DC B0 7B 99 FA 03 25 A4 0B C7 2F BA E6 D6 B7 37 04 14 03 41 F1 B1 A6 4A 7F DB DF F5 CF BC 19 A8 D3 C6 48 22 73 29 0F 2B 81 E7 E6 F0 4F 06 88 E0 2E E0 AD 58 74 46 F2 6B 01 3C C8 8B 98 89 CB 6E 79 A9 D1 E0 E9 D5 BD 4E 4B E9 8D CA 61 43 38 9F 73 49 5D 43 BB 17 44 DB 0E A2 17 92 9F B4 95 22 90 E6 52 17 05 73 40 F0 2A A3 DB 6B 50 0F 60 F6 20 9E 95 3D BE EB 5B 17 C5 5B D2 AA 5E A4 19 B5 41 7F 11 2F 10 AB 75 23 C0 92 06 73 F4 F8 AD 1E B3 BA 9D CB B3 1F AD A7 A4 70 5F B0 27 DF EB 1A 26 BE 40 55 9F A4 7D C4 E4 BB 01 E8 E3 38 0D DA F8 82 B5 DB A4 68 8E 64 28 0F F8 EF 4C 92 5B 4D 83 CB 18 70 8E 5F 01 EF 97 6D EE 60 AB C5 34 12 A3 CF 6F A1 D0 53 93 EF B7 A4 64 62 6F 16 4D 06 35 F1 54 1D 08 5B 56 8C 79 89 C3 76 77 A3 09 24 B3 71 20 53 AB EF 83 8E B0 92 72 F6 7F 1D 79 57 1B F0 9B 9E FA 37 18 BA D5 36 5D 74 AB BE 3B AD C1 49 8F 26 8B 74 94 9F 2C C0 1F AF 6E 3D F1 82 22 BE 94 3E E5 F9 73 95 57 24 D9 A6 31 03 B6 32 3D 93 EC 2F 21 3C DF 43 6C 78 E4 19 88 46 80 CB 24 28 F5 FD 9B AE 86 BE 1B 97 D0 FF 32 A5 25 F8 E8 9C 5E 86 B0 A8 20 EB 0B 93 64 36 4F 3A 90 CA 00 89 51 E5 D6 24 E2 FD 69 A6 07 50 B3 25 56 D4 51 7A 09 AF DF A9 14 73 C4 5D 83 5E D9 17 83 99 19 D0 31 6B A0 1D 71 B8 59 35 0C F5 8D BD 54 65 F4 69 E7 3F A3 BE E7 25 38 6C

![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/lorem_ipsum_zci65.png)

**Input:** File [shakespeare_romeo-and-juliet.pdf](Resource/shakespeare_romeo-and-juliet.pdf)

### The output stream produced by the encryption has a uniformly random distribution
To compare the quality of the random distribution of the output, the encryption output streams of two algorithms were analyzed and compared: Salsa20 and SecRnd (a simple, but slow algorithm that generates a secure random sequence based on SHA-256 which puts exclusive with the input stream)



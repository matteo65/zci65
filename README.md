# zci65
**Fast, secure, efficent stream cipher**

Zci65 uses a 32-byte key and does not use an Initialization Vector (IV); the algorithm use a internal 256 length array s[] of 32 bit integer and other 2 internal 32 bit fields, k and c; in total the status size is 1088 bytes.
The internal status is continuously changed by the source content to be encrypted.
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

Point 1 can be verified with statistical analysis; point 2 by measuring the times and comparing them with benchmark values, while point 3 can only be verified by an independent cryptographic analysis and therefore I invite the researchers to consider zci65 and confirm or deny this feature!

### Entropy of encryption
This tool shows the byte distribution of a file on a square window displaying a 16x16 matrix (one element for each byte). 
The least frequent byte is displayed in white, the most frequent byte in black, the others are proportionally distributed in shades of gray (total 256 shades).
The result is that a tendentially dark image corresponds to a higher chaotic level of the analyzed data.

|lorem_ipsum.txt|lorem_ipsum.txt.zci65|
|:--- |:--- |
|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/lorem_ipsum_txt.png)|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/lorem_ipsum_zci65.png)|
|Length = 2982 bytes                |Length = 2982 bytes              |
|Min Frequency = 0 (214 instances)  |Min Frequency = 4 (12 instances) |
|Max Frequency = 441 (13 instances) |Max Frequency = 22 (14 instances)|
|Average Frequency μ = 11.65        |Average Frequency μ = 11.65        |
|Variance σ<sup>2</sup> = 2318.26   |Variance σ<sup>2</sup> = 10.95                 |
|Deviation Standard σ = 48.15       |Deviation Standard σ = 3.31        |
|Chi squared 𝛘<sup>2</sup> = 2318.26 |Chi squared 𝛘<sup>2</sup>= 10.95              |
|**Coefficient of Variation <sup>σ</sup>/<sub>μ</sub> = 413.35%**|**Coefficient of Variation <sup>σ</sup>/<sub>μ</sub> = 28.4%**|

|shakespeare_romeo-and-juliet.pdf|shakespeare_romeo-and-juliet.pdf.zci65|
|:--- |:--- |
|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/shakespeare_romeo-and-juliet.pdf.png)|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/shakespeare_romeo-and-juliet.pdf.zci65.png)|
|Length = 1064366 bytes             |Length = 1064366 bytes            |
|Min Frequency = 2634 (7 instances) |Min Frequency = 3904 (4 instances)|
|Max Frequency = 31436 (4 instances)|Max Frequency = 4313 (7 instances)|
|Average Frequency μ = 4157.68      |Average Frequency μ = 4157.68       |
|Variance σ<sup>2</sup> = 5780361.23|Variance σ<sup>2</sup> = 4280.4                 |
|Deviation Standard σ = 2404.24     |Deviation Standard σ = 65.42        |
|Chi squared 𝛘<sup>2</sup> = 5780361.23 |Chi squared 𝛘<sup>2</sup> = 4280.4              |
|**Coefficient of Variation <sup>σ</sup>/<sub>μ</sub> = 57.83%**|**Coefficient of Variation <sup>σ</sup>/<sub>μ</sub> = 1.57%**|

|shakespeare_romeo-and-juliet.7z|shakespeare_romeo-and-juliet.7z.zci65|
|:--- |:--- |
|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/shakespeare_romeo-and-juliet.7z.png)|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/shakespeare_romeo-and-juliet.7z.zci65.png)|
|Length = 922394 bytes              |Length = 922394 bytes
|Min Frequency = 3423 (5 instances) |Min Frequency = 3369 (6 instances)
|Max Frequency = 3769 (5 instances) |Max Frequency = 3755 (8 instances)
|Average Frequency μ = 3603.1       |Average Frequency μ = 3603.1
|Variance σ<sup>2</sup> = 4038.68   |Variance σ<sup>2</sup> = 3638.29
|Deviation Standard σ = 63.55       |Deviation Standard σ = 60.32
|Chi squared 𝛘<sup>2</sup> = 4038.68  |Chi squared 𝛘<sup>2</sup> = 3638.29
|**Coefficient of Variation <sup>σ</sup>/<sub>μ</sub> = 1.76%** |**Coefficient of Variation <sup>σ</sup>/<sub>μ</sub> = 1.67%**

In this case the file was zipped with the maximum compression level and also encrypted with a password (AES-256 algorithm). However, it is noted that the level of entropy is slightly lower than the same file encrypted with zci65; in fact the zci65 image is visibly darker.

### Statistical analysis of encryption output
The following tables show the values of **Standard Deviation**, **Chi Squared** and **Coefficient of Variation** calculated by analyzing 1,000,000 output arrays produced from 1 input array with 1,000,000 random keys by the zci65 and salsa20 algorithms.
As a benchmark, the same indices were calculated on a sample of 1,000,000 random arrays.

#### 1) Input size: 1,000,000 bytes, all set to 0x00 
|**Index**                                  |**zci65**   |**salsa20**   |**benchmark**  |
|:---                                       |---:        |---:          |---:       |
|Min Standard Dev. min(σ)                   |50.664      |49.544        |49.406     |
|Max Standard Dev. max(σ)                   |74.976      |75.912        |78.532     |
|**Average Standard Dev. avg(σ)**           |**62.315**  |**62.318**    |**62.320**  |
|Min Chi Squared min(𝛘<sup>2</sup>)         |168.223     |160.862       |159.968    |
|Max Chi Squared max(𝛘<sup>2</sup>)         |368.408     |377.662       |404.176    |
|**Average Chi squared avg(𝛘<sup>2</sup>)** |**254.987** |**255.013**   |**255.030** |
|Min Coef.of Variation min(<sup>σ</sup>/<sub>μ</sub>)          |1.297%     |1.268%       |   |
|Max Coef.of Variation max(<sup>σ</sup>/<sub>μ</sub>)          |1.919%     |1.943%       |   |
|**Average Coef. of Variation avg(<sup>σ</sup>/<sub>μ</sub>)** |**1.595%** |**1.595%**   |   |


#### 2) Input size: 1,000,000 bytes, all set to 0xFF 
|**Index**                                  |**zci65**   |**salsa20**   |**benchmark**  |
|:---                                       |---:        |---:          |---:       |
|Min Standard Dev. min(σ)                   |48.189      |49.544        |           |
|Max Standard Dev. max(σ)                   |77.811      |75.912        |           |
|**Average Standard Dev. avg(σ)**           |**62.311**  |**62.318**    |           |
|Min Chi Squared min(𝛘<sup>2</sup>)         |152.186     |160.862       |           |
|Max Chi Squared max(𝛘<sup>2</sup>)         |396.791     |377.662       |           |
|**Average Chi squared avg(𝛘<sup>2</sup>)** |**254.95**  |**255.013**   |           |
|Min Coef.of Variation min(<sup>σ</sup>/<sub>μ</sub>)          |1.234%  |1.268%     |      |
|Max Coef.of Variation max(<sup>σ</sup>/<sub>μ</sub>)          |1.992%  |1.943%     |      |
|**Average Coef. of Variation avg(<sup>σ</sup>/<sub>μ</sub>)** |**1.595%** |**1.595%**    |      |

#### 3) Input size: 1,000,000 bytes, content: random bytes
|**Index**                                  |**zci65**   |**salsa20**   |**benchmark**  |
|:---                                       |---:        |---:          |---:       |
|Min Standard Dev. min(σ)                   |49.570      |              |           |
|Max Standard Dev. max(σ)                   |77.763      |              |           |
|**Average Standard Dev. avg(σ)**           |**62.310**  |              |           |
|Min Chi Squared min(𝛘<sup>2</sup>)         |161.035     |              |           |
|Max Chi Squared max(𝛘<sup>2</sup>)         |396.303     |              |           |
|**Average Chi squared avg(𝛘<sup>2</sup>)** |**254.944** |              |           |
|Min Coef.of Variation min(<sup>σ</sup>/<sub>μ</sub>)          |1.269%  |           |      |
|Max Coef.of Variation max(<sup>σ</sup>/<sub>μ</sub>)          |1.991%  |           |      |
|**Average Coef. of Variation avg(<sup>σ</sup>/<sub>μ</sub>)** |**1.595%** |           |      |


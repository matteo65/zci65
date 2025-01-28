# zci65
**Fast, secure, efficent stream cipher**

Zci65 uses a 32-byte key and does not use an Initialization Vector (IV); the algorithm use a internal 256 length array s[] of 32 bit integer and other 2 internal 32 bit fields, k and c; in total the status size is 1088 bytes.
The internal status is continuously changed by the source content to be encrypted.
The code is very simple, the main functions are:
```java	
public byte encipher(byte b) {
	int p = ((k >>> 24) ^ (k >>> 16) ^ (k >>> 8) ^ k) & 0xFF;
	int r = s[p];
	s[p] = k ^ (r << 2) ^ (r >>> 2);
	k = s[(b + c++) & 0xFF] ^ (k << 2) ^ (k >>> 2);
	return (byte)((r >>> 24) ^ (r >>> 16) ^ (r >>> 8) ^ r ^ b);
}

public byte decipher(byte b) {
	int p = ((k >>> 24) ^ (k >>> 16) ^ (k >>> 8) ^ k) & 0xFF;
	int t = s[p];
	s[p] = k ^ (t << 2) ^ (t >>> 2);
	int r = ((t >>> 24) ^ (t >>> 16) ^ (t >>> 8) ^ t ^ b) & 0xFF;
	k = s[(r + c++) & 0xFF] ^ (k << 2) ^ (k >>> 2);
	return (byte)r;
}
```
The main properties of a good encryption algorithm are:

1. Produce an output indistinguishable from a random sequence of bytes 
2. Do it in the shortest possible time (and this is especially true for a stream cipher) 
3. Make it impossible to predict the output sequence starting from the data already produced.

Point 1 can be verified with statistical analysis; point 2 by measuring the times and comparing them with benchmark values, while point 3 can only be verified by an independent cryptographic analysis and therefore I invite the researchers to consider zci65 and confirm or deny this feature!

### Characteristics of the encryption output
This tool shows the byte distribution of a file on a square window displaying a 16x16 matrix (one element for each byte). 
The least frequent byte is displayed in white, the most frequent byte in black, the others are proportionally distributed in shades of gray (total 256 shades).
The result is that a tendentially dark image corresponds to a higher chaotic level of the analyzed data.
A good indicator of the degree of chaos of a sequence of bytes (cryptographic output) is the estimate of pi: each successive sequence of six bytes is used as 24 bit X and Y co-ordinates within a square. If the distance of the randomly-generated point is less than the radius of a circle inscribed within the square, the six-byte sequence is considered a “hit”. The percentage of hits is used to calculate the value of Pi.

|lorem_ipsum.txt|lorem_ipsum.txt.zci65|
|:--- |:--- |
|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/lorem_ipsum_txt.png)|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/lorem_ipsum_zci65.png)|
|Length = 2982 bytes                |Length = 2982 bytes              |
|Min Frequency = 0 (214 instances)  |Min Frequency = 3 (11 instances) |
|Max Frequency = 441 (13 instances) |Max Frequency = 25 (10 instances)|
|Average Frequency μ = 11.648       |Average Frequency μ = 11.648        |
|Variance σ<sup>2</sup> = 2318.259  |Variance σ<sup>2</sup> = 12.205                 |
|Standard Deviation σ = 48.148      |Standard Deviation σ = 3.493        |
|Chi squared 𝛘<sup>2</sup> = 50948.838 |Chi squared 𝛘<sup>2</sup>= 268.211              |
|Coefficient of Variation <sup>σ</sup>/<sub>μ</sub> = 413.346%|Coefficient of Variation <sup>σ</sup>/<sub>μ</sub> = 29.991%|
|Mean value of bytes = 93.703       |Mean value of bytes = 126.811|
|Entropy = 4.226                    |Entropy = 7.934  |
|Monte Carlo π = 4.0 (error 27.324%)  |Monte Carlo π = 3.0744 (error 2.137%)|

|shakespeare_romeo-and-juliet.pdf|shakespeare_romeo-and-juliet.pdf.zci65|
|:--- |:--- |
|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/shakespeare_romeo-and-juliet.pdf.png)|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/shakespeare_romeo-and-juliet.pdf.zci65.png)|
|Length = 1064366 bytes              |Length = 1064366 bytes             |
|Min Frequency = 2634 (7 instances)  |Min Frequency = 3933 (10 instances) |
|Max Frequency = 31436 (4 instances) |Max Frequency = 4369 (6 instances)  |
|Average Frequency μ = 4157.68       |Average Frequency μ = 4157.68       |
|Variance σ<sup>2</sup> = 5780361.233|Variance σ<sup>2</sup> = 5183.101   |
|Standard Deviation σ = 2404.238     |Standard Deviation σ = 65.42        |
|Chi squared 𝛘<sup>2</sup> = 355913.054 |Chi squared 𝛘<sup>2</sup> = 319.138 |
|Coefficient of Variation <sup>σ</sup>/<sub>μ</sub> = 57.826%|Coefficient of Variation <sup>σ</sup>/<sub>μ</sub> = 1.732%|
|Mean value of bytes = 115.424       |Mean value of bytes = 127.45|
|Entropy = 7.866                     |Entropy = 8.000 |
|Monte Carlo π = 3.31 (error 5.36%)    |Monte Carlo π = 3.1416 (error 0.001%)|

|shakespeare_romeo-and-juliet.7z|shakespeare_romeo-and-juliet.7z.zci65|
|:--- |:--- |
|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/shakespeare_romeo-and-juliet.7z.png)|![Alt Text](https://raw.githubusercontent.com/matteo65/zci65/main/Resource/shakespeare_romeo-and-juliet.7z.zci65.png)|
|Length = 922394 bytes              |Length = 922394 bytes                 |
|Min Frequency = 3423 (5 instances) |Min Frequency = 3469 (5 instances)    |
|Max Frequency = 3769 (5 instances) |Max Frequency = 3750 (8 instances)    |
|Average Frequency μ = 3603.102     |Average Frequency μ = 3603.102        |
|Variance σ<sup>2</sup> = 4038.685  |Variance σ<sup>2</sup> = 3427.122     |
|Standard Deviation σ = 63.551      |Standard Deviation σ = 58.542         |
|Chi squared 𝛘<sup>2</sup> = 286.948  |Chi squared 𝛘<sup>2</sup> = 243.497 |
|Coefficient of Variation <sup>σ</sup>/<sub>μ</sub> = 1.764% |Coefficient of Variation <sup>σ</sup>/<sub>μ</sub> = 1.625% |
|Mean value of bytes = 127.515       |Mean value of bytes = 127.386|
|Entropy = 8.000                     |Entropy = 8.000 |
|Monte Carlo π = 3.143 (error 0.046%)  |Monte Carlo π = 3.1443 (error 0.087%)|

In this case the file was zipped with the maximum compression level and also encrypted with a password (AES-256 algorithm). The statistical indicators and the degree of darkness of the images are very close.

### Randomness analysis of the encryption output
In this chapter, the output produced by cryptography is analyzed. In particular, two main groups of indicators are presented: frequency-based indexes that ignore byte positions and indexes that depend on the distribution of bytes.
### A) Byte Frequency Indexes
This group focuses on analyzing the frequency of byte occurrences within the data, without considering the order in which the bytes appear. These tests are designed to assess various statistical properties of the byte frequency. The tests in this group are:

1.	**Variance σ<sup>2</sup>**: This test calculates the variance of the byte frequencies, measuring the spread or dispersion of frequencies from their mean.
2.	**Standard Deviation σ**: The standard deviation is a common statistical measure that shows how much the byte frequencies deviate from the average frequency. It is the square root of the variance.
3.	**Coefficient of Variation <sup>σ</sup>/<sub>μ</sub>**: This is the ratio of the standard deviation to the mean frequency, providing a normalized measure of the dispersion relative to the average frequency.
4.	**Chi-Square 𝛘<sup>2</sup>**: This statistical test compares the observed byte frequencies to an expected uniform distribution, assessing whether the data deviates from randomness.
5.	**Mean Byte Value (Sum)**: This test computes the sum of all byte values in the dataset, providing a raw measure of the average byte value. If the data are close to random, this should be about 127.5.

### B) Distribution-Dependent Indexes
The second group of indexes examines the actual distribution of bytes within the data, meaning that the relative positions and sequences of bytes are considered. These indexes are more focused on patterns that arise from how the bytes are ordered. The tests in this group are:

6.	**Pi Estimation Using Monte Carlo in 2D**: One of the more intriguing tests in VisualRT is the estimation of π using the contents of the file. The file is interpreted as a series of 6-byte sequences, with the first 3 bytes used as the x-coordinate and the next 3 bytes as the y-coordinate on a 2D plane. The number of points that fall inside a unit circle (with radius 0xFFFFFF) is used to estimate π, drawing from **Monte Carlo** methods. The closer the estimate is to the true value of π, the more uniform the distribution of bytes in the file might be.
7.	**Pi Estimation Using Monte Carlo in 3D**: Similar to the 2D version, this test uses triplets of adjacent bytes to estimate π in three dimensions (sphere), adding an extra layer of complexity and accuracy to the π approximation.
8.	**Mean of All Adjacent Byte Pairs**: This test computes the mean value of every pair of adjacent bytes, providing insight into how consecutive byte values are related. For a file with a truly random byte distribution, the expected average should approach 32767.5, which is the midpoint of the possible 16-bit range. Deviations from this expected average can indicate non-random patterns or structured data, helping to assess the file's randomness level.
9.	**Number of Collisions of 4-Byte Sequences**: This test analyzes the file by dividing it into 4-byte sequences and counting how many times each sequence appears (collisions). Each 4-byte sequence is treated as a unique 32-bit value, with possible values ranging from 0 to 2<sup>32</sup>-1. For a file with a completely random byte distribution, the expected number of collisions can be estimated using the **birthday problem** analogy. ​This test helps evaluate the randomness of the file. A number of collisions significantly different from the expected number could indicate non-random patterns or redundancy in the data, while a number of collisions close to the expected value suggests a higher degree of randomness.

The following tables show the values ​​of the above mentioned indices calculated by analyzing 1,000 output arrays produced from 1 input array with 1,000,000 random keys by the zci65 and salsa20 algorithms.
As a benchmark, the same indices were calculated on a sample of 1,000,000 random arrays.
For more information about test see: https://github.com/matteo65/VisualRT

#### 1) Input size: 1,000,000 bytes, all set to 0x00 
|**Index**                                  |**zci65**   |**salsa20**   |**benchmark**  |
|:---                                       |---:        |---:          |---:       |
|Min Standard Dev. min(σ)                   |50.664      |49.544        |49.406     |
|Max Standard Dev. max(σ)                   |74.976      |75.912        |78.532     |
|**Average Standard Dev. avg(σ)**           |**62.315**  |**62.318**    |**62.320**  |
|Min Chi Squared min(𝛘<sup>2</sup>)         |168.223     |160.862       |159.968    |
|Max Chi Squared max(𝛘<sup>2</sup>)         |368.408     |377.662       |404.176    |
|**Average Chi squared avg(𝛘<sup>2</sup>)** |**254.987** |**255.013**   |**255.030** |
|Min Coef.of Variation min(<sup>σ</sup>/<sub>μ</sub>)          |1.297%     |1.268%       |1.265%   |
|Max Coef.of Variation max(<sup>σ</sup>/<sub>μ</sub>)          |1.919%     |1.943%       |2.010%   |
|**Average Coef. of Variation avg(<sup>σ</sup>/<sub>μ</sub>)** |**1.595%** |**1.595%**   |**1.595%**   |


#### 2) Input size: 1,000,000 bytes, all set to 0xFF 
|**Index**                                  |**zci65**   |**salsa20**   |**benchmark**  |
|:---                                       |---:        |---:          |---:       |
|Min Standard Dev. min(σ)                   |48.189      |49.544        |49.406    |
|Max Standard Dev. max(σ)                   |77.811      |75.912        |78.532     |
|**Average Standard Dev. avg(σ)**           |**62.311**  |**62.318**    |**62.320** |
|Min Chi Squared min(𝛘<sup>2</sup>)         |152.186     |160.862       |159.968    |
|Max Chi Squared max(𝛘<sup>2</sup>)         |396.791     |377.662       |404.176    |
|**Average Chi squared avg(𝛘<sup>2</sup>)** |**254.95**  |**255.013**   |**255.030**|
|Min Coef.of Variation min(<sup>σ</sup>/<sub>μ</sub>)          |1.234%  |1.268%     |1.265% |
|Max Coef.of Variation max(<sup>σ</sup>/<sub>μ</sub>)          |1.992%  |1.943%     |2.010% |
|**Average Coef. of Variation avg(<sup>σ</sup>/<sub>μ</sub>)** |**1.595%** |**1.595%** |**1.595%** |

#### 3) Input size: 1,000,000 bytes, content: random bytes
|**Index**                                  |**zci65**   |**salsa20**   |**benchmark**  |
|:---                                       |---:        |---:          |---:       |
|Min Standard Dev. min(σ)                   |49.570      |50.401        |49.406    |
|Max Standard Dev. max(σ)                   |77.763      |75.819        |78.532      |
|**Average Standard Dev. avg(σ)**           |**62.310**  |**62.316**    |**62.320**|
|Min Chi Squared min(𝛘<sup>2</sup>)         |161.035     |166.481       |159.968   |
|Max Chi Squared max(𝛘<sup>2</sup>)         |396.303     |376.735       |404.176   |
|**Average Chi squared avg(𝛘<sup>2</sup>)** |**254.944** |**254.991**   |**255.030** |
|Min Coef.of Variation min(<sup>σ</sup>/<sub>μ</sub>)          |1.269%  |1.290%     |1.265% |
|Max Coef.of Variation max(<sup>σ</sup>/<sub>μ</sub>)          |1.991%  |1.941%     |2.010% |
|**Average Coef. of Variation avg(<sup>σ</sup>/<sub>μ</sub>)** |**1.595%** |**1.595%** |**1.595%** |

### Conclusions
zci65 can be considered a valid alternative in implementations that require a fast and secure streaming encryption algorithm.

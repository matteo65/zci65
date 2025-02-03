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
4. Produce a random output with any key used for encryption

Points 1 and 4 can be verified with statistical analysis; point 2 by measuring the times and comparing them with benchmark values, while point 3 can only be verified by an independent cryptographic analysis and therefore I invite the researchers to consider zci65 and confirm or deny this feature!

### Statistical tests
In order to establish the level of randomness of the output produced by the encryption operation, I considered using these statistical indicators:

#### Byte Frequency Tests
This group focuses on analyzing the frequency of byte occurrences within the data, without considering the order in which the bytes appear. These tests are designed to assess various statistical properties of the byte frequency. The tests in this group are:

1.	**Variance σ2**: This test calculates the variance of the byte frequencies, measuring the spread or dispersion of frequencies from their mean.
2.	**Standard Deviation σ**: The standard deviation is a common statistical measure that shows how much the byte frequencies deviate from the average frequency. It is the square root of the variance.
3.	**Coefficient of Variation σ/μ**: This is the ratio of the standard deviation to the mean frequency, providing a normalized measure of the dispersion relative to the average frequency.
4.	**Chi-Square 𝛘2**: This statistical test compares the observed byte frequencies to an expected uniform distribution, assessing whether the data deviates from randomness.
5.	**Mean Byte Value (Sum)**: This test computes the sum of all byte values in the dataset, providing a raw measure of the average byte value. If the data are close to random, this should be about 127.5.
6.	**Entropy**: Entropy measures the randomness or uncertainty in the byte distribution. Higher entropy indicates a more random or uniform distribution of byte values.

#### Distribution-Dependent Tests
The second group of tests examines the actual distribution of bytes within the data, meaning that the relative positions and sequences of bytes are considered. These tests are more focused on patterns that arise from how the bytes are ordered. The tests in this group are:

7.	**Pi Estimation Using Monte Carlo in 2D**: One of the more intriguing tests in VisualRT is the estimation of π using the contents of the file. The file is interpreted as a series of 6-byte sequences, with the first 3 bytes used as the x-coordinate and the next 3 bytes as the y-coordinate on a 2D plane. The number of points that fall inside a unit circle (with radius 0xFFFFFF) is used to estimate π, drawing from **Monte Carlo** methods. The closer the estimate is to the true value of π, the more uniform the distribution of bytes in the file might be.
8.	**Pi Estimation Using Monte Carlo in 3D**: Similar to the 2D version, this test uses triplets of adjacent bytes to estimate π in three dimensions (sphere), adding an extra layer of complexity and accuracy to the π approximation.
9.	**Mean of All Adjacent Byte Pairs**: This test computes the mean value of every pair of adjacent bytes, providing insight into how consecutive byte values are related. For a file with a truly random byte distribution, the expected average should approach 32767.5, which is the midpoint of the possible 16-bit range. Deviations from this expected average can indicate non-random patterns or structured data, helping to assess the file's randomness level.
10.	**Number of Collisions of 4-Byte Sequences**: This test analyzes the file by dividing it into 4-byte sequences and counting how many times each sequence appears (collisions). Each 4-byte sequence is treated as a unique 32-bit value, with possible values ranging from 0 to 2<sup>32</sup>-1. For a file with a completely random byte distribution, the expected number of collisions can be estimated using the **birthday problem** analogy. ​This test helps evaluate the randomness of the file. A number of collisions significantly different from the expected number coul

### Byte distribution of the encryption output
This tool shows the byte distribution of a file on a square window displaying a 16x16 matrix (one element for each byte). 
The least frequent byte is displayed in white, the most frequent byte in black, the others are proportionally distributed in shades of gray (total 256 shades).
The result is that a tendentially dark image corresponds to a higher chaotic level of the analyzed data.

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

### Statistical analysis of output when encryption keys change
In this chapter we analyze the outputs produced by encrypting the same input by changing only the encryption keys.
The input is 1,000,000 bytes long and 10,000 encryptions were performed using different random keys each time.
The benchmark was performed by analyzing a random sequence of bytes produced by a secure random generator.

#### 1) Input size: 1,000,000 bytes, all set to 0x00 
|**Index**                      |**zci65**  |**Salsa20** |**Benchmark**|
|:---                           |---:       |---:        |---:         |
|Min Standard Deviation min(σ)  |   51.5054 |   51.7363  |   52.7582   |
|Max Standard Deviation max(σ)  |   72.7786 |   72.5432  |   73.7579   |
|**Avg Standard Deviationmi avg(σ)**|   **62.3012** |   **62.3333**  |   **62.2891**   |
|Min Chi Squared min(𝛘<sup>2</sup>)      |  173.8542 |  175.4168  |  182.4148   |
|Max Chi Squared max(𝛘<sup>2</sup>)      |  347.1258 |  344.8842  |  356.5312   |
|**Avg Chi Squared avg(𝛘<sup>2</sup>)**      |  **254.8719** |  **255.1253**  |  **254.7649**   |
|Min Coef.Variation min(<sup>σ</sup>/<sub>μ</sub>)   |    1.3185 |    1.3245  |    1.3506   |
|Max Coef.Variation max(<sup>σ</sup>/<sub>μ</sub>)   |    1.8631 |    1.8571  |    1.8882   |
|**Avg Coef.Variation avg(<sup>σ</sup>/<sub>μ</sub>)**   |    **1.5949** |    **1.5957**  |    **1.5946**   |
|Min Entropy           |    7.9997 |    7.9998  |    7.9997   |
|Max Entropy           |    7.9999 |    7.9999  |    7.9999   |
|**Avg Entropy**           |    **7.9998** |    **7.9998**  |    **7.9998**   |
|Min Mean Bytes Value  |  127.2232 |  127.2365  |  127.2328   |
|Max Mean Bytes Value  |  127.7899 |  127.7811  |  127.7525   |
|**Avg Mean Bytes Value**  |  **127.5016** |  **127.5**     |  **127.5005**   |
|Min π 2D             |    3.1253 |    3.1236  |    3.1215   |
|Max π 2D             |    3.1579 |    3.157   |    3.1604   |
|**Avg π 2D**             |    **3.1416** |    **3.1416**  |    **3.1416**   |
|Min π 3D             |    3.1093 |    3.1028  |    3.1021   |
|Max π 3D             |    3.1735 |    3.1724  |    3.1759   |
|**Avg π 3D**             |    **3.1416** |    **3.1415**  |    **3.1416**   |
|Min collisions 32     |    0      |    0       |    0        |
|Max collisions 32     |   19      |   20       |   20        |
|**Avg collisions 32**     |    **7.2442** |    **7.2333**  |    **7.2348**   |
|Min Mean 2Bytes Value |32696.3555 |32699.7777  |32698.8275   |
|Max Mean 2Bytes Value |32841.9673 |32839.778   |32832.3744   |
|**Avg Mean 2Bytes Value** |**32767.9196** |**32767.4991**  |**32767.6308**   |



#### 2) Input size: 1,000,000 bytes, all set to 0xFF 
|**Index**                      |**zci65**  |**Salsa20** |**Benchmark**|
|:---                           |---:       |---:        |---:         |
|Min Standard Deviation min(σ)  |  51.582   | 51.7363    |  52.7582    |
|Max Standard Deviation max(σ)  | 73.843    | 72.5432    |  73.7579    |
|**Avg Standard Deviation avg(σ)**  | **62.3194**   | **62.3333**    |  **62.2891**    |
|Min Chi Squared min(𝛘<sup>2</sup>)| 174.3718|  175.4168  | 182.4148 |
|Max Chi Squared max(𝛘<sup>2</sup>)| 357.354 |  344.8842  | 356.5312 |
|**Avg Chi Squared avg(𝛘<sup>2</sup>)**| **255.0224**|  **255.1253**  | **254.7649** |
|Min Coef.Variation min(<sup>σ</sup>/<sub>μ</sub>) | 1.3205 | 1.3245  |    1.3506 |
|Max Coef.Variation max(<sup>σ</sup>/<sub>μ</sub>) | 1.8904 | 1.8571  |    1.8882 |
|**Avg Coef.Variation avg(<sup>σ</sup>/<sub>μ</sub>)** | **1.5954** | **1.5957**  |    **1.5946** |
|Min Entropy                |7.9997  |       7.9998   |          7.9997|
|Max Entropy                |7.9999  |       7.9999   |          7.9999|
|**Avg Entropy**                |**7.9998**  |       **7.9998**   |          **7.9998**|
|Min Mean Bytes Value     |127.1881|       127.2189|           127.2328|
|Max Mean Bytes Value     |127.8011|       127.7635 |          127.7525|
|**Avg Mean Bytes Value**     |**127.5002**|       **127.5**  |            **127.5005**|
|Min π 2D                 | 3.1274 |        3.1266 |            3.1215|
|Max π 2D                 | 3.1615 |        3.1554 |            3.1604|
|**Avg π 2D**                 | **3.1416** |        **3.1416** |            **3.1416**|
|Min π 3D                 | 3.1086 |        3.1107 |            3.1021|
|Max π 3D                 | 3.1809 |        3.1769 |            3.1759|
|**Avg π 3D**                 | **3.1416** |        **3.1417** |            **3.1416**|
|Min collisions 32     |     0      |        0           |       0|
|Max collisions 32     |    21      |       20           |      20|
|**Avg collisions 32**     |     **7.285**  |        **7.2333**      |       **7.2348**|
|Min Mean 2Bytes Value  |32687.379  |       32695.222    |   32698.8 275|
|Max Mean 2Bytes Value  |32844.9091 |       32835.2223   |   32832.3744|
|**Avg Mean 2Bytes Value**  |**32767.5494** |       **32767.5009**   |   **32767.6308**|


#### 3) Input size: 1,000,000 bytes, content: random bytes
|**Index**                      |**zci65**  |**Salsa20** |**Benchmark**|
|:---                           |---:       |---:        |---:         |
|Min Standard Deviation min(σ)|   52.4911  |    50.9746  |    52.7582 |
|Max Standard Deviation max(σ)|   72.4453  |    72.92    |    73.7579 |
|**Avg Standard Deviation avg(σ)**|   **62.3085**  |    **62.3241**  |    **62.2891** |
|Min Chi Squared min(𝛘<sup>2</sup>)       |  180.5727  |   170.2892  |   182.4148 |
|Max Chi Squared max(𝛘<sup>2</sup>)       |  343.9544  |   348.4759  |   356.5312 |
|**Avg Chi Squared avg(𝛘<sup>2</sup>)**       |  **254.9292**  |   **255.0607**  |   **254.7649** |
|Min Coef.Variation min(<sup>σ</sup>/<sub>μ</sub>)    |    1.3438  |     1.3049  |     1.3506 |
|Max Coef.Variation max(<sup>σ</sup>/<sub>μ</sub>)    |    1.8546  |     1.8668  |     1.8882 |
|**Avg Coef.Variation avg(<sup>σ</sup>/<sub>μ</sub>)**    |    **1.5951**  |     **1.5955**  |     **1.5946** |
|Min Entropy            |    7.9998  |     7.9997  |     7.9997 |
|Max Entropy            |    7.9999  |     7.9999  |     7.9999 |
|**Avg Entropy**            |    **7.9998**  |     **7.9998**  |     **7.9998** |
|Min Mean Bytes Value   |  127.2367  |   127.2113  |   127.2328 |
|Max Mean Bytes Value   |  127.8227  |   127.7724  |   127.7525 |
|**Avg Mean Bytes Value**   |  **127.5004**  |   **127.4988**  |   **127.5005** |
|Min π 2D              |    3.1276  |     3.1238  |     3.1215 |
|Max π 2D              |    3.1559  |     3.1557  |     3.1604 |
|**Avg π 2D**              |    **3.1416**  |     **3.1417**  |     **3.1416** |
|Min π 3D              |    3.1047  |     3.1057  |     3.1021 |
|Max π 3D              |    3.1736  |     3.1763  |     3.1759 |
|**Avg π 3D**              |    **3.1416**  |     **3.1417**  |     **3.1416** |
|Min collisions         |    0       |     0       |     0      |
|Max collisions         |    20      |    18       |    20      |
|**Avg collisions**         |    **7.334**   |     **7.2493**  |     **7.2348** |
|Min Mean 2Bytes Value  |32699.8499  | 32693.2708  | 32698.8275 |
|Max Mean 2Bytes Value  |32850.421   | 32837.4988  | 32832.3744 |
|**Avg Mean 2Bytes Value**  |**32767.6128**  | **32767.1987**  | **32767.6308** |

### Conclusions
zci65 can be considered a valid alternative in implementations that require a fast and secure streaming encryption algorithm.

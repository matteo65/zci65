package zci65;

/**
 * Zci65 
 * Fast, secure, efficent stream cipher
 * 
 * Author: Matteo Zapparoli <zapparoli.matteo@gmail.com>
 * Date: 2023
 * Licence: Public Domain
 * 
 * Non-Thradesafe version
 * 
 * This is free and unencumbered software released into the public domain.
 *
 * Anyone is free to copy, modify, publish, use, compile, sell, or
 * distribute this software, either in source code form or as a compiled
 * binary, for any purpose, commercial or non-commercial, and by any
 * means.
 * 
 * In jurisdictions that recognize copyright laws, the author or authors
 * of this software dedicate any and all copyright interest in the
 * software to the public domain. We make this dedication for the benefit
 * of the public at large and to the detriment of our heirs and
 * successors. We intend this dedication to be an overt act of
 * relinquishment in perpetuity of all present and future rights to this
 * software under copyright law.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS BE LIABLE FOR ANY CLAIM, DAMAGES OR
 * OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 * 
 * For more information, please refer to <https://unlicense.org>
 * 
 */
public class Zci65 {
	
	/**
	 * s[] : 256 length array of 32 bit integer random values
	 * It changes during encipher/decipher operations
	 */
	protected final int[] s = new int[256];
	
	/**
	 * k: is the index of the next element of s[] for each input byte
	 * enciphered (or deciphered)
	 * Set to a random value by init()
	 */
	protected int k;
	
	/**
	 * c: is a 32 bit integer counter, set to a random value by init() and it is incremented
	 * by 1 each byte enciphered (or deciphered)
	 */
	protected int c;

	/**
	 * Store a copy of the key: used by reset() to restore the initial conditions
	 */
	private final byte[] key = new byte[32];
	
	/**
	 * Constructor of the encipher/decipher
	 * You cannot mix calls to encryption methods with decryption 
	 * methods: you can do this by calling the reset() method which 
	 * resets the state to the initial conditions
	 * @param key non-null 32 byte length secret key
	 */
	public Zci65(byte[] key) {
		if(key.length != 32)
			throw new IllegalArgumentException("key.length != 32");
		
		for(int i = 0; i < 32; i++)
			this.key[i] = key[i];
		
		init();
	}
	
	/**
	 * Uses key[] to generate the initial random values of s[], k and c 
	 */
	protected void init() {
		int h = 0x95DE1432;
		for(int i = 0; i < 32; i++)
			h = (0xA8657C5B * (i + key[i])) ^ (h << 2) ^ (h >>> 2);
			
		for(int y = 0; y <= 224; y += 32) {
			for(int i = 0; i < 32; i++) {
				h = (0xA8657C5B * (y + i + key[i])) ^ (h << 2) ^ (h >>> 2);
				s[y + i] = h;
			}
		}
		k = 0x2F63BE6A;
		c = 0x8462E35C;
		for(int i = 0; i < 32; i++) {
			k = (0x2F63BE6A * (i + key[i])) ^ (k << 2) ^ (k >>> 2);
			c = (0x8462E35C * (i + key[i])) ^ (c << 2) ^ (c >>> 2);
		}
	}
	
	/**
	 * Restores the initial conditions
	 */
	public void reset() {
		init();
	}
	
	/**
	 * Encipher a byte
	 * @param b byte to be enciphered
	 * @return the enciphered byte
	 */
	public byte encipher(byte b) {
		int p = ((k >>> 24) ^ (k >>> 16) ^ (k >>> 8) ^ k) & 0xFF;
		int r = s[p];
		s[p] = k ^ (r << 2) ^ (r >>> 2);
		k = s[(b + c++) & 0xFF] ^ (k << 2) ^ (k >>> 2);
		return (byte)((r >>> 24) ^ (r >>> 16) ^ (r >>> 8) ^ r ^ b);
	}
	
	/**
	 * Encipher the byte less significant of a int value
	 * @param b int to encipher
	 * @return the input value with the byte less significant enciphered
	 */
	public int encipher(int b) {
		int p = ((k >>> 24) ^ (k >>> 16) ^ (k >>> 8) ^ k) & 0xFF;
		int r = s[p];
		s[p] = k ^ (r << 2) ^ (r >>> 2);
		k = s[(b + c++) & 0xFF] ^ (k << 2) ^ (k >>> 2);
		return ((r >>> 24) ^ (r >>> 16) ^ (r >>> 8) ^ r ^ b);
	}
	
	/**
	 * Encipher an array of byte and replace every byte with the enciphered value
	 * @param data: the array to encipher
	 * @param offset: the start index
	 * @param len: the number of byte to be enciphered
	 */
	public void encipher(byte data[], int offset, int len) {
		len += offset;
		for(int i = offset; i < len; i++) {
			int p = ((k >>> 24) ^ (k >>> 16) ^ (k >>> 8) ^ k) & 0xFF;
			int r = s[p];
			s[p] = k ^ (r << 2) ^ (r >>> 2);
			k = s[(data[i] + c++) & 0xFF] ^ (k << 2) ^ (k >>> 2);
			data[i] = (byte)((r >>> 24) ^ (r >>> 16) ^ (r >>> 8) ^ r ^ data[i]);
		}
	}
	
	/**
	 * Encipher an array of byte and write the enciphered output into an other array 
	 * @param data: the array to encipher
	 * @param offset: the start index
	 * @param len: the number of byte to be enciphered
	 * @param output: array length at least len bytes
	 */
	public void encipher(byte data[], int offset, int len, byte[] output) {
		for(int i = 0; i < len; i++) {
			int p = ((k >>> 24) ^ (k >>> 16) ^ (k >>> 8) ^ k) & 0xFF;
			int r = s[p];
			s[p] = k ^ (r << 2) ^ (r >>> 2);
			k = s[(data[i + offset] + c++) & 0xFF] ^ (k << 2) ^ (k >>> 2);
			output[i] = (byte)((r >>> 24) ^ (r >>> 16) ^ (r >>> 8) ^ r ^ data[i + offset]);
		}
	}
	
	/**
	 * Decipher a byte
	 * @param b byte to be decipher
	 * @return the deciphered byte
	 */
	public byte decipher(byte b) {
		int p = ((k >>> 24) ^ (k >>> 16) ^ (k >>> 8) ^ k) & 0xFF;
		int t = s[p];
		s[p] = k ^ (t << 2) ^ (t >>> 2);
		int r = ((t >>> 24) ^ (t >>> 16) ^ (t >>> 8) ^ t ^ b) & 0xFF;
		k = s[(r + c++) & 0xFF] ^ (k << 2) ^ (k >>> 2);
		return (byte)r;
	}
	
	/**
	 * Decipher the byte less significant of a int value
	 * @param b int to decipher
	 * @return the input value with the byte less significant deciphered
	 */
	public int decipher(int b) {
		int p = ((k >>> 24) ^ (k >>> 16) ^ (k >>> 8) ^ k) & 0xFF;
		int t = s[p];
		s[p] = k ^ (t << 2) ^ (t >>> 2);
		int r = ((t >>> 24) ^ (t >>> 16) ^ (t >>> 8) ^ t ^ b);
		k = s[(r + c++) & 0xFF] ^ (k << 2) ^ (k >>> 2);
		return r;
	}
	
	/**
	 * Decipher an array of byte and replace every byte with the enciphered value
	 * @param data: the array to decipher
	 * @param offset: the start index
	 * @param len: the number of byte to be enciphered
	 */
	public void decipher(byte data[], int offset, int len) {
		len += offset;
		for(int i = offset; i < len; i++) {
			int p = ((k >>> 24) ^ (k >>> 16) ^ (k >>> 8) ^ k) & 0xFF;
			int t = s[p];
			s[p] = k ^ (t << 2) ^ (t >>> 2);
			data[i] = (byte)((t >>> 24) ^ (t >>> 16) ^ (t >>> 8) ^ t ^ data[i]);
			k = s[(data[i] + c++) & 0xFF] ^ (k << 2) ^ (k >>> 2);
		}
	}
	
	/**
	 * Decipher an array of byte and write the deciphered output into an other array 
	 * @param data: the array to decipher
	 * @param offset: the start index
	 * @param len: the number of byte to be deciphered
	 * @param output: array length at least len bytes
	 */
	public void decipher(byte data[], int offset, int len, byte[] output) {
		for(int i = 0; i < len; i++) {
			int p = ((k >>> 24) ^ (k >>> 16) ^ (k >>> 8) ^ k) & 0xFF;
			int t = s[p];
			s[p] = k ^ (t << 2) ^ (t >>> 2);
			output[i] = (byte)((t >>> 24) ^ (t >>> 16) ^ (t >>> 8) ^ t ^ data[i + offset]);
			k = s[(output[i] + c++) & 0xFF] ^ (k << 2) ^ (k >>> 2);
		}
	}

}
	


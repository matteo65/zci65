package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.JFrame;

/**
 * Zci65 
 * Visual Entropy
 * 
 * Author: Matteo Zapparoli <zapparoli.matteo@gmail.com>
 * Date: 2023
 * Licence: Public Domain
 * 
 * This program shows the byte distribution of a file on a square window 
 * displaying a 16x16 matrix (one element for each byte). The least frequent 
 * byte is displayed in white, the most frequent byte in black, the others 
 * are proportionally distributed in shades of gray (total 256 shades)
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
public class VisualEntropy extends JFrame {
	
	private static final long IN_CIRCLE = 0xFFFFFFL * 0xFFFFFFL; // In-circle distance for Monte Carlo Pi
	private static final long IN_SPHERE = 0xFFFFFFL * 0xFFFFFFL; // In-sphere distance for Monte Carlo Pi
	
	public static final int SIDE = 256;
	
	protected final EntropyPanel panel;
	
	public VisualEntropy(String fileName, int[] freq, 
			             long pointsInsideCircle, long numSamples2D, 
			             long pointsInsideSphere, long numSamples3D, 
			             long collision, long sumPairBytes) {
		setSize(SIDE + 30, SIDE + 60);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(fileName);
		System.out.println(fileName);
		panel = new EntropyPanel(freq, pointsInsideCircle, numSamples2D, 
	                             pointsInsideSphere, numSamples3D, 
	                             collision, sumPairBytes);
		add(panel);
		setVisible(true);
	}

	public static final void main(String[] args) throws FileNotFoundException, IOException {
		if(args.length == 0) return;
		
		int[] freq = new int[256];
		
		byte[] buffer = new byte[65536];
		
		for(int i = 0; i < args.length; i++) {
			
			for(int k = 0; k < 256; k++)
				freq[k] = 0;
			
			File file = new File(args[i]);
			long pointsInsideCircle = 0;
			
			// Variables for Monte Carlo Pi 2D
			long numSamples2D = 0L;
			long x2D = 0L, y2D = 0L;
			int counter2D = 0;
			
			// Variables for Monte Carlo Pi 3D
			long numSamples3D = 0L;
			long x3D = 0L, y3D = 0L, z3D = 0L;
			long pointsInsideSphere = 0L;
			int counter3D = 0;
			
			long sumPairBytes = 0L;
			
			int precByte = -1;
			int h = 0;
			int value32 = 0;
			
			/**
			 * 2^32 bits, 1 byte = 8 hash values
			 */
			byte[] bitTable = new byte[536870912];
			long collisionsCounter = 0L;
			
			try(FileInputStream fis = new FileInputStream(file)) {
				int n;
				while((n = fis.read(buffer)) != -1) {
					for(int k = 0; k < n; k++) {
						int b = buffer[k] & 0xFF;
						freq[b]++;
						
						// Monte Carlo 2D
						if(++counter2D <= 3) {
							x2D = (x2D << 8) | b;
						}
						else if(counter2D <= 6) {
							y2D = (y2D << 8) | b;
							
							if(counter2D == 6) {
								if(x2D * x2D + y2D * y2D <= IN_CIRCLE) {
									pointsInsideCircle++;
								}
								numSamples2D++;
								counter2D = 0;
								x2D = y2D = 0L;
							}
						}
						
						// Monte Carlo 3D
						if(++counter3D <= 3) {
							x3D = (x3D << 8) | b;
						}
						else if(counter3D <= 6) {
							y3D = (y3D << 8) | b;
						}
						else if(counter3D <= 9) {
							z3D = (z3D << 8) | b;
							
							if(counter3D == 9) {
								if(x3D * x3D + y3D * y3D + z3D * z3D <= IN_SPHERE) {
									pointsInsideSphere++;
								}
								numSamples3D++;
								counter3D = 0;
								x3D = y3D = z3D = 0L;
							}
						}
						
						// Media 2 byte
						if(precByte != -1) {
							sumPairBytes += (b << 8) + precByte;
						}
						precByte = b;
						
						// Collisions 32 bit
						value32 = (value32 << 8) + b;
						if(h++ == 3) {
							int index = (value32 >>> 3); // byte index of bitTable (value32 / 8)
							int bit = (value32 & 0x07); // bit into byte (value32 mod 8)
							int mask = 0x01 << bit; // bit mask
							
							if((bitTable[index] & mask) != 0) {
								collisionsCounter++;
							}
							else {
								bitTable[index] |= mask;
							}
							value32 = 0;
							h = 0;
						}
					}
				}
			}
			new VisualEntropy(file.getName(), freq, pointsInsideCircle, numSamples2D, pointsInsideSphere, numSamples3D, collisionsCounter, sumPairBytes);
		}
	}	

}

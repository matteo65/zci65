package test;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * Zci65 
 * Visual Entropy
 * 
 * Author: Matteo Zapparoli <zapparoli.matteo@gmail.com>
 * Date: 2023
 * Licence: Public Domain
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
public class EntropyPanel extends JPanel {
	
	private final BufferedImage canvas = new BufferedImage(256, 256, BufferedImage.TYPE_INT_ARGB);
	
	private final static int RGB_WHITE_COLOR = new Color(0, 0, 0, 0).getRGB();
	
	public EntropyPanel(int[] freq) {
		if(freq.length != 256) throw new IllegalArgumentException("freq.length != 256");
		
		int length = 0;
		for(int i = 0; i < 256; i++) {
			if(freq[i] < 0) throw new IllegalArgumentException("freq[" + i + "] < 0");
			length += freq[i];
		}
		
		double average = length / 256.0;
		
		// Imposto la tavolozza a "bianco", ovvero nero con alpha = 0 (totalmente opaco)
		for (int x = 0; x < 256; x++) {
            for (int y = 0; y < 256; y++) {
                this.canvas.setRGB(x, y, RGB_WHITE_COLOR);
            }
        }
		
		// Calcolo la frequenza minima e massima, lo scarto quadratico medio e chi quadrato
		int minFreq = Integer.MAX_VALUE;
		int maxFreq = Integer.MIN_VALUE;
		int countMinFreq = 0;
		int countMaxFreq = 0;
		
		double variance = 0.0;
		double chi_squared = 0.0;
		
		for(int i = 0; i < 256; i++) {
			if(freq[i] < minFreq) {
				minFreq = freq[i];
			}
			if(freq[i] > maxFreq) {
				maxFreq = freq[i];
			}
			if(freq[i] == minFreq) {
				countMinFreq++;
			}
			if(freq[i] == maxFreq) {
				countMaxFreq++;
			}
			variance += (freq[i] - average) * (freq[i] - average);
			chi_squared += (freq[i] - average) * (freq[i] - average) / 256.0;
		}
		variance /= 256.0;
		
		double standard_deviation = Math.sqrt(variance);
		
		System.out.println("Length = " + length + " bytes");
		System.out.println("Min Frequency = " + minFreq + " (" + countMinFreq + " instances)");
		System.out.println("Max Frequency = " + maxFreq + " (" + countMaxFreq + " instances)");
		System.out.println("Average Frequency = " + round(average));
		System.out.println("Variance = " + round(variance));
		System.out.println("Deviation Standard = " + round(standard_deviation));
		System.out.println("Chi squared = " + round(chi_squared));
		System.out.println("Coefficient of Variation = " + round((standard_deviation / average) * 100) + "%");
		System.out.println();
		
		assert maxFreq >= 0 && minFreq >= 0 && maxFreq >= minFreq;
		
		// If there is not variability, return all white
		if(maxFreq == minFreq)
			return; 
		
		// Calcolo il fattore di "normalizzazione" s
		double s = 255.0d / (maxFreq - minFreq);
		
		// Disegno i quadrati di ogni byte
		for(int b = 0; b < 256; b++) {
			int x = 16 * (b & 0xF);
			int y = 16 * ((b >> 4) & 0xF);
			int rgb = this.canvas.getRGB(x, y);
			
			// Calcolo l'alpha del byte b
			int alpha = (int)Math.round(((freq[b] - minFreq) * s));
			
			assert alpha >= 0 && alpha <= 255;
			
			rgb = (rgb & 0x00FFFFFF) | (alpha << 24);
			for(int i = 0; i < 16; i++) {
				for(int j = 0; j < 16; j++) {
					this.canvas.setRGB(x + i, y + j, rgb);
				}
			}
		}
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawImage(canvas, 0, 0, null);
	}
	
	double round(double d) {
		return Math.round(d * 100.0) / 100.0;
	}
	
}

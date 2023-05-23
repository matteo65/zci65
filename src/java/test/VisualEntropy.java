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
	
	public static final int SIDE = 256;
	
	protected final EntropyPanel panel;
	
	public VisualEntropy(String fileName, int[] freq) {
		setSize(SIDE + 30, SIDE + 60);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setTitle(fileName);
		System.out.println(fileName);
		panel = new EntropyPanel(freq);
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
			try(FileInputStream fis = new FileInputStream(file)) {
				int n;
				while((n = fis.read(buffer)) != -1) {
					for(int k = 0; k < n; k++)
						freq[buffer[k] & 0xFF]++;
				}
			}
			new VisualEntropy(file.getName(), freq);
		}
	}	

}

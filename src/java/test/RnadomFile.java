package test;

import java.io.File;
import java.io.FileOutputStream;
import java.security.SecureRandom;

/**
 * RandomFile Generator
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
public class RnadomFile {
	
	private static final int BUFFER_SIZE = 65536;
	
	public static final void main(String[] args) {
		if(args == null || args.length == 0) {
			System.out.println("RandomFile - v.1.0 2023 by Matteo Zapparoli");
			System.out.println("Usage: randomFile <length> <file path>");
			System.out.println("Create a new file if it does not already exist, or overwrite it if it does");
			System.out.println("It uses the java.security.SecureRandom()");
			System.exit(0);
		}
			
		if(args.length != 2) {
			System.err.println("*** Error: invalid arguments number");
			System.exit(1);
		}
		
		int length = Integer.valueOf(args[0]);
		if(length < 0) {
			System.err.println("*** Error: length < 0");
			System.exit(1);
		}
		
		File file = new File(args[1]);
		
		SecureRandom secRnd = new SecureRandom();
		
		assert BUFFER_SIZE > 0;
		
		try {
    		byte[] buffer = new byte[BUFFER_SIZE];
    		try (FileOutputStream fos = new FileOutputStream(file)) {
    			while(length >= BUFFER_SIZE) {
    				
    				for(int i = 0; i < BUFFER_SIZE; i++)
    					buffer[i] = (byte)secRnd.nextInt(256);
    				
    				fos.write(buffer);
    			
    				length -= BUFFER_SIZE;
    			}
    			
    			if(length > 0) {
    				for(int i = 0; i < length; i++)
    					buffer[i] = (byte)secRnd.nextInt(256);
    				
    				fos.write(buffer, 0, length);
    			}
    			
    		}
    		System.exit(0);
		}
		catch(Exception e) {
			System.err.println("*** Error: " + e.toString());
			e.printStackTrace();
			System.exit(1);
		}
	}

}

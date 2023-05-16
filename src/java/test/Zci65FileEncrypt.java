package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import zci65.Zci65;
import zci65.Zci65cryptInputStream;

/**
 * Zci65FileEncrypt 
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
public class Zci65FileEncrypt {

	private static final int BUFFER_SIZE = 65536;

	public static final void main(String[] args) throws FileNotFoundException, IOException, NoSuchAlgorithmException {
		if(args == null || args.length == 0) {
			System.out.println("Usage: zci65fileEncrypt <pw> <file source> <file dest>");
			System.exit(0);
		}
		
		if(args.length != 3) {
			System.err.println("*** Error: wrong number of arguments");
			System.exit(1);
		}
		
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] pw = md.digest(args[0].getBytes());
		
		Zci65 zci65 = new Zci65(pw);
		
		File source = new File(args[1]);
		if(!source.canRead()) {
			System.err.println("*** Error: file " + args[1] + " not readable");
			System.exit(1);
		}
		File dest = new File(args[2]);
		
		byte[] buffer = new byte[BUFFER_SIZE];

		long time = System.currentTimeMillis();
		try(Zci65cryptInputStream zci65cryptInputStream = new Zci65cryptInputStream(new FileInputStream(source), zci65);
				FileOutputStream fos = new FileOutputStream(dest)) {
			int n;
			while((n = zci65cryptInputStream.read(buffer)) > 0) {
				fos.write(buffer, 0, n);
			}
		}
		System.out.println("Execution time: " + (System.currentTimeMillis() - time));
	}
	
}

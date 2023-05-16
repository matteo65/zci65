package zci65;

import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Zci65decryptInputStream 
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
public class Zci65decryptInputStream extends FilterInputStream {
	
	private final Zci65 zci65;
	
	public Zci65decryptInputStream(InputStream in, Zci65 zci65) {
		super(in);
		if(zci65 == null) throw new NullPointerException("zci65 == null");
		this.zci65 = zci65;
	}
	
	@Override
	public int read() throws IOException {
        	int ret = in.read();
       		if(ret != -1) ret = zci65.decipher(ret);
        	return ret;
	}
	
	@Override
	public int read(byte buf[]) throws IOException {
		return read(buf, 0, buf.length);
	}
	
    	@Override
	public int read(byte buf[], int off, int len) throws IOException {
        	int ret = in.read(buf, off, len);
        	if(ret > 0) zci65.decipher(buf, off, ret);
        	return ret;
   	 }
	
	@Override
	public synchronized void mark(int readlimit) {
		throw new UnsupportedOperationException("mark not supported");
	}

	@Override
	public synchronized void reset() throws IOException {
		throw new UnsupportedOperationException("reset not supported");
	}

	@Override
	public boolean markSupported() {
		return false;
	}

}

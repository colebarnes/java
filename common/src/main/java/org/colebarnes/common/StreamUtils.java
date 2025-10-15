/*
 * Copyright © 2025 cole@colebarnes.org, https://colebarnes.org
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the “Software”), to deal in
 * the Software without restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the
 * Software, and to permit persons to whom the Software is furnished to do so,
 * subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR  COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.colebarnes.common;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.colebarnes.common.logger.Logger;

public class StreamUtils {
	public static int DEFAULT_BUFFER_LEN = 1024;

	public static long copy(InputStream in, OutputStream out) throws IOException {
		return StreamUtils.copy(in, out, DEFAULT_BUFFER_LEN);
	}

	public static long copy(InputStream in, OutputStream out, int bufferLen) throws IOException {
		Logger.entering();
		// TODO: check inputs

		byte[] buffer = new byte[bufferLen];
		int bytesRead = 0;
		long totalBytesRead = 0;

		while ((bytesRead = in.read(buffer)) >= 0) {
			out.write(buffer, 0, bytesRead);
			totalBytesRead += bytesRead;
		}

		Logger.exiting(totalBytesRead);
		return totalBytesRead;
	}
}

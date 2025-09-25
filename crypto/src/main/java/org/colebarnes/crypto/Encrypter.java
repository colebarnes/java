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

package org.colebarnes.crypto;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import javax.crypto.SecretKey;

public class Encrypter {
	public static Encrypter getSerpentInstance() {
		return new Encrypter("serpent", "gcm", "nopadding", 256);
	}

	public static Encrypter getAesInstance() {
		return new Encrypter("aes", "gcm", "nopadding", 256);
	}

	public static Encrypter getTwofishInstance() {
		return new Encrypter("twofish", "gcm", "nopadding", 256);
	}

	private String algorithm;
	private String mode;
	private String padding;
	private int keySize;

	private Encrypter(String algorithm, String mode, String padding, int keySize) {
		// TODO: check input
		this.algorithm = algorithm;
		this.mode = mode;
		this.padding = padding;
		this.keySize = keySize;
	}

	public String getCipherAlgorithm() {
		return this.algorithm;
	}

	public String getCipherMode() {
		return this.mode;
	}

	public String getCipherPadding() {
		return this.padding;
	}

	public int getKeySize() {
		return this.keySize;
	}

	public String getCipherTransform() {
		return String.format("%s/%s/%s", this.getCipherAlgorithm(), this.getCipherMode(), this.getCipherPadding());
	}

	public byte[] encrypt(byte[] plainText, char[] password) {
		// TODO: implement
		return null;
	}

	public File encrypt(File plainText, char[] password) {
		// TODO: implement
		return null;
	}
	
	private void encrypt(InputStream in, OutputStream out, SecretKey key) {
		// TODO: implement
	}
}

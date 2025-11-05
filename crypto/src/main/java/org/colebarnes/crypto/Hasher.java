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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.Provider.Service;
import java.util.Set;
import java.util.TreeSet;

import org.colebarnes.common.ByteUtils;
import org.colebarnes.common.StreamUtils;
import org.colebarnes.common.StringUtils;
import org.colebarnes.crypto.common.CryptoException;
import org.colebarnes.crypto.common.CryptoUtils;

public class Hasher {
	public static Set<String> getSupportedAlgorithms() {
		Set<String> algorithms = new TreeSet<>();

		for (Service service : CryptoUtils.getBouncyCastleProvider().getServices()) {
			if (service.getType().equalsIgnoreCase("MessageDigest")) {
				algorithms.add(service.getAlgorithm());
			}
		}

		return algorithms;
	}

	public static Hasher sha256() {
		return Hasher.getInstance("sha256");
	}

	public static Hasher getInstance(String algorithm) {
		return new Hasher(algorithm);
	}

	private String algotithm;

	private Hasher(String algorithm) {
		this.algotithm = algorithm;
	}

	public String hash(String str) throws CryptoException {
		return this.hash(StringUtils.toBytes(str));
	}

	public String hash(File file) throws CryptoException {
		byte[] digest;

		try (FileInputStream in = new FileInputStream(file)) {
			digest = this.hash(in);
		} catch (IOException | NoSuchAlgorithmException e) {
			throw new CryptoException(CryptoException.ERROR_UNKNOWN, "Error hashing file.", e);
		}

		return ByteUtils.toHex(digest);
	}

	public String hash(byte[] bytes) throws CryptoException {
		byte[] digest;

		try (ByteArrayInputStream in = new ByteArrayInputStream(bytes)) {
			digest = this.hash(in);
		} catch (IOException | NoSuchAlgorithmException e) {
			throw new CryptoException(CryptoException.ERROR_UNKNOWN, "Error hashing data.", e);
		}

		return ByteUtils.toHex(digest);
	}

	private byte[] hash(InputStream in) throws NoSuchAlgorithmException, IOException {
		MessageDigest digest = MessageDigest.getInstance(this.algotithm, CryptoUtils.getBouncyCastleProvider());

		try (DigestInputStream din = new DigestInputStream(in, digest)) {
			StreamUtils.copy(din, null);
		}

		return digest.digest();
	}
}

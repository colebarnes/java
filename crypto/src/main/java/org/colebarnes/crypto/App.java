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
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.colebarnes.common.StreamUtils;
import org.colebarnes.common.StringUtils;
import org.colebarnes.common.logger.Logger;
import org.colebarnes.crypto.common.CryptoException;
import org.colebarnes.crypto.common.CryptoUtils;

public class App {
	private static void test() {
		Logger.entering();

		String message = "This is my super secret message!";
		byte[] plainText = StringUtils.toBytes(message);
		byte[] keyBytes = CryptoUtils.random(256 / 8);
		SecretKey key = new SecretKeySpec(keyBytes, "aes");
		byte[] iv = CryptoUtils.random(12);
		GCMParameterSpec params = new GCMParameterSpec(128, iv);
		Cipher cipher = null;
		byte[] cipherText = null;
		byte[] decryptedBytes = null;

		try {
			cipher = Cipher.getInstance("aes/gcm/nopadding", CryptoUtils.getBouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, key, params);

			try (ByteArrayInputStream in = new ByteArrayInputStream(plainText); CipherInputStream cin = new CipherInputStream(in, cipher); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				StreamUtils.copy(cin, out);
				cipherText = out.toByteArray();
			}

			cipher = Cipher.getInstance("aes/gcm/nopadding", CryptoUtils.getBouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, key, params);

			try (ByteArrayInputStream in = new ByteArrayInputStream(cipherText); CipherInputStream cin = new CipherInputStream(in, cipher); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
				StreamUtils.copy(cin, out);
				decryptedBytes = out.toByteArray();
			}

			Logger.info(StringUtils.fromBytes(decryptedBytes));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Logger.exiting();
	}

	public static void main(String[] args) {
		Logger.setLogLevel(Logger.INFO);
		Logger.entering();

//		App.test();

		byte[] plainText = StringUtils.toBytes("This is my super secret message!");
		char[] password = "password1".toCharArray();

		try (Encrypter enc = Encrypter.getSerpentInstance()) {
			byte[] cipherText = enc.encrypt(plainText, password);

			try (Decrypter dec = Decrypter.getInstance(cipherText)) {
				byte[] data = dec.decryptToBytes(password);
				Logger.info("decrypted data: %s", StringUtils.fromBytes(data));
			}
		} catch (IOException | CryptoException e) {
			e.printStackTrace();
		}

		Logger.exiting();
	}
}

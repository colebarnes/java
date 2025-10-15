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

package org.colebarnes.crypto.common;

import java.security.NoSuchAlgorithmException;
import java.security.Provider;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
import org.colebarnes.common.ByteUtils;
import org.colebarnes.common.logger.Logger;

public class CryptoUtils {
	public static Provider getCipherProvider() {
		return CryptoUtils.getBouncyCastleProvider();
	}

	public static synchronized Provider getBouncyCastleProvider() {
		Logger.trace("getting bcfips provider");
		Provider provider = Security.getProvider("BCFIPS");

		if (provider == null) {
			Logger.info("creating and registering new BCFIPS provider ...");
			// TODO: fips mode???
			provider = new BouncyCastleFipsProvider();
			Security.insertProviderAt(provider, 1);
		}

		return provider;
	}

	public static SecretKey pkbdf2Sha256(char[] password, byte[] salt, int iterations, int keySize, String keyCipherAlgorithm) throws NoSuchAlgorithmException, InvalidKeySpecException {
		// TODO: check input
		SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256", CryptoUtils.getBouncyCastleProvider());
		KeySpec ks = new PBEKeySpec(password, salt, iterations, keySize);

		return new SecretKeySpec(f.generateSecret(ks).getEncoded(), keyCipherAlgorithm);
	}

	public static byte[] random(int size) {
		// FIXME: do not use insecure random like this!!!
		return ByteUtils.random(size);
	}

	public static void random(byte[] bytes) {
		// FIXME: do not use insecure random like this!!!
		ByteUtils.random(bytes);
	}
}

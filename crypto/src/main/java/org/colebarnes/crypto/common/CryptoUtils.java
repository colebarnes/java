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

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Provider.Service;
import java.security.PublicKey;
import java.security.Security;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.bouncycastle.jcajce.provider.BouncyCastleFipsProvider;
import org.colebarnes.common.ByteUtils;
import org.colebarnes.common.logger.Logger;

public class CryptoUtils {
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

	public static SecretKey randomSecretKey(String algorithm, int keySize) {
		byte[] keyMaterial = CryptoUtils.random(keySize / 8);
		return new SecretKeySpec(keyMaterial, algorithm);
	}

	public static byte[] wrapKey(SecretKey secretKey, PublicKey publicKey) throws CryptoException {
		try {
			Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm(), CryptoUtils.getBouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, publicKey);
			return cipher.doFinal(secretKey.getEncoded());
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new CryptoException(CryptoException.ERROR_UNKNOWN, "Error wrapping key", e);
		}
	}

	public static SecretKey unwrapKey(byte[] wrappedKey, String algorithm, PrivateKey privateKey) throws CryptoException {
		try {
			Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm(), CryptoUtils.getBouncyCastleProvider());
			cipher.init(Cipher.DECRYPT_MODE, privateKey);
			return new SecretKeySpec(cipher.doFinal(wrappedKey), algorithm);
		} catch (NoSuchAlgorithmException | NoSuchPaddingException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException e) {
			throw new CryptoException(CryptoException.ERROR_UNKNOWN, "Error wrapping key", e);
		}
	}

	public static void printProviderInfo(Provider provider) {
		CryptoUtils.printProviderInfo(provider, null);
	}

	public static void printProviderInfo(Provider provider, String typeFilter) {
		if (provider != null) {
			Logger.info("Provider: %s v%s", provider.getName(), provider.getVersionStr());

			Map<String, List<String>> services = new HashMap<>();
			for (Service service : provider.getServices()) {
				String algorithm = service.getAlgorithm();
				String type = service.getType();

				if (typeFilter != null && type.compareToIgnoreCase(typeFilter) != 0) {
					continue;
				}

				List<String> algorithms = services.get(type);
				if (algorithms == null) {
					algorithms = new ArrayList<>();
					services.put(type, algorithms);
				}

				algorithms.add(algorithm);
			}

			for (String type : services.keySet()) {
				Logger.info("\t%s", type);
				List<String> algorithms = services.get(type);

				for (String algorithm : algorithms) {
					Logger.info("\t\t%s", algorithm);
				}
			}
		}
	}
}

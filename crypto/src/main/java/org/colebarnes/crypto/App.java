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

import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Enumeration;

import org.colebarnes.common.StringUtils;
import org.colebarnes.common.logger.Logger;
import org.colebarnes.crypto.common.CryptoException;
import org.colebarnes.crypto.common.CryptoUtils;

public class App {
	private static void pbeTest() {
		try (Encrypter enc = Encrypter.getSerpentInstance()) {
			byte[] plainText = StringUtils.toBytes("This is my super secret message! - pbe");
			char[] password = "Qwerty_123".toCharArray();

			byte[] cipherText = enc.encrypt(plainText, password);

			try (Decrypter dec = Decrypter.getInstance(cipherText)) {
				byte[] data = dec.decryptToBytes(password);
				Logger.info("decrypted data: %s", StringUtils.fromBytes(data));
			}
		} catch (IOException | CryptoException e) {
			e.printStackTrace();
		}
	}

	private static void pkiTest() {
		try (Encrypter enc = Encrypter.getSerpentInstance(); FileInputStream fin = new FileInputStream("/home/cbarnes/Desktop/colebarnes.p12")) {
			byte[] plainText = StringUtils.toBytes("This is my super secret message! - pki");
			char[] password = "Qwerty_123".toCharArray();

			KeyStore ks = KeyStore.getInstance("PKCS12", CryptoUtils.getBouncyCastleProvider());
			ks.load(fin, password);

			Enumeration<String> aliases = ks.aliases();
			while (aliases.hasMoreElements()) {
				String alias = aliases.nextElement();

				X509Certificate cert = (X509Certificate) ks.getCertificate(alias);
				byte[] cipherText = enc.encrypt(plainText, cert);

				try (Decrypter dec = Decrypter.getInstance(cipherText)) {
					PrivateKey privateKey = (PrivateKey) ks.getKey(alias, password);
					byte[] data = dec.decryptToBytes(privateKey);
					Logger.info("decrypted data: %s", StringUtils.fromBytes(data));
				}
			}
		} catch (IOException | KeyStoreException | NoSuchAlgorithmException | CertificateException | UnrecoverableKeyException | CryptoException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		Logger.setLogLevel(Logger.INFO);
		Logger.entering();

		App.pbeTest();
		App.pkiTest();

		Logger.exiting();
	}
}

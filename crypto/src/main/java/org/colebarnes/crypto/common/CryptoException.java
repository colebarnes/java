package org.colebarnes.crypto.common;

import org.colebarnes.common.BaseException;

public class CryptoException extends BaseException {
	private static final long serialVersionUID = 5508651593300599397L;

	public CryptoException(long code, String message) {
		super(code, message);
	}

	public CryptoException(long code, String message, Throwable cause) {
		super(code, message, cause);
	}
}

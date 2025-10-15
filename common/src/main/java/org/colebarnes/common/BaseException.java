package org.colebarnes.common;

public class BaseException extends Exception {
	private static final long serialVersionUID = -7307624570312088415L;

	public static final long ERROR_UNKNOWN = Long.MIN_VALUE;

	private long code = BaseException.ERROR_UNKNOWN;

	public BaseException(long code, String message) {
		super(message);
		this.code = code;
	}

	public BaseException(long code, String message, Throwable cause) {
		super(message, cause);
		this.code = code;
	}

	public long getCode() {
		return this.code;
	}

	@Override
	public String getMessage() {
		return String.format("[%d]: %s", this.code, super.getMessage());
	}

	@Override
	public String toString() {
		return String.format("%s: %s", this.getClass().getCanonicalName(), this.getMessage());
	}
}

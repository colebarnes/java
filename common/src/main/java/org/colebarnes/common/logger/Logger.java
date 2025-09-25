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

package org.colebarnes.common.logger;

import java.io.PrintStream;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

import org.colebarnes.common.DateUtils;
import org.colebarnes.common.ThreadUtils;;

public class Logger {
	private static Logger logger = new Logger();

	public static void error(Throwable cause, Object... args) {
		String msgFmt;

		if (cause == null) {
			msgFmt = "An unknown exception occured.";
		} else {
			msgFmt = String.format("An exception occured: %s", cause.getMessage());
		}

		Logger.error(msgFmt);
	}

	public static void error(String msgFmt, Object... args) {
		Logger.logger.log(ERROR, msgFmt, args);
	}

	public static void warn(Throwable cause, Object... args) {
		String msgFmt;

		if (cause == null) {
			msgFmt = "an unknown exception occured";
		} else {
			msgFmt = String.format("An exception occured: %s", cause.getMessage());
		}

		Logger.warn(msgFmt);
	}

	public static void warn(String msgFmt, Object... args) {
		Logger.logger.log(WARN, msgFmt, args);
	}

	public static void info(String msgFmt, Object... args) {
		Logger.logger.log(INFO, msgFmt, args);
	}

	public static void trace(String msgFmt, Object... args) {
		Logger.logger.log(TRACE, msgFmt, args);
	}

	public static void entering() {
		Logger.logger.log(TRACE, ">>> ENTERING %s", ThreadUtils.getCallerInfoString(4, false, false));
	}

	public static void exiting() {
		Logger.logger.log(TRACE, "<<< EXITING %s", ThreadUtils.getCallerInfoString(4, false, false));
	}

	public static void exiting(Object retVal) {
		Logger.logger.log(TRACE, "<<< EXITING %s: %s", ThreadUtils.getCallerInfoString(4, false, false),
				(retVal == null) ? "" : retVal.toString());
	}

	public static void setLogLevel(int level) {
		Logger.logger.level = level;
	}

	public static void addPrintStream(PrintStream printStream) {
		// TODO: check input
		Logger.logger.printStreams.add(printStream);
	}

	/* IMPLEMENTATION */
	public static final int OFF = 0;
	public static final int ERROR = 1;
	public static final int WARN = 2;
	public static final int INFO = 3;
	public static final int TRACE = 4;

	private volatile int level;
	private Collection<PrintStream> printStreams;

	private Logger() {
		this.level = Logger.WARN;

		this.printStreams = Collections.synchronizedCollection(new HashSet<PrintStream>());
		this.printStreams.add(System.out);
	}

	private String getLevelString(int level) {
		String levelName = "UNKNOWN";

		switch (level) {
		case Logger.OFF:
			levelName = "OFF";
			break;
		case Logger.ERROR:
			levelName = "ERR";
			break;
		case Logger.WARN:
			levelName = "WRN";
			break;
		case Logger.INFO:
			levelName = "INF";
			break;
		case Logger.TRACE:
			levelName = "TRC";
			break;
		}

		return levelName;
	}

	private String formatDefaultLogEntry(int level, String msgFmt, Object... args) {
		StringBuffer msgBuffer = new StringBuffer();

		msgBuffer.append('[').append(ThreadUtils.threadId()).append(']');
		msgBuffer.append('[').append(DateUtils.iso8601CurrentDate()).append(']');
		msgBuffer.append('[').append(this.getLevelString(level)).append(']');
		msgBuffer.append('[').append(ThreadUtils.getCallerInfoString(7, true, true)).append(']');

		// TODO: OTHER STUFF in log entry?

		msgBuffer.append(':').append(String.format(msgFmt, args));

		return msgBuffer.toString();
	}

	private String formatLogEntry(int level, String msgFmt, Object... args) {
		return this.formatDefaultLogEntry(level, msgFmt, args);
	}

	private void log(int level, String msgFmt, Object... args) {
		if (this.level > Logger.OFF && this.level >= level) {
			synchronized (this.printStreams) {
				Iterator<PrintStream> iter = this.printStreams.iterator();
				while (iter.hasNext()) {
					String logEntry = this.formatLogEntry(level, msgFmt, args);
					iter.next().println(logEntry);
				}
			}
		}
	}
}

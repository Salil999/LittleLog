package littlelog;

import java.io.PrintStream;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * The Logger class serves as a simple logging mechanism. Rather than typing
 * customized logs, the Logger class is able to aid in creating and writing
 * simple logs to multiple streams.
 */
public final class Logger {

	/**
	 * Some constants for quick referencing on commonly used
	 * tag values
	 */
	public static final String VERBOSE = "VERBOSE";
	public static final String INFO = "INFO";
	public static final String ERROR = "ERROR";
	public static final String WARN = "WARN";
	public static final String DEBUG = "DEBUG";

	/**
	 * Every object will hold a PrintStream object
	 * to write logs to. This PrintStream object cannot
	 * be null by contract to ensure proper writing.
	 */
	private PrintStream printStream;

	/**
	 * Constructs the object with a custom PrintStream.
	 *
	 * @param ps PrintStream object to be written to
	 */
	public Logger(final PrintStream ps) {
		this.printStream = ps;
	}

	/**
	 * Constructs the object with the system default PrintStream.
	 */
	public Logger() {
		this(System.out);
	}

	/**
	 * Gets the PrintStream object that is currently stored.
	 *
	 * @return A PrintStream object
	 */
	PrintStream getPrintStream() {
		return this.printStream;
	}

	/**
	 * Sets a new PrintStream to write to.
	 *
	 * @param printStream The PrintStream to set to
	 */
	public void setPrintStream(final PrintStream printStream) {
		this.printStream = printStream;
	}

	/**
	 * Writes a log entry to the print stream with a tag value.
	 *
	 * @param args Strings that are to be included in the log, where the last arg
	 *             is the message in the log
	 */
	public void log(final String... args) {
		this.printStream.println(formatMessage(args));
	}

	/**
	 * Formats a message to "nicely" display the log message with some
	 * other information (like date and time).
	 *
	 * @param args Strings that are to be included in the log, where the last arg
	 *             is the message in the log
	 */
	private String formatMessage(final String... args) {
		final StringBuilder sb = new StringBuilder();
		sb.append("[" + LocalDate.now() + "] ");
		sb.append("[" + LocalTime.now() + "] ");
		for (int i = 0; i < args.length - 1; i++) {
			sb.append("[" + args[i] + "] ");
		}
		sb.append(args[args.length - 1]);
		return sb.toString();
	}
}

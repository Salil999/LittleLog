package parser;

import java.text.ParseException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommonLogFormatParser extends LogParser {

	private final String LOG_ENTRY_PATTERN =
			"^(\\S+) (\\S+) (\\S+) \\[([\\w:/]+\\s[+\\-]\\d{4})\\] \"(\\S+) (\\S+) (\\S+)\" (\\d{3}) (\\d+)";
	private final Pattern PATTERN = Pattern.compile(this.LOG_ENTRY_PATTERN);

	/**
	 * Formatting order of the CommonLogFormat is as follows:
	 * 1. IP - IP address of the client (remote host)
	 * 2. Client - RFC 1413 Identity information of the client
	 * 3. User - userid of the person requesting the document
	 * 4. DateTime - Time when the server finished processing request
	 * 5. Method - HTTP method that was received by server
	 * 6. Request Type - HTTP method that was received by server
	 * 7. HTTP Protocol - HTTP protocol type used by client
	 * 8. Response Code - HTTP response code that server sent to client
	 * 9. Size - Size of the object sent to client (does NOT include headers)
	 */

	private String IP;
	private String clientIdentity;
	private String userId;
	private String dateTime;
	private String requestMethod;
	private String requestType;
	private String HttpProtocol;
	private String responseCode;
	private String length;

	public CommonLogFormatParser(final String log) {
		this.rawEntry = log;
		this.modifiedEntry = log.replace("\t", " ");
	}

	private void assignFields(final Matcher m) {
		this.IP = m.group(1);
		this.clientIdentity = m.group(2);
		this.userId = m.group(3);
		this.dateTime = m.group(4);
		this.requestMethod = m.group(5);
		this.requestType = m.group(6);
		this.HttpProtocol = m.group(7);
		this.responseCode = m.group(8);
		this.length = m.group(9);
		int i = 0;
		while (m.find()) {
			this.rawEntryItems[i] = m.group(i++);
		}
	}

	@Override
	public void parse() throws ParseException {
		final Matcher m = this.PATTERN.matcher(this.modifiedEntry);
		if (!m.find()) {
			throw new ParseException("Error parsing log entry. Most likely, the input log is NOT in CLF format.", 0);
		}
		this.rawEntryItems = new String[9];
		this.assignFields(m);
	}

	public String getIP() {
		return this.IP;
	}

	public String getClientIdentity() {
		return this.clientIdentity;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getDateTime() {
		return this.dateTime;
	}

	public String getRequestMethod() {
		return this.requestMethod;
	}

	public String getRequestType() {
		return this.requestType;
	}

	public String getHttpProtocol() {
		return this.HttpProtocol;
	}

	public String getResponseCode() {
		return this.responseCode;
	}

	public String getLength() {
		return this.length;
	}
}

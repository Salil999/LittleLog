package parser;

import java.text.ParseException;

public abstract class LogParser {

	String[] rawEntryItems;
	String rawEntry;
	String modifiedEntry;

	public abstract void parse() throws ParseException;

	public String[] getEntires() {
		return this.rawEntryItems;
	}

	public String getRawEntry() {
		return this.rawEntry;
	}

	public String getModifiedEntry() {
		return this.modifiedEntry;
	}

}

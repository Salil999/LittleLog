package littlelog;

public class ExtractedLine {
    public String text;
    public Long lineEndIndex;

    public ExtractedLine(final String text, final Long lineEndIndex) {
        this.text = text;
        this.lineEndIndex = lineEndIndex;
    }
}

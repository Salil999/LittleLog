package littlelog;

import edu.berkeley.cs.succinct.buffers.SuccinctFileBuffer;
import edu.berkeley.cs.succinct.regex.RegExMatch;
import edu.berkeley.cs.succinct.regex.SuccinctRegEx;
import edu.berkeley.cs.succinct.regex.parser.RegExParsingException;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import static java.lang.Math.toIntExact;


public class SuccinctLog {
    private final SuccinctFileBuffer succinctFileBuffer;
    private final String filename;
    private Integer fileSize;

    public SuccinctLog(final String filepath) {
        final File file = new File(filepath);
        this.filename = file.getName();
        this.fileSize = 0;
        this.succinctFileBuffer = this.readFromFile(filepath);
    }

    private SuccinctFileBuffer readFromFile(final String filename) {
        final SuccinctFileBuffer succinctFileBuffer = new SuccinctFileBuffer();
        try {
            if (filename.endsWith(".succinct")) {
                succinctFileBuffer.readFromFile(filename);
                this.fileSize = succinctFileBuffer.getSize();
                return succinctFileBuffer;
            }
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return succinctFileBuffer;
    }

    public Integer getFileSize() {
        return this.fileSize;
    }

    public void query(final String query, final ArrayList<String> results, final Integer index, final Object lock) {
        try {
            final SuccinctRegEx succinctRegEx = new SuccinctRegEx(this.succinctFileBuffer, query);
            final Set<RegExMatch> chunkResults = succinctRegEx.compute();
            ExtractedLine extractedLine;
            Long lastLineEnd = new Long(0);
            final StringBuilder sb = new StringBuilder();

            for (final RegExMatch result : chunkResults) {
//                this.extractLine(result.getOffset());
                extractedLine = this.extractLine(result.getOffset());
                if (extractedLine.lineEndIndex > lastLineEnd) {
                    sb.append(extractedLine.text.trim() + "\n");
//                    System.out.println(extractedLine.text);
                    lastLineEnd = extractedLine.lineEndIndex;
                }
            }
            synchronized (lock) {
                results.set(index, sb.toString());
            }
//            System.out.println("Result size = " + chunkResults.size());
        } catch (final RegExParsingException e) {
            System.err.println("Could not parse regular expression: [" + query + "]: " + e.getMessage());
        }
    }

    public ExtractedLine extractLine(final Long originalOffset) {
        final StringBuilder line = new StringBuilder();
        String extracted;
        Long offset = originalOffset;
        final Integer shift = 30;

        while (true) {
            if (offset >= this.fileSize - shift) {
                final int length = toIntExact(this.fileSize - offset);
                line.append(this.extract(offset, length));
                break;
            }
            extracted = this.extract(offset, shift);
            if (extracted.contains("\n")) {
                final int index = extracted.indexOf("\n");
                final String substring = extracted.substring(0, index);
                line.append(substring);
                offset += substring.length();
                break;
            } else {
                line.append(extracted);
                offset += shift;
            }
        }

        final Long lineEnd = offset;

        offset = originalOffset - shift;
        while (true) {
            if (offset < 0) {
                line.append(this.extract(new Long(0), toIntExact(offset + shift)));
                break;
            }
            extracted = this.extract(offset, shift);
            if (extracted.contains("\n")) {
                final int index = extracted.indexOf("\n");
                if (index < extracted.length()) {
                    final String substring = extracted.substring(index + 1);
                    line.append(substring);
                    offset += substring.length();
                }
                break;
            } else {
                line.insert(0, extracted);
                offset -= shift;
            }
        }

        return new ExtractedLine(line.toString(), lineEnd);
    }

    public String extract(final Long offset, final Integer length) {
        final String extracted = new String(this.succinctFileBuffer.extract(offset, length));
        return extracted;
    }

    public void query(final String query) {
        try {
            final SuccinctRegEx succinctRegEx = new SuccinctRegEx(this.succinctFileBuffer, query);
            final Set<RegExMatch> chunkResults = succinctRegEx.compute();
            ExtractedLine extractedLine;
            Long lastLineEnd = new Long(0);
            for (final RegExMatch result : chunkResults) {
//                this.extractLine(result.getOffset());
                extractedLine = this.extractLine(result.getOffset());
                if (extractedLine.lineEndIndex > lastLineEnd) {
                    System.out.println(extractedLine.text);
                    lastLineEnd = extractedLine.lineEndIndex;
                }
            }
//            System.out.println("Result size = " + chunkResults.size());
        } catch (final RegExParsingException e) {
            System.err.println("Could not parse regular expression: [" + query + "]: " + e.getMessage());
        }
    }

    public void count(final String query) {
        final long count = this.succinctFileBuffer.count(query.getBytes());
        if (count > 0) {
            System.out.println(count);
        }
    }

    public void count(final String query, final long[] total, final Object lock) {
        final long count = this.succinctFileBuffer.count(query.getBytes());
        if (count > 0) {
            synchronized (lock) {
                total[0] = total[0] + count;
            }
        }
    }
}

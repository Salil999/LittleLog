package littlelog;

import edu.berkeley.cs.succinct.buffers.SuccinctFileBuffer;
import edu.berkeley.cs.succinct.regex.RegExMatch;
import edu.berkeley.cs.succinct.regex.SuccinctRegEx;
import edu.berkeley.cs.succinct.regex.parser.RegExParsingException;

import java.io.IOException;
import java.util.Set;

import static java.lang.Math.toIntExact;


public class SuccinctLog {
    private final SuccinctFileBuffer succinctFileBuffer;
    private Integer fileSize;

    public SuccinctLog(final String filepath) {
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

    public void query(final String query) {
        try {
            final SuccinctRegEx succinctRegEx = new SuccinctRegEx(this.succinctFileBuffer, query);
            final Set<RegExMatch> chunkResults = succinctRegEx.compute();
            for (final RegExMatch result : chunkResults) {
                System.out.println(this.extractLine(result.getOffset()));
            }
//            System.out.println("Result size = " + chunkResults.size());
        } catch (final RegExParsingException e) {
            System.err.println("Could not parse regular expression: [" + query + "]: " + e.getMessage());
        }
    }

    public String extractLine(final Long originalOffset) {
        final StringBuilder line = new StringBuilder();
        String extracted;
        Long offset = originalOffset;
        final Integer shift = 10;
        while (true) {
            if (offset <= this.fileSize - shift) {
                line.append(this.extract(offset, toIntExact(this.fileSize - offset)));
                break;
            }
            extracted = this.extract(offset, shift);
            if (extracted.contains("\n")) {
                final String[] split = extracted.split("\\\n");
                if (split.length > 0) {
                    line.append(split[0]);
                }
                break;
            } else {
                line.append(extracted);
                offset += shift;
            }
        }
        offset = originalOffset - shift;
        while (true) {
            if (offset < 0) {
                line.append(this.extract(new Long(0), toIntExact(offset + shift)));
                break;
            }
            extracted = this.extract(offset, shift);
            if (extracted.contains("\n")) {
                final String[] split = extracted.split("\\\n");
                if (split.length == 2) {
                    line.insert(0, split[1]);
                }
                break;
            } else {
                line.insert(0, extracted);
                offset -= shift;
            }
        }
        return line.toString();
    }

    public String extract(final Long offset, final Integer length) {
        final String extracted = new String(this.succinctFileBuffer.extract(offset, length));
        return extracted;
    }

    public void count(final String query) {
        System.out.println("Count[" + query + "] = " + this.succinctFileBuffer.count(query.getBytes()));
    }

}

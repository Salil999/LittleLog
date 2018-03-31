package succinct;

import edu.berkeley.cs.succinct.buffers.SuccinctFileBuffer;
import edu.berkeley.cs.succinct.regex.RegExMatch;
import edu.berkeley.cs.succinct.regex.SuccinctRegEx;
import edu.berkeley.cs.succinct.regex.parser.RegExParsingException;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import static java.lang.Math.toIntExact;


public class SuccinctLog {

    private final String filename;
    private final String extension;
    private final String inputDirectory;
    private final String outputDirectory;
    private Integer fileSize;
    private SuccinctFileBuffer succinctFileBuffer;

    public SuccinctLog(final String filename, final String extension, final String inputDirectory, final String outputDirectory) {
        this.filename = filename;
        this.extension = extension;
        this.inputDirectory = inputDirectory;
        this.outputDirectory = outputDirectory;
        this.fileSize = 0;
    }

    public SuccinctLog(final String filename, final String inputDirectory) {
        this.filename = filename;
        this.extension = "succinct";
        this.inputDirectory = inputDirectory;
        this.outputDirectory = "";
        this.fileSize = 0;
        this.succinctFileBuffer = this.readFromFile(this.inputDirectory + this.filename + "." + this.extension);
    }

    private SuccinctFileBuffer readFromFile(final String filename) {
        SuccinctFileBuffer succinctFileBuffer = new SuccinctFileBuffer();
        try {
            if (filename.endsWith(".succinct")) {
//                System.out.println(filename);
                succinctFileBuffer.readFromFile(filename);
                return succinctFileBuffer;
            }

            final File file = new File(filename);
            if (file.length() > 1L << 31) {
                System.err.println("Cant handle files > 2GB");
                System.exit(-1);
            }
            final byte[] fileData = new byte[(int) file.length()];
            System.out.println(filename + " File size: " + fileData.length + " bytes");
            this.fileSize = fileData.length;
            final DataInputStream dis = new DataInputStream(new FileInputStream(file));
            dis.readFully(fileData, 0, (int) file.length());

            succinctFileBuffer = new SuccinctFileBuffer(fileData);
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return succinctFileBuffer;
    }

    public void compressFile() {
        System.out.println("Compressing " + this.filename + "." + this.extension);
        this.succinctFileBuffer = this.readFromFile(this.inputDirectory + this.filename + "." + this.extension);
        this.writeToFile(this.outputDirectory + this.filename + ".succinct");
    }

    private void writeToFile(final String filename) {
        try {
            this.succinctFileBuffer.writeToFile(filename);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public Integer getFileSize() {
        return this.fileSize;
    }

    public void search(final String query) {
        final Long[] results = this.succinctFileBuffer.search(query.getBytes());
        for (final Long result : results) {
            System.out.println(this.extractLine(result));
        }
        System.out.println("Result size = " + results.length);
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

    public void regex(final String query) {
        final Map<Long, Integer> results;
        try {
            final SuccinctRegEx succinctRegEx = new SuccinctRegEx(this.succinctFileBuffer, query);
            final Set<RegExMatch> chunkResults = succinctRegEx.compute();
            for (final RegExMatch result : chunkResults) {
                System.out.println(this.extractLine(result.getOffset()));
            }
            System.out.println("Result size = " + chunkResults.size());
        } catch (final RegExParsingException e) {
            System.err.println("Could not parse regular expression: [" + query + "]: " + e.getMessage());
        }
    }

    public void count(final String query) {
        System.out.println("Count[" + query + "] = " + this.succinctFileBuffer.count(query.getBytes()));
    }

}

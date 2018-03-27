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
import java.util.TreeMap;


public class Succinct {

    private final SuccinctFileBuffer succinctFileBuffer;
    private Integer fileSize;

    public Succinct(final String filename) {
        this.fileSize = 0;
        this.succinctFileBuffer = this.readFromFile(filename);
    }

    public SuccinctFileBuffer readFromFile(final String filename) {
        SuccinctFileBuffer succinctFileBuffer = new SuccinctFileBuffer();
        try {
            if (filename.endsWith(".succinct")) {
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

    public Integer getFileSize() {
        return this.fileSize;
    }

    public void writeToFile(final String filename) {
        try {
            this.succinctFileBuffer.writeToFile(filename);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public void search(final String query) {
        final Long[] results = this.succinctFileBuffer.search(query.getBytes());
        System.out.println("Result size = " + results.length);
        System.out.print("Search[" + query + "] = {");
        if (results.length < 10) {
            for (final Long result : results) {
                System.out.print(result + ", ");
            }
            System.out.println("}");
        } else {
            for (int i = 0; i < 10; i++) {
                System.out.print(results[i] + ", ");
            }
            System.out.println("...}");
        }
    }

    public void extract(final Integer offset, final Integer length) {
        final String extracted = new String(this.succinctFileBuffer.extract(offset, length));
//        System.out.println("Extract[" + offset + ", " + length + "] = " + extracted);
        System.out.println("Extracted Length: " + extracted.length() + " bytes");
    }

    public void regex(final String query) {
        final Map<Long, Integer> results;
        try {
            final SuccinctRegEx succinctRegEx = new SuccinctRegEx(this.succinctFileBuffer, query);

            System.out.println("Parsed Expression: ");
            succinctRegEx.printRegEx();
            System.out.println();

            final Set<RegExMatch> chunkResults = succinctRegEx.compute();
            results = new TreeMap<>();
            for (final RegExMatch result : chunkResults) {
                results.put(result.getOffset(), result.getLength());
            }

            System.out.println("Result size = " + results.size());
            System.out.print("Regex[" + query + "] = {");
            int count = 0;
            for (final Map.Entry<Long, Integer> entry : results.entrySet()) {
                if (count >= 10) {
                    break;
                }
                System.out.print("offset = " + entry.getKey() + "; len = " + entry.getValue() + ", ");
                count++;
            }
            System.out.println("...}");

        } catch (final RegExParsingException e) {
            System.err.println("Could not parse regular expression: [" + query + "]: " + e.getMessage());
        }
    }

    public void count(final String query) {
        System.out.println("Count[" + query + "] = " + this.succinctFileBuffer.count(query.getBytes()));
    }

}

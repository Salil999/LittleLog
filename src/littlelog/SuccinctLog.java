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
    //    private final ArrayList<Long> newlines;
    private Integer fileSize;

    public SuccinctLog(final String filepath) {
        final File file = new File(filepath);
        this.filename = file.getName();
        this.fileSize = 0;
        this.succinctFileBuffer = this.readFromFile(filepath);
//        this.newlines = new ArrayList<>();
//        this.findNewlines();
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

//    private void findNewlines() {
//        try {
//            final SuccinctRegEx succinctRegEx = new SuccinctRegEx(this.succinctFileBuffer, "\n");
//            final Set<RegExMatch> chunkResults = succinctRegEx.compute();
//
//            for (final RegExMatch result : chunkResults) {
//                this.newlines.add(result.getOffset());
//            }
//
//        } catch (final RegExParsingException e) {
//            System.err.println("Could not parse query for newline: " + e.getMessage());
//        }
//    }


    public void query(final String query, final Integer limit, final ArrayList<StringNumTuple> results, final Integer index, final Object lock) {
        try {
            final SuccinctRegEx succinctRegEx = new SuccinctRegEx(this.succinctFileBuffer, query);
            final Set<RegExMatch> chunkResults = succinctRegEx.compute();
            StringNumTuple extractedLine;
            Long lastLineEnd = new Long(0);
            final StringBuilder sb = new StringBuilder();
            int resultCount = 0;

            for (final RegExMatch result : chunkResults) {
                if (resultCount >= limit) {
                    break;
                }

                extractedLine = this.extractLine(result.getOffset());
//                stringNumTuple = this.extractLineBinarySearch(result.getOffset());


                if (extractedLine.num > lastLineEnd) {
                    sb.append(extractedLine.str.trim() + "\n");
//                    System.out.println(stringNumTuple.str);
                    lastLineEnd = extractedLine.num;
                    resultCount++;
                }
            }
            synchronized (lock) {
                results.set(index, new StringNumTuple(sb.toString(), new Long(chunkResults.size() - resultCount)));
            }
//            System.out.println("Result size = " + chunkResults.size());
        } catch (final RegExParsingException e) {
            System.err.println("Could not parse regular expression: [" + query + "]: " + e.getMessage());
        }
    }

    private StringNumTuple extractLine(final Long originalOffset) {
        final StringBuilder line = new StringBuilder();
        String extracted;
        Long offset = originalOffset;
        final Integer shift = 40;

        while (true) {
            if (offset >= this.fileSize - shift) {
                final int length = toIntExact(this.fileSize - offset);
                line.append(this.succinctFileBuffer.extract(offset, length));
                break;
            }

            extracted = this.succinctFileBuffer.extract(offset, shift);
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
                line.append(this.succinctFileBuffer.extract(0, toIntExact(offset + shift)));
                break;
            }

            extracted = this.succinctFileBuffer.extract(offset, shift);
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

        return new StringNumTuple(line.toString(), lineEnd);
    }

//    private StringNumTuple extractLineBinarySearch(final Long originalOffset) {
//        Long start = new Long(0);
//        Long end = new Long(this.newlines.size() - 1);
//        Long mid = (start + end) / 2;
//
//        String line = "";
//        while (true) {
//            final Long prevVal = this.newlines.get(toIntExact(mid - 1));
//            final Long midVal = this.newlines.get(toIntExact(mid));
//            final Long nextVal = this.newlines.get(toIntExact(mid + 1));
//
//            if (originalOffset < midVal) {
//                if (originalOffset > prevVal) {
//                    line = this.succinctFileBuffer.extract(prevVal, toIntExact(midVal - prevVal));
//                    return new StringNumTuple(line, midVal);
//                }
//
//                end = mid - 1;
//                mid = (start + end) / 2;
//
//            } else if (originalOffset > midVal) {
//                if (originalOffset < nextVal) {
//                    line = this.succinctFileBuffer.extract(midVal, toIntExact(nextVal - midVal));
//                    return new StringNumTuple(line, nextVal);
//                }
//
//                start = mid + 1;
//                mid = (start + end) / 2;
//
//            } else {
//                line = this.succinctFileBuffer.extract(midVal, toIntExact(nextVal - midVal));
//                return new StringNumTuple(line, nextVal);
//            }
//        }
//    }

    public void query(final String query, final Integer limit) {
        try {
            final SuccinctRegEx succinctRegEx = new SuccinctRegEx(this.succinctFileBuffer, query);
            final Set<RegExMatch> chunkResults = succinctRegEx.compute();
            StringNumTuple stringNumTuple;
            Long lastLineEnd = new Long(0);
            int count = 0;
            final int last = chunkResults.size() - 1;
            for (final RegExMatch result : chunkResults) {
                stringNumTuple = this.extractLine(result.getOffset());
                if (stringNumTuple.num > lastLineEnd) {
                    System.out.print(stringNumTuple.str);
                    if (count != last) {
                        System.out.println();
//                        System.out.println(stringNumTuple.str);
                    }
                    lastLineEnd = stringNumTuple.num;
                }
                count++;
            }
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

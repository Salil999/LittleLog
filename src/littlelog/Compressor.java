package littlelog;

import edu.berkeley.cs.succinct.SuccinctCore;
import edu.berkeley.cs.succinct.buffers.SuccinctFileBuffer;
import edu.berkeley.cs.succinct.buffers.SuccinctIndexedFileBuffer;
import edu.berkeley.cs.succinct.util.SuccinctConfiguration;
import edu.berkeley.cs.succinct.util.container.IntArrayList;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.logging.Level;


public class Compressor {
    public Compressor() {
    }

    public static void compressBuffer(final ByteBuffer byteBuffer, final String outputpath) throws IOException {
        final FileOutputStream fos = new FileOutputStream(outputpath);
        final DataOutputStream os = new DataOutputStream(fos);

        final long start = System.currentTimeMillis();
        SuccinctCore.LOG.setLevel(Level.OFF);

        SuccinctFileBuffer.construct(byteBuffer.array(), os, new SuccinctConfiguration());

        final long end = System.currentTimeMillis();
        System.out.println("output: " + outputpath + " : " + (end - start) / 1000L + "s");
    }

    public void compress(final String inputpath, final String outputpath) {
        try {
            this.compressFile(inputpath, outputpath);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void compressFile(final String inputpath, final String outputpath) throws IOException {
        final File file = new File(inputpath);
        if (file.length() > 2147483648L) {
            System.err.println("Cant handle files > 2GB");
            System.exit(-1);
        }

        final FileOutputStream fos = new FileOutputStream(outputpath);
        final DataOutputStream os = new DataOutputStream(fos);
        final String type = "text-file";

        final long start = System.currentTimeMillis();
        SuccinctCore.LOG.setLevel(Level.OFF);
        byte var8 = -1;
        switch (type.hashCode()) {
            case -1559206968:
                if (type.equals("binary-file")) {
                    var8 = 1;
                }
                break;
            case -1236542228:
                if (type.equals("indexed-binary-file")) {
                    var8 = 3;
                }
                break;
            case -1084160484:
                if (type.equals("text-file")) {
                    var8 = 0;
                }
                break;
            case -391088064:
                if (type.equals("indexed-text-file")) {
                    var8 = 2;
                }
        }

        final IntArrayList offsets;
        int i;
        switch (var8) {
            case 0:
                SuccinctFileBuffer.construct(Compressor.readTextFile(file), os, new SuccinctConfiguration());
                break;
            case 1:
                SuccinctFileBuffer.construct(Compressor.readBinaryFile(file), os, new SuccinctConfiguration());
                break;
            case 2:
                final char[] fileData = Compressor.readTextFile(file);
                offsets = new IntArrayList();
                offsets.add(0);

                for (i = 0; i < fileData.length; ++i) {
                    if (fileData[i] == '\n') {
                        offsets.add(i + 1);
                    }
                }

                SuccinctIndexedFileBuffer.construct(fileData, offsets.toArray(), os, new SuccinctConfiguration());
                break;
            case 3:
                final byte[] fileData1 = Compressor.readBinaryFile(file);
                offsets = new IntArrayList();
                offsets.add(0);

                for (i = 0; i < fileData1.length; ++i) {
                    if (fileData1[i] == 10) {
                        offsets.add(i + 1);
                    }
                }

                SuccinctIndexedFileBuffer.construct(fileData1, offsets.toArray(), os, new SuccinctConfiguration());
                break;
            default:
                throw new UnsupportedOperationException("Unsupported mode: " + type);
        }

        final long end = System.currentTimeMillis();
        System.out.println("output: " + outputpath + " : " + (end - start) / 1000L + "s");
    }

    private static char[] readTextFile(final File file) throws IOException {
        final char[] fileData = new char[(int) file.length()];
        final FileReader fr = new FileReader(file);
        fr.read(fileData);
        fr.close();
        return fileData;
    }

    private static byte[] readBinaryFile(final File file) throws IOException {
        final byte[] fileData = new byte[(int) file.length()];
        System.out.println("File size: " + fileData.length + " bytes");
        final DataInputStream dis = new DataInputStream(new FileInputStream(file));
        dis.readFully(fileData, 0, (int) file.length());
        return fileData;
    }


}

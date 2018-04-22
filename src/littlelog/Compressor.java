package littlelog;

import edu.berkeley.cs.succinct.SuccinctCore;
import edu.berkeley.cs.succinct.buffers.SuccinctFileBuffer;
import edu.berkeley.cs.succinct.buffers.SuccinctIndexedFileBuffer;
import edu.berkeley.cs.succinct.util.SuccinctConfiguration;
import edu.berkeley.cs.succinct.util.container.IntArrayList;

import java.io.*;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

import static java.lang.Math.toIntExact;


public class Compressor {
    public static void compress(final File input) {
        final File output = new File(input.getParent() + "/" + Compressor.getFilePathWithoutExtension(input) + ".succinct");
        Compressor.compress(input.getAbsolutePath(), output.getAbsolutePath());
    }

    private static String getFilePathWithoutExtension(final File file) {
        final String fileName = file.getName();
        final int position = fileName.lastIndexOf('.');
        if (position > 0) {
            return fileName.substring(0, position);
        }
        return fileName;
    }

    private static void compress(final String inputPath, final String outputPath) {
        try {
            Compressor.compressFile(inputPath, outputPath);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    private static void compressFile(final String inputPath, final String outputPath) throws IOException {
        final File file = new File(inputPath);
        if (file.length() > 2147483648L) {
            System.err.println("Cant handle files > 2GB");
            System.exit(-1);
        }

        final FileOutputStream fos = new FileOutputStream(outputPath);
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
        System.out.println("output: " + outputPath + " : " + (end - start) / 1000L + "s");
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

    public static void compress(final File input, final File output) {
        try {
            Compressor.compressFile(input.getAbsolutePath(), output.getAbsolutePath());
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    public static void compress(final File input, final Integer shardSize) {
        final File output = new File(input.getParent() + "/" + Compressor.getFilePathWithoutExtension(input) + "_compressed/");
        Compressor.compress(input, output, shardSize);
    }

    public static void compress(final File input, final File outputDir, final Integer shardSize) {
        final ExecutorService pool = Executors.newFixedThreadPool(100);
        final ArrayList<Long> splitPoints = Compressor.findSplitPoints(input.getAbsolutePath(), shardSize);

        final int totalDigits = String.valueOf(splitPoints.size()).length();
        String index;
        String outputPath;

        final File output = Compressor.generateOutputDirectory(outputDir);

        for (int i = 0; i < splitPoints.size() - 1; i++) {
            index = String.valueOf(i);
            while (index.length() < totalDigits) {
                index = "0" + index;
            }
            outputPath = output.getAbsolutePath() + "/" + Compressor.getFilePathWithoutExtension(input) + "_" + index + ".succinct";
            Compressor.compressShard(pool, input.getAbsolutePath(), splitPoints.get(i), splitPoints.get(i + 1) - splitPoints.get(i), outputPath);
        }
        pool.shutdown();
    }

    private static ArrayList<Long> findSplitPoints(final String filepath, final Integer shardSize) {
        final ArrayList<Long> array = new ArrayList<>();
        try {
            final RandomAccessFile file = new RandomAccessFile(filepath, "r");

            byte[] bytes;
            String data;
            long position = shardSize;
            int index = 0;

            array.add(new Long(0));

            while (position < file.length()) {
                while (true) {
                    file.seek(position);
                    bytes = new byte[100];
                    file.read(bytes);
                    data = new String(bytes);
                    index = data.indexOf('\n');

                    if (index == -1) {
                        position += 100;
                    } else {
                        position += index;
                        break;
                    }

                    if (position > file.length()) {
                        break;
                    }
                }

//                System.out.println(position);
                array.add(position);
                position += shardSize;
            }
            array.add(file.length());
            file.close();
            return array;

        } catch (final IOException e) {
            e.printStackTrace();
        }
        return array;
    }

    private static File generateOutputDirectory(final File file) {
        final File output;
        try {
            output = new File(file.getCanonicalPath());
            if (!output.exists()) {
                try {
                    output.mkdirs();
                } catch (final SecurityException se) {
                    se.printStackTrace();
                }
            }
            return output;

        } catch (final IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void compressShard(final ExecutorService pool, final String filename, final Long position, final Long size, final String outputPath) {
        pool.execute(() -> {
            try {
                final RandomAccessFile file = new RandomAccessFile(filename, "r");
                file.seek(position);
                final byte[] bytes = new byte[toIntExact(size)];
                file.read(bytes);
                file.close();
//                System.out.println(position + "\t" + size + "\t" + bytes.length);

                final Compressor compressor = new Compressor();
                compressor.compressBuffer(bytes, outputPath);

            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
    }

    private static void compressBuffer(final byte[] bytes, final String outputpath) throws IOException {
        final FileOutputStream fos = new FileOutputStream(outputpath);
        final DataOutputStream os = new DataOutputStream(fos);

        final long start = System.currentTimeMillis();
        SuccinctCore.LOG.setLevel(Level.OFF);

        SuccinctFileBuffer.construct(bytes, os, new SuccinctConfiguration());

        final long end = System.currentTimeMillis();
        System.out.println("output: " + outputpath + " : " + (end - start) / 1000L + "s");
    }
}

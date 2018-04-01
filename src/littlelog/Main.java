package littlelog;

import java.io.File;

public class Main {
    public static void main(final String[] args) {
//        final LittleLogConsumer consumer = new LittleLogConsumer();
//        new Thread(consumer).start();
//        Main.succinctTest("test", ".txt", "GET");
//
//        final LittleLog littleLog = new LittleLog();
//        littleLog.compress("logfiles/read.log");
//        littleLog.search("12/Aug/2019");
//        littleLog.shutdown();


        final File file = new File("src/logfiles/access.log");
        System.out.println(file.getName());
    }


//    public static void succinctTest(final String filename, final String extension, final String query) {
//        final String inputFile = "logfiles/" + filename + extension;
//        final String outputFile = "logfiles/" + filename + ".succinct";
//
//        long startTime = System.currentTimeMillis();
//        final SuccinctLog succinctLog = new SuccinctLog(inputFile);
//        long endTime = System.currentTimeMillis();
//        System.out.println("File Compression Time: " + Long.toString(endTime - startTime) + " ms");
//
//        final File file = new File(outputFile);
//        final byte[] fileData = new byte[(int) file.length()];
//        System.out.println("Compressed File Size: " + fileData.length + " bytes\n");
//
//
//        startTime = System.currentTimeMillis();
//        succinctLog.writeToFile(outputFile);
//        endTime = System.currentTimeMillis();
//        System.out.println("SuccinctLog File Write To Disk Time: " + Long.toString(endTime - startTime) + " ms\n");
//
//
//        startTime = System.currentTimeMillis();
//        succinctLog.readFromFile(outputFile);
//        endTime = System.currentTimeMillis();
//        System.out.println("SuccinctLog File Read From Disk Time: " + Long.toString(endTime - startTime) + " ms\n");
//
//
//        startTime = System.currentTimeMillis();
//        succinctLog.extract(0, succinctLog.getFileSize());
//        endTime = System.currentTimeMillis();
//        System.out.println("SuccinctLog Full File Extraction Time: " + Long.toString(endTime - startTime) + " ms\n");
//
//        startTime = System.currentTimeMillis();
//        succinctLog.search(query);
//        endTime = System.currentTimeMillis();
//        System.out.println("SuccinctLog File Search Time: " + Long.toString(endTime - startTime) + " ms\n");
//    }
}
package littlelog;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LittleLog {
    ExecutorService pool;
    String outputDirectory;

    public LittleLog() {
        this.pool = Executors.newCachedThreadPool();
    }

    public void count(final String query) {
        this.runSuccinctTask(SuccinctTaskType.COUNT, query);
    }

    public void query(final String query) {
        this.runSuccinctTask(SuccinctTaskType.REGEX, query);
    }

    private void runSuccinctTask(final SuccinctTaskType succinctTaskType, final String query) {
        for (final String filename : this.getAllFiles(this.outputDirectory)) {
            final String filepath = this.outputDirectory + filename;
            try {
                final SuccinctTask succinctTask = new SuccinctTask.Builder()
                        .succinctTaskType(succinctTaskType)
                        .filepath(filepath)
                        .outputDirectory(this.outputDirectory)
                        .query(query)
                        .build();
                this.pool.execute(succinctTask);
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
    }

//    public void compress(final File file) {
//        try {
//            final SuccinctTask succinctTask = new SuccinctTask(SuccinctTaskType.COMPRESS, file.getAbsolutePath());
//            this.pool.execute(succinctTask);
//        } catch (final Exception e) {
//            System.out.println(e.getLocalizedMessage());
//        }
//    }

    public void shutdown() {
        this.pool.shutdown();
    }

    private ArrayList<String> getAllFiles(final String directory) {
        final ArrayList<String> files = new ArrayList<>();
        this.listf(directory, files);
        return files;
    }

    public void listf(final String directoryName, final ArrayList<String> files) {
        final File directory = new File(directoryName);

        final File[] fList = directory.listFiles();
        for (final File file : fList) {
            if (file.isFile()) {
                files.add(file.getName());
            } else if (file.isDirectory()) {
                this.listf(file.getAbsolutePath(), files);
            }
        }
    }

    private void getDirectory(final File file) {
//        file.getCanonicalPath();
    }

//
//    public void succinctTest(final String filename) {
//        final String inputFile = "logfiles/" + filename;
//        String datetime = java.time.LocalDate.now().toString() + "-" + java.time.LocalTime.now().toString().split(":")[0];
//        datetime = datetime.replace("-", "/");
//
//        LittleLog.createDirectory("compressed_logs/" + datetime);
//        final String newfilename = filename.split("\\.")[0];
//        final String outputFile = "compressed_logs/" + datetime + "/" + newfilename + ".succinct";
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
//        System.out.println("Succinct File Write To Disk Time: " + Long.toString(endTime - startTime) + " ms\n");
//    }
//
//    public static void createDirectory(final String pathname) {
//        final File theDir = new File(pathname);
//        if (!theDir.exists()) {
//            try {
//                theDir.mkdirs();
//            } catch (final SecurityException se) {
//                se.printStackTrace();
//            }
//        }
//    }
}


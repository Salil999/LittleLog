package littlelog;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class LittleLog {
    ExecutorService pool;

    public LittleLog(final Integer nThreads) {
        this.pool = Executors.newFixedThreadPool(nThreads);
    }

    public LittleLog() {
        this.pool = Executors.newFixedThreadPool(1);
    }

    private static void createDirectory(final String pathname) {
        final File theDir = new File(pathname);
        if (!theDir.exists()) {
            try {
                theDir.mkdirs();
            } catch (final SecurityException se) {
                se.printStackTrace();
            }
        }
    }

    public void setThreadPoolSize(final Integer nThreads) {
        this.pool = Executors.newFixedThreadPool(nThreads);
    }

    public void compress(final String inputFilepath, final String outputFilepath) {
        try {
            final SuccinctTask succinctTask = new SuccinctTask(SuccinctTaskType.COMPRESS, inputFilepath, outputFilepath, "");
            this.pool.execute(succinctTask);
        } catch (final Exception e) {
            e.printStackTrace();
        }
    }

    public void count(final String query, final String directory) {
        this.runSuccinctTask(SuccinctTaskType.COUNT, directory, query);
    }

    public void query(final String query, final String directory) {
        this.runSuccinctTask(SuccinctTaskType.QUERY, directory, query);
    }

    private void runSuccinctTask(final SuccinctTaskType succinctTaskType, final String directory, final String query) {
        for (final String filepath : this.getAllFiles(directory)) {
            if (filepath.endsWith(".succinct")) {
                try {
                    final SuccinctTask succinctTask = new SuccinctTask(succinctTaskType, filepath, "", query);
                    this.pool.execute(succinctTask);
                } catch (final Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

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
                files.add(file.getAbsolutePath());
            } else if (file.isDirectory()) {
                this.listf(file.getAbsolutePath(), files);
            }
        }
    }

    public void compressDirectory(final String directory) {
        final String outputDirectory;
        if (directory.endsWith("/")) {
            outputDirectory = directory.substring(0, directory.length() - 1) + "_compressed/";
            LittleLog.createDirectory(outputDirectory);
        } else {
            System.out.println("Couldn't parse directory: " + directory + "\nmissing ending \"/\"");
            return;
        }

        final ArrayList<String> files = this.getAllFiles(directory);
        String[] split;
        for (final String f : files) {
            split = f.split("\\.");
            if (split.length == 2) {
                split = split[0].split("\\/");
                if (split.length > 0) {
                    final String name = split[split.length - 1];
                    this.compress(f, outputDirectory + name + ".succinct");
                } else {
                    System.out.println("Couldn't parse file: " + f);
                }
            } else {
                System.out.println("Couldn't parse file: " + f);
            }
        }
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
}


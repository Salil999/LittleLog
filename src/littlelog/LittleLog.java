package littlelog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

public class LittleLog {
    ExecutorService pool;
    Double chunkSize;

    public LittleLog() {
        this.pool = Executors.newFixedThreadPool(1);
        this.chunkSize = 100.0;
    }

    public LittleLog(final Integer nThreads) {
        this.pool = Executors.newFixedThreadPool(nThreads);
        this.chunkSize = 100.0;
    }

    public LittleLog(final Integer nThreads, final Double chunkSize) {
        this.pool = Executors.newFixedThreadPool(nThreads);
        this.chunkSize = chunkSize;
    }

    public LittleLog(final Double chunkSize) {
        this.chunkSize = chunkSize;
    }

    private static String getFilePathWithoutExtension(final File file) {
        final String fileName = file.getName();
        final int position = fileName.lastIndexOf('.');
        if (position > 0) {
            return fileName.substring(0, position);
        }
        return fileName;
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

    private void compress(final File input, final File output) {
        this.compress(input.getAbsolutePath(), output.getAbsolutePath());
    }

    private void compress(final String inputFilePath, final String outputFilePath) {
        final SuccinctTask succinctTask = new SuccinctTask(SuccinctTaskType.COMPRESS, inputFilePath, outputFilePath, "");
        succinctTask.run();
    }

    public void count(final String query, final File file) {
        this.runSuccinctTask(SuccinctTaskType.COUNT, file, Pattern.compile(query).pattern());
    }

    public void query(final String query, final File file) {
        this.runSuccinctTask(SuccinctTaskType.QUERY, file, Pattern.compile(query).pattern());
    }

    private void runSuccinctTask(final SuccinctTaskType succinctTaskType, final File directory, final String query) {
        for (final File file : this.getAllFiles(directory)) {
            if (file.getName().endsWith(".succinct")) {
                try {
                    final SuccinctTask succinctTask = new SuccinctTask(succinctTaskType, file.getAbsolutePath(), "", query);
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

    private ArrayList<File> getAllFiles(final File directory) {
        final ArrayList<File> files = new ArrayList<>();
        this.listf(directory, files);
        return files;
    }

    public void listf(final File directory, final ArrayList<File> files) {
        if (!directory.isDirectory()) {
            System.out.println(directory.getName() + " is not a directory");
            return;
        }

        final File[] fList = directory.listFiles();
        for (final File file : fList) {
            if (file.isFile()) {
                files.add(file);
            } else if (file.isDirectory()) {
                this.listf(file, files);
            }
        }
    }

    public void compressDirectory(final File directory) {
        final String output;
        try {
            output = directory.getCanonicalPath() + "_compressed/";
            LittleLog.createDirectory(output);
            this.compressDirectory(directory, new File(output));
        } catch (final IOException e) {
            e.printStackTrace();
            return;
        }
    }

    public void compressDirectory(final File directory, final File outputDirectory) {
        if (!directory.isDirectory()) {
            System.out.println("usage: [input-directory] [output-directory]");
            return;
        }
        System.out.println("input: " + directory.getAbsolutePath());

        LittleLog.createDirectory(outputDirectory.getAbsolutePath());
        final String output;
        try {
            output = outputDirectory.getCanonicalPath();
//            System.out.println(outputDirectory.getCanonicalPath());
        } catch (final Exception e) {
            System.out.println("Couldn't generate output directory path");
            return;
        }

        final ArrayList<File> files = this.getAllFiles(directory);
        for (final File f : files) {
            this.pool.execute(() -> {
//                System.out.println("compressing " + f.getName());
                final String name = this.getFilePathWithoutExtension(f);
                this.compress(f, new File(output + "/" + name + ".succinct"));
            });
        }
    }


//    public void compressLog(final File file) {
//        if (!file.isFile()) {
//            System.out.println("usage: [file]");
//            return;
//        }
//        final Sharder sharder = new Sharder(this.chunkSize, )
//    }


}


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
        this.pool = Executors.newFixedThreadPool(10);
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

//    public void setThreadPoolSize(final Integer nThreads) {
//        this.pool = Executors.newFixedThreadPool(nThreads);
//    }

    public void count(final String query, final File file) {
        this.runSuccinctTask(SuccinctTaskType.COUNT, file, Pattern.compile(query).pattern());
    }

    public void query(final String query, final File file) {
        this.runSuccinctTask(SuccinctTaskType.QUERY, file, Pattern.compile(query).pattern());

        //TODO: create mini-terminal for littlelog to maintain highest-query-result-yiled succinct cache to improve query time
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

    private void listf(final File directory, final ArrayList<File> files) {
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

    public void compress(final File input) {
        Compressor.compress(input);
    }

    public void compress(final File input, final File output) {
        Compressor.compress(input, output);
    }

    public void compress(final File input, final Integer shardSize) {
        Compressor.compress(input, shardSize);

    }

    public void compress(final File input, final File output, final Integer shardSize) {
        Compressor.compress(input, output, shardSize);
    }

    private File generateOutputDirectory(final File file) {
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

    public void compressDirectory(final File directory) {
        final File output = new File(directory + "_compressed/");
        this.compressDirectory(directory, output);
    }

    public void compressDirectory(final File directory, final File outputDirectory) {
        if (!directory.isDirectory()) {
            System.out.println("usage: [input-directory] [output-directory]");
            return;
        }
        System.out.println("input: " + directory.getAbsolutePath());

        final File output = this.generateOutputDirectory(outputDirectory);
        final ArrayList<File> files = this.getAllFiles(directory);

        for (final File f : files) {
            this.pool.execute(() -> {
                try {
                final String name = this.getFilePathWithoutExtension(f);
                    Compressor.compress(f, new File(output.getCanonicalPath() + "/" + name + ".succinct"));
                } catch (final IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}


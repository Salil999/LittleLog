package littlelog;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

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

    public void queryInOrder(final String query, final File input) {
        final ArrayList<String> results = new ArrayList<>();

        if (input.isFile() && input.getName().endsWith(".succinct")) {

            final SuccinctLog succinctLog = new SuccinctLog(input.getAbsolutePath());
            succinctLog.query(query);

        } else if (input.isDirectory()) {
            final ArrayList<File> files = this.getAllFiles(input);

            Collections.sort(files, new Comparator<File>() {
                @Override
                public int compare(final File o1, final File o2) {
                    final int n1 = this.extractNumber(o1.getName());
                    final int n2 = this.extractNumber(o2.getName());
                    return n1 - n2;
                }

                private int extractNumber(final String name) {
                    int i = 0;
                    try {
                        final int s = name.indexOf('_') + 1;
                        final int e = name.lastIndexOf('.');
                        final String number = name.substring(s, e);
                        i = Integer.parseInt(number);
                    } catch (final Exception e) {
                        i = 0; // if filename does not match the format then default to 0
                    }
                    return i;
                }
            });

            for (int i = 0; i < files.size(); i++) {
                results.add("");
            }

            final Object lock = new Object();

            for (int i = 0; i < files.size(); i++) {
                final File file = files.get(i);
                final Integer index = i;
                if (file.getName().endsWith(".succinct")) {
                    this.pool.execute(() -> {
                        final SuccinctLog succinctLog = new SuccinctLog(file.getAbsolutePath());
                        succinctLog.query(query, results, index, lock);
                    });
                }
            }
        }
        this.pool.shutdown();

        try {
            while (!this.pool.awaitTermination(50, TimeUnit.MILLISECONDS)) {
            }
            final StringBuilder sb = new StringBuilder();
            for (final String result : results) {
                sb.append(result);
            }
            System.out.println(sb.toString().trim());
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void query(final String query, final File input) {
        if (input.isFile() && input.getName().endsWith(".succinct")) {

            final SuccinctLog succinctLog = new SuccinctLog(input.getAbsolutePath());
            succinctLog.query(query);

        } else if (input.isDirectory()) {
            final ArrayList<File> files = this.getAllFiles(input);

            for (int i = 0; i < files.size(); i++) {
                final File file = files.get(i);
                if (file.getName().endsWith(".succinct")) {
                    this.pool.execute(() -> {
                        final SuccinctLog succinctLog = new SuccinctLog(file.getAbsolutePath());
                        succinctLog.query(query);
                    });
                }
            }
        }
        this.pool.shutdown();
    }

    public void count(final String query, final File input) {
        final long[] total = {0};

        if (input.isFile() && input.getName().endsWith(".succinct")) {
            final SuccinctLog succinctLog = new SuccinctLog(input.getAbsolutePath());
            succinctLog.count(query);

        } else if (input.isDirectory()) {
            final Object lock = new Object();

            for (final File file : this.getAllFiles(input)) {
                if (file.getName().endsWith(".succinct")) {
                    this.pool.execute(() -> {
                        final SuccinctLog succinctLog = new SuccinctLog(file.getAbsolutePath());
                        succinctLog.count(query, total, lock);
                    });
                }
            }
        }

        this.pool.shutdown();

        try {
            while (!this.pool.awaitTermination(50, TimeUnit.MILLISECONDS)) {
            }
            System.out.println(total[0]);
        } catch (final InterruptedException e) {
            e.printStackTrace();
        }
    }
    //TODO: create mini-terminal for littlelog to maintain highest-query-result-yiled succinct cache to improve query time

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

//    public void compressDirectory(final File directory) {
//        final File output = new File(directory + "_compressed/");
//        this.compressDirectory(directory, output);
//    }
//
//    private void compressDirectory(final File directory, final File outputDirectory) {
//        if (!directory.isDirectory()) {
//            System.out.println("usage: [input-directory] [output-directory]");
//            return;
//        }
//        System.out.println("input: " + directory.getAbsolutePath());
//
//        final File output = this.generateOutputDirectory(outputDirectory);
//        final ArrayList<File> files = this.getAllFiles(directory);
//
//        for (final File f : files) {
//            this.pool.execute(() -> {
//                try {
//                final String name = this.getFilePathWithoutExtension(f);
//                    Compressor.compress(f, new File(output.getCanonicalPath() + "/" + name + ".succinct"));
//                } catch (final IOException e) {
//                    e.printStackTrace();
//                }
//            });
//        }
//    }
}


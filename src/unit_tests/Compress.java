package unit_tests;

import littlelog.LittleLog;

import java.io.File;

public class Compress {
    public static void main(final String[] args) {
        if (args.length == 2) {
            final Integer numThreads = Integer.valueOf(args[0]);
            final LittleLog littleLog = new LittleLog(numThreads);

            final File input = new File(args[1]);

            littleLog.compressDirectory(input);
            littleLog.shutdown();

        } else if (args.length == 3) {
            final Integer numThreads = Integer.valueOf(args[0]);
            final LittleLog littleLog = new LittleLog(numThreads);

            final File input = new File(args[1]);
            final File output = new File(args[2]);

            littleLog.compressDirectory(input, output);
            littleLog.shutdown();

        } else {
            System.out.println("usage ./compress.sh [num-threads] [input-directory] <optional>[output-directory]");
        }
    }
}

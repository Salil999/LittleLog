package unit_tests;

import littlelog.LittleLog;

import java.io.File;

public class Compress {
    public static void main(final String[] args) {

        final LittleLog littleLog = new LittleLog();
        if (args.length == 1) {
            littleLog.compressDirectory(new File(args[0]));
        } else if (args.length == 2) {
            littleLog.compressDirectory(new File(args[0]), new File(args[1]));
        } else {
            System.out.println("usage: [input-directory] <optional>[output-directory]");
        }
        littleLog.shutdown();
    }
}

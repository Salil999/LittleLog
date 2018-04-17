package unit_tests;

import littlelog.LittleLog;

import java.io.File;

public class Compress {
    public static void main(final String[] args) {
        if (args.length == 1) {
            final LittleLog littleLog = new LittleLog();
            littleLog.compressDirectory(new File(args[0]));
            littleLog.shutdown();
        } else {
            System.out.println("Usage: [directory]");
        }
    }
}

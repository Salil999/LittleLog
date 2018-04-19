package unit_tests;

import littlelog.LittleLog;

import java.io.File;

public class Grep {
    public static void main(final String[] args) {
        if (args.length == 2) {
            final LittleLog littleLog = new LittleLog(100);
            littleLog.query(args[0], new File(args[1]));
            littleLog.shutdown();
        } else {
            System.out.println("usage: ./grep.sh [query] [file-directory]");
        }
    }
}

package littlelog;

import file.Sharder;
import java.io.IOException;
import java.io.File;

public class Main {
    public static void main(final String[] args) throws IOException{
//        if (args.length == 1) {
//            final LittleLog littleLog = new LittleLog();
////            littleLog.compressDirectory(args[0]);
//            littleLog.shutdown();
//        } else {
//            System.out.println("Parameters: [input-directory-to-compress]");
//        }

        if (args.length == 3) {
            final LittleLog littleLog = new LittleLog(10);

            if (args[0].equals("grep")) {
                littleLog.query(args[2], new File(args[1]));
            } else if (args[0].equals("count")) {
                littleLog.count(args[2], new File(args[1]));
            } else {
                System.out.println("Usage: ./[count-grep] [file-directory] [query]");
            }

            littleLog.shutdown();
        } else {
            System.out.println("Usage: ./[count-grep] [file-directory] [query]");
          return;
        }
      
        final File input = new File(args[0]);
        final File output = new File(args[1]);
        final Sharder sharder = new Sharder(input, output);

        sharder.shardDirectory();
    }
}
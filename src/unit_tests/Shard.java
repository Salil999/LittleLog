package unit_tests;

import littlelog.Sharder;

import java.io.File;

public class Shard {
    public static void main(final String[] args) {
        if (args.length == 2) {
            final double chunkSize = Double.valueOf(args[0]);
            final File input = new File(args[1]);
            Sharder.shardFile(input, chunkSize);
        } else if (args.length == 3) {
            final double chunkSize = Double.valueOf(args[0]);
            final File input = new File(args[1]);
            final File output = new File(args[2]);
            final Sharder sharder = new Sharder(chunkSize, input, output);
            sharder.shardDirectory();
        } else {
            System.out.println("usage ./shard.sh [chunk-size] [input-directory] [output-directory]");
            System.out.println("usage ./shard.sh [chunk-size] [input-file]");
        }
    }
}

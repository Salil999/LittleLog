package littlelog;

import file.Sharder;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(final String[] args) throws IOException {
        for (final String arg : args) {
            System.out.println(arg);
        }
        final File input = new File(args[0]);
        final File output = new File(args[1]);
        final Sharder sharder = new Sharder(input, output);

        sharder.shardDirectory();
    }
}

package littlelog;

import file.Sharder;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(final String[] args) throws IOException {
        if (args.length == 3) {
            final double chunkSize = Double.valueOf(args[0]);
            final File input = new File(args[1]);
            final File output = new File(args[2]);
            final Sharder sharder = new Sharder(chunkSize, input, output);
            sharder.shardDirectory();
        } else {
            System.out.println("Error: Format is ./run.sh [Chunk Size] [Input Directory] [Output Directory]");
        }
    }
}

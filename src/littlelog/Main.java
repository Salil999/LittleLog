package littlelog;

import succinct.Compressor;

public class Main {
    public static void main(final String[] args) {
//        final LittleLogConsumer consumer = new LittleLogConsumer();
//        consumer.run();
//
//        final LittleLog littleLog = new LittleLog("compressed_logs_200MB/");
//        littleLog.compressDirectory("200mb/");
//        littleLog.shutdown();
//
//        final Construct construct = new Construct();
//
//        try {
//            construct.readTextFile(new File("logs/ll_0.log"));
//        } catch (final IOException e) {
//            e.printStackTrace();
//        }

        final Compressor compressor = new Compressor();
        compressor.compress("/Users/rahulsurti/Desktop/cs525/LittleLog/src/logfiles/access.log", "/Users/rahulsurti/Desktop/cs525/LittleLog/src/compressed_logs/http.succinct");
    }
}

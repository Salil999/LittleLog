package littlelog;

import java.io.File;

public class Main {
    public static final int ONE_MB = 1000000;

    public Main() {
    }

    public static void main(final String[] args) {
        final Compressor compressor = new Compressor();
        compressor.compress(new File(args[0]), Main.ONE_MB * 10);
//        compressor.compress(args[0], args[1], Main.ONE_MB * 10);

//        final ArrayList<Integer> array = compressor.findSplitPoints(args[0], Main.ONE_MB * 10);
//        System.out.println(array.toString());


//        final ExecutorService pool = Executors.newFixedThreadPool(3);
//        final int shardSize = 10 * Main.ONE_MB;

//        SuccinctLog succinctLog = new SuccinctLog("access1.succinct");
//        System.out.print(succinctLog.extract(new Long(0), shardSize));
//        succinctLog = new SuccinctLog("access2.succinct");
//        System.out.print(succinctLog.extract(new Long(0), shardSize));
//        succinctLog = new SuccinctLog("access3.succinct");
//        System.out.print(succinctLog.extract(new Long(0), shardSize));
//
//
//        Main.compressShard(pool, args[0], 0, (int) shardSize, "access1.succinct");
//        Main.compressShard(pool, args[0], shardSize, (int) shardSize, "access2.succinct");
//        Main.compressShard(pool, args[0], shardSize + shardSize, (int) shardSize, "access3.succinct");

//        pool.shutdown();



    }
    //TODO: if grep over 100 lines, dont output to terminal, specify filepath, make line number tunable param
    //TODO: figure out way to extract data from thread instead of printing to terminal, use vars to return, possibly on .shutdown()

    //TODO: make grep correct order using parameter passing and string builder

//    public static ByteBuffer compressShard(final FileChannel fileChannel, final Integer offset, final Integer length) {
//        try {
//            final ByteBuffer buf = fileChannel.map(FileChannel.MapMode.READ_ONLY, offset, length);
//            final Compressor compressor = new Compressor();
//            compressor.compressBuffer(buf, "compressed_shard/");
//        } catch (final IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
}

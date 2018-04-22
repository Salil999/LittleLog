package littlelog;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Main {
    public static final int ONE_MB = 1000000;

    public Main() {
    }

    public static void main(final String[] args) {

        final ExecutorService pool = Executors.newFixedThreadPool(3);
        final int shardSize = 10 * Main.ONE_MB;

//        SuccinctLog succinctLog = new SuccinctLog("access1.succinct");
//        System.out.print(succinctLog.extract(new Long(0), shardSize));
//        succinctLog = new SuccinctLog("access2.succinct");
//        System.out.print(succinctLog.extract(new Long(0), shardSize));
//        succinctLog = new SuccinctLog("access3.succinct");
//        System.out.print(succinctLog.extract(new Long(0), shardSize));
//
//
        Main.compressShard(pool, args[0], 0, (int) shardSize, "access1.succinct");
        Main.compressShard(pool, args[0], shardSize, (int) shardSize, "access2.succinct");
        Main.compressShard(pool, args[0], shardSize + shardSize, (int) shardSize, "access3.succinct");

        pool.shutdown();



    }
    //TODO: if grep over 100 lines, dont output to terminal, specify filepath, make line number tunable param
    //TODO: figure out way to extract data from thread instead of printing to terminal, use vars to return, possibly on .shutdown()


    public static void compressShard(final ExecutorService pool, final String filename,
                                     final Integer position, final Integer size, final String outputPath) {
        pool.execute(() -> {
            try {
                final RandomAccessFile file = new RandomAccessFile(filename, "r");
                file.seek(position);
                final byte[] bytes = new byte[size];
                file.read(bytes);
                file.close();
//                System.out.println(new String(bytes));
                final Compressor compressor = new Compressor();
                compressor.compressBuffer(bytes, outputPath);
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
    }

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

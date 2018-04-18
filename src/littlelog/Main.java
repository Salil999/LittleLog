package littlelog;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static littlelog.Sharder.ONE_MB;

public class Main {
    public static void main(final String[] args) {

        try {
            final File file = new File(args[0]);
            final FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel();
            final ExecutorService pool = Executors.newFixedThreadPool(100);
            final double shardSize = 10.0 * ONE_MB;


            Main.compressShard(pool, fileChannel, 0, (int) shardSize);

            pool.shutdown();
        } catch (final IOException e) {
            e.printStackTrace();
        }


//        final LittleLog littleLog = new LittleLog();
//
////        final String a = littleLog.getFilenameNoExtension(new File(args[0]));
////        System.out.println(a);
//        final File file = new File(args[0]);
//        try {
//            Main.readTextFile(file);
////            System.out.println(file.getCanonicalPath());
//        } catch (final IOException e) {
//            System.out.println("fail");
//        }

//        FileInputStream inputStream = null;
//        Scanner sc = null;
//        try {
//            inputStream = new FileInputStream(args[0]);
//            sc = new Scanner(inputStream, "UTF-8");
//            while (sc.hasNextLine()) {
//                final String line = sc.nextLine();
//                // System.out.println(line);
//            }
//            // note that Scanner suppresses exceptions
//            if (sc.ioException() != null) {
//                throw sc.ioException();
//            }
//        } catch (final Exception e) {
//            e.printStackTrace();
//        } finally {
//            if (inputStream != null) {
//                inputStream.close();
//            }
//            if (sc != null) {
//                sc.close();
//            }
//        }
    }
//
//    private static char[] readTextFile(final File file) throws IOException {
//        final char[] fileData = new char[(int) file.length()];
//        final FileReader fr = new FileReader(file);
//        fr.read(fileData);
//        fr.close();
//        return fileData;
//    }

    public static ByteBuffer compressShard(final ExecutorService pool, final FileChannel fileChannel, final Integer offset, final Integer length) {
        pool.execute(() -> {
            try {
                final ByteBuffer buf = fileChannel.map(FileChannel.MapMode.READ_ONLY, offset, length);
                final Compressor compressor = new Compressor();
                compressor.compressBuffer(buf, "compressed_shard/");
            } catch (final IOException e) {
                e.printStackTrace();
            }
        });
        return null;
    }

    public static ByteBuffer compressShard(final FileChannel fileChannel, final Integer offset, final Integer length) {
        try {
            final ByteBuffer buf = fileChannel.map(FileChannel.MapMode.READ_ONLY, offset, length);
            final Compressor compressor = new Compressor();
            compressor.compressBuffer(buf, "compressed_shard/");
        } catch (final IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

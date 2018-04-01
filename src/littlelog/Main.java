package littlelog;

public class Main {
    public static void main(final String[] args) {
//        final LittleLogConsumer consumer = new LittleLogConsumer();
//        consumer.run();

        final LittleLog littleLog = new LittleLog("compressed_logs/");
        littleLog.compressDirectory("logs/");
        littleLog.shutdown();
    }
}

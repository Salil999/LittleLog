package littlelog;

public class Main {
    public static void main(final String[] args) {
        final LittleLog littleLog = new LittleLog();
        littleLog.compressDirectory("/Users/rahulsurti/Desktop/cs525/LittleLog/src/logfiles/");
        littleLog.shutdown();
    }
}

package littlelog;

public class Main {
    public static void main(final String[] args) {
        if (args.length == 1) {
            final LittleLog littleLog = new LittleLog();
            littleLog.compressDirectory(args[0]);
            littleLog.shutdown();
        } else {
            System.out.println("Parameters: [input-directory-to-compress]");
        }
    }
}

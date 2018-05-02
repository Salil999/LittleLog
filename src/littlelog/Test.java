package littlelog;

import java.io.File;

public class Test {
    public static void main(final String[] args) {
        final File file = new File("logfiles/access_compressed/access_01.succinct");

        final SuccinctLog succinctLog = new SuccinctLog(file.getAbsolutePath());
//        succinctLog.findNewlines();
//        final LittleLog littleLog = new LittleLog();
//        littleLog.query("\n", file);

    }
}


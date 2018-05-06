package littlelog;

import java.io.File;

public class Test {
    public static void main(final String[] args) {
        final File file = new File("logfiles/access/");

//        final SuccinctLog succinctLog = new SuccinctLog(file.getAbsolutePath());
//        succinctLog.findNewlines();
//        final LittleLog littleLog = new LittleLog();
//        littleLog.query("\n", file);

        final LittleLog littleLog = new LittleLog(100);
        littleLog.query("109.169.248.247", file, 100);

    }
}


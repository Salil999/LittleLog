package littlelog;

import java.io.File;
import java.io.IOException;

public class Main {
    public static void main(final String[] args) {
        final LittleLog littleLog = new LittleLog();

//        final String a = littleLog.getFilenameNoExtension(new File(args[0]));
//        System.out.println(a);
        final File file = new File(args[0]);
        try {
            System.out.println(file.getCanonicalPath());
        } catch (final IOException e) {
            System.out.println("fail");
        }
    }
}
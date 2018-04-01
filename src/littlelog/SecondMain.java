package littlelog;

public class SecondMain {

	public static void main(final String[] args) {
		final LittleLog littleLog = new LittleLog("compressed_logs/");
		final String searching = " 200 ";
//		littleLog.compress("logfiles/read.log");
		littleLog.regex(searching);
//		littleLog.count(searching);
//		littleLog.regex(searching);
		littleLog.shutdown();
	}
}

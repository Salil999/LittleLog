package littlelog;

import kafka.LittleLogConsumer;

public class Main {
	public static void main(final String[] args) {
		final LittleLogConsumer consumer = new LittleLogConsumer();
		new Thread(consumer).start();
	}
}

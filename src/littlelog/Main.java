package littlelog;

import kafka.LittleLogConsumer;
import succinct.Succinct;

public class Main {
    public static void main(final String[] args) {
        final LittleLogConsumer consumer = new LittleLogConsumer();
        new Thread(consumer).start();
    }

    public static void succinctTest() {

        final String filename = "access";
        final String extension = ".log";

        final String inputFile = "files/" + filename + extension;
        final String outputFile = "files/" + filename + ".succinct";

        long startTime = System.currentTimeMillis();
        final Succinct succinct = new Succinct(inputFile);
        long endTime = System.currentTimeMillis();
        System.out.println("File Read Time: " + Long.toString(endTime - startTime) + " ms");

        startTime = System.currentTimeMillis();
        succinct.writeToFile(outputFile);
        endTime = System.currentTimeMillis();
        System.out.println("Succinct File Write Time: " + Long.toString(endTime - startTime) + " ms");


        startTime = System.currentTimeMillis();
        succinct.readFromFile(outputFile);
        endTime = System.currentTimeMillis();
        System.out.println("Succinct File Read Time: " + Long.toString(endTime - startTime) + " ms");


        startTime = System.currentTimeMillis();
        succinct.extract(0, succinct.getFileSize());
        endTime = System.currentTimeMillis();
        System.out.println("Succinct File Extraction Time: " + Long.toString(endTime - startTime) + " ms");
    }
}
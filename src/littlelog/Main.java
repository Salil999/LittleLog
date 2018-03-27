package littlelog;

import succinct.Succinct;

public class Main {
    public static void main(final String[] args) {
        final Succinct succinct = new Succinct("test.txt");
        succinct.writeToFile("test.succinct");
        succinct.readFromFile("test.succinct");
        succinct.extract(0, succinct.getFileSize());
    }
}

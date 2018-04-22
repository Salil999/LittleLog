package littlelog;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    public static final int ONE_MB = 1000000;

    public Main() {
    }

    public static void main(final String[] args) {
        final Options options = new Options();
        final OptionGroup optionGroup = new OptionGroup();

        optionGroup.addOption(Option.builder("g")
                .longOpt("grep")
                .required(false)
                .type(String.class)
                .hasArg()
                .argName("regex-string")
                .build());

        optionGroup.addOption(Option.builder("ct")
                .longOpt("count")
                .required(false)
                .type(String.class)
                .hasArg()
                .argName("regex-string")
                .build());

        optionGroup.addOption(Option.builder("c")
                .longOpt("compress")
                .required(false)
                .type(Boolean.TYPE)
                .build());

        optionGroup.setRequired(true);
        options.addOptionGroup(optionGroup);

        options.addOption(Option.builder("i")
                .longOpt("input")
                .required(true)
                .type(String.class)
                .desc("input path")
                .hasArg()
                .argName("path")
                .build());

        options.addOption(Option.builder("o")
                .longOpt("output")
                .required(false)
                .type(String.class)
                .desc("output path")
                .hasArg()
                .argName("path")
                .build());

        options.addOption(Option.builder("s")
                .longOpt("shardSize")
                .required(false)
                .type(String.class)
                .desc("shard size in MB")
                .hasArg()
                .argName("int")
                .build());


        final CommandLineParser parser = new DefaultParser();
        final HelpFormatter formatter = new HelpFormatter();
        final CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (final ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("./littlelog", options);

            System.exit(1);
            return;
        }

        final Boolean compress = cmd.hasOption("c");
        final Boolean grep = cmd.hasOption("g");
        final Boolean count = cmd.hasOption("ct");

        if (compress) {
            Main.compress(cmd);
        } else if (grep) {
            Main.grep(cmd);
        } else if (count) {
            Main.count(cmd);
        }


//        final Compressor compressor = new Compressor();
//        compressor.compress(new File(args[0]), Main.ONE_MB * 10);
//        compressor.compress(args[0], args[1], Main.ONE_MB * 10);

//        final ArrayList<Integer> array = compressor.findSplitPoints(args[0], Main.ONE_MB * 10);
//        System.out.println(array.toString());


//        final ExecutorService pool = Executors.newFixedThreadPool(3);
//        final int shardSize = 10 * Main.ONE_MB;

//        SuccinctLog succinctLog = new SuccinctLog("access1.succinct");
//        System.out.print(succinctLog.extract(new Long(0), shardSize));
//        succinctLog = new SuccinctLog("access2.succinct");
//        System.out.print(succinctLog.extract(new Long(0), shardSize));
//        succinctLog = new SuccinctLog("access3.succinct");
//        System.out.print(succinctLog.extract(new Long(0), shardSize));
//
//
//        Main.compressShard(pool, args[0], 0, (int) shardSize, "access1.succinct");
//        Main.compressShard(pool, args[0], shardSize, (int) shardSize, "access2.succinct");
//        Main.compressShard(pool, args[0], shardSize + shardSize, (int) shardSize, "access3.succinct");

//        pool.shutdown();


    }
    //TODO: if grep over 100 lines, dont output to terminal, specify filepath, make line number tunable param
    //TODO: figure out way to extract data from thread instead of printing to terminal, use vars to return, possibly on .shutdown()

    //TODO: make grep correct order using parameter passing and string builder

//    public static ByteBuffer compressShard(final FileChannel fileChannel, final Integer offset, final Integer length) {
//        try {
//            final ByteBuffer buf = fileChannel.map(FileChannel.MapMode.READ_ONLY, offset, length);
//            final Compressor compressor = new Compressor();
//            compressor.compressBuffer(buf, "compressed_shard/");
//        } catch (final IOException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    private static void compress(final CommandLine cmd) {
        final File input;
        try {
            input = new File(cmd.getOptionValue("i"));
            if (!input.isFile()) {
                throw new FileNotFoundException();
            }
        } catch (final FileNotFoundException e) {
            System.out.println("ERROR: input must be valid file");
            return;
        }

        final Boolean hasOutput = cmd.hasOption("o");
        Integer shardSize = null;

        if (cmd.hasOption("s")) {
            try {
                shardSize = Integer.parseInt((String) cmd.getParsedOptionValue("s"));
            } catch (final Exception e) {
                System.out.println("ERROR: shardSize must be integer");
                return;
            }
        }

        System.out.println("compress");
        if (hasOutput) {
            final File output = new File(cmd.getOptionValue("o"));
            if (shardSize != null) {

                if (output.getName().contains(".")) {
                    System.out.println("ERROR: output must be directory if shard size is given");
                    return;
                }

                Compressor.compress(input, output, shardSize * Main.ONE_MB);

            } else {

                if (!output.getName().endsWith(".succinct")) {
                    System.out.println("ERROR: output must have .succinct extension if shard size is not given");
                    return;
                }

                Compressor.compress(input, output);
            }

        } else {
            if (shardSize != null) {
                Compressor.compress(input, shardSize * Main.ONE_MB);
            } else {
                Compressor.compress(input);
            }
        }
    }

    private static void grep(final CommandLine cmd) {
        final File input;
        try {
            input = new File(cmd.getOptionValue("i"));
            if (!input.isFile()) {
                throw new FileNotFoundException();
            }
        } catch (final FileNotFoundException e) {
            System.out.println("ERROR: input must be valid file");
        }
        final Boolean hasOutput = cmd.hasOption("o");

        System.out.println("grep");
        if (hasOutput) {
            final File output = new File(cmd.getOptionValue("o"));
            if (output.isDirectory()) {
//                    Compressor.compress(input, output);
            }
        } else {
//                Compressor.compress(input);
        }
    }

    private static void count(final CommandLine cmd) {
        final File input = new File(cmd.getOptionValue("i"));
        final Boolean hasOutput = cmd.hasOption("o");

        if (!input.isFile()) {
            System.out.println("ERROR: input must be valid file");
        }

        System.out.println("count");
        if (hasOutput) {
            final File output = new File(cmd.getOptionValue("o"));
            if (output.isDirectory()) {
//                    Compressor.compress(input, output);
            }
        } else {
//                Compressor.compress(input);
        }
    }
}


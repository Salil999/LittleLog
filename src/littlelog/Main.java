package littlelog;

import org.apache.commons.cli.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class Main {
    private static final int ONE_MB = 1000000;

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

        optionGroup.addOption(Option.builder("n")
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


        options.addOption(Option.builder("t")
                .longOpt("threads")
                .required(false)
                .type(String.class)
                .desc("number of threads")
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

//        System.out.println("i " + cmd.getOptionValue("i"));
//        System.out.println("o " + cmd.getOptionValue("o"));
//        System.out.println("c " + cmd.getOptionValue("c"));
//        System.out.println("g " + cmd.getOptionValue("g"));
//        System.out.println("n " + cmd.getOptionValue("n"));
//        System.out.println("t " + cmd.getOptionValue("t"));
//        System.out.println("s " + cmd.getOptionValue("s"));

        final Boolean compress = cmd.hasOption("c");

        if (compress) {
            Main.compress(cmd);
        } else {
            Main.search(cmd);
        }
    }

    //TODO: if grep over 100 lines, dont output to terminal, specify filepath, make line number tunable param
    //TODO: figure out way to extract data from thread instead of printing to terminal, use vars to return, possibly on .shutdown()
    //TODO: make grep correct order using parameter passing and string builder
    //TODO: do not allow to search for "\n" newlines

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
        Integer numThreads = null;

        if (cmd.hasOption("s") || cmd.hasOption("t")) {
            try {
                shardSize = Integer.parseInt((String) cmd.getParsedOptionValue("s"));
                numThreads = Integer.parseInt((String) cmd.getParsedOptionValue("t"));
            } catch (final Exception e) {
                System.out.println("ERROR: shardSize and numthreads are both required and must be integers");
                return;
            }
        }

        if (hasOutput) {
            final File output = new File(cmd.getOptionValue("o"));
            if (shardSize != null) {

                if (output.getName().contains(".")) {
                    System.out.println("ERROR: output must be directory if shard size is given");
                    return;
                }

                Compressor.compress(input, output, shardSize * Main.ONE_MB, numThreads);

            } else {

                if (!output.getName().endsWith(".succinct")) {
                    System.out.println("ERROR: output must have .succinct extension if shard size is not given");
                    return;
                }

                Compressor.compress(input, output);
            }

        } else {
            if (shardSize != null) {
                Compressor.compress(input, shardSize * Main.ONE_MB, numThreads);
            } else {
                Compressor.compress(input);
            }
        }
    }

    private static void search(final CommandLine cmd) {
        final File input;
        try {
            input = new File(cmd.getOptionValue("i"));
            if (!(input.isFile() || input.isDirectory())) {
                throw new FileNotFoundException();
            }
        } catch (final FileNotFoundException e) {
            System.out.println("ERROR: input must be valid file or directory");
            return;
        }

        Integer numThreads = null;
        try {
            numThreads = Integer.parseInt((String) cmd.getParsedOptionValue("t"));
        } catch (final Exception e) {
            System.out.println("ERROR: numthreads required for search");
            return;
        }

        final Boolean hasOutput = cmd.hasOption("o");

        if (hasOutput) {
            final File output = new File(cmd.getOptionValue("o"));
            if (!output.getName().contains(".")) {
                System.out.println("ERROR: output must be file");
                return;
            }
            if (output.exists()) {
                System.out.println("ERROR: output must be new file");
                return;
            }
            try {
                output.createNewFile();
            } catch (final IOException e) {
                e.printStackTrace();
                return;
            }
            System.out.println(output.getAbsolutePath());
        } else {
            final LittleLog littleLog = new LittleLog(numThreads);
            if (cmd.hasOption("g")) {
                final String query = cmd.getOptionValue("g");
                littleLog.query(query, input);
            } else if (cmd.hasOption("n")) {
                final String query = cmd.getOptionValue("n");
                littleLog.count(query, input);
            }
        }
    }
}


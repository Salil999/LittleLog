package littlelog;

import succinct.SuccinctLog;

public class SuccinctTask implements Runnable {
    private final SuccinctTaskType succinctTaskType;
    private final String inputDirectory;
    private final String outputDirectory;
    private final String filename;
    private final String extension;
    private String query;


    public SuccinctTask(final SuccinctTaskType succinctTaskType, final String filepath, final String outputDirectory) throws Exception {
        this.succinctTaskType = succinctTaskType;

        String[] split = filepath.split("/");
        if (split.length != 2) {
            throw new Exception("couldn't parse filepath " + filepath);
        }
        this.inputDirectory = split[0] + '/';

        split = split[1].split("\\.");
        if (split.length != 2) {
            throw new Exception("couldn't parse filepath " + filepath);
        }
        this.filename = split[0];
        this.extension = split[1];
        this.outputDirectory = outputDirectory;
    }

    private SuccinctTask(final Builder b) {
        this.succinctTaskType = b.succinctTaskType;
        this.inputDirectory = b.inputDirectory;
        this.filename = b.filename;
        this.extension = b.extension;
        this.outputDirectory = b.outputDirectory;
        this.query = b.query;
    }

    @Override
    public void run() {
        final SuccinctLog succinctLog;
        switch (this.succinctTaskType) {
            case COMPRESS:
                succinctLog = new SuccinctLog(this.filename, this.extension, this.inputDirectory, this.outputDirectory);
                succinctLog.compressFile();
                break;
            case COUNT:
                succinctLog = new SuccinctLog(this.filename, this.outputDirectory);
                succinctLog.count(this.query);
                break;
            case REGEX:
                succinctLog = new SuccinctLog(this.filename, this.outputDirectory);
                succinctLog.regex(this.query);
                break;
            case SEARCH:
                succinctLog = new SuccinctLog(this.filename, this.outputDirectory);
                succinctLog.search(this.query);
                break;
            default:
                System.out.println("SuccinctTaskType: " + this.succinctTaskType + " not valid");
                break;
        }
    }

    public static class Builder {
        private String query;
        private String filename;
        private String extension;
        private SuccinctTaskType succinctTaskType;
        private String inputDirectory;
        private String outputDirectory;

        public Builder succinctTaskType(final SuccinctTaskType succinctTaskType) {
            this.succinctTaskType = succinctTaskType;
            return this;
        }

        public Builder filepath(final String filepath) throws Exception {
            String[] split = filepath.split("/");
            if (split.length != 2) {
                throw new Exception("couldn't parse filepath " + filepath);
            }
            this.inputDirectory = split[0] + '/';

            split = split[1].split("\\.");
            if (split.length != 2) {
                throw new Exception("couldn't parse filepath " + filepath);
            }
            this.filename = split[0];
            this.extension = split[1];
            return this;
        }

        public Builder outputDirectory(final String outputDirectory) {
            this.outputDirectory = outputDirectory;
            return this;
        }

        public Builder query(final String query) {
            this.query = query;
            return this;
        }

        public SuccinctTask build() {
            return new SuccinctTask(this);
        }


    }
}

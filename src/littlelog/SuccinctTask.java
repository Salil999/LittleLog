package littlelog;

public class SuccinctTask implements Runnable {
    private final SuccinctTaskType succinctTaskType;
    private final String inputFilepath;
    private final String outputFilepath;
    private final String query;

    public SuccinctTask(final SuccinctTaskType succinctTaskType, final String inputFilepath, final String outputFilepath, final String query) {
        this.succinctTaskType = succinctTaskType;
        this.inputFilepath = inputFilepath;
        this.outputFilepath = outputFilepath;
        this.query = query;
    }

    @Override
    public void run() {
        final SuccinctLog succinctLog;
        switch (this.succinctTaskType) {
            case COUNT:
                succinctLog = new SuccinctLog(this.inputFilepath);
                succinctLog.count(this.query);
                break;
            case QUERY:
                succinctLog = new SuccinctLog(this.inputFilepath);
                succinctLog.query(this.query);
                break;
            default:
                System.out.println("SuccinctTaskType: " + this.succinctTaskType + " not valid");
                break;
        }
    }
}

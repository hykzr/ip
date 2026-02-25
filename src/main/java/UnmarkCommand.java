public class UnmarkCommand extends Command {
    private final String indexArgs;

    public UnmarkCommand(String indexArgs) {
        super(false);
        this.indexArgs = indexArgs;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CommandException {
        int taskIndex = Parser.parseTaskIndex(indexArgs, tasks.getTaskCount());
        tasks.unmarkTask(taskIndex);
    }
}

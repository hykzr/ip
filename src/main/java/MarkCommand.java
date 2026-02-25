public class MarkCommand extends Command {
    private final String indexArgs;

    public MarkCommand(String indexArgs) {
        super(false);
        this.indexArgs = indexArgs;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CommandException {
        int taskIndex = Parser.parseTaskIndex(indexArgs, tasks.getTaskCount());
        tasks.markTask(taskIndex);
    }
}

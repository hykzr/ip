public class DeleteCommand extends Command {
    private final String indexArgs;

    public DeleteCommand(String indexArgs) {
        super(false);
        this.indexArgs = indexArgs;
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CommandException {
        int taskIndex = Parser.parseTaskIndex(indexArgs, tasks.getTaskCount());
        tasks.deleteTask(taskIndex);
    }
}

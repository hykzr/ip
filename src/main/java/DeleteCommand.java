/**
 * Represents a command to delete a task from the task list.
 */
public class DeleteCommand extends Command {
    private final String indexArgs;

    /**
     * Constructs a DeleteCommand with the arguments specifying the task index.
     *
     * @param indexArgs The string argument containing the task index.
     */
    public DeleteCommand(String indexArgs) {
        super(false);
        this.indexArgs = indexArgs;
    }

    /**
     * Executes the delete command by parsing the index and removing the task.
     *
     * @param tasks   The task list to operate on.
     * @param ui      The UI to use for user interaction.
     * @param storage The storage (unused).
     * @throws CommandException If the index is invalid.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CommandException {
        int taskIndex = Parser.parseTaskIndex(indexArgs, tasks.getTaskCount());
        tasks.deleteTask(taskIndex);
    }
}

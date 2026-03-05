/**
 * Represents a command to mark a task as not done.
 */
public class UnmarkCommand extends Command {
    private final String indexArgs;

    /**
     * Constructs an UnmarkCommand with the arguments specifying the task index.
     *
     * @param indexArgs The string argument containing the task index.
     */
    public UnmarkCommand(String indexArgs) {
        super(false);
        this.indexArgs = indexArgs;
    }

    /**
     * Executes the unmark command by parsing the index and marking the task as not done.
     *
     * @param tasks   The task list to operate on.
     * @param ui      The UI (unused).
     * @param storage Unused.
     * @throws CommandException If the index is invalid.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws CommandException {
        int taskIndex = Parser.parseTaskIndex(indexArgs, tasks.getTaskCount());
        tasks.unmarkTask(taskIndex);
    }
}

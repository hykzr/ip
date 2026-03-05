/**
 * Represents a command to list all tasks in the task list.
 */
public class ListCommand extends Command {
    /**
     * Constructs a ListCommand.
     */
    public ListCommand() {
        super(false);
    }

    /**
     * Executes the list command by displaying all tasks in the task list.
     *
     * @param tasks   The task list to display.
     * @param ui      The UI to display the list.
     * @param storage Unused.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.printTaskList();
    }
}

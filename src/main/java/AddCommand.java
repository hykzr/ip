/**
 * Represents a command to add a task to the task list.
 */
public class AddCommand extends Command {
    private final Task task;

    /**
     * Constructs an AddCommand with the task to be added.
     *
     * @param task The task to add.
     */
    public AddCommand(Task task) {
        super(false);
        this.task = task;
    }

    /**
     * Executes the add command by adding the task to the task list.
     *
     * @param tasks   The task list to operate on.
     * @param ui      The UI to use for user interaction.
     * @param storage The storage (unused in this command).
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.addTask(task);
    }
}

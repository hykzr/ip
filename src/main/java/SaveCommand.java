/**
 * Represents a command to save the current tasks to storage.
 */
public class SaveCommand extends Command {
    /**
     * Constructs a SaveCommand.
     */
    public SaveCommand() {
        super(false);
    }

    /**
     * Executes the save command by saving the current task list to storage.
     *
     * @param tasks   The task list to save.
     * @param ui      The UI (unused).
     * @param storage The storage to save data to.
     * @throws StorageError If an error occurs during saving.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws StorageError {
        storage.save(tasks.getTasks());
    }
}

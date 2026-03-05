import java.util.ArrayList;

/**
 * Represents a command to manually load tasks from storage.
 */
public class LoadCommand extends Command {
    /**
     * Constructs a LoadCommand.
     */
    public LoadCommand() {
        super(false);
    }

    /**
     * Executes the load command by loading tasks from storage and replacing the current task list.
     *
     * @param tasks   The task list to update.
     * @param ui      The UI (unused).
     * @param storage The storage to load data from.
     * @throws StorageError If an error occurs during loading.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws StorageError {
        ArrayList<Task> loadedTasks = storage.load();
        tasks.replaceTasks(loadedTasks);
    }
}

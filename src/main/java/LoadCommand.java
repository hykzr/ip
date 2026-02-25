import java.util.ArrayList;

public class LoadCommand extends Command {
    public LoadCommand() {
        super(false);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws StorageError {
        ArrayList<Task> loadedTasks = storage.load();
        tasks.replaceTasks(loadedTasks);
    }
}

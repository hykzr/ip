public class SaveCommand extends Command {
    public SaveCommand() {
        super(false);
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws StorageError {
        storage.save(tasks.getTasks());
    }
}

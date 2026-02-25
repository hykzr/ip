public abstract class Command {
    private final boolean isExit;

    protected Command(boolean isExit) {
        this.isExit = isExit;
    }

    public boolean isExit() {
        return isExit;
    }

    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws CommandException, StorageError;
}

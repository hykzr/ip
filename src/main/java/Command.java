/**
 * Represents an abstract command that can be executed by the application.
 * All specific commands retain an isExit flag to indicate if the application should terminate.
 */
public abstract class Command {
    private final boolean isExit;

    /**
     * Constructs a Command with the specified exit status.
     *
     * @param isExit True if the command should cause the application to exit, false otherwise.
     */
    protected Command(boolean isExit) {
        this.isExit = isExit;
    }

    /**
     * Checks if the command triggers the application to exit.
     *
     * @return True if the command is an exit command, false otherwise.
     */
    public boolean isExit() {
        return isExit;
    }

    /**
     * Executes the command.
     *
     * @param tasks   The task list to operate on.
     * @param ui      The UI to use for user interaction.
     * @param storage The storage to use for saving/loading data.
     * @throws CommandException If an error occurs during execution.
     * @throws StorageError     If an error occurs during storage operations.
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws CommandException, StorageError;
}

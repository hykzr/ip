/**
 * Represents a command to exit the application.
 */
public class ExitCommand extends Command {
    /**
     * Constructs an ExitCommand.
     * Sets the isExit flag to true.
     */
    public ExitCommand() {
        super(true);
    }

    /**
     * Executes the exit command by displaying a goodbye message.
     *
     * @param tasks   Unused.
     * @param ui      The UI to display the goodbye message.
     * @param storage Unused.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showGoodbye();
    }
}

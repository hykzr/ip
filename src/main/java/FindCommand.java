/**
 * Represents a command to find tasks matching a keyword.
 */
public class FindCommand extends Command {
    private final String keyword;

    /**
     * Constructs a FindCommand with the specified keyword.
     *
     * @param keyword The keyword to search for.
     * @throws CommandFormatException If the keyword is empty or null.
     */
    public FindCommand(String keyword) throws CommandFormatException {
        super(false);
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new CommandFormatException("Please provide a keyword to search for.");
        }
        this.keyword = keyword.trim();
    }

    /**
     * Executes the find command by searching for tasks in the task list.
     *
     * @param tasks   The task list to search.
     * @param ui      The UI to display the results.
     * @param storage Unused.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.findTasks(keyword);
    }
}

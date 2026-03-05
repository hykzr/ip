public class FindCommand extends Command {
    private final String keyword;

    public FindCommand(String keyword) throws CommandFormatException {
        super(false);
        if (keyword == null || keyword.trim().isEmpty()) {
            throw new CommandFormatException("Please provide a keyword to search for.");
        }
        this.keyword = keyword.trim();
    }

    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        tasks.findTasks(keyword);
    }
}

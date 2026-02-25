public class Parser {
    public static final String COMMAND_TODO = "todo";
    public static final String COMMAND_DEADLINE = "deadline";
    public static final String COMMAND_EVENT = "event";
    public static final String COMMAND_MARK = "mark";
    public static final String COMMAND_UNMARK = "unmark";
    public static final String COMMAND_DELETE = "delete";
    public static final String COMMAND_LIST = "list";
    public static final String COMMAND_SAVE = "save";
    public static final String COMMAND_LOAD = "load";
    public static final String COMMAND_BYE = "bye";

    private static final String ERROR_TODO_EMPTY = "A todo needs a description.";
    private static final String ERROR_DEADLINE_FORMAT = "A deadline needs a description and '/by <time>'.";
    private static final String ERROR_EVENT_FORMAT = "An event needs a description, '/from <start>' and '/to <end>'.";
    private static final String ERROR_TASK_NUMBER_MISSING = "Please provide a task number.";
    private static final String ERROR_TASK_NUMBER_RANGE = "That task number is out of range.";
    private static final String ERROR_TASK_NUMBER_NOT_NUMBER = "Task numbers must be whole numbers.";
    private static final String ERROR_UNKNOWN_COMMAND = "Sorry, I do not recognize that command.";
    private static final String ERROR_EMPTY_COMMAND = "Please enter a command.";

    private static final String STORAGE_TODO = "T";
    private static final String STORAGE_DEADLINE = "D";
    private static final String STORAGE_EVENT = "E";

    public static String getFirstWord(String input) {
        int firstSpaceIndex = input.indexOf(' ');
        return firstSpaceIndex == -1 ? input : input.substring(0, firstSpaceIndex);
    }

    public static String getRestOfInput(String input) {
        int firstSpaceIndex = input.indexOf(' ');
        return firstSpaceIndex == -1 ? "" : input.substring(firstSpaceIndex + 1);
    }

    public static Task parseTask(String commandWord, String args) throws CommandException {
        return switch (commandWord) {
            case COMMAND_TODO -> parseTodo(args);
            case COMMAND_DEADLINE -> parseDeadline(args);
            case COMMAND_EVENT -> parseEvent(args);
            default -> throw new CommandFormatException(ERROR_UNKNOWN_COMMAND);
        };
    }

    private static Task parseTodo(String args) throws CommandException {
        if (args.trim().isEmpty()) {
            throw new CommandFormatException(ERROR_TODO_EMPTY);
        }
        return new ToDo(args.trim());
    }

    private static Task parseDeadline(String args) throws CommandException {
        if (args.trim().isEmpty()) {
            throw new CommandFormatException(ERROR_DEADLINE_FORMAT);
        }
        String[] parts = args.split("/by", 2);
        if (parts.length < 2) {
            throw new CommandFormatException(ERROR_DEADLINE_FORMAT);
        }
        String description = parts[0].trim();
        String by = parts[1].trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new CommandFormatException(ERROR_DEADLINE_FORMAT);
        }
        return new Deadline(description, by);
    }

    private static Task parseEvent(String args) throws CommandException {
        if (args.trim().isEmpty()) {
            throw new CommandFormatException(ERROR_EVENT_FORMAT);
        }
        String[] parts = args.split("/from|/to", 3);
        if (parts.length < 3) {
            throw new CommandFormatException(ERROR_EVENT_FORMAT);
        }
        String description = parts[0].trim();
        String from = parts[1].trim();
        String to = parts[2].trim();
        if (description.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw new CommandFormatException(ERROR_EVENT_FORMAT);
        }
        return new Event(description, from, to);
    }

    public static int parseTaskIndex(String args, int taskCount) throws CommandException {
        String trimmedInput = args.trim();
        if (trimmedInput.isEmpty()) {
            throw new CommandFormatException(ERROR_TASK_NUMBER_MISSING);
        }
        try {
            int taskIndex = Integer.parseInt(trimmedInput) - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                throw new CommandRangeException(ERROR_TASK_NUMBER_RANGE);
            }
            return taskIndex;
        } catch (NumberFormatException e) {
            throw new CommandFormatException(ERROR_TASK_NUMBER_NOT_NUMBER);
        }
    }

    public static void handleUnknownCommand(String input) throws CommandException {
        if (input.isBlank()) {
            throw new CommandException(ERROR_EMPTY_COMMAND);
        }
        throw new CommandException(ERROR_UNKNOWN_COMMAND);
    }

    public static Task parseStoredTaskLine(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return null;
        }

        String type = parts[0];
        boolean isDone = "1".equals(parts[1]);
        String description = parts[2];

        Task task = null;
        switch (type) {
        case STORAGE_TODO:
            task = new ToDo(description);
            break;
        case STORAGE_DEADLINE:
            if (parts.length >= 4) {
                task = new Deadline(description, parts[3]);
            }
            break;
        case STORAGE_EVENT:
            if (parts.length >= 5) {
                task = new Event(description, parts[3], parts[4]);
            }
            break;
        default:
            return null;
        }

        if (task != null && isDone) {
            task.markAsDone();
        }
        return task;
    }

    public static Command parseCommand(String input) throws CommandException {
        if (input == null || input.trim().isEmpty()) {
            return null;
        }
        String trimmedInput = input.trim();
        String commandWord = getFirstWord(trimmedInput);
        String restOfInput = getRestOfInput(trimmedInput);

        return switch (commandWord) {
            case COMMAND_TODO, COMMAND_DEADLINE, COMMAND_EVENT -> new AddCommand(parseTask(commandWord, restOfInput));
            case COMMAND_LIST -> new ListCommand();
            case COMMAND_MARK -> new MarkCommand(restOfInput);
            case COMMAND_UNMARK -> new UnmarkCommand(restOfInput);
            case COMMAND_DELETE -> new DeleteCommand(restOfInput);
            case COMMAND_SAVE -> new SaveCommand();
            case COMMAND_LOAD -> new LoadCommand();
            case COMMAND_BYE -> new ExitCommand();
            default -> null;
        };
    }
}

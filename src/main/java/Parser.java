import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

/**
 * Parses user input and converts it into commands or tasks.
 * Handles parsing of dates, times, and command arguments.
 */
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
    public static final String COMMAND_FIND = "find";

    private static final String ERROR_TODO_EMPTY = "A todo needs a description.";
    private static final String ERROR_DEADLINE_FORMAT = "A deadline needs a description and '/by <time>'.";
    private static final String ERROR_EVENT_FORMAT = "An event needs a description, '/from <start>' and '/to <end>'.";
    private static final String ERROR_TASK_NUMBER_MISSING = "Please provide a task number.";
    private static final String ERROR_TASK_NUMBER_RANGE = "That task number is out of range.";
    private static final String ERROR_TASK_NUMBER_NOT_NUMBER = "Task numbers must be whole numbers.";
    private static final String ERROR_UNKNOWN_COMMAND = "Sorry, I do not recognize that command.";
    private static final String ERROR_EMPTY_COMMAND = "Please enter a command.";
    private static final String ERROR_INVALID_DATETIME =
            "Invalid date/time format. Use d/M/yyyy HHmm (e.g., 2/12/2019 1800) or yyyy-MM-dd HHmm.";

    private static final String STORAGE_TODO = "T";
    private static final String STORAGE_DEADLINE = "D";
    private static final String STORAGE_EVENT = "E";

    // Accepted input formats (date+time and date-only)
    private static final List<DateTimeFormatter> INPUT_FORMATS = List.of(
            DateTimeFormatter.ofPattern("d/M/yyyy HHmm"),
            DateTimeFormatter.ofPattern("d/M/yyyy"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm"),
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
    );
    private static final DateTimeFormatter STORAGE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Parses a date/time string using several accepted formats.
     * Returns a LocalDateTime (dates without a time component default to midnight).
     *
     * @param raw The raw date/time string.
     * @return The parsed LocalDateTime.
     * @throws CommandFormatException If the date/time format is invalid.
     */
    private static LocalDateTime parseDateTime(String raw) throws CommandFormatException {
        String trimmed = raw.trim();
        for (DateTimeFormatter fmt : INPUT_FORMATS) {
            try {
                // Try full date-time first
                return LocalDateTime.parse(trimmed, fmt);
            } catch (DateTimeParseException e1) {
                // Try as date-only (no time component)
                try {
                    return LocalDate.parse(trimmed, fmt).atStartOfDay();
                } catch (DateTimeParseException ignored) {
                    // try next format
                }
            }
        }
        throw new CommandFormatException(ERROR_INVALID_DATETIME);
    }

    /**
     * Extracts the first word from the input string.
     * This is typically the command keyword.
     *
     * @param input The input string.
     * @return The first word.
     */
    public static String getFirstWord(String input) {
        int firstSpaceIndex = input.indexOf(' ');
        return firstSpaceIndex == -1 ? input : input.substring(0, firstSpaceIndex);
    }

    /**
     * Extracts the rest of the input string after the first word.
     * This typically contains the arguments for the command.
     *
     * @param input The input string.
     * @return The rest of the input string, or an empty string if there are no arguments.
     */
    public static String getRestOfInput(String input) {
        int firstSpaceIndex = input.indexOf(' ');
        return firstSpaceIndex == -1 ? "" : input.substring(firstSpaceIndex + 1);
    }

    /**
     * Parses a task command and creates a Task object.
     *
     * @param commandWord The command keyword (todo, deadline, event).
     * @param args        The arguments for the task command.
     * @return The created Task object.
     * @throws CommandException If the task command is invalid.
     */
    public static Task parseTask(String commandWord, String args) throws CommandException {
        return switch (commandWord) {
            case COMMAND_TODO -> parseTodo(args);
            case COMMAND_DEADLINE -> parseDeadline(args);
            case COMMAND_EVENT -> parseEvent(args);
            default -> throw new CommandFormatException(ERROR_UNKNOWN_COMMAND);
        };
    }

    /**
     * Parses a todo command arguments.
     *
     * @param args The arguments.
     * @return The ToDo task.
     * @throws CommandException If arguments are invalid.
     */
    private static Task parseTodo(String args) throws CommandException {
        if (args.trim().isEmpty()) {
            throw new CommandFormatException(ERROR_TODO_EMPTY);
        }
        return new ToDo(args.trim());
    }

    /**
     * Parses a deadline command arguments.
     *
     * @param args The arguments.
     * @return The Deadline task.
     * @throws CommandException If arguments are invalid.
     */
    private static Task parseDeadline(String args) throws CommandException {
        if (args.trim().isEmpty()) {
            throw new CommandFormatException(ERROR_DEADLINE_FORMAT);
        }
        String[] parts = args.split("/by", 2);
        if (parts.length < 2) {
            throw new CommandFormatException(ERROR_DEADLINE_FORMAT);
        }
        String description = parts[0].trim();
        String byRaw = parts[1].trim();
        if (description.isEmpty() || byRaw.isEmpty()) {
            throw new CommandFormatException(ERROR_DEADLINE_FORMAT);
        }
        LocalDateTime by = parseDateTime(byRaw);
        return new Deadline(description, by);
    }

    /**
     * Parses an event command arguments.
     *
     * @param args The arguments.
     * @return The Event task.
     * @throws CommandException If arguments are invalid.
     */
    private static Task parseEvent(String args) throws CommandException {
        if (args.trim().isEmpty()) {
            throw new CommandFormatException(ERROR_EVENT_FORMAT);
        }
        String[] parts = args.split("/from|/to", 3);
        if (parts.length < 3) {
            throw new CommandFormatException(ERROR_EVENT_FORMAT);
        }
        String description = parts[0].trim();
        String fromRaw = parts[1].trim();
        String toRaw = parts[2].trim();
        if (description.isEmpty() || fromRaw.isEmpty() || toRaw.isEmpty()) {
            throw new CommandFormatException(ERROR_EVENT_FORMAT);
        }
        LocalDateTime from = parseDateTime(fromRaw);
        LocalDateTime to = parseDateTime(toRaw);
        return new Event(description, from, to);
    }

    /**
     * Parses a task index from the arguments.
     *
     * @param args      The arguments containing the task number.
     * @param taskCount The total number of tasks currently in the list (for range checking).
     * @return The 0-based index of the task.
     * @throws CommandException If the task number is missing, invalid, or out of range.
     */
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

    /**
     * Handles unknown commands by throwing an exception.
     *
     * @param input The unknown command input.
     * @throws CommandException Always throws, detailing the error.
     */
    public static void handleUnknownCommand(String input) throws CommandException {
        if (input.isBlank()) {
            throw new CommandException(ERROR_EMPTY_COMMAND);
        }
        throw new CommandException(ERROR_UNKNOWN_COMMAND);
    }

    /**
     * Parses a line from the storage file into a Task object.
     *
     * @param line The line from the file.
     * @return The Task object, or null if the line cannot be parsed.
     */
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
                try {
                    LocalDateTime by = LocalDateTime.parse(parts[3].trim(), STORAGE_FORMAT);
                    task = new Deadline(description, by);
                } catch (DateTimeParseException e) {
                    return null;
                }
            }
            break;
        case STORAGE_EVENT:
            if (parts.length >= 5) {
                try {
                    LocalDateTime from = LocalDateTime.parse(parts[3].trim(), STORAGE_FORMAT);
                    LocalDateTime to = LocalDateTime.parse(parts[4].trim(), STORAGE_FORMAT);
                    task = new Event(description, from, to);
                } catch (DateTimeParseException e) {
                    return null;
                }
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

    /**
     * Parses a full command input string into a Command object.
     *
     * @param input The full user input.
     * @return The corresponding Command object, or null if input is empty.
     * @throws CommandException If the command is invalid or arguments are incorrect.
     */
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
            case COMMAND_FIND -> new FindCommand(restOfInput);
            default -> null;
        };
    }
}

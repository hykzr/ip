import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TaskManager {
    public static final String COMMAND_TODO = "todo";
    public static final String COMMAND_DEADLINE = "deadline";
    public static final String COMMAND_EVENT = "event";
    public static final String COMMAND_MARK = "mark";
    public static final String COMMAND_UNMARK = "unmark";
    public static final String COMMAND_DELETE = "delete";
    public static final String COMMAND_LIST = "list";

    private final List<Task> tasks;

    private static final String SEPARATOR = "____________________________________________________________";
    private static final String ERROR_TODO_EMPTY = "A todo needs a description.";
    private static final String ERROR_DEADLINE_FORMAT = "A deadline needs a description and '/by <time>'.";
    private static final String ERROR_EVENT_FORMAT = "An event needs a description, '/from <start>' and '/to <end>'.";
    private static final String ERROR_TASK_NUMBER_MISSING = "Please provide a task number.";
    private static final String ERROR_TASK_NUMBER_RANGE = "That task number is out of range.";
    private static final String ERROR_TASK_NUMBER_NOT_NUMBER = "Task numbers must be whole numbers.";

    public TaskManager() {
        this.tasks = new ArrayList<>();
    }

    private void printBox(String message) {
        System.out.println(SEPARATOR);
        System.out.println(message);
        System.out.println(SEPARATOR + System.lineSeparator());
    }

    public void printTaskList() {
        String listOutput = String.join(System.lineSeparator(),
                IntStream.range(0, tasks.size())
                        .mapToObj(i -> (i + 1) + "." + tasks.get(i))
                        .toList());
        printBox(listOutput);
    }

    public void addTodo(String args) throws CommandException {
        if (args.trim().isEmpty()) {
            throw new CommandFormatException(ERROR_TODO_EMPTY);
        }
        Task todo = new ToDo(args.trim());
        addTask(todo);
    }

    public void addDeadline(String args) throws CommandException {
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
        Task deadline = new Deadline(description, by);
        addTask(deadline);
    }

    public void addEvent(String args) throws CommandException {
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
        Task event = new Event(description, from, to);
        addTask(event);
    }

    public void markTask(String args) throws CommandException {
        int taskIndex = parseTaskIndex(args);
        tasks.get(taskIndex).markAsDone();
        String message = String.format("Nice! I've marked this task as done:%n  %s",
                tasks.get(taskIndex));
        printBox(message);
    }

    public void unmarkTask(String args) throws CommandException {
        int taskIndex = parseTaskIndex(args);
        tasks.get(taskIndex).markAsNotDone();
        String message = String.format("OK, I've marked this task as not done yet:%n  %s",
                tasks.get(taskIndex));
        printBox(message);
    }

    public void deleteTask(String args) throws CommandException {
        int taskIndex = parseTaskIndex(args);
        Task removedTask = tasks.remove(taskIndex);
        String message = String.format("Noted. I've removed this task:%n  %s%nNow you have %d tasks in the list.",
                removedTask, tasks.size());
        printBox(message);
    }

    private void addTask(Task task) {
        tasks.add(task);
        String message = String.format(
                "Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                task, tasks.size());
        printBox(message);
    }

    private int parseTaskIndex(String args) throws CommandException {
        String trimmedInput = args.trim();
        if (trimmedInput.isEmpty()) {
            throw new CommandFormatException(ERROR_TASK_NUMBER_MISSING);
        }
        try {
            int taskIndex = Integer.parseInt(trimmedInput) - 1;
            if (taskIndex < 0 || taskIndex >= tasks.size()) {
                throw new CommandRangeException(ERROR_TASK_NUMBER_RANGE);
            }
            return taskIndex;
        } catch (NumberFormatException e) {
            throw new CommandFormatException(ERROR_TASK_NUMBER_NOT_NUMBER);
        }
    }
}

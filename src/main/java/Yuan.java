import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Yuan {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String GREETINGS = "Hello! I'm Yuan.\nWhat can I do for you?";
    private static final String GOODBYE = "Bye. Hope to see you again soon!";
    private static final String COMMAND_BYE = "bye";
    private static final String COMMAND_LIST = "list";
    private static final String COMMAND_TODO = "todo";
    private static final String COMMAND_DEADLINE = "deadline";
    private static final String COMMAND_EVENT = "event";
    private static final String COMMAND_MARK = "mark";
    private static final String COMMAND_UNMARK = "unmark";
    private static final String ERROR_TODO_EMPTY = "A todo needs a description.";
    private static final String ERROR_DEADLINE_FORMAT = "A deadline needs a description and '/by <time>'.";
    private static final String ERROR_EVENT_FORMAT = "An event needs a description, '/from <start>' and '/to <end>'.";
    private static final String ERROR_TASK_NUMBER_MISSING = "Please provide a task number.";
    private static final String ERROR_TASK_NUMBER_RANGE = "That task number is out of range.";
    private static final String ERROR_TASK_NUMBER_NOT_NUMBER = "Task numbers must be whole numbers.";
    private static final String ERROR_UNKNOWN_COMMAND = "Sorry, I do not recognize that command.";
    private static final String ERROR_EMPTY_COMMAND = "Please enter a command.";

    private static void printBox(String message) {
        System.out.println(SEPARATOR);
        System.out.println(message);
        System.out.println(SEPARATOR + System.lineSeparator());
    }

    public static void main(String[] args) {
        printBox(GREETINGS);
        List<Task> tasks = new ArrayList<>();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = scanner.nextLine().trim();
                String commandWord = getFirstWord(input);
                String restOfInput = getRestOfInput(input);

                if (COMMAND_BYE.equals(commandWord) && restOfInput.isEmpty()) {
                    printBox(GOODBYE);
                    return;
                }

                try {
                    switch (commandWord) {
                    case COMMAND_LIST -> printTaskList(tasks);
                    case COMMAND_TODO -> addTodo(restOfInput, tasks);
                    case COMMAND_DEADLINE -> addDeadline(restOfInput, tasks);
                    case COMMAND_EVENT -> addEvent(restOfInput, tasks);
                    case COMMAND_MARK -> markTask(restOfInput, tasks);
                    case COMMAND_UNMARK -> unmarkTask(restOfInput, tasks);
                    default -> handleUnknownCommand(input);
                    }
                } catch (CommandException e) {
                    printBox(e.getMessage());
                }
            }
        }
    }

    private static void printTaskList(List<Task> tasks) {
        String listOutput = String.join(System.lineSeparator(),
                IntStream.range(0, tasks.size())
                        .mapToObj(i -> (i + 1) + "." + tasks.get(i))
                        .toList());
        printBox(listOutput);
    }

    private static void addTodo(String restOfInput, List<Task> tasks) throws CommandException {
        if (restOfInput.trim().isEmpty()) {
            throw new CommandFormatException(ERROR_TODO_EMPTY);
        }
        Task todo = new ToDo(restOfInput.trim());
        addTask(todo, tasks);
    }

    private static void addDeadline(String restOfInput, List<Task> tasks) throws CommandException {
        if (restOfInput.trim().isEmpty()) {
            throw new CommandFormatException(ERROR_DEADLINE_FORMAT);
        }
        String[] parts = restOfInput.split("/by", 2);
        if (parts.length < 2) {
            throw new CommandFormatException(ERROR_DEADLINE_FORMAT);
        }
        String description = parts[0].trim();
        String by = parts[1].trim();
        if (description.isEmpty() || by.isEmpty()) {
            throw new CommandFormatException(ERROR_DEADLINE_FORMAT);
        }
        Task deadline = new Deadline(description, by);
        addTask(deadline, tasks);
    }

    private static void addEvent(String restOfInput, List<Task> tasks) throws CommandException {
        if (restOfInput.trim().isEmpty()) {
            throw new CommandFormatException(ERROR_EVENT_FORMAT);
        }
        String[] parts = restOfInput.split("/from|/to", 3);
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
        addTask(event, tasks);
    }

    private static void markTask(String restOfInput, List<Task> tasks) throws CommandException {
        int taskIndex = parseTaskIndex(restOfInput, tasks.size());
        tasks.get(taskIndex).markAsDone();
        String message = String.format("Nice! I've marked this task as done:%n  %s",
                tasks.get(taskIndex));
        printBox(message);
    }

    private static void unmarkTask(String restOfInput, List<Task> tasks) throws CommandException {
        int taskIndex = parseTaskIndex(restOfInput, tasks.size());
        tasks.get(taskIndex).markAsNotDone();
        String message = String.format("OK, I've marked this task as not done yet:%n  %s",
                tasks.get(taskIndex));
        printBox(message);
    }

    private static int parseTaskIndex(String restOfInput, int taskCount) throws CommandException {
        String trimmedInput = restOfInput.trim();
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

    private static void addTask(Task task, List<Task> tasks) {
        tasks.add(task);
        String message = String.format(
                "Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                task, tasks.size());
        printBox(message);
    }

    private static void handleUnknownCommand(String input) throws CommandException {
        if (input.isBlank()) {
            throw new CommandException(ERROR_EMPTY_COMMAND);
        }
        throw new CommandException(ERROR_UNKNOWN_COMMAND);
    }

    private static String getFirstWord(String input) {
        int firstSpaceIndex = input.indexOf(' ');
        return firstSpaceIndex == -1 ? input : input.substring(0, firstSpaceIndex);
    }

    private static String getRestOfInput(String input) {
        int firstSpaceIndex = input.indexOf(' ');
        return firstSpaceIndex == -1 ? "" : input.substring(firstSpaceIndex + 1);
    }
}

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

                switch (commandWord) {
                    case COMMAND_LIST -> printTaskList(tasks);
                    case COMMAND_TODO -> addTodo(restOfInput, tasks);
                    case COMMAND_DEADLINE -> addDeadline(restOfInput, tasks);
                    case COMMAND_EVENT -> addEvent(restOfInput, tasks);
                    case COMMAND_MARK -> markTask(restOfInput, tasks);
                    case COMMAND_UNMARK -> unmarkTask(restOfInput, tasks);
                    default -> handleUnknownCommand(input, tasks);
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

    private static void addTodo(String restOfInput, List<Task> tasks) {
        Task todo = new ToDo(restOfInput);
        addTask(todo, tasks);
    }

    private static void addDeadline(String restOfInput, List<Task> tasks) {
        String[] parts = restOfInput.split("/by", 2);
        if (parts.length < 2) {
            printBox("Invalid deadline format. Use: deadline <description> /by <due date>");
            return;
        }
        String description = parts[0].trim();
        String by = parts[1].trim();
        Task deadline = new Deadline(description, by);
        addTask(deadline, tasks);
    }

    private static void addEvent(String restOfInput, List<Task> tasks) {
        String[] parts = restOfInput.split("/from|/to");
        if (parts.length < 3) {
            printBox("Invalid event format. Use: event <description> /from <start time> /to <end time>");
            return;
        }
        String description = parts[0].trim();
        String from = parts[1].trim();
        String to = parts[2].trim();
        Task event = new Event(description, from, to);
        addTask(event, tasks);
    }

    private static void markTask(String restOfInput, List<Task> tasks) {
        int taskIndex = parseTaskIndex(restOfInput, tasks.size());
        if (taskIndex == -1) {
            printBox("Invalid task number.");
            return;
        }
        tasks.get(taskIndex).markAsDone();
        String message = String.format("Nice! I've marked this task as done:%n  %s",
                tasks.get(taskIndex));
        printBox(message);
    }

    private static void unmarkTask(String restOfInput, List<Task> tasks) {
        int taskIndex = parseTaskIndex(restOfInput, tasks.size());
        if (taskIndex == -1) {
            printBox("Invalid task number.");
            return;
        }
        tasks.get(taskIndex).markAsNotDone();
        String message = String.format("OK, I've marked this task as not done yet:%n  %s",
                tasks.get(taskIndex));
        printBox(message);
    }

    private static int parseTaskIndex(String restOfInput, int taskCount) {
        try {
            int taskIndex = Integer.parseInt(restOfInput.trim()) - 1;
            if (taskIndex < 0 || taskIndex >= taskCount) {
                return -1;
            }
            return taskIndex;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void addTask(Task task, List<Task> tasks) {
        tasks.add(task);
        String message = String.format(
                "Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                task, tasks.size());
        printBox(message);
    }

    private static void handleUnknownCommand(String input, List<Task> tasks) {
        printBox(input);
        tasks.add(new Task(input));
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

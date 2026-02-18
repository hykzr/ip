import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TaskManager {
    public static final String COMMAND_TODO = "todo";
    public static final String COMMAND_DEADLINE = "deadline";
    public static final String COMMAND_EVENT = "event";
    public static final String COMMAND_MARK = "mark";
    public static final String COMMAND_UNMARK = "unmark";
    public static final String COMMAND_LIST = "list";
    public static final String COMMAND_SAVE = "save";
    public static final String COMMAND_LOAD = "load";


    private static final String FILE_PATH = "./data/yuan.txt";
    private static final String DATA_DIR = "./data";

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
        loadTasks();
    }

    private void ensureDataDirectoryExists() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
                System.out.println("Data directory created successfully.");
            } else {
                System.out.println("Failed to create data directory.");
            }
        }
    }

    public void loadTasks() {
        ensureDataDirectoryExists();

        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try {
                    parseTaskLine(line);
                } catch (Exception e) {
                    System.out.println("Error parsing line: " + line);
                }
            }
            printBox("Tasks loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Data file not found: " + e.getMessage());
        }
    }

    private void parseTaskLine(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            return; // Skip invalid format
        }

        String type = parts[0];
        boolean isDone = "1".equals(parts[1]);
        String description = parts[2];

        Task task = null;
        switch (type) {
        case "T":
            task = new ToDo(description);
            break;
        case "D":
            if (parts.length >= 4) {
                task = new Deadline(description, parts[3]);
            }
            break;
        case "E":
            if (parts.length >= 5) {
                task = new Event(description, parts[3], parts[4]);
            }
            break;
        }

        if (task != null) {
            if (isDone) {
                task.markAsDone();
            }
            tasks.add(task);
        }
    }

    public void saveTasks() {
        ensureDataDirectoryExists();
        try {
            FileWriter writer = new FileWriter(FILE_PATH);
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
            writer.close();
            printBox("Tasks saved successfully.");
        } catch (IOException e) {
            System.out.println("Error saving tasks: " + e.getMessage());
        }
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
        saveTasks();
        String message = String.format("Nice! I've marked this task as done:%n  %s",
                tasks.get(taskIndex));
        printBox(message);
    }

    public void unmarkTask(String args) throws CommandException {
        int taskIndex = parseTaskIndex(args);
        tasks.get(taskIndex).markAsNotDone();
        saveTasks();
        String message = String.format("OK, I've marked this task as not done yet:%n  %s",
                tasks.get(taskIndex));
        printBox(message);
    }

    private void addTask(Task task) {
        tasks.add(task);
        saveTasks();
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

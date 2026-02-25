import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

public class TaskList {
    private static final String FILE_PATH = "./data/yuan.txt";
    private static final String DATA_DIR = "./data";

    private final List<Task> tasks;

    private static final String SEPARATOR = "____________________________________________________________";

    public TaskList() {
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
            tasks.clear();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                try {
                    Task task = Parser.parseStoredTaskLine(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                } catch (Exception e) {
                    System.out.println("Error parsing line: " + line);
                }
            }
            printBox("Tasks loaded successfully.");
        } catch (FileNotFoundException e) {
            System.out.println("Data file not found: " + e.getMessage());
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

    public int getTaskCount() {
        return tasks.size();
    }

    public void markTask(int taskIndex) {
        tasks.get(taskIndex).markAsDone();
        saveTasks();
        String message = String.format("Nice! I've marked this task as done:%n  %s",
                tasks.get(taskIndex));
        printBox(message);
    }

    public void unmarkTask(int taskIndex) {
        tasks.get(taskIndex).markAsNotDone();
        saveTasks();
        String message = String.format("OK, I've marked this task as not done yet:%n  %s",
                tasks.get(taskIndex));
        printBox(message);
    }

    public void deleteTask(int taskIndex) {
        Task removedTask = tasks.remove(taskIndex);
        saveTasks();
        String message = String.format("Noted. I've removed this task:%n  %s%nNow you have %d tasks in the list.",
                removedTask, tasks.size());
        printBox(message);
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
        String message = String.format(
                "Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                task, tasks.size());
        printBox(message);
    }
}

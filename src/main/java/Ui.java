import java.util.List;
import java.util.stream.IntStream;

public class Ui {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String GREETINGS = "Hello! I'm Yuan.\nWhat can I do for you?";
    private static final String GOODBYE = "Bye. Hope to see you again soon!";
    private static final String DATA_DIR_CREATED = "Data directory created successfully.";
    private static final String DATA_DIR_CREATE_FAILED = "Failed to create data directory.";
    private static final String TASKS_LOADED = "Tasks loaded successfully.";
    private static final String TASKS_SAVED = "Tasks saved successfully.";

    private void printBox(String message) {
        System.out.println(SEPARATOR);
        System.out.println(message);
        System.out.println(SEPARATOR + System.lineSeparator());
    }

    public void showGreeting() {
        printBox(GREETINGS);
    }

    public void showGoodbye() {
        printBox(GOODBYE);
    }

    public void showDataDirectoryCreated() {
        System.out.println(DATA_DIR_CREATED);
    }

    public void showDataDirectoryCreateFailed() {
        System.out.println(DATA_DIR_CREATE_FAILED);
    }

    public void showTasksLoaded() {
        printBox(TASKS_LOADED);
    }

    public void showTasksSaved() {
        printBox(TASKS_SAVED);
    }

    public void showTaskList(List<Task> tasks) {
        String listOutput = String.join(System.lineSeparator(),
                IntStream.range(0, tasks.size())
                        .mapToObj(i -> (i + 1) + "." + tasks.get(i))
                        .toList());
        printBox(listOutput);
    }

    public void showTaskMarked(Task task) {
        String message = String.format("Nice! I've marked this task as done:%n  %s", task);
        printBox(message);
    }

    public void showTaskUnmarked(Task task) {
        String message = String.format("OK, I've marked this task as not done yet:%n  %s", task);
        printBox(message);
    }

    public void showTaskDeleted(Task task, int remainingCount) {
        String message = String.format("Noted. I've removed this task:%n  %s%nNow you have %d tasks in the list.",
                task, remainingCount);
        printBox(message);
    }

    public void showTaskAdded(Task task, int totalCount) {
        String message = String.format(
                "Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                task, totalCount);
        printBox(message);
    }

    public void showMessage(String message) {
        printBox(message);
    }

    public void showLine(String message) {
        System.out.println(message);
    }
}

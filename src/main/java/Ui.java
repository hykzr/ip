import java.util.List;
import java.util.stream.IntStream;

/**
 * Handles user interface operations provided by the application.
 * Responsible for reading user input and printing messages to the console.
 */
public class Ui {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String GREETINGS = "Hello! I'm Yuan.\nWhat can I do for you?";
    private static final String GOODBYE = "Bye. Hope to see you again soon!";
    private static final String DATA_DIR_CREATED = "Data directory created successfully.";
    private static final String TASKS_LOADED = "Tasks loaded successfully.";
    private static final String TASKS_SAVED = "Tasks saved successfully.";

    /**
     * Prints a message enclosed in a separator box.
     *
     * @param message The message to print.
     */
    private void printBox(String message) {
        System.out.println(SEPARATOR);
        System.out.println(message);
        System.out.println(SEPARATOR + System.lineSeparator());
    }

    /**
     * Displays the welcome greeting message.
     */
    public void showGreeting() {
        printBox(GREETINGS);
    }

    /**
     * Displays the goodbye message.
     */
    public void showGoodbye() {
        printBox(GOODBYE);
    }

    /**
     * Displays a message indicating successful data directory creation.
     */
    public void showDataDirectoryCreated() {
        System.out.println(DATA_DIR_CREATED);
    }

    /**
     * Displays a message indicating tasks were loaded successfully.
     */
    public void showTasksLoaded() {
        printBox(TASKS_LOADED);
    }

    /**
     * Displays a message indicating tasks were saved successfully.
     */
    public void showTasksSaved() {
        printBox(TASKS_SAVED);
    }

    /**
     * Displays the list of tasks.
     *
     * @param tasks The list of tasks to display.
     */
    public void showTaskList(List<Task> tasks) {
        String listOutput = String.join(System.lineSeparator(),
                IntStream.range(0, tasks.size())
                        .mapToObj(i -> (i + 1) + "." + tasks.get(i))
                        .toList());
        printBox(listOutput);
    }

    /**
     * Displays a message indicating a task has been marked as done.
     *
     * @param task The task that was marked.
     */
    public void showTaskMarked(Task task) {
        String message = String.format("Nice! I've marked this task as done:%n  %s", task);
        printBox(message);
    }

    /**
     * Displays a message indicating a task has been marked as not done.
     *
     * @param task The task that was unmarked.
     */
    public void showTaskUnmarked(Task task) {
        String message = String.format("OK, I've marked this task as not done yet:%n  %s", task);
        printBox(message);
    }

    /**
     * Displays a message indicating a task has been deleted.
     *
     * @param task           The task that was deleted.
     * @param remainingCount The number of tasks remaining in the list.
     */
    public void showTaskDeleted(Task task, int remainingCount) {
        String message = String.format("Noted. I've removed this task:%n  %s%nNow you have %d tasks in the list.",
                task, remainingCount);
        printBox(message);
    }

    /**
     * Displays a message indicating a task has been added.
     *
     * @param task       The task that was added.
     * @param totalCount The total number of tasks in the list.
     */
    public void showTaskAdded(Task task, int totalCount) {
        String message = String.format(
                "Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                task, totalCount);
        printBox(message);
    }

    /**
     * Displays the list of found tasks matching a keyword.
     *
     * @param tasks The list of matching tasks.
     */
    public void showFoundTasks(List<Task> tasks) {
        if (tasks.isEmpty()) {
            printBox("No matching tasks found.");
            return;
        }
        String listOutput = "Here are the matching tasks in your list:" + System.lineSeparator()
                + String.join(System.lineSeparator(),
                IntStream.range(0, tasks.size())
                        .mapToObj(i -> (i + 1) + "." + tasks.get(i))
                        .toList());
        printBox(listOutput);
    }

    /**
     * Displays a generic message in a box.
     *
     * @param message The message to display.
     */
    public void showMessage(String message) {
        printBox(message);
    }
}

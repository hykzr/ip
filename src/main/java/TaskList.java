import java.util.ArrayList;
import java.util.List;

/**
 * Manages the list of tasks.
 * Provides methods to add, delete, mark, unmark, and find tasks.
 */
public class TaskList {
    private final List<Task> tasks;
    private final Ui ui;

    /**
     * Constructs an empty TaskList.
     *
     * @param ui The UI object to use for displaying messages.
     */
    public TaskList(Ui ui) {
        this.tasks = new ArrayList<>();
        this.ui = ui;
    }

    /**
     * Constructs a TaskList initialized with the given tasks.
     *
     * @param tasks The initial list of tasks.
     * @param ui    The UI object to use for displaying messages.
     */
    public TaskList(ArrayList<Task> tasks, Ui ui) {
        this.tasks = new ArrayList<>(tasks);
        this.ui = ui;
    }

    /**
     * Returns the list of tasks.
     *
     * @return The list of tasks.
     */
    public List<Task> getTasks() {
        return tasks;
    }

    /**
     * Replaces the current task list with a new set of tasks.
     *
     * @param newTasks The new list of tasks.
     */
    public void replaceTasks(ArrayList<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
    }

    /**
     * Prints the current list of tasks to the UI.
     */
    public void printTaskList() {
        ui.showTaskList(tasks);
    }

    /**
     * Returns the number of tasks in the list.
     *
     * @return The number of tasks.
     */
    public int getTaskCount() {
        return tasks.size();
    }

    /**
     * Marks a task as done by its index.
     *
     * @param taskIndex The 0-based index of the task.
     */
    public void markTask(int taskIndex) {
        tasks.get(taskIndex).markAsDone();
        ui.showTaskMarked(tasks.get(taskIndex));
    }

    /**
     * Marks a task as not done by its index.
     *
     * @param taskIndex The 0-based index of the task.
     */
    public void unmarkTask(int taskIndex) {
        tasks.get(taskIndex).markAsNotDone();
        ui.showTaskUnmarked(tasks.get(taskIndex));
    }

    /**
     * Deletes a task by its index.
     *
     * @param taskIndex The 0-based index of the task.
     */
    public void deleteTask(int taskIndex) {
        Task removedTask = tasks.remove(taskIndex);
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    /**
     * Adds a task to the list.
     *
     * @param task The task to add.
     */
    public void addTask(Task task) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
    }

    /**
     * Finds and displays tasks matching the keyword.
     *
     * @param keyword The keyword to search for in task descriptions.
     */
    public void findTasks(String keyword) {
        List<Task> matched = tasks.stream()
                .filter(t -> t.description.toLowerCase().contains(keyword.toLowerCase()))
                .toList();
        ui.showFoundTasks(matched);
    }
}

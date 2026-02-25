import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private final List<Task> tasks;
    private final Ui ui;

    public TaskList(Ui ui) {
        this.tasks = new ArrayList<>();
        this.ui = ui;
    }

    public TaskList(ArrayList<Task> tasks, Ui ui) {
        this.tasks = new ArrayList<>(tasks);
        this.ui = ui;
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public void replaceTasks(ArrayList<Task> newTasks) {
        tasks.clear();
        tasks.addAll(newTasks);
    }

    public void printTaskList() {
        ui.showTaskList(tasks);
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public void markTask(int taskIndex) {
        tasks.get(taskIndex).markAsDone();
        ui.showTaskMarked(tasks.get(taskIndex));
    }

    public void unmarkTask(int taskIndex) {
        tasks.get(taskIndex).markAsNotDone();
        ui.showTaskUnmarked(tasks.get(taskIndex));
    }

    public void deleteTask(int taskIndex) {
        Task removedTask = tasks.remove(taskIndex);
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    public void addTask(Task task) {
        tasks.add(task);
        ui.showTaskAdded(task, tasks.size());
    }
}

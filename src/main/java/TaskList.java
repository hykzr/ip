import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class TaskList {
    private static final String FILE_PATH = "./data/yuan.txt";
    private static final String DATA_DIR = "./data";

    private final List<Task> tasks;
    private final Ui ui;

    public TaskList(Ui ui) {
        this.tasks = new ArrayList<>();
        this.ui = ui;
        loadTasks();
    }

    private void ensureDataDirectoryExists() {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
                ui.showDataDirectoryCreated();
            } else {
                ui.showDataDirectoryCreateFailed();
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
                    ui.showLine("Error parsing line: " + line);
                }
            }
            ui.showTasksLoaded();
        } catch (FileNotFoundException e) {
            ui.showLine("Data file not found: " + e.getMessage());
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
            ui.showTasksSaved();
        } catch (IOException e) {
            ui.showLine("Error saving tasks: " + e.getMessage());
        }
    }

    public void printTaskList() {
        ui.showTaskList(tasks);
    }

    public int getTaskCount() {
        return tasks.size();
    }

    public void markTask(int taskIndex) {
        tasks.get(taskIndex).markAsDone();
        saveTasks();
        ui.showTaskMarked(tasks.get(taskIndex));
    }

    public void unmarkTask(int taskIndex) {
        tasks.get(taskIndex).markAsNotDone();
        saveTasks();
        ui.showTaskUnmarked(tasks.get(taskIndex));
    }

    public void deleteTask(int taskIndex) {
        Task removedTask = tasks.remove(taskIndex);
        saveTasks();
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    public void addTask(Task task) {
        tasks.add(task);
        saveTasks();
        ui.showTaskAdded(task, tasks.size());
    }
}

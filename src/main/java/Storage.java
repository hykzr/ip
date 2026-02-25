import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private static final String FILE_PATH = "./data/yuan.txt";
    private static final String DATA_DIR = "./data";

    private final Ui ui;

    public Storage(Ui ui) {
        this.ui = ui;
    }

    private void ensureDataDirectoryExists() throws StorageError {
        File dataDir = new File(DATA_DIR);
        if (!dataDir.exists()) {
            if (dataDir.mkdirs()) {
                ui.showDataDirectoryCreated();
            } else {
                throw new StorageError("Failed to create data directory.");
            }
        }
    }

    public ArrayList<Task> load() throws StorageError {
        ensureDataDirectoryExists();

        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return tasks;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Task task = Parser.parseStoredTaskLine(line);
                if (task == null) {
                    throw new StorageError("Error parsing line: " + line);
                }
                tasks.add(task);
            }
            ui.showTasksLoaded();
        } catch (FileNotFoundException e) {
            throw new StorageError("Data file not found: " + e.getMessage(), e);
        }

        return tasks;
    }

    public void save(List<Task> tasks) throws StorageError {
        ensureDataDirectoryExists();
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            for (Task task : tasks) {
                writer.write(task.toFileFormat() + System.lineSeparator());
            }
            ui.showTasksSaved();
        } catch (IOException e) {
            throw new StorageError("Error saving tasks: " + e.getMessage(), e);
        }
    }
}

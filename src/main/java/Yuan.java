import java.util.ArrayList;
import java.util.Scanner;

/**
 * Main class for the Yuan application.
 * Starts the application by initializing the UI, Storage, and TaskList,
 * and enters the main command loop.
 */
public class Yuan {
    /**
     * Main entry point of the application.
     *
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        Ui ui = new Ui();
        Storage storage = new Storage(ui);
        TaskList taskManager;

        try {
            ArrayList<Task> tasks = storage.load();
            taskManager = new TaskList(tasks, ui);
        } catch (StorageError e) {
            ui.showMessage(e.getMessage());
            taskManager = new TaskList(ui);
        }

        ui.showGreeting();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = scanner.nextLine();

                try {
                    Command command = Parser.parseCommand(input);
                    if (command == null) {
                        Parser.handleUnknownCommand(input);
                        continue;
                    }
                    command.execute(taskManager, ui, storage);
                    if (command.isExit()) {
                        return;
                    }
                } catch (CommandException | StorageError e) {
                    ui.showMessage(e.getMessage());
                }
            }
        }
    }
}

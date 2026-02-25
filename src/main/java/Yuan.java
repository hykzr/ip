import java.util.Scanner;

public class Yuan {
    private static final Ui ui = new Ui();
    private static final TaskList taskManager = new TaskList(ui);

    public static void main(String[] args) {
        ui.showGreeting();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = scanner.nextLine().trim();
                String commandWord = Parser.getFirstWord(input);
                String restOfInput = Parser.getRestOfInput(input);

                if (Parser.COMMAND_BYE.equals(commandWord) && restOfInput.isEmpty()) {
                    ui.showGoodbye();
                    return;
                }

                try {
                    switch (commandWord) {
                    case Parser.COMMAND_LIST -> taskManager.printTaskList();
                    case Parser.COMMAND_TODO,
                         Parser.COMMAND_DEADLINE,
                         Parser.COMMAND_EVENT -> taskManager.addTask(Parser.parseTask(commandWord, restOfInput));
                    case Parser.COMMAND_MARK -> taskManager.markTask(
                            Parser.parseTaskIndex(restOfInput, taskManager.getTaskCount()));
                    case Parser.COMMAND_UNMARK -> taskManager.unmarkTask(
                            Parser.parseTaskIndex(restOfInput, taskManager.getTaskCount()));
                    case Parser.COMMAND_DELETE -> taskManager.deleteTask(
                            Parser.parseTaskIndex(restOfInput, taskManager.getTaskCount()));
                    case Parser.COMMAND_SAVE -> taskManager.saveTasks();
                    case Parser.COMMAND_LOAD -> taskManager.loadTasks();
                    default -> Parser.handleUnknownCommand(input);
                    }
                } catch (CommandException e) {
                    ui.showMessage(e.getMessage());
                }
            }
        }
    }
}

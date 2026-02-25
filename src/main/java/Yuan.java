import java.util.Scanner;

public class Yuan {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String GREETINGS = "Hello! I'm Yuan.\nWhat can I do for you?";
    private static final String GOODBYE = "Bye. Hope to see you again soon!";

    private static final TaskList taskManager = new TaskList();

    private static void printBox(String message) {
        System.out.println(SEPARATOR);
        System.out.println(message);
        System.out.println(SEPARATOR + System.lineSeparator());
    }

    public static void main(String[] args) {
        printBox(GREETINGS);

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                String input = scanner.nextLine().trim();
                String commandWord = Parser.getFirstWord(input);
                String restOfInput = Parser.getRestOfInput(input);

                if (Parser.COMMAND_BYE.equals(commandWord) && restOfInput.isEmpty()) {
                    printBox(GOODBYE);
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
                    printBox(e.getMessage());
                }
            }
        }
    }
}

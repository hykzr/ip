import java.util.Scanner;

public class Yuan {
    private static final String SEPARATOR = "____________________________________________________________";
    private static final String GREETINGS = "Hello! I'm Yuan.\nWhat can I do for you?";
    private static final String GOODBYE = "Bye. Hope to see you again soon!";
    private static final String COMMAND_BYE = "bye";
    private static final String ERROR_UNKNOWN_COMMAND = "Sorry, I do not recognize that command.";
    private static final String ERROR_EMPTY_COMMAND = "Please enter a command.";

    private static final TaskManager taskManager = new TaskManager();

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
                String commandWord = getFirstWord(input);
                String restOfInput = getRestOfInput(input);

                if (COMMAND_BYE.equals(commandWord) && restOfInput.isEmpty()) {
                    printBox(GOODBYE);
                    return;
                }

                try {
                    switch (commandWord) {
                    case TaskManager.COMMAND_LIST -> taskManager.printTaskList();
                    case TaskManager.COMMAND_TODO -> taskManager.addTodo(restOfInput);
                    case TaskManager.COMMAND_DEADLINE -> taskManager.addDeadline(restOfInput);
                    case TaskManager.COMMAND_EVENT -> taskManager.addEvent(restOfInput);
                    case TaskManager.COMMAND_MARK -> taskManager.markTask(restOfInput);
                    case TaskManager.COMMAND_UNMARK -> taskManager.unmarkTask(restOfInput);
                    case TaskManager.COMMAND_DELETE -> taskManager.deleteTask(restOfInput);
                    default -> handleUnknownCommand(input);
                    }
                } catch (CommandException e) {
                    printBox(e.getMessage());
                }
            }
        }
    }

    private static void handleUnknownCommand(String input) throws CommandException {
        if (input.isBlank()) {
            throw new CommandException(ERROR_EMPTY_COMMAND);
        }
        throw new CommandException(ERROR_UNKNOWN_COMMAND);
    }

    private static String getFirstWord(String input) {
        int firstSpaceIndex = input.indexOf(' ');
        return firstSpaceIndex == -1 ? input : input.substring(0, firstSpaceIndex);
    }

    private static String getRestOfInput(String input) {
        int firstSpaceIndex = input.indexOf(' ');
        return firstSpaceIndex == -1 ? "" : input.substring(firstSpaceIndex + 1);
    }
}

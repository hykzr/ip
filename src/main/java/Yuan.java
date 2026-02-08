import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;

public class Yuan {
    private static void print(String message) {
        System.out.println("____________________________________________________________");
        System.out.println(message);
        System.out.println("____________________________________________________________\n");
    }

    public static void main(String[] args) {
        String greetings = """
                Hello! I'm Yuan.
                What can I do for you?""";
        String bye = "Bye. Hope to see you again soon!";
        print(greetings);

        // array to store each user input tasks
        ArrayList<Task> tasks = new ArrayList<>();

        // echo until "bye" is received
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine().trim();
            int firstSpaceIndex = input.indexOf(' ');
            String firstWord = firstSpaceIndex == -1 ? input : input.substring(0, firstSpaceIndex);
            String restOfInput = firstSpaceIndex == -1 ? "" : input.substring(firstSpaceIndex + 1);
            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                String listOutput = String.join(System.lineSeparator(),
                        IntStream.range(0, tasks.size())
                                .mapToObj(i -> (i + 1) + "." + tasks.get(i))
                                .toList());
                print(listOutput);
            }
            switch (firstWord) {
                case "todo" -> {
                    Task todo = new ToDo(restOfInput);
                    tasks.add(todo);
                    String message = String.format(
                            "Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                            todo, tasks.size());
                    print(message);
                }
                case "deadline" -> {
                    String[] parts = restOfInput.split("/by");
                    if (parts.length < 2) {
                        print("Invalid deadline format. Use: deadline <description> /by <due date>");
                        continue;
                    }
                    String description = parts[0].trim();
                    String by = parts[1].trim();
                    Task deadline = new Deadline(description, by);
                    tasks.add(deadline);
                    String message = String.format(
                            "Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                            deadline, tasks.size());
                    print(message);
                }
                case "event" -> {
                    String[] parts = restOfInput.split("/from|/to");
                    if (parts.length < 3) {
                        print("Invalid event format. Use: event <description> /from <start time> /to <end time>");
                        continue;
                    }
                    String description = parts[0].trim();
                    String from = parts[1].trim();
                    String to = parts[2].trim();
                    Task event = new Event(description, from, to);
                    tasks.add(event);
                    String message = String.format(
                            "Got it. I've added this task:%n  %s%nNow you have %d tasks in the list.",
                            event, tasks.size());
                    print(message);
                }
                case "mark" -> {
                    int taskNumber = Integer.parseInt(restOfInput) - 1;
                    if (taskNumber < 0 || taskNumber >= tasks.size()) {
                        print("Invalid task number.");
                        continue;
                    }
                    tasks.get(taskNumber).markAsDone();
                    String message = String.format(
                            "Nice! I've marked this task as done:%n  %s",
                            tasks.get(taskNumber));
                    print(message);
                }
                case "unmark" -> {
                    int taskNumber = Integer.parseInt(restOfInput) - 1;
                    if (taskNumber < 0 || taskNumber >= tasks.size()) {
                        print("Invalid task number.");
                        continue;
                    }
                    tasks.get(taskNumber).markAsNotDone();
                    String message = String.format(
                            "OK, I've marked this task as not done yet:%n  %s",
                            tasks.get(taskNumber));
                    print(message);
                }
                default -> {
                    print(input);
                    tasks.add(new Task(input));
                }
            }
        }

        print(bye);
        scanner.close();
    }
}

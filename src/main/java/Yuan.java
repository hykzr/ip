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
        ArrayList<Task> tasks = new ArrayList<Task>();

        // echo until "bye" is received
        Scanner scanner = new Scanner(System.in);
        while (true) {
            String input = scanner.nextLine();
            if (input.equals("bye")) {
                break;
            } else if (input.equals("list")) {
                print(String.join("\n",
                        IntStream.range(0, tasks.size())
                                .mapToObj(i -> (i + 1) + "." + tasks.get(i))
                                .toList()));
            } else if (input.startsWith("mark ")) {
                int taskNumber = Integer.parseInt(input.substring(5)) - 1;
                if (taskNumber < 0 || taskNumber >= tasks.size()) {
                    print("Invalid task number.");
                    continue;
                }
                tasks.get(taskNumber).markAsDone();
                print("Nice! I've marked this task as done:\n  " + tasks.get(taskNumber));
            } else if (input.startsWith("unmark ")) {
                int taskNumber = Integer.parseInt(input.substring(7)) - 1;
                if (taskNumber < 0 || taskNumber >= tasks.size()) {
                    print("Invalid task number.");
                    continue;
                }
                tasks.get(taskNumber).markAsNotDone();
                print("OK, I've marked this task as not done yet:\n  " + tasks.get(taskNumber));
            } else {
                print(input);
                tasks.add(new Task(input));
            }
        }

        print(bye);
        scanner.close();
    }
}

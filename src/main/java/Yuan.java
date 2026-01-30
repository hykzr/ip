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
            } else {
                print(input);
                tasks.add(new Task(input));
            }
        }

        print(bye);
        scanner.close();
    }
}

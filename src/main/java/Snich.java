import javafx.concurrent.Task;

import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Snich {

    static ArrayList<Todo> library = new ArrayList<>();

    public static void main(String[] args) {
        String logo = """
  _____ _   _ _____ _____ _    _ 
 / ____| \\ | |_   _/ ____| |  | |
| (___ |  \\| | | || |    | |__| |
 \\___ \\| . ` | | || |    |  __  |
 ____) | |\\  |_| || |____| |  | |
|_____/|_| \\_|_____\\_____|_|  |_|
""";

        String art = """
              .-\"\"\"-.
           .-'  _   _'-.
         .'    (o) (o)  '.
        /      .  _\\\\_.    \\
       :       |  ---|      :
       |        \\.__./      |
       |      .-`-__-`-.    |
      / \\    /  /\\__/\\  \\  /\\
     /   '._/__/  \\\\/  \\__\\'  \\
    :         /        \\       :
    |        /  .--.    \\      |
    |        \\ (____)   /      |
     \\        '.__.__.' /     /
      '._              _.'_.'
         '-.__    __.-'.-'
              '\"\"\"'
""";
        System.out.println("Hello from\n" + logo);
        System.out.println(art);
        System.out.println("What can I do for you?");
        Scanner input = new Scanner(System.in);




        while (true) {
            System.out.print("You: ");
            String userInput = input.nextLine().trim();

            if (userInput.equalsIgnoreCase("bye")) {
                System.out.println("Bot: Bye. Hope to see you again soon!");
                break;

            } else if (userInput.equalsIgnoreCase("list")) {
                printList();

            } else if (userInput.toLowerCase().startsWith("mark")) {
                // e.g. "mark 1"
                int index = Integer.parseInt(userInput.split("\\s+")[1]) - 1;
                library.get(index).setCompletion(true);
                printList();

            } else if (userInput.toLowerCase().startsWith("unmark")) {
                // e.g. "unmark 1"
                int index = Integer.parseInt(userInput.split("\\s+")[1]) - 1;
                library.get(index).setCompletion(false);
                printList();

            } else if (userInput.toLowerCase().startsWith("todo")) {
                // "todo borrow book"
                String desc = userInput.substring(4).trim();
                Todo t = new Todo(desc);
                library.add(t);
                printAdded(t);

            } else if (userInput.toLowerCase().startsWith("deadline")) {
                // "deadline return book /by Sunday"
                String[] parts = userInput.substring(8).split("/by", 2);
                String desc = parts[0].trim();
                String by = parts.length > 1 ? parts[1].trim() : "";
                Todo.Deadline d = new Todo.Deadline(desc, by);
                library.add(d);
                printAdded(d);

            } else if (userInput.toLowerCase().startsWith("event")) {
                // "event project meeting /from Mon 2pm /to 4pm"
                String[] p1 = userInput.substring(5).split("/from", 2);
                String desc = p1[0].trim();
                String[] p2 = p1[1].split("/to", 2);
                String from = p2[0].trim();
                String to = p2[1].trim();
                Todo.Event e = new Todo.Event(desc, from, to);
                library.add(e);
                printAdded(e);

            } else if  (userInput.toLowerCase().startsWith("delete")) {
                System.out.println("Noted. I've removed this task:");
                int index = Integer.parseInt(userInput.split("\\s+")[1]) - 1;
                printRemoved(library.get(index));
                library.remove(index);
            }


            else {
                // Unknown command; you can print a help or ignore.
                System.out.println("nani desu ka?");
            }
        }

        input.close();
    }

    private static void printList() {
        System.out.println("    ____________________________________________________________");
        System.out.println("     Here are the tasks in your list:");
        for (int i = 0; i < library.size(); i++) {
            System.out.println("     " + (i + 1) + "." + library.get(i));
        }
        System.out.println("    ____________________________________________________________");
    }

    private static void printAdded(Todo t) {
        System.out.println("    ____________________________________________________________");
        System.out.println("     Got it. I've added this task:");
        System.out.println("       " + t);
        System.out.println("     Now you have " + library.size() + " tasks in the list.");
        System.out.println("    ____________________________________________________________");
    }

    private static void printRemoved(Todo t) {
        System.out.println("    ____________________________________________________________");
        System.out.println("     Got it. I've removed this task:");
        System.out.println("       " + t);
        System.out.println("     Now you have " + (library.size() - 1) + " tasks in the list.");
        System.out.println("    ____________________________________________________________");
    }
}

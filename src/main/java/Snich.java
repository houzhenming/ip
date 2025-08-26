import javafx.concurrent.Task;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Snich {

    static ArrayList<Todo> library = new ArrayList<>();
    static File storage = new File("data/toDoList.txt");

    public static void main(String[] args) throws IOException {

        if (!storage.exists()) {
            storage.getParentFile().mkdirs();
            storage.createNewFile();
        }

        initLibrary();

        //<editor-fold desc="Logos">
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
        //</editor-fold>

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
                Todo t = library.get(index);
                t.setCompletion(true);
                writeToStorage(t, index);
                printList();

            } else if (userInput.toLowerCase().startsWith("unmark")) {
                // e.g. "unmark 1"
                int index = Integer.parseInt(userInput.split("\\s+")[1]) - 1;
                Todo t = library.get(index);
                t.setCompletion(false);
                writeToStorage(t, index);
                printList();

            } else if (userInput.toLowerCase().startsWith("todo")) {
                // "todo borrow book"
                String desc = userInput.substring(4).trim();
                Todo t = new Todo(desc);
                library.add(t);
                printAdded(t);
                writeToStorage(t, library.size() - 1);

            } else if (userInput.toLowerCase().startsWith("deadline")) {
                // "deadline return book /by Sunday"
                String[] parts = userInput.substring(8).split("/by", 2);
                String desc = parts[0].trim();
                String by = parts[1].trim();
                Todo.Deadline d = new Todo.Deadline(desc, by);
                library.add(d);
                printAdded(d);
                writeToStorage(d, library.size() - 1);

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
                writeToStorage(e, library.size() - 1);

            } else if  (userInput.toLowerCase().startsWith("delete")) {
                System.out.println("Noted. I've removed this task:");
                int index = Integer.parseInt(userInput.split("\\s+")[1]) - 1;
                printRemoved(library.get(index));
                library.remove(index);
                deleteFromStorage(index);
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

    private static void writeToStorage(Todo t, int index) throws IOException {
        Path path = Paths.get(storage.getAbsolutePath());
        List<String> lines = Files.readAllLines(path);

        if (lines.isEmpty() || index >= lines.size()) {
            lines.add(t.toString());
            Files.write(path, lines);
        } else {
            lines.set(index, t.toString());
            Files.write(path, lines);
        }
    }

    private static void deleteFromStorage(int index) throws IOException {
        Path path = Paths.get(storage.getAbsolutePath());
        List<String> lines = Files.readAllLines(path);

        lines.remove(index);
        Files.write(path, lines);
    }

    private static void initLibrary() throws IOException {
        Scanner sc = new Scanner(storage);
        while (sc.hasNextLine()) {
            String line = sc.nextLine().trim();

            if (line.isEmpty()) continue; // skip blank lines

            String type = line.substring(1, 2);   // T, D, or E
            boolean completed = line.charAt(4) == 'X';

            Todo task = null;
            if (type.equals("T")) {
                String desc = line.substring(7);
                task = new Todo(desc);
            } else if (type.equals("D")) {
                int byIdx = line.indexOf("(by");
                String desc = line.substring(7, byIdx).trim();
                String by = line.substring(byIdx + 4, line.length() - 1).trim();
                task = new Todo.Deadline(desc, by);
            } else if (type.equals("E")) {
                int fromIdx = line.indexOf("(from:");
                int toIdx = line.indexOf("to:", fromIdx);
                String desc = line.substring(7, fromIdx).trim();
                String from = line.substring(fromIdx + 6, toIdx).trim();
                String to = line.substring(toIdx + 3, line.length() - 1).trim();
                task = new Todo.Event(desc, from, to);
            }

            if (task != null) {
                task.setCompletion(completed);
                library.add(task);
            }
        }
        sc.close();
    }
}

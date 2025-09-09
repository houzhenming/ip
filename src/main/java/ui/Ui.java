package ui;

import todo.Todo;

import java.util.List;
import java.util.Scanner;

public class Ui {
    private final Scanner in = new Scanner(System.in);

    private static final String LOGO = """
  _____ _   _ _____ _____ _    _ 
 / ____| \\ | |_   _/ ____| |  | |
| (___ |  \\| | || |    | |__| |
 \\___ \\| . ` | | || |    |  __  |
 ____) | |\\  |_| || |____| |  | |
|_____/|_| \\_|_____\\_____|_|  |_|
""";

    private static final String ART = """
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

    // ---- changed from void: now returns the full welcome text ----
    public String showWelcome() {
        // previously printed on three separate lines; replicate with newlines
        return "Hello from\n" + LOGO + "\n" + ART + "\nWhat can I do for you?";
    }

    // unchanged: still prompts and returns the user’s input
    public String readCommand() {
        System.out.print("You: ");
        return in.nextLine().trim();
    }

    // ---- changed from void ----
    public String showGoodbye() {
        return "Bot: Bye. Hope to see you again soon!";
    }

    // ---- changed from void ----
    public String showList(List<Todo> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks in your list:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append('.').append(tasks.get(i)).append('\n');
        }
        // Trim the trailing newline to match typical single-print behavior
        return sb.toString().trim();
    }

    // ---- changed from void ----
    public String showFind(List<Todo> tasks) {
        StringBuilder sb = new StringBuilder();
        sb.append("Here are the tasks that match your description:\n");
        for (int i = 0; i < tasks.size(); i++) {
            sb.append(i + 1).append('.').append(tasks.get(i)).append('\n');
        }
        return sb.toString().trim();
    }

    // ---- changed from void ----
    public String showAdded(Todo t, int total) {
        // exactly what was printed, line by line
        return "Got it. I've added this task:\n"
                + "  " + t + "\n"
                + "Now you have " + total + " tasks.";
    }

    // ---- changed from void ----
    public String showRemoved(Todo t, int totalAfterRemoval) {
        return "Removed task: " + t + "\n"
                + "Now you have " + totalAfterRemoval + " tasks.";
    }

    // ---- changed from void ----
    public String showUnknown() {
        return "nani desu ka?";
    }

    // ---- changed from void ----
    public String showError(String message) {
        return "Error: " + message;
    }
}
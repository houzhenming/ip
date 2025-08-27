import java.util.List;
import java.util.Scanner;

public class Ui {
    private final Scanner in = new Scanner(System.in);

    private static final String LOGO = """
  _____ _   _ _____ _____ _    _ 
 / ____| \\ | |_   _/ ____| |  | |
| (___ |  \\| | | || |    | |__| |
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

    public void showWelcome() {
        System.out.println("Hello from\n" + LOGO);
        System.out.println(ART);
        System.out.println("What can I do for you?");
    }

    public String readCommand() {
        System.out.print("You: ");
        return in.nextLine().trim();
    }

    public void showGoodbye() {
        System.out.println("Bot: Bye. Hope to see you again soon!");
    }

    public void showList(List<Todo> tasks) {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println((i + 1) + "." + tasks.get(i));
        }
    }

    public void showAdded(Todo t, int total) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + total + " tasks.");
    }

    public void showRemoved(Todo t, int totalAfterRemoval) {
        System.out.println("Removed task: " + t);
        System.out.println("Now you have " + totalAfterRemoval + " tasks.");
    }

    public void showUnknown() {
        System.out.println("nani desu ka?");
    }

    public void showError(String message) {
        System.out.println("Error: " + message);
    }
}
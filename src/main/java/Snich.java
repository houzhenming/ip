import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Snich {

    static ArrayList<String> library = new  ArrayList<String>();
    static ArrayList<Boolean> checkList = new ArrayList<Boolean>();

    public static void printList() {
        AtomicInteger counter = new AtomicInteger(1);
        library.forEach(x -> {
            String check;
            if (checkList.get(counter.get() - 1)) {
                check = "X";
            } else {
                check = " ";
            }
            System.out.println(counter + ". " + "[" + check + "] " + x);
            counter.addAndGet(1);
        });
    }

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
            String userInput = input.nextLine();

            if (userInput.equalsIgnoreCase("bye")) {
                System.out.println("Bot: Bye. Hope to see you again soon!");
                break; // exit loop
            } else if (userInput.equalsIgnoreCase("list")) {
                printList();
            } else if (userInput.startsWith("mark")) {
                checkList.set(Integer.parseInt(userInput.substring(userInput.length() - 1)) - 1, true);
                printList();
            } else if (userInput.startsWith("unmark")) {
                checkList.set(Integer.parseInt(userInput.substring(userInput.length() - 1)) - 1, false);
                printList();
            } else {
                System.out.println("Added: " + userInput);
                library.add(userInput);
                checkList.add(false);
            }
        }

        input.close();
    }
}

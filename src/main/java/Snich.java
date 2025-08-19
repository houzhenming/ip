import java.util.Scanner;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class Snich {
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
        ArrayList<String> library = new  ArrayList<String>();

        while (true) {
            System.out.print("You: ");
            String userInput = input.nextLine();

            if (userInput.equalsIgnoreCase("bye")) {
                System.out.println("Bot: Bye. Hope to see you again soon!");
                break; // exit loop
            } else if (userInput.equalsIgnoreCase("list")) {
                AtomicInteger counter = new AtomicInteger(1);
                library.forEach(x -> {
                    System.out.println(counter + ". " + x);
                    counter.addAndGet(1);
                });
            } else {
                System.out.println("Added: " + userInput);
                library.add(userInput);
            }
        }

        input.close();
    }
}

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;

public class Snich {
    static ArrayList<Todo> library = new ArrayList<>();
    static File storage = new File("data/toDoList.txt");
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) throws IOException {
        if (!storage.exists()) {
            storage.getParentFile().mkdirs();
            storage.createNewFile();
        }
        initLibrary();

        Ui ui = new Ui();
        ui.showWelcome();

        while (true) {
            String userInput = ui.readCommand();

            if (userInput.equalsIgnoreCase("bye")) {
                ui.showGoodbye();
                break;

            } else if (userInput.equalsIgnoreCase("list")) {
                ui.showList(library);

            } else if (userInput.toLowerCase().startsWith("mark")) {
                int index = Integer.parseInt(userInput.split("\\s+")[1]) - 1;
                Todo t = library.get(index);
                t.setCompletion(true);
                writeToStorage(t, index);
                ui.showList(library);

            } else if (userInput.toLowerCase().startsWith("unmark")) {
                int index = Integer.parseInt(userInput.split("\\s+")[1]) - 1;
                Todo t = library.get(index);
                t.setCompletion(false);
                writeToStorage(t, index);
                ui.showList(library);

            } else if (userInput.toLowerCase().startsWith("todo")) {
                String desc = userInput.substring(4).trim();
                Todo t = new Todo(desc);
                library.add(t);
                ui.showAdded(t, library.size());
                writeToStorage(t, library.size() - 1);

            } else if (userInput.toLowerCase().startsWith("deadline")) {
                String[] parts = userInput.substring(8).split("/by", 2);
                String desc = parts[0].trim();
                LocalDateTime by = LocalDateTime.parse(parts[1].trim(), FORMATTER);
                Todo.Deadline d = new Todo.Deadline(desc, by);
                library.add(d);
                ui.showAdded(d, library.size());
                writeToStorage(d, library.size() - 1);

            } else if (userInput.toLowerCase().startsWith("event")) {
                String[] p1 = userInput.substring(5).split("/from", 2);
                String desc = p1[0].trim();
                String[] p2 = p1[1].split("/to", 2);
                LocalDateTime from = LocalDateTime.parse(p2[0].trim(), FORMATTER);
                LocalDateTime to = LocalDateTime.parse(p2[1].trim(), FORMATTER);
                Todo.Event e = new Todo.Event(desc, from, to);
                library.add(e);
                ui.showAdded(e, library.size());
                writeToStorage(e, library.size() - 1);

            } else if (userInput.toLowerCase().startsWith("delete")) {
                int index = Integer.parseInt(userInput.split("\\s+")[1]) - 1;
                Todo removed = library.get(index);
                ui.showRemoved(removed, library.size() - 1);
                library.remove(index);
                deleteFromStorage(index);

            } else {
                ui.showUnknown();
            }
        }
    }

    // ---------------- helper methods ----------------
    private static void printList() {
        System.out.println("Here are the tasks in your list:");
        for (int i = 0; i < library.size(); i++) {
            System.out.println((i + 1) + "." + library.get(i));
        }
    }

    private static void printAdded(Todo t) {
        System.out.println("Got it. I've added this task:");
        System.out.println("  " + t);
        System.out.println("Now you have " + library.size() + " tasks.");
    }

    private static void printRemoved(Todo t) {
        System.out.println("Removed task: " + t);
        System.out.println("Now you have " + (library.size()) + " tasks.");
    }

    private static void writeToStorage(Todo t, int index) throws IOException {
        Path path = Paths.get(storage.getAbsolutePath());
        List<String> lines = Files.exists(path) ? Files.readAllLines(path) : new ArrayList<>();

        if (index >= lines.size()) {
            lines.add(t.toString());
        } else {
            lines.set(index, t.toString());
        }
        Files.write(path, lines);
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
            if (line.isEmpty()) continue;

            String type = line.substring(1, 2);   // T, D, or E
            boolean completed = line.charAt(4) == 'X';

            Todo task = null;
            if (type.equals("T")) {
                String desc = line.substring(7);
                task = new Todo(desc);
            } else if (type.equals("D")) {
                int byIdx = line.indexOf("(by");
                String desc = line.substring(7, byIdx).trim();
                String byStr = line.substring(byIdx + 4, line.length() - 1).trim();
                LocalDateTime by = LocalDateTime.parse(byStr, FORMATTER);
                task = new Todo.Deadline(desc, by);
            } else if (type.equals("E")) {
                int fromIdx = line.indexOf("(from:");
                int toIdx = line.indexOf("to:", fromIdx);
                String desc = line.substring(7, fromIdx).trim();
                String fromStr = line.substring(fromIdx + 6, toIdx).trim();
                String toStr = line.substring(toIdx + 3, line.length() - 1).trim();
                LocalDateTime from = LocalDateTime.parse(fromStr, FORMATTER);
                LocalDateTime to = LocalDateTime.parse(toStr, FORMATTER);
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
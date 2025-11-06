package storage;

import todo.Todo;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Storage {
    private File storageFile;
    private final DateTimeFormatter formatter;

    public Storage(String filepath, DateTimeFormatter formatter) throws IOException {
        this.storageFile = new File(filepath);
        this.formatter = formatter;
        ensureFile();
    }

    public void newFilePath(String filepath) throws IOException {
        this.storageFile = new File(filepath);
        ensureFile();
    }

    private void ensureFile() throws IOException {
        if (!storageFile.exists()) {
            File parent = storageFile.getParentFile();
            if (parent != null) parent.mkdirs();
            storageFile.createNewFile();
        }
    }

    public List<Todo> load() throws IOException {
        List<Todo> loaded = new ArrayList<>();
        try (Scanner sc = new Scanner(storageFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (!line.isEmpty()) {
                    Todo task = parseTask(line);
                    if (task != null) loaded.add(task);
                }
            }
        }
        return loaded;
    }

    /** Parse one task line and return a Todo object, or null if invalid. */
    private Todo parseTask(String line) {
        try {
            String type = line.substring(1, 2);
            boolean completed = line.charAt(4) == 'X';
            Todo task = switch (type) {
                case "T" -> parseTodo(line);
                case "D" -> parseDeadline(line);
                case "E" -> parseEvent(line);
                default -> null;
            };
            if (task != null) task.setCompletion(completed);
            return task;
        } catch (Exception e) {
            return null; // ignore malformed lines
        }
    }

    private Todo parseTodo(String line) {
        return new Todo(line.substring(7));
    }

    private Todo parseDeadline(String line) {
        int byIdx = line.indexOf("(by");
        String desc = line.substring(7, byIdx).trim();
        String byStr = line.substring(byIdx + 4, line.length() - 1).trim();
        LocalDateTime by = LocalDateTime.parse(byStr, formatter);
        return new Todo.Deadline(desc, by);
    }

    private Todo parseEvent(String line) {
        int fromIdx = line.indexOf("(from:");
        int toIdx = line.indexOf("to:", fromIdx);
        String desc = line.substring(7, fromIdx).trim();
        String fromStr = line.substring(fromIdx + 6, toIdx).trim();
        String toStr = line.substring(toIdx + 3, line.length() - 1).trim();
        LocalDateTime from = LocalDateTime.parse(fromStr, formatter);
        LocalDateTime to = LocalDateTime.parse(toStr, formatter);
        return new Todo.Event(desc, from, to);
    }

    /** Upsert a single task line at index. */
    public void saveAt(Todo t, int index) throws IOException {
        Path path = Paths.get(storageFile.getAbsolutePath());
        List<String> lines = Files.exists(path) ? Files.readAllLines(path) : new ArrayList<>();
        String line = t.toString();
        if (index >= lines.size()) {
            lines.add(line);
        } else {
            lines.set(index, line);
        }
        Files.write(path, lines);
    }

    /** Delete a single task line at index. */
    public void deleteAt(int index) throws IOException {
        Path path = Paths.get(storageFile.getAbsolutePath());
        List<String> lines = Files.readAllLines(path);
        if (index >= 0 && index < lines.size()) {
            lines.remove(index);
            Files.write(path, lines);
        }
    }
}
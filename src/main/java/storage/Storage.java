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

    /** Load tasks from storage and return them. */
    public List<Todo> load() throws IOException {
        List<Todo> loaded = new ArrayList<>();

        try (Scanner sc = new Scanner(storageFile)) {
            while (sc.hasNextLine()) {
                String line = sc.nextLine().trim();
                if (line.isEmpty()) {
                    continue; // skip empty lines early
                }

                String type = line.substring(1, 2); // T, D, or E
                boolean completed = line.charAt(4) == 'X';
                Todo task;

                switch (type) {
                    case "T":
                        String desc = line.substring(7);
                        task = new Todo(desc);
                        break;

                    case "D":
                        int byIdx = line.indexOf("(by");
                        String dDesc = line.substring(7, byIdx).trim();
                        String byStr = line.substring(byIdx + 4, line.length() - 1).trim();
                        LocalDateTime by = LocalDateTime.parse(byStr, formatter);
                        task = new Todo.Deadline(dDesc, by);
                        break;

                    case "E":
                        int fromIdx = line.indexOf("(from:");
                        int toIdx = line.indexOf("to:", fromIdx);
                        String eDesc = line.substring(7, fromIdx).trim();
                        String fromStr = line.substring(fromIdx + 6, toIdx).trim();
                        String toStr = line.substring(toIdx + 3, line.length() - 1).trim();
                        LocalDateTime from = LocalDateTime.parse(fromStr, formatter);
                        LocalDateTime to = LocalDateTime.parse(toStr, formatter);
                        task = new Todo.Event(eDesc, from, to);
                        break;

                    default:
                        continue; // unknown type, skip this line
                }

                task.setCompletion(completed);
                loaded.add(task);
            }
        }

        return loaded;
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
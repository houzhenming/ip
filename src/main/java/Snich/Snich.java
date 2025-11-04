package Snich;

import storage.Storage;
import todo.Todo;
import todo.TodoList;
import ui.Parser;
import ui.Ui;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class Snich {
    private final TodoList todoList;
    private final Storage storage;
    private final Parser parser;
    private final Ui ui;

    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Snich() throws IOException {
        this.storage = new Storage("data/toDoList.txt", FORMATTER);
        this.todoList = new TodoList(storage.load());
        this.parser = new Parser(Todo.STORAGE_FORMAT);
        this.ui = new Ui();
    }

    /** used ChatGPT to clean up code in following methods.
     * Originally used if else blocks, used AI to change o switch block.
     * @param input
     * @return String (response from Snich)
     * @throws IOException
     */
    public String getResponse(String input) throws IOException {
        String userInput = input;

        Parser.ParsedCommand pc = null;
        try {
            pc = parser.parse(userInput);
        } catch (IllegalArgumentException ex) {
            ui.showError(ex.getMessage());
        }

        assert pc != null : "Parser should never return null";

        switch (pc.type) {
            case BYE:
                return ui.showGoodbye();

            case LIST: {
                return ui.showList(todoList.asList());
            }

            case FIND: {
                return ui.showFind(todoList.filter(pc.desc));
            }
            case MARK: {
                assert pc.index > 0 && pc.index <= todoList.size() : "Index out of range: " + pc.index;
                Todo t = todoList.mark(pc.index);
                storage.saveAt(t, TodoList.toZeroBased(pc.index));
                return ui.showList(todoList.asList());
            }

            case UNMARK: {
                assert pc.index > 0 && pc.index <= todoList.size() : "Index out of range: " + pc.index;
                Todo t = todoList.unmark(pc.index);
                storage.saveAt(t, TodoList.toZeroBased(pc.index));
                return ui.showList(todoList.asList());
            }

            case DELETE: {
                assert pc.index > 0 && pc.index <= todoList.size() : "Index out of range: " + pc.index;
                Todo removed = todoList.remove(pc.index);
                storage.deleteAt(TodoList.toZeroBased(pc.index));
                return ui.showRemoved(removed, todoList.size());
            }

            case TODO: {
                Todo t = new Todo(pc.desc);
                int idx = todoList.addAndIndex(t); // 1-based
                storage.saveAt(t, idx - 1);
                return ui.showAdded(t, todoList.size());
            }

            case DEADLINE: {
                Todo.Deadline d = new Todo.Deadline(pc.desc, pc.by);
                int idx = todoList.addAndIndex(d);
                storage.saveAt(d, idx - 1);
                return ui.showAdded(d, todoList.size());
            }

            case EVENT: {
                Todo.Event e = new Todo.Event(pc.desc, pc.from, pc.to);
                int idx = todoList.addAndIndex(e);
                storage.saveAt(e, idx - 1);
                return ui.showAdded(e, todoList.size());
            }

            case REBASE: {
                storage.newFilePath(pc.filepath);
                todoList.clear();
                todoList.add(storage.load());
                return ui.showRebased(pc.filepath);
            }

            default:
                return ui.showUnknown();
        }
    }
}

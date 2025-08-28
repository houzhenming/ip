import storage.Storage;
import todo.Todo;
import todo.TodoList;
import ui.Parser;
import ui.Ui;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class Snich {
    static TodoList todoList = new TodoList();   // ⬅️ was ArrayList<todo.Todo>
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) throws IOException {
        Storage storage = new Storage("data/toDoList.txt", FORMATTER);

        // load tasks
        todoList = new TodoList(storage.load());
        // create ui object
        Ui ui = new Ui();
        ui.showWelcome();

        Parser parser = new Parser(Todo.STORAGE_FORMAT);

        while (true) {
            String userInput = ui.readCommand();

            Parser.ParsedCommand pc;
            try {
                pc = parser.parse(userInput);
            } catch (IllegalArgumentException ex) {
                ui.showError(ex.getMessage());
                continue;
            }

            switch (pc.type) {
                case BYE:
                    ui.showGoodbye();
                    return;

                case LIST:
                    ui.showList(todoList.asList());
                    break;

                case MARK: {
                    Todo t = todoList.mark(pc.index);
                    storage.saveAt(t, TodoList.toZeroBased(pc.index));
                    ui.showList(todoList.asList());
                    break;
                }

                case UNMARK: {
                    Todo t = todoList.unmark(pc.index);
                    storage.saveAt(t, TodoList.toZeroBased(pc.index));
                    ui.showList(todoList.asList());
                    break;
                }

                case DELETE: {
                    Todo removed = todoList.remove(pc.index);
                    storage.deleteAt(TodoList.toZeroBased(pc.index));
                    ui.showRemoved(removed, todoList.size());
                    break;
                }

                case TODO: {
                    Todo t = new Todo(pc.desc);
                    int idx = todoList.addAndIndex(t); // 1-based
                    storage.saveAt(t, idx - 1);
                    ui.showAdded(t, todoList.size());
                    break;
                }

                case DEADLINE: {
                    Todo.Deadline d = new Todo.Deadline(pc.desc, pc.by);
                    int idx = todoList.addAndIndex(d);
                    storage.saveAt(d, idx - 1);
                    ui.showAdded(d, todoList.size());
                    break;
                }

                case EVENT: {
                    Todo.Event e = new Todo.Event(pc.desc, pc.from, pc.to);
                    int idx = todoList.addAndIndex(e);
                    storage.saveAt(e, idx - 1);
                    ui.showAdded(e, todoList.size());
                    break;
                }

                default:
                    ui.showUnknown();
            }
        }
    }
}
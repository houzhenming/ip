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

    public String getResponse(String input) throws IOException {
        String userInput = input;

        Parser.ParsedCommand pc = null;
        try {
            pc = parser.parse(userInput);
        } catch (IllegalArgumentException ex) {
            ui.showError(ex.getMessage());
        }

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
                Todo t = todoList.mark(pc.index);
                storage.saveAt(t, TodoList.toZeroBased(pc.index));
                return ui.showList(todoList.asList());
            }

            case UNMARK: {
                Todo t = todoList.unmark(pc.index);
                storage.saveAt(t, TodoList.toZeroBased(pc.index));
                return ui.showList(todoList.asList());
            }

            case DELETE: {
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

            default:
                return ui.showUnknown();
        }
    }
}


//    public static void main(String[] args) throws IOException {
//        Storage storage = new Storage("data/toDoList.txt", FORMATTER);
//
//        // load tasks
//        todoList = new TodoList(storage.load());
//        // create ui object
//        Ui ui = new Ui();
//        ui.showWelcome();
//
//        Parser parser = new Parser(Todo.STORAGE_FORMAT);
//
//        while (true) {
//            String userInput = ui.readCommand();
//
//            Parser.ParsedCommand pc;
//            try {
//                pc = parser.parse(userInput);
//            } catch (IllegalArgumentException ex) {
//                ui.showError(ex.getMessage());
//                continue;
//            }
//
//            switch (pc.type) {
//                case BYE:
//                    ui.showGoodbye();
//                    return;
//
//                case LIST: {
//                    ui.showList(todoList.asList());
//                    break;
//                }
//
//                case FIND: {
//                    ui.showFind(todoList.filter(pc.desc));
//                    break;
//                }
//                case MARK: {
//                    Todo t = todoList.mark(pc.index);
//                    storage.saveAt(t, TodoList.toZeroBased(pc.index));
//                    ui.showList(todoList.asList());
//                    break;
//                }
//
//                case UNMARK: {
//                    Todo t = todoList.unmark(pc.index);
//                    storage.saveAt(t, TodoList.toZeroBased(pc.index));
//                    ui.showList(todoList.asList());
//                    break;
//                }
//
//                case DELETE: {
//                    Todo removed = todoList.remove(pc.index);
//                    storage.deleteAt(TodoList.toZeroBased(pc.index));
//                    ui.showRemoved(removed, todoList.size());
//                    break;
//                }
//
//                case TODO: {
//                    Todo t = new Todo(pc.desc);
//                    int idx = todoList.addAndIndex(t); // 1-based
//                    storage.saveAt(t, idx - 1);
//                    ui.showAdded(t, todoList.size());
//                    break;
//                }
//
//                case DEADLINE: {
//                    Todo.Deadline d = new Todo.Deadline(pc.desc, pc.by);
//                    int idx = todoList.addAndIndex(d);
//                    storage.saveAt(d, idx - 1);
//                    ui.showAdded(d, todoList.size());
//                    break;
//                }
//
//                case EVENT: {
//                    Todo.Event e = new Todo.Event(pc.desc, pc.from, pc.to);
//                    int idx = todoList.addAndIndex(e);
//                    storage.saveAt(e, idx - 1);
//                    ui.showAdded(e, todoList.size());
//                    break;
//                }
//
//                default:
//                    ui.showUnknown();
//            }
//        }
//    }

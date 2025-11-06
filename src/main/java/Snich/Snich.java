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
        Parser.ParsedCommand pc;
        try {
            pc = parser.parse(input);
        } catch (IllegalArgumentException ex) {
            ui.showError(ex.getMessage());
            return ui.showUnknown();
        }

        assert pc != null : "Parser should never return null";

        return switch (pc.type) {
            case BYE      -> handleBye();
            case LIST     -> handleList();
            case FIND     -> handleFind(pc.desc);
            case MARK     -> handleMark(pc.index);
            case UNMARK   -> handleUnmark(pc.index);
            case DELETE   -> handleDelete(pc.index);
            case TODO     -> handleTodo(pc.desc);
            case DEADLINE -> handleDeadline(pc.desc, pc.by);
            case EVENT    -> handleEvent(pc.desc, pc.from, pc.to);
            case REBASE   -> handleRebase(pc.filepath);
            default       -> ui.showUnknown();
        };
    }

    public String showWelcome() {
        return ui.showWelcome();
    }

    /**
     * Handles the "bye" command by returning the goodbye message.
     *
     * @return a farewell message from the UI
     */
    private String handleBye() {
        return ui.showGoodbye();
    }

    /**
     * Handles the "list" command by showing all existing todos.
     *
     * @return a formatted list of todos from the UI
     */
    private String handleList() {
        return ui.showList(todoList.asList());
    }

    /**
     * Handles the "find" command by filtering todos based on the given query.
     *
     * @param query the keyword or phrase to search for
     * @return a formatted list of matching todos from the UI
     */
    private String handleFind(String query) {
        return ui.showFind(todoList.filter(query));
    }

    /**
     * Validates that the given index is within the valid range of the todo list.
     * Throws an assertion error if the index is invalid.
     *
     * @param oneBasedIndex the 1-based index to validate
     */
    private void validateIndex(int oneBasedIndex) {
        assert oneBasedIndex > 0 && oneBasedIndex <= todoList.size()
                : "Index out of range: " + oneBasedIndex;
    }

    /**
     * Handles the "mark" command by marking the specified todo as completed.
     *
     * @param index the 1-based index of the todo to mark
     * @return a formatted list of updated todos from the UI
     * @throws IOException if saving to storage fails
     */
    private String handleMark(int index) throws IOException {
        validateIndex(index);
        Todo t = todoList.mark(index);
        storage.saveAt(t, TodoList.toZeroBased(index));
        return ui.showList(todoList.asList());
    }

    /**
     * Handles the "unmark" command by marking the specified todo as incomplete.
     *
     * @param index the 1-based index of the todo to unmark
     * @return a formatted list of updated todos from the UI
     * @throws IOException if saving to storage fails
     */
    private String handleUnmark(int index) throws IOException {
        validateIndex(index);
        Todo t = todoList.unmark(index);
        storage.saveAt(t, TodoList.toZeroBased(index));
        return ui.showList(todoList.asList());
    }

    /**
     * Handles the "delete" command by removing the specified todo from the list.
     *
     * @param index the 1-based index of the todo to delete
     * @return a message indicating the removed todo and remaining list size
     * @throws IOException if deletion from storage fails
     */
    private String handleDelete(int index) throws IOException {
        validateIndex(index);
        Todo removed = todoList.remove(index);
        storage.deleteAt(TodoList.toZeroBased(index));
        return ui.showRemoved(removed, todoList.size());
    }

    /**
     * Handles the "todo" command by adding a simple todo item.
     *
     * @param desc the description of the todo
     * @return a message confirming the added todo
     * @throws IOException if saving to storage fails
     */
    private String handleTodo(String desc) throws IOException {
        Todo t = new Todo(desc);
        int idx = todoList.addAndIndex(t); // 1-based
        storage.saveAt(t, idx - 1);
        return ui.showAdded(t, todoList.size());
    }

    /**
     * Handles the "deadline" command by adding a deadline task.
     *
     * @param desc the description of the deadline
     * @param by   the date and time by which the task is due
     * @return a message confirming the added deadline
     * @throws IOException if saving to storage fails
     */
    private String handleDeadline(String desc, java.time.LocalDateTime by) throws IOException {
        Todo.Deadline d = new Todo.Deadline(desc, by);
        int idx = todoList.addAndIndex(d);
        storage.saveAt(d, idx - 1);
        return ui.showAdded(d, todoList.size());
    }

    /**
     * Handles the "event" command by adding an event with start and end times.
     *
     * @param desc the description of the event
     * @param from the start date and time of the event
     * @param to   the end date and time of the event
     * @return a message confirming the added event
     * @throws IOException if saving to storage fails
     */
    private String handleEvent(String desc, java.time.LocalDateTime from, java.time.LocalDateTime to) throws IOException {
        Todo.Event e = new Todo.Event(desc, from, to);
        int idx = todoList.addAndIndex(e);
        storage.saveAt(e, idx - 1);
        return ui.showAdded(e, todoList.size());
    }

    /**
     * Handles the "rebase" command by changing the file storage path and reloading data.
     *
     * @param filepath the new file path for storage
     * @return a message confirming the successful rebase
     * @throws IOException if reloading or saving data fails
     */
    private String handleRebase(String filepath) throws IOException {
        storage.newFilePath(filepath);
        todoList.clear();
        todoList.add(storage.load());
        return ui.showRebased(filepath);
    }
}

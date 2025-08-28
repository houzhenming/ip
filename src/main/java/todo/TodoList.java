package todo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TodoList {
    private final ArrayList<Todo> items;

    public TodoList() {
        this.items = new ArrayList<>();
    }

    public TodoList(List<Todo> initial) {
        this.items = new ArrayList<>(initial == null ? List.of() : initial);
    }

    /** Number of tasks. */
    public int size() {
        return items.size();
    }

    /** Read-only view for UI rendering. */
    public List<Todo> asList() {
        return Collections.unmodifiableList(items);
    }

    /** Add any todo.Todo (todo.Todo, Deadline, Event). Returns the added task. */
    public Todo add(Todo t) {
        if (t == null) throw new IllegalArgumentException("Task cannot be null");
        items.add(t);
        return t;
    }

    /** Remove by 1-based index (as users see). Returns the removed task. */
    public Todo remove(int oneBasedIndex) {
        int i = toZeroBased(oneBasedIndex);
        return items.remove(i);
    }

    /** Get by 1-based index (as users see). */
    public Todo get(int oneBasedIndex) {
        int i = toZeroBased(oneBasedIndex);
        return items.get(i);
    }

    /** Mark complete by 1-based index. Returns the task. */
    public Todo mark(int oneBasedIndex) {
        Todo t = get(oneBasedIndex);
        t.setCompletion(true);
        return t;
    }

    /** Unmark by 1-based index. Returns the task. */
    public Todo unmark(int oneBasedIndex) {
        Todo t = get(oneBasedIndex);
        t.setCompletion(false);
        return t;
    }

    /** Append helpers that return the 1-based index for convenience. */
    public int addAndIndex(Todo t) {
        add(t);
        return items.size(); // new item is last → 1-based index
    }

    /** Zero-based index for snich.storage.Storage.saveAt/deleteAt interop. */
    public static int toZeroBased(int oneBasedIndex) {
        if (oneBasedIndex <= 0) {
            throw new IllegalArgumentException("Index must be >= 1 (was " + oneBasedIndex + ")");
        }
        return oneBasedIndex - 1;
    }
}
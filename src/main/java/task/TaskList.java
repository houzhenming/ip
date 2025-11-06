package task;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaskList {
    private final ArrayList<Task> items;

    public TaskList() {
        this.items = new ArrayList<>();
    }

    public TaskList(List<Task> initial) {
        this.items = new ArrayList<>(initial == null ? List.of() : initial);
    }

    /**
     * Returns the number of tasks in the todo list.
     *
     * @return number of tasks
     */
    public int size() {
        return items.size();
    }

    /**
     * Returns an unmodifiable view of the todo list for UI rendering.
     *
     * @return read-only list of todos
     */
    public List<Task> asList() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Clears all tasks from the todo list.
     */
    public void clear() {
        items.clear();
    }

    /**
     * Adds a single todo item to the list.
     *
     * @param t the todo item to add (must not be null)
     * @return the added todo
     * @throws IllegalArgumentException if the task is null
     */
    public Task add(Task t) {
        if (t == null) throw new IllegalArgumentException("Task cannot be null");
        items.add(t);
        return t;
    }

    /**
     * Adds a list of todo items to the list.
     *
     * @param todos the list of todos to add (must not be null and must not contain nulls)
     * @return number of tasks added
     * @throws IllegalArgumentException if the list is null or contains a null task
     */
    public int add(List<Task> todos) {
        if (todos == null) {
            throw new IllegalArgumentException("Task list cannot be null");
        }
        for (Task t : todos) {
            if (t == null) {
                throw new IllegalArgumentException("Task list contains null task");
            }
        }
        items.addAll(todos);
        return todos.size();
    }

    /**
     * Removes a task by its 1-based index.
     *
     * @param oneBasedIndex index of the task to remove (1-based)
     * @return the removed task
     * @throws IllegalArgumentException if index is invalid
     */
    public Task remove(int oneBasedIndex) {
        int i = toZeroBased(oneBasedIndex);
        return items.remove(i);
    }

    /**
     * Retrieves a task by its 1-based index.
     *
     * @param oneBasedIndex index of the task to get (1-based)
     * @return the retrieved task
     * @throws IllegalArgumentException if index is invalid
     */
    public Task get(int oneBasedIndex) {
        int i = toZeroBased(oneBasedIndex);
        return items.get(i);
    }

    /**
     * Marks a task as complete by its 1-based index.
     *
     * @param oneBasedIndex index of the task to mark (1-based)
     * @return the updated task
     */
    public Task mark(int oneBasedIndex) {
        Task t = get(oneBasedIndex);
        t.setCompletion(true);
        return t;
    }

    /**
     * Unmarks a task (sets as incomplete) by its 1-based index.
     *
     * @param oneBasedIndex index of the task to unmark (1-based)
     * @return the updated task
     */
    public Task unmark(int oneBasedIndex) {
        Task t = get(oneBasedIndex);
        t.setCompletion(false);
        return t;
    }

    /**
     * Adds a task and returns its new 1-based index.
     *
     * @param t the task to add
     * @return 1-based index of the added task
     */
    public int addAndIndex(Task t) {
        add(t);
        return items.size(); // new item is last → 1-based index
    }

    /**
     * Converts a 1-based index into a 0-based index for internal use.
     *
     * @param oneBasedIndex index starting from 1
     * @return zero-based index
     * @throws IllegalArgumentException if index < 1
     */
    public static int toZeroBased(int oneBasedIndex) {
        if (oneBasedIndex <= 0) {
            throw new IllegalArgumentException("Index must be >= 1 (was " + oneBasedIndex + ")");
        }
        return oneBasedIndex - 1;
    }

    /**
     * Finds all tasks whose descriptions contain the given substring (case-insensitive).
     *
     * @param desc the search keyword
     * @return list of matching tasks, or empty list if no matches
     */
    public List<Task> filter(String desc) {
        if (desc == null || desc.isEmpty()) {
            return List.of();
        }
        String needle = desc.toLowerCase();
        return items.stream()
                .filter(x -> {
                    String haystack = x.getDescription();
                    return haystack != null && haystack.toLowerCase().contains(needle);
                })
                .toList();
    }
}
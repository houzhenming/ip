package todo;  // same package as the class being tested

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TodoListTest {

    @Test
    public void add_and_size_shouldWork() {
        TodoList list = new TodoList();
        assertEquals(0, list.size());

        list.add(new Todo("Buy milk"));
        assertEquals(1, list.size());

        list.add(new Todo("Walk dog"));
        assertEquals(2, list.size());
    }

    @Test
    public void addAndIndex_returnsCorrectIndex() {
        TodoList list = new TodoList();
        int idx1 = list.addAndIndex(new Todo("Task A"));
        int idx2 = list.addAndIndex(new Todo("Task B"));
        assertEquals(1, idx1);
        assertEquals(2, idx2);
    }

    @Test
    public void remove_shouldReturnAndShrinkList() {
        TodoList list = new TodoList();
        list.add(new Todo("First"));
        list.add(new Todo("Second"));
        assertEquals(2, list.size());

        Todo removed = list.remove(1);
        assertEquals("First", removed.getTodo());
        assertEquals(1, list.size());
        assertEquals("Second", list.get(1).getTodo());
    }

    @Test
    public void filter_shouldReturnMatchingTodos() {
        TodoList list = new TodoList();
        list.add(new Todo("Buy milk"));
        list.add(new Todo("Walk dog"));
        list.add(new Todo("Milk the cow"));

        List<Todo> matches = list.filter("milk");
        assertEquals(2, matches.size());
        assertTrue(matches.stream().allMatch(t -> t.getTodo().toLowerCase().contains("milk")));
    }

    @Test
    public void toZeroBased_shouldThrowOnInvalidIndex() {
        assertThrows(IllegalArgumentException.class, () -> TodoList.toZeroBased(0));
        assertThrows(IllegalArgumentException.class, () -> TodoList.toZeroBased(-3));
    }

    // Optional dummy tests in the same style as your DukeTest
    @Test
    public void dummyTest() {
        assertEquals(2, 2);
    }

    @Test
    public void anotherDummyTest() {
        assertEquals(4, 4);
    }
}
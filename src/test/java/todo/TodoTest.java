package todo;  // same package as the class being tested

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TodoTest {

    @Test
    public void todo_toString_incomplete() {
        Todo t = new Todo("Buy milk");
        // default: completion = false, type = "T"
        assertEquals("[T] [ ] Buy milk", t.toString());
    }

    @Test
    public void todo_toString_completed() {
        Todo t = new Todo("Buy milk");
        t.setCompletion(true);
        assertEquals("[T] [X] Buy milk", t.toString());
    }

    @Test
    public void deadline_toString_withEndTime() {
        LocalDateTime by = LocalDateTime.of(2025, 9, 1, 13, 45);
        Todo.Deadline d = new Todo.Deadline("Submit report", by);
        // STORAGE_FORMAT: "yyyy-MM-dd HH:mm"
        assertEquals("[D] [ ] Submit report (by 2025-09-01 13:45)", d.toString());
    }

    @Test
    public void deadline_toString_nullEndTime_showsDash() {
        Todo.Deadline d = new Todo.Deadline("Submit report", null);
        assertEquals("[D] [ ] Submit report (by -)", d.toString());
    }

    @Test
    public void event_toString_withTimes() {
        LocalDateTime start = LocalDateTime.of(2025, 9, 1, 10, 0);
        LocalDateTime end   = LocalDateTime.of(2025, 9, 1, 12, 0);
        Todo.Event e = new Todo.Event("Team meeting", start, end);
        assertEquals("[E] [ ] Team meeting (from: 2025-09-01 10:00 to: 2025-09-01 12:00)", e.toString());
    }

    @Test
    public void event_toString_nullTimes_showDashes() {
        Todo.Event e = new Todo.Event("Team meeting", null, null);
        assertEquals("[E] [ ] Team meeting (from: - to: -)", e.toString());
    }

}
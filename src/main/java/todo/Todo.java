package todo;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Todo {
    private Boolean completion;
    private String todoType;
    private String todo;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    /**
     * public constructor.
     * @param todo
     */
    public Todo(String todo) {
        this.completion = false;
        this.setTodoType("T");
        this.todo = todo;
    }

    /**
     * Overloaded constructor.
     * @param todo
     * @param startTime
     * @param endTime
     */
    public Todo(String todo, LocalDateTime startTime, LocalDateTime endTime) {
        this.completion = false;
        this.todo = todo;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public static final DateTimeFormatter STORAGE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    void setTodoType(String todoType) {
        this.todoType = todoType;
    }

    public Boolean getCompletion() {
        return completion;
    }

    public void setCompletion(Boolean completion) {
        this.completion = completion;
    }

    public String getTodoType() {
        return todoType;
    }

    public String getTodo() {
        return todo;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString(){
        String x = this.getCompletion() ? "X" : " ";
        return String.format("[%s] [%s] %s",
                this.getTodoType(),
                x,
                this.getTodo());
    }

    // ---------------- Subclasses ----------------

    public static class Deadline extends Todo {

        public Deadline(String todo, LocalDateTime endTime) {
            super(todo, null, endTime);
            this.setTodoType("D");
        }

        @Override
        public String toString() {
            String x = this.getCompletion() ? "X" : " ";
            String by = (getEndTime() == null) ? "-" : getEndTime().format(STORAGE_FORMAT);
            return String.format("[%s] [%s] %s (by %s)",
                    this.getTodoType(), x, this.getTodo(), by);
        }
    }

    public static class Event extends Todo {

        public Event(String todo, LocalDateTime startTime, LocalDateTime endTime) {
            super(todo, startTime, endTime);
            this.setTodoType("E");
        }

        @Override
        public String toString() {
            String x = this.getCompletion() ? "X" : " ";
            String from = (getStartTime() == null) ? "-" : getStartTime().format(STORAGE_FORMAT);
            String to   = (getEndTime() == null)   ? "-" : getEndTime().format(STORAGE_FORMAT);
            return String.format("[%s] [%s] %s (from: %s to: %s)",
                    this.getTodoType(), x, this.getTodo(), from, to);
        }
    }
}
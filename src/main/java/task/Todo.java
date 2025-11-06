package task;

public class Todo extends Task {

    public Todo(String description) {
        super(description);
        setTaskType("T");
    }

    @Override
    public String toString() {
        String x = getCompletion() ? "X" : " ";
        return String.format("[%s] [%s] %s",
                getTaskType(), x, getDescription());
    }
}
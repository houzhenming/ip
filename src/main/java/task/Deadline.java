package task;

import java.time.LocalDateTime;

public class Deadline extends Task {

    public Deadline(String description, LocalDateTime endTime) {
        super(description, null, endTime);
        setTaskType("D");
    }

    @Override
    public String toString() {
        String x = getCompletion() ? "X" : " ";
        String by = (getEndTime() == null) ? "-" : getEndTime().format(STORAGE_FORMAT);
        return String.format("[%s] [%s] %s (by %s)",
                getTaskType(), x, getDescription(), by);
    }
}
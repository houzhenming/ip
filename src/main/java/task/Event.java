package task;

import java.time.LocalDateTime;

public class Event extends Task {

    public Event(String description, LocalDateTime startTime, LocalDateTime endTime) {
        super(description, startTime, endTime);
        setTaskType("E");
    }

    @Override
    public String toString() {
        String x = getCompletion() ? "X" : " ";
        String from = (getStartTime() == null) ? "-" : getStartTime().format(STORAGE_FORMAT);
        String to = (getEndTime() == null) ? "-" : getEndTime().format(STORAGE_FORMAT);
        return String.format("[%s] [%s] %s (from: %s to: %s)",
                getTaskType(), x, getDescription(), from, to);
    }
}
package task;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Task {
    private Boolean completion;
    private String taskType;
    private String description;
    private LocalDateTime startTime;
    private LocalDateTime endTime;

    public static final DateTimeFormatter STORAGE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public Task(String description) {
        this.completion = false;
        this.description = description;
    }

    public Task(String description, LocalDateTime startTime, LocalDateTime endTime) {
        this.completion = false;
        this.description = description;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // ---------- Getters / Setters ----------
    public Boolean getCompletion() {
        return completion;
    }

    public void setCompletion(Boolean completion) {
        this.completion = completion;
    }

    public String getTaskType() {
        return taskType;
    }

    protected void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getDescription() {
        return description;
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

    // ---------- Abstract ----------
    @Override
    public abstract String toString();
}
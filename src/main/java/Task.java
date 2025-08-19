    private Boolean completion;
    private String taskType;
    private String task;

    public Task(String task) {
        this.completion = false;
        this.setTaskType("T");
        this.task = task;
    }

    void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public Boolean getCompletion() {
        return completion;
    }

    public String getTaskType() {
        return taskType;
    }

    public String getTask() {
        return task;
    }

    @Override
    public String toString(){
        String x = this.getCompletion() ? "X" : " ";
        return String.format("[%s] [%s] %s",
                this.getTaskType(),
                x,
                this.getTask());
    }

    public class Deadline extends Task {
        private String endTime;

        public Deadline(String task, String endTime) {
            super(task);
            this.setTaskType(endTime);
            this.setTaskType("T");
        }

        void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getEndTime() {
            return endTime;
        }

        @Override
        public String toString(){
            String x = this.getCompletion() ? "X" : " ";
            return String.format("[%s] [%s] %s (by s%)",
                    this.getTaskType(),
                    x,
                    this.getTask(),
                    this.getEndTime());
        }

    }

    public class Event extends Deadline {
        private String startTime;

        public Event(String task, String startTime, String endTime) {
            super(task, endTime);
            this.setStartTime(startTime);
        }

        void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        @Override
        public String toString(){
            String x = this.getCompletion() ? "X" : " ";
            return String.format("[%s] [%s] %s (from: %s to: %s)",
                    this.getTaskType(),
                    x,
                    this.getTask(),
                    this.getEndTime());
        }
    }
}

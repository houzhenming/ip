public class Todo {
    private Boolean completion;
    private String todoType;
    private String todo;

    public Todo(String Todo) {
        this.completion = false;
        this.setTodoType("T");
        this.todo = Todo;
    }

    void setTodoType(String TodoType) {
        this.todoType = TodoType;
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

    @Override
    public String toString(){
        String x = this.getCompletion() ? "X" : " ";
        return String.format("[%s] [%s] %s",
                this.getTodoType(),
                x,
                this.getTodo());
    }

    public static class Deadline extends Todo {
        private String endTime;

        public Deadline(String Todo, String endTime) {
            super(Todo);
            this.setTodoType(endTime);
            this.setTodoType("D");
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
            return String.format("[%s] [%s] %s (by %s)",
                    this.getTodoType(),
                    x,
                    this.getTodo(),
                    this.getEndTime());
        }

    }

    public static class Event extends Deadline {
        private String startTime;

        public Event(String Todo, String startTime, String endTime) {
            super(Todo, endTime);
            this.setStartTime(startTime);
            this.setEndTime(endTime);
            this.setTodoType("E");
        }

        void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getStartTime() {
            return startTime;
        }

        @Override
        public String toString(){
            String x = this.getCompletion() ? "X" : " ";
            return String.format("[%s] [%s] %s (from: %s to: %s)",
                    this.getTodoType(),
                    x,
                    this.getTodo(),
                    this.getStartTime(),
                    this.getEndTime());
        }
    }
}

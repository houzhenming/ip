# Snich ✅

Snich is a simple, extensible **command-line task management assistant** written in Java.  
It helps you keep track of todos, deadlines, and events, while also supporting persistent storage so your tasks are saved between runs.

---

## ✨ Features

- **Basic Commands**
  - `list` – show all tasks
  - `todo <description>` – add a todo
  - `deadline <description> /by <yyyy-MM-dd HH:mm>` – add a deadline task
  - `event <description> /from <yyyy-MM-dd HH:mm> /to <yyyy-MM-dd HH:mm>` – add an event
  - `mark <index>` – mark a task as done
  - `unmark <index>` – unmark a task
  - `delete <index>` – delete a task
  - `find <keyword>` – search for tasks
  - `bye` – exit Snich

- **Extra Commands**
  - `rebase: <filepath>` – switch to a different storage file dynamically.

- **Persistent Storage**  
  Tasks are automatically saved into a text file (`data/toDoList.txt` by default).

- **Assertions & Safety**  
  Assertions are used to ensure indices are valid and parsing is safe.

- **Readable Responses**  
  Output is formatted via the `Ui` class for a consistent and friendly experience.

---

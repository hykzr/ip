# Yuan — Personal Task Manager

Yuan is a command-line task management application written in Java. It helps you track your to-dos, deadlines, and
events from the terminal, with automatic persistence so your tasks are saved between sessions.

---

## Table of Contents

- [Features](#features)
- [Usage](#usage)
    - [Adding a To-Do](#adding-a-to-do)
    - [Adding a Deadline](#adding-a-deadline)
    - [Adding an Event](#adding-an-event)
    - [Listing All Tasks](#listing-all-tasks)
    - [Marking a Task as Done](#marking-a-task-as-done)
    - [Unmarking a Task](#unmarking-a-task)
    - [Deleting a Task](#deleting-a-task)
    - [Finding Tasks](#finding-tasks)
    - [Saving Tasks](#saving-tasks)
    - [Loading Tasks](#loading-tasks)
    - [Exiting the Application](#exiting-the-application)
- [Date & Time Formats](#date--time-formats)
- [Data Persistence](#data-persistence)

---

## Features

| Feature                | Description                                                      |
|------------------------|------------------------------------------------------------------|
| **To-Do tasks**        | Simple tasks with a description and no time constraint           |
| **Deadline tasks**     | Tasks with a due date/time                                       |
| **Event tasks**        | Tasks with a start and end date/time                             |
| **Mark / Unmark**      | Toggle task completion status                                    |
| **Delete**             | Remove tasks from your list                                      |
| **Find**               | Search tasks by keyword                                          |
| **Persistent storage** | Tasks are automatically saved to and loaded from `data/yuan.txt` |
| **Manual save / load** | Explicitly save or reload the task list at any time              |

---

## Usage

Commands are typed directly into the terminal. Yuan responds with a confirmation message inside a separator box.

### Adding a To-Do

```
todo <description>
```

**Example:**

```
todo Read chapter 3
```

> `[T][ ] Read chapter 3`

---

### Adding a Deadline

```
deadline <description> /by <date/time>
```

**Example:**

```
deadline Submit assignment /by 15/3/2026 2359
```

> `[D][ ] Submit assignment (by: Mar 15 2026, 11:59PM)`

---

### Adding an Event

```
event <description> /from <start date/time> /to <end date/time>
```

**Example:**

```
event Team meeting /from 20/3/2026 1400 /to 20/3/2026 1600
```

> `[E][ ] Team meeting (from: Mar 20 2026, 2:00PM to: Mar 20 2026, 4:00PM)`

---

### Listing All Tasks

```
list
```

Displays all tasks with their index, type, status, and details.

---

### Marking a Task as Done

```
mark <task number>
```

**Example:**

```
mark 2
```

Sets the status of task 2 to done (`[X]`).

---

### Unmarking a Task

```
unmark <task number>
```

**Example:**

```
unmark 2
```

Sets the status of task 2 back to not done (`[ ]`).

---

### Deleting a Task

```
delete <task number>
```

**Example:**

```
delete 3
```

Permanently removes task 3 from the list.

---

### Finding Tasks

```
find <keyword>
```

**Example:**

```
find assignment
```

Displays all tasks whose description contains the keyword (case-sensitive).

---

### Saving Tasks

```
save
```

Manually saves the current task list to `data/yuan.txt`.

---

### Loading Tasks

```
load
```

Manually reloads the task list from `data/yuan.txt`, replacing the current in-memory list.

---

### Exiting the Application

```
bye
```

Saves the task list and exits Yuan.

---

## Date & Time Formats

Yuan accepts the following formats for deadlines and events:

| Format            | Example           |
|-------------------|-------------------|
| `d/M/yyyy HHmm`   | `15/3/2026 2359`  |
| `d/M/yyyy`        | `15/3/2026`       |
| `yyyy-MM-dd HHmm` | `2026-03-15 2359` |
| `yyyy-MM-dd`      | `2026-03-15`      |

> **Note:** When only a date is given (no time), the time defaults to `00:00` (midnight).

---

## Data Persistence

Tasks are stored in plain text at `./data/yuan.txt` relative to where you run the application. The data directory is
created automatically if it does not exist. The file format is:

```
<type> | <done> | <description> [| <date fields>]
```

- `T | 0 | Buy groceries`
- `D | 1 | Submit report | 2026-03-15 2359`
- `E | 0 | Hackathon | 2026-04-01 0900 | 2026-04-02 1800`

---
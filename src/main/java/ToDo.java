/**
 * Represents a todo task.
 * A todo task has a description but no specific date or time.
 */
public class ToDo extends Task {
    /**
     * Constructs a ToDo task.
     *
     * @param description The description of the task.
     */
    public ToDo(String description) {
        super(description);
    }

    /**
     * Returns the string representation of the todo task.
     * Format: "[T][status] description".
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Returns the string representation of the todo task for file storage.
     * Format: "T | status | description".
     *
     * @return The file format string.
     */
    @Override
    public String toFileFormat() {
        return "T | " + super.toFileFormat();
    }
}

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents a task with a deadline.
 * A deadline task has a description and a due date/time.
 */
public class Deadline extends Task {
    protected LocalDateTime by;

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");
    private static final DateTimeFormatter STORAGE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Constructs a Deadline task.
     *
     * @param description The description of the task.
     * @param by          The deadline date/time.
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns the string representation of the deadline task.
     * Format: "[D][status] description (by: formatted_date)".
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by.format(DISPLAY_FORMAT) + ")";
    }

    /**
     * Returns the string representation of the deadline task for file storage.
     * Format: "D | status | description | formatted_date".
     *
     * @return The file format string.
     */
    @Override
    public String toFileFormat() {
        return "D | " + super.toFileFormat() + " | " + by.format(STORAGE_FORMAT);
    }
}

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Represents an event task.
 * An event task has a description, a start time, and an end time.
 */
public class Event extends Task {
    protected LocalDateTime from;
    protected LocalDateTime to;

    private static final DateTimeFormatter DISPLAY_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy, h:mma");
    private static final DateTimeFormatter STORAGE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");

    /**
     * Constructs an Event task.
     *
     * @param description The description of the event.
     * @param from        The start date/time.
     * @param to          The end date/time.
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns the string representation of the event task.
     * Format: "[E][status] description (from: start to: end)".
     *
     * @return The string representation.
     */
    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)",
                super.toString(), from.format(DISPLAY_FORMAT), to.format(DISPLAY_FORMAT));
    }

    /**
     * Returns the string representation of the event task for file storage.
     * Format: "E | status | description | start | end".
     *
     * @return The file format string.
     */
    @Override
    public String toFileFormat() {
        return "E | " + super.toFileFormat() + " | " + from.format(STORAGE_FORMAT) + " | " + to.format(STORAGE_FORMAT);
    }
}

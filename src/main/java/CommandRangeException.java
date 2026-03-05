/**
 * Represents an exception indicating that a command argument (e.g., index) was out of range.
 */
public class CommandRangeException extends CommandException {
    /**
     * Constructs a CommandRangeException with the specified message.
     *
     * @param message The error message.
     */
    public CommandRangeException(String message) {
        super(message);
    }
}

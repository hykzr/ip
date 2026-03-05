/**
 * Represents an exception that occurs during command execution.
 */
public class CommandException extends Exception {
    /**
     * Constructs a CommandException with the specified message.
     *
     * @param message The error message.
     */
    public CommandException(String message) {
        super(message);
    }
}

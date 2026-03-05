/**
 * Represents an exception indicating that a command was in an invalid format.
 */
public class CommandFormatException extends CommandException {
    /**
     * Constructs a CommandFormatException with the specified message.
     *
     * @param message The error message.
     */
    public CommandFormatException(String message) {
        super(message);
    }
}

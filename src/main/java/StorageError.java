/**
 * Represents an exception that occurs during storage operations (loading/saving).
 */
public class StorageError extends Exception {
    /**
     * Constructs a StorageError with the specified message.
     *
     * @param message The error message.
     */
    public StorageError(String message) {
        super(message);
    }

    /**
     * Constructs a StorageError with the specified message and cause.
     *
     * @param message The error message.
     * @param cause   The cause of the exception.
     */
    public StorageError(String message, Throwable cause) {
        super(message, cause);
    }
}

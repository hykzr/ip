public class StorageError extends Exception {
    public StorageError(String message) {
        super(message);
    }

    public StorageError(String message, Throwable cause) {
        super(message, cause);
    }
}

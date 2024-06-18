package prelude.protocol;

public class InvalidPacketException extends Exception {
    public InvalidPacketException(String message) {
        super(message);
    }

    public InvalidPacketException(String message, Throwable cause) {
        super(message, cause);
    }
}

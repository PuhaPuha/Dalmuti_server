package mju.dalmutiserver.exception;

public class CannotAccessRoomException extends RuntimeException {
    public CannotAccessRoomException() {
    }

    public CannotAccessRoomException(String message) {
        super(message);
    }
}

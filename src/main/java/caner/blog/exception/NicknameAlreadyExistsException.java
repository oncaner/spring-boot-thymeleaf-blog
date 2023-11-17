package caner.blog.exception;

public class NicknameAlreadyExistsException extends RuntimeException {

    public NicknameAlreadyExistsException(String message) {
        super(message);
    }
}

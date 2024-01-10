package caner.blog.exception;

public class AdminCannotBeLockedException extends RuntimeException {

    public AdminCannotBeLockedException(String message) {
        super(message);
    }
}

package caner.blog.exception;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.NoSuchElementException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ProblemDetails handleImageExtensionException(ImageExtensionException exception) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setMessage(exception.getMessage());

        return problemDetails;
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ProblemDetails handleEmailAlreadyExistsException(EmailAlreadyExistsException exception) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setMessage(exception.getMessage());

        return problemDetails;
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ProblemDetails handleEmptyResultDataAccessException(EmptyResultDataAccessException emptyResultDataAccessException) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setMessage("No data matching the entered value was found!");

        return problemDetails;
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ProblemDetails handleNoSuchElementException(NoSuchElementException noSuchElementException) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setMessage(noSuchElementException.getMessage());

        return problemDetails;
    }

    @ExceptionHandler
    @ResponseStatus(code = HttpStatus.BAD_REQUEST)
    public ProblemDetails handleHttpMessageNotReadableException(HttpMessageNotReadableException httpMessageNotReadableException) {
        ProblemDetails problemDetails = new ProblemDetails();
        problemDetails.setMessage(httpMessageNotReadableException.getMessage());

        return problemDetails;
    }
}

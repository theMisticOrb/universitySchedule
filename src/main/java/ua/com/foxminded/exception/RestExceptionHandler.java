package ua.com.foxminded.exception;

import javax.persistence.EntityNotFoundException;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(EntityNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, "Entity not found!", new HttpHeaders(), HttpStatus.NOT_FOUND, request);

    }

    @ExceptionHandler({ ConstraintViolationException.class, DataIntegrityViolationException.class })
    public ResponseEntity<Object> handleBadRequest(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getLocalizedMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST,
                request);
    }
    
    @ExceptionHandler( AccessDeniedException.class )
    public ResponseEntity<Object> handleForbidden(Exception ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getLocalizedMessage(), new HttpHeaders(), HttpStatus.FORBIDDEN,
                request);
    }

    @ExceptionHandler(DuplicateEntityException.class)
    public ResponseEntity<Object> handleConflict(DuplicateEntityException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.CONFLICT, request);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<Object> handleExpired(TokenExpiredException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.GONE, request);
    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<Object> handleNotFound(TokenNotFoundException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(TokenAlreadyUsedException.class)
    public ResponseEntity<Object> handleUsed(TokenAlreadyUsedException ex, WebRequest request) {
        return handleExceptionInternal(ex, ex.getMessage(), new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}

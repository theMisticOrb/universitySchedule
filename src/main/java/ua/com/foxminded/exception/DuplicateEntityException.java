package ua.com.foxminded.exception;

public class DuplicateEntityException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public DuplicateEntityException(String errorMessage, Throwable err) {
        super(errorMessage, err);
    }

    public DuplicateEntityException(String errorMessage) {
        super(errorMessage);
    }

}

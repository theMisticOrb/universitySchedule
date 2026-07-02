package ua.com.foxminded.exception;

public class TokenAlreadyUsedException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TokenAlreadyUsedException() {
        super("Token has already been used.");
    }
}

package ua.com.foxminded.exception;

public class TokenExpiredException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public TokenExpiredException() {
        super("Token has been expired.");
    }
}

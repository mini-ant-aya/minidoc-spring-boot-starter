package org.github.minidoc.core.exception;

public class MiniDocException extends RuntimeException {

    private static final long serialVersionUID = -7205957086790093782L;

    public MiniDocException() {
        super();
    }

    public MiniDocException(String message) {
        super(message);
    }

    public MiniDocException(Throwable cause) {
        super(cause);
    }

    public MiniDocException(String message, Throwable cause) {
        super(message, cause);
    }

}

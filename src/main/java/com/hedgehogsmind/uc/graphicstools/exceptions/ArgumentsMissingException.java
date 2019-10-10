package com.hedgehogsmind.uc.graphicstools.exceptions;

public class ArgumentsMissingException extends Exception {

    public ArgumentsMissingException() {
    }

    public ArgumentsMissingException(String message) {
        super(message);
    }

    public ArgumentsMissingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentsMissingException(Throwable cause) {
        super(cause);
    }
}

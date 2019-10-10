package com.hedgehogsmind.uc.graphicstools.exceptions;

public class ArgumentParsingException extends Exception {

    public ArgumentParsingException() {
    }

    public ArgumentParsingException(String message) {
        super(message);
    }

    public ArgumentParsingException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArgumentParsingException(Throwable cause) {
        super(cause);
    }
}

package com.hedgehogsmind.uc.graphicstools.tools.exceptions;

public class ToolExecutionException extends Exception {

    public ToolExecutionException() {
    }

    public ToolExecutionException(String message) {
        super(message);
    }

    public ToolExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public ToolExecutionException(Throwable cause) {
        super(cause);
    }
}

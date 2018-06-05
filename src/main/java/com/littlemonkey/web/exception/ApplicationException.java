package com.littlemonkey.web.exception;

public class ApplicationException extends RuntimeException {

    private int errorCode;

    public ApplicationException(int errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}

package com.littlemonkey.web.exception;

/**
 * <p>未授权异常</p>
 */
public class UnauthorizedException extends ApplicationException {
    public UnauthorizedException(int errorCode, String message) {
        super(errorCode, message);
    }
}

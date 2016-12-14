package com.springapp.mvc;

/**
 * 自定义异常
 *
 * @author yangweimin
 */
public class CustomException extends Exception {
    private static final long serialVersionUID = 3583566093089790852L;

    private int code;

    public CustomException(int code, String message) {
        super(message);
        this.code = code;
    }

    public CustomException(String message) {
        super(message);
    }

    public CustomException(Throwable cause) {
        super(cause);
    }

    public CustomException(String message, Throwable cause) {
        super(message, cause);
    }

    public int getCode() {
        return code;
    }
}

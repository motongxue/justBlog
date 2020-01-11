package com.nbclass.framework.exception;


/**
 * ZbException
 *
 * @version V1.0
 * @date 2020/01/11
 * @author nbclass
 */
public class MailException extends RuntimeException {

    public MailException() {
        super();
    }

    public MailException(String message) {
        super(message);
    }

    public MailException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.nbclass.exception;


/**
 * 文章404 Exception
 *
 * @version V1.0
 * @date 2019/10/10
 * @author nbclass
 */
public class ArticleNotFoundException extends RuntimeException {
    public ArticleNotFoundException() {
        super();
    }

    public ArticleNotFoundException(String message) {
        super(message);
    }

    public ArticleNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

package com.br.totvs.exception;

import org.springframework.http.HttpStatus;

/**
 * Classe para representação de exceções customizadas
 * extends {@link RuntimeException}
 */
public class CustomException extends RuntimeException {
    private final HttpStatus errorCode;

    public CustomException(HttpStatus errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }

    public CustomException(HttpStatus errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public HttpStatus getErrorCode() {
        return errorCode;
    }
}
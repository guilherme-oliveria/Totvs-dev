package com.br.totvs.exception;

import org.springframework.http.HttpStatus;

/**
 * Classe para representação de exceções de campos inválidos
 * extends {@link RuntimeException}
 */
public class InvalidFieldException extends RuntimeException {
    private final HttpStatus errorCode;

    public InvalidFieldException(HttpStatus errorCode, String detailMessage) {
        super(detailMessage);
        this.errorCode = errorCode;
    }


    public HttpStatus getErrorCode() {
        return errorCode;
    }
}
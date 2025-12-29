package com.luizalebs.comunicacao_api.business.exceptions;

public class NotFaundException extends RuntimeException {
    public NotFaundException(String message) {
        super(message);
    }

    public NotFaundException(String message, Throwable cause) {
        super(message, cause);
    }
}

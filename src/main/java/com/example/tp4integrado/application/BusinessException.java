package com.example.tp4integrado.application;

/**
 * Exceção de negócio genérica usada para falhas tratadas (fail gracefully).
 */
public class BusinessException extends RuntimeException {
    public BusinessException(String message) {
        super(message);
    }
}

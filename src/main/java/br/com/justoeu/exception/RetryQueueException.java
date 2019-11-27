package br.com.justoeu.exception;

public class RetryQueueException extends RuntimeException {

    public RetryQueueException(String message) {
        super(message);
    }
    public RetryQueueException(String message, Throwable cause){
        super(message, cause);
    }
}
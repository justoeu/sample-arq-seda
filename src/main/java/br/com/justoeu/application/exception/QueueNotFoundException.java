package br.com.justoeu.application.exception;

public class QueueNotFoundException extends RuntimeException {
    public QueueNotFoundException(String s) {
        super(s);
    }
}
package br.com.justoeu.exception;

public class QueueNotFoundException extends RuntimeException {
    public QueueNotFoundException(String s) {
        super(s);
    }
}
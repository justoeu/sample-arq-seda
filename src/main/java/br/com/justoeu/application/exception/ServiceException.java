package br.com.justoeu.application.exception;

public class ServiceException extends Exception {

	public ServiceException(Exception e) {
		super(e);
	}
	public ServiceException(String msg) {
		super(msg);
	}
	public ServiceException(String msg, Exception e) {
		super(msg, e);
	}
}

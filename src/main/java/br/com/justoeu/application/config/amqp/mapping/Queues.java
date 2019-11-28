package br.com.justoeu.application.config.amqp.mapping;

public interface Queues {
    String INITIAL_INVOICE_PROCESS = "app.event.initial.invoice.queue";
    String VALIDATE_INVOICE_PROCESS = "app.event.validate.invoice.queue";;
    String CREATE_INVOICE_PROCESS = "app.event.create.invoice.queue";
    String SIGN_INVOICE_PROCESS = "app.event.sign.invoice.queue";
    String ERROR_INVOICE_PROCESS = "app.event.error.invoice.process.queue";

    String SEFAZ_AUTH_PROCESS = "app.event.sefaz.auth.queue";
}
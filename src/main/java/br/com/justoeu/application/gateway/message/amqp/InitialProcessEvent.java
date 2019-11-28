package br.com.justoeu.application.gateway.message.amqp;

import br.com.justoeu.domain.InvoiceRequest;

public interface InitialProcessEvent {

    String startProcess(InvoiceRequest invoice);

}
package br.com.justoeu.application.gateway.message.amqp;

import br.com.justoeu.domain.Invoice;
import br.com.justoeu.domain.InvoiceRequest;

public interface InitialProcessEvent {

    Invoice startProcess(InvoiceRequest invoice);

}
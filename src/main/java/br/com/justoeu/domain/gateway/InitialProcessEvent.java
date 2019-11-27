package br.com.justoeu.domain.gateway;

import br.com.justoeu.domain.model.InvoiceRequest;

public interface InitialProcessEvent {

    void startProcess(InvoiceRequest invoice);

}

package br.com.justoeu.domain.usecase;

import br.com.justoeu.domain.model.InvoiceRequest;

public interface ValidadeInvoice {

    void execute(InvoiceRequest invoice);

}
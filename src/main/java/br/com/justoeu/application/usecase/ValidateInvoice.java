package br.com.justoeu.application.usecase;

import br.com.justoeu.domain.Invoice;
import br.com.justoeu.domain.InvoiceErrors;

import java.util.List;

public interface ValidateInvoice {

    List<InvoiceErrors> validate(Invoice invoice);

}
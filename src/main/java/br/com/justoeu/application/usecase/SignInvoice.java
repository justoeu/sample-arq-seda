package br.com.justoeu.application.usecase;

import br.com.justoeu.domain.Invoice;

public interface SignInvoice {
    boolean sign(Invoice invoice);
}

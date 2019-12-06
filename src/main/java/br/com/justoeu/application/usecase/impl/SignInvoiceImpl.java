package br.com.justoeu.application.usecase.impl;

import br.com.justoeu.application.usecase.SignInvoice;
import br.com.justoeu.domain.Invoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SignInvoiceImpl implements SignInvoice {

    @Override
    public void sign(Invoice invoice) {
        invoice.setSign(true);
    }

}
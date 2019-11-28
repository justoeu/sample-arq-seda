package br.com.justoeu.application.usecase.impl;

import br.com.justoeu.application.gateway.repository.InvoiceRepository;
import br.com.justoeu.application.gateway.repository.model.InvoiceEntity;
import br.com.justoeu.application.usecase.UpdateInvoice;
import br.com.justoeu.domain.Invoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class UpdateInvoiceImpl extends BaseCrud implements UpdateInvoice {

    @Autowired
    private InvoiceRepository repository;

    @Override
    public boolean update(Invoice invoice) {

        InvoiceEntity invoiceEntity = repository.get(invoice.getId());

        if (invoiceEntity != null) {
            repository.update(toInvoiceEntity(invoice));
            return true;
        } else{
            return false;
        }

    }

}

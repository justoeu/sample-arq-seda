package br.com.justoeu.application.usecase.impl;

import br.com.justoeu.application.exception.BusinessException;
import br.com.justoeu.application.gateway.repository.InvoiceRepository;
import br.com.justoeu.application.gateway.repository.model.InvoiceEntity;
import br.com.justoeu.application.gateway.repository.model.InvoiceStatus;
import br.com.justoeu.domain.Invoice;
import br.com.justoeu.application.usecase.CreateInvoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CreateInvoiceImpl extends BaseCrud implements CreateInvoice {

    @Autowired
    private InvoiceRepository repository;

    @Override
    public boolean create(final Invoice invoice) throws BusinessException {

        InvoiceEntity invoiceEntity = repository.get(invoice.getId());

        if (invoiceEntity == null) {
            repository.create(toInvoiceEntity(invoice));
            return true;
        } else{
            throw new BusinessException("Duplicate Invoice Process");
        }

    }



}

package br.com.justoeu.application.usecase.impl;

import br.com.justoeu.application.gateway.repository.model.InvoiceEntity;
import br.com.justoeu.application.gateway.repository.model.InvoiceStatus;
import br.com.justoeu.domain.Invoice;

import java.time.LocalDateTime;

public class BaseCrud {

    public InvoiceEntity toInvoiceEntity(Invoice invoice){
        final InvoiceStatus status = InvoiceStatus.builder()
                .status(invoice.getStatus())
                .updateDate(LocalDateTime.now())
                .build();

        return InvoiceEntity.builder()
                .createdDate(invoice.getCreatedDate())
                .id(invoice.getId())
                .packId(invoice.getPackId())
                .referenceInvoiceId(invoice.getReferenceInvoiceId())
                .sellerId(invoice.getSellerId())
                .shipmentId(invoice.getShipmentId())
                .status(status)
                .build();
    }

}

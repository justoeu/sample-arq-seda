package br.com.justoeu.application.gateway.repository;

import br.com.justoeu.application.gateway.repository.model.InvoiceEntity;

public interface InvoiceRepository {

    void create(InvoiceEntity entity);
    void update(InvoiceEntity entity);
    InvoiceEntity get(Long id);

}

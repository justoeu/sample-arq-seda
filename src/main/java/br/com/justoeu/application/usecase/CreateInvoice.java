package br.com.justoeu.application.usecase;

import br.com.justoeu.application.exception.BusinessException;
import br.com.justoeu.domain.Invoice;

public interface CreateInvoice {
    boolean create(Invoice invoice) throws BusinessException;
}
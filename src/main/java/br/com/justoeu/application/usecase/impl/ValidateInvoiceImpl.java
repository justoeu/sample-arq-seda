package br.com.justoeu.application.usecase.impl;

import br.com.justoeu.domain.Invoice;
import br.com.justoeu.domain.InvoiceErrors;
import br.com.justoeu.application.usecase.ValidateInvoice;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
public class ValidateInvoiceImpl implements ValidateInvoice {

    @Override
    public List<InvoiceErrors> validate(Invoice invoice) {

      log.info("Execute Validates");

      List<InvoiceErrors> errors = new ArrayList<>();

      if (invoice.getId() == null){
          errors.add(InvoiceErrors.builder()
                  .validateStep("ValidateID")
                  .error("ID of Invoice is null")
                  .build()
          );
      }

      if (invoice.getReferenceInvoiceId() == null){
          errors.add(InvoiceErrors.builder()
                  .validateStep("ValidateReferenceInvoiceID")
                  .error("ID of Origin Invoice is null")
                  .build());
      }

        if (StringUtils.isEmpty(invoice.getStatus())){
            errors.add(InvoiceErrors.builder()
                    .validateStep("ValidateInvoiceStatus")
                    .error("The Status of Invoice is null")
                    .build());
        }

        return errors;
    }
}
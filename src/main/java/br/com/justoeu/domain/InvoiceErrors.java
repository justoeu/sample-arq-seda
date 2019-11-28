package br.com.justoeu.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class InvoiceErrors {

    private String validateStep;
    private String error;

}

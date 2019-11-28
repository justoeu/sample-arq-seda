package br.com.justoeu.application.gateway.repository.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class InvoiceStatus {

    private String status;
    private LocalDateTime updateDate;

}

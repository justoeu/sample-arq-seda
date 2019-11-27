package br.com.justoeu.domain.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Invoice {

    private Long id;
    private String sellerId;
    private LocalDateTime createdDate;
    private String status;
    private Long shipmentId;
    private Long packId;
    private Long referenceInvoiceId;

}

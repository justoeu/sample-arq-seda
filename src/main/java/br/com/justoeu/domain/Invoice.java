package br.com.justoeu.domain;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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
    private String trackingID;

    private List<InvoiceErrors> errors;
}

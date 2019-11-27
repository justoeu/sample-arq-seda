package br.com.justoeu.domain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class InvoiceRequest implements Serializable {

    private Long id;
    private String sellerId;
    private LocalDateTime createdDate;
    private String status;
    private Long shipmentId;
    private Long packId;
    private Long referenceInvoiceId;

}

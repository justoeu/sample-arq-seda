package br.com.justoeu.application.gateway.repository.model;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class InvoiceEntity {

    private Long id;
    private String sellerId;
    private LocalDateTime createdDate;
    private List<InvoiceStatus> status;
    private Long shipmentId;
    private Long packId;
    private Long referenceInvoiceId;

    public static class InvoiceEntityBuilder {

        public InvoiceEntityBuilder status(InvoiceStatus status){

            if (this.status == null){
                this.status = new ArrayList<>();
            }

            this.status.add(status);

            return this;
        }

    }

}

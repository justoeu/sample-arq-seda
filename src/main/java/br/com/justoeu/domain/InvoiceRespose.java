package br.com.justoeu.domain;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class InvoiceRespose implements Serializable {

    Long id;
    Long referenceInvoiceId;
    String TrackingID;
}

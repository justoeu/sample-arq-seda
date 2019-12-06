package br.com.justoeu.application.gateway.message.amqp.pub;

import br.com.justoeu.application.config.amqp.mapping.Queues;
import br.com.justoeu.application.gateway.message.amqp.BaseMessage;
import br.com.justoeu.application.gateway.message.amqp.InitialProcessEvent;
import br.com.justoeu.domain.Invoice;
import br.com.justoeu.domain.InvoiceRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Random;
import java.util.UUID;

@Component
public class InitialInvoiceProcessPub extends BaseMessage implements InitialProcessEvent {

    @Override
    public Invoice startProcess(final InvoiceRequest invoice) {

        Invoice newInvoice = transform(invoice);

        sendNewEvent(newInvoice, Queues.INITIAL_INVOICE_PROCESS);

        return newInvoice;
    }

    private Invoice transform(final InvoiceRequest request){

        if (StringUtils.isEmpty(request.getTrackingID())){
            request.setTrackingID(UUID.randomUUID().toString());
        }

        if (request.getId() == null){
            request.setId(new Random().nextLong());
        }

        return Invoice.builder().id(request.getId())
                                .createdDate(request.getCreatedDate())
                                .packId(request.getPackId())
                                .referenceInvoiceId(request.getReferenceInvoiceId())
                                .sellerId(request.getSellerId())
                                .shipmentId(request.getShipmentId())
                                .status(request.getStatus())
                                .trackingID(request.getTrackingID())
                        .build();
    }

}

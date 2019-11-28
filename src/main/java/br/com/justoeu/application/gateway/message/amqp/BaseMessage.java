package br.com.justoeu.application.gateway.message.amqp;

import br.com.justoeu.application.gateway.message.amqp.pub.Postman;
import br.com.justoeu.domain.Invoice;
import br.com.justoeu.domain.InvoiceRequest;
import br.com.justoeu.domain.event.EventMessage;
import br.com.justoeu.domain.event.EventMessageBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.UUID;

@Component
@Slf4j
public class BaseMessage {

    @Autowired
    private Postman rabbitSender;

    public EventMessage<InvoiceRequest> createEventMessage(Invoice invoice) {

        if (StringUtils.isEmpty(invoice.getTrackingID())) {
            invoice.setTrackingID(UUID.randomUUID().toString());
        }

        return (EventMessage<InvoiceRequest>) new EventMessageBuilder()
                .trackingId(invoice.getTrackingID())
                .content(Arrays.asList(invoice))
                .creationDate()
                .build();
    }

    public void sendNewEvent(Invoice invoice, String queueName) {
        log.info("SendNewEvent - " + invoice.getStatus() + " - TrackingID: " + invoice.getTrackingID());
        rabbitSender.fireEvent(createEventMessage(invoice), queueName);
    }

    public Invoice newEvent(Invoice invoice) {
        return Invoice.builder()
                .createdDate(invoice.getCreatedDate())
                .id(invoice.getId())
                .packId(invoice.getPackId())
                .referenceInvoiceId(invoice.getReferenceInvoiceId())
                .sellerId(invoice.getSellerId())
                .shipmentId(invoice.getShipmentId())
                .status(invoice.getStatus())
                .trackingID(invoice.getTrackingID())
                .errors(invoice.getErrors())
                .build();
    }

}

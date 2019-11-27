package br.com.justoeu.application.gateway.message.amqp.pub;

import br.com.justoeu.config.amqp.mapping.Queues;
import br.com.justoeu.domain.gateway.InitialProcessEvent;
import br.com.justoeu.domain.model.Invoice;
import br.com.justoeu.domain.model.InvoiceRequest;
import br.com.justoeu.domain.model.event.EventMessage;
import br.com.justoeu.domain.model.event.EventMessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.UUID;

@Component
public class InitialInvoiceProcessPub implements InitialProcessEvent {

    @Autowired
    private Postman rabbitSender;

    @Override
    public void startProcess(InvoiceRequest invoice) {

        EventMessage<InvoiceRequest> event = new EventMessageBuilder()
                .trackingId(UUID.randomUUID().toString())
                .content(Arrays.asList(transform(invoice)))
                .creationDate()
                .build();

        rabbitSender.fireEvent(event, Queues.INITIAL_INVOICE_PROCESS);

    }

    private Invoice transform(InvoiceRequest request){

        return Invoice.builder().id(request.getId())
                                .createdDate(request.getCreatedDate())
                                .packId(request.getPackId())
                                .referenceInvoiceId(request.getReferenceInvoiceId())
                                .sellerId(request.getSellerId())
                                .shipmentId(request.getShipmentId())
                                .status(request.getStatus())
                        .build();
    }

}

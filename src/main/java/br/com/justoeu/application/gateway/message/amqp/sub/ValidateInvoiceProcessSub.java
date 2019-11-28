package br.com.justoeu.application.gateway.message.amqp.sub;

import br.com.justoeu.application.config.amqp.mapping.Queues;
import br.com.justoeu.application.exception.RetryQueueException;
import br.com.justoeu.application.gateway.message.amqp.BaseMessage;
import br.com.justoeu.application.usecase.UpdateInvoice;
import br.com.justoeu.domain.Invoice;
import br.com.justoeu.domain.InvoiceErrors;
import br.com.justoeu.domain.event.EventMessage;
import br.com.justoeu.application.usecase.ValidateInvoice;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ValidateInvoiceProcessSub extends BaseMessage {

    private final ObjectMapper objectMapper;

    @Autowired
    private ValidateInvoice step;

    @RabbitListener(queues = Queues.VALIDATE_INVOICE_PROCESS)
    public void dequeue(final Message message) {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start("ValidateInvoiceProcess");

        try {
            final EventMessage<Invoice> eventMessage = this.objectMapper
                    .readValue(message.getPayload().toString(), new TypeReference<>() {});

            log.debug("A new message has arrived: {}", eventMessage.getTrackingId());

            eventMessage.getContent().stream().forEach(invoice -> {
                try {
                    execute(invoice);
                } catch (RuntimeException e) {
                    throw e;
                }
            });

            log.debug("Message process {} has been finished ", eventMessage.getTrackingId());

        } catch (final Exception e) {
            log.error("An error has occurred whilst deserialize the message.", e);
            throw new RetryQueueException(e.getMessage(), e);
        } finally {
            stopWatch.stop();
            log.debug(stopWatch.prettyPrint());
        }
    }

    private void execute(final Invoice invoice) {

        Invoice event = newEvent(invoice);

        try {

            List<InvoiceErrors> errors = step.validate(invoice);

            if (errors.size() > 0) {
                event.setStatus("ERROR");
                event.setErrors(errors);

                sendNewEvent(event, Queues.ERROR_INVOICE_PROCESS);

            } else {
                log.info("Invoice Initialize - Send Event to Create Invoice");
                event.setStatus("CREATE-INVOICE");

                sendNewEvent(event, Queues.CREATE_INVOICE_PROCESS);

            }
        } catch (Exception e){
            throw e;
        }
    }

}
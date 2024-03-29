package br.com.justoeu.application.gateway.message.amqp.sub;

import br.com.justoeu.application.config.amqp.mapping.Queues;
import br.com.justoeu.application.exception.RetryQueueException;
import br.com.justoeu.application.gateway.message.amqp.BaseMessage;
import br.com.justoeu.domain.Invoice;
import br.com.justoeu.domain.event.EventMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class InitialInvoiceProcessSub extends BaseMessage {

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = Queues.INITIAL_INVOICE_PROCESS)
    public void dequeue(final Message message) {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start("InitialInvoiceProcess");

        try {
            final EventMessage<Invoice> eventMessage = this.objectMapper
                    .readValue(message.getPayload().toString(), new TypeReference<>() {});

            log.debug("A new message has arrived: {}", eventMessage.getTrackingId());

            eventMessage.getContent().stream().forEach(invoice -> execute(invoice) );

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

        log.info("Invoice Initialize - Send Event to Validate Invoice");
        invoice.setStatus("VALIDATE-INVOICE");

        sendNewEvent(invoice,Queues.VALIDATE_INVOICE_PROCESS);
    }

}
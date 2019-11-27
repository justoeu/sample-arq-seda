package br.com.justoeu.application.gateway.message.amqp.sub;

import br.com.justoeu.config.amqp.mapping.Queues;
import br.com.justoeu.domain.model.Invoice;
import br.com.justoeu.exception.ServiceException;
import br.com.justoeu.domain.model.event.EventMessage;
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
public class InitialInvoiceProcessSub {

    private final ObjectMapper objectMapper;

    @RabbitListener(queues = Queues.INITIAL_INVOICE_PROCESS)
    public void dequeue(final Message message) {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start("InitialInvoiceProcess");

        try {
            final EventMessage<Invoice> eventMessage = this.objectMapper
                    .readValue(message.getPayload().toString(), new TypeReference<>() {});

            log.debug("A new message has arrived: {}", eventMessage.getTrackingId());

            eventMessage.getContent().stream().forEach(invoice -> {
                try {
                    execute(invoice);
                } catch (ServiceException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            });

            log.debug("Message process {} has been finished ", eventMessage.getTrackingId());

        } catch (final Exception e) {
            log.error("An error has occurred whilst deserialize the message.", e);
            throw new AmqpRejectAndDontRequeueException(e.getMessage(), e);
        } finally {
            stopWatch.stop();
            log.debug(stopWatch.prettyPrint());
        }
    }

    private void execute(Invoice event) throws ServiceException {

        log.info(event.toString());

    }

}
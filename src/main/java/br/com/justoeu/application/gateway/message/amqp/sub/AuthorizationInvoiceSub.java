package br.com.justoeu.application.gateway.message.amqp.sub;

import br.com.justoeu.application.config.amqp.mapping.Queues;
import br.com.justoeu.application.exception.RetryQueueException;
import br.com.justoeu.domain.Invoice;
import br.com.justoeu.domain.event.EventMessage;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Random;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class AuthorizationInvoiceSub {

    private final ObjectMapper objectMapper;

    //@RabbitListener(queues = Queues.SEFAZ_AUTH_PROCESS)
    public void dequeue(final String message) {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start("AuthorizationInvoiceProcess");

        try {

            final EventMessage<Invoice> eventMessage = this.objectMapper.readValue(message, new TypeReference<EventMessage<Invoice>>() { });

            eventMessage.getContent().stream().forEach(invoice -> {
                try {
                    authorize(invoice);
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

    private void authorize(Invoice invoice) {

        try{

            int delay = new Random().nextInt(10000);

            log.info("Execution will have delay ... " + delay);
            Thread.sleep(delay);
            log.info("Finish Execution ");

        } catch (InterruptedException e){
            throw new RuntimeException("Ops, ThreadExecution over down.");
        }

    }
}

package br.com.justoeu.application.gateway.message.amqp.sub;

import br.com.justoeu.application.config.amqp.mapping.Queues;
import br.com.justoeu.application.exception.RetryQueueException;
import br.com.justoeu.application.gateway.message.amqp.BaseMessage;
import br.com.justoeu.application.usecase.UpdateInvoice;
import br.com.justoeu.domain.Invoice;
import br.com.justoeu.domain.event.EventMessage;
import br.com.justoeu.application.usecase.SignInvoice;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class SignInvoiceProcessSub extends BaseMessage {

    private final ObjectMapper objectMapper;

    @Autowired
    private SignInvoice step;

    @Autowired
    private UpdateInvoice ucInvoice;

    @RabbitListener(queues = Queues.SIGN_INVOICE_PROCESS)
    public void dequeue(final Message message) {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start("SignInvoiceProcess");

        try {
            final EventMessage<Invoice> eventMessage = this.objectMapper
                    .readValue(message.getPayload().toString(), new TypeReference<>() {});

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

    private void execute(Invoice invoice) {

        Invoice event = newEvent(invoice);

        if (step.sign(invoice)){
            event.setStatus("SEFAZ-AUTH");
            ucInvoice.update(event);

            sendNewEvent(event, Queues.SEFAZ_AUTH_PROCESS);
        } else{
            event.setStatus("ERROR");
            ucInvoice.update(event);

            sendNewEvent(event, Queues.ERROR_INVOICE_PROCESS);
        }

    }

}

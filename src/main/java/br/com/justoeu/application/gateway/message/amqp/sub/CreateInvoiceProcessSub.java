package br.com.justoeu.application.gateway.message.amqp.sub;

import br.com.justoeu.application.config.amqp.mapping.Queues;
import br.com.justoeu.application.exception.BusinessException;
import br.com.justoeu.application.exception.RetryQueueException;
import br.com.justoeu.application.gateway.message.amqp.BaseMessage;
import br.com.justoeu.domain.Invoice;
import br.com.justoeu.domain.InvoiceErrors;
import br.com.justoeu.domain.event.EventMessage;
import br.com.justoeu.application.usecase.CreateInvoice;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

import java.util.Arrays;

@Component
@Slf4j
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class CreateInvoiceProcessSub extends BaseMessage {

    private final ObjectMapper objectMapper;

    @Autowired
    private CreateInvoice step;

    @RabbitListener(queues = Queues.CREATE_INVOICE_PROCESS)
    public void dequeue(final Message message) {
        final StopWatch stopWatch = new StopWatch();

        stopWatch.start("CreateInvoiceProcess");

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

        try {
            step.create(invoice);
            event.setStatus("SIGN-INVOICE");
            sendNewEvent(event, Queues.SIGN_INVOICE_PROCESS);

        } catch (BusinessException e){
            event.setStatus("ERROR");
            event.setErrors(Arrays.asList(InvoiceErrors.builder().error(e.getMessage()).validateStep("CREATE-INVOICE").build()));
            sendNewEvent(event, Queues.ERROR_INVOICE_PROCESS);
        }
    }
}

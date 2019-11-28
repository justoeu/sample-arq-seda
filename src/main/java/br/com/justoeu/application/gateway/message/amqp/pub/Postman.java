package br.com.justoeu.application.gateway.message.amqp.pub;

import br.com.justoeu.application.config.amqp.MessageContext;
import br.com.justoeu.application.config.amqp.RabbitMQPublish;
import br.com.justoeu.application.config.amqp.mapping.RabbitMQConfigurationProperties;
import br.com.justoeu.application.exception.QueueNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class Postman {


    private final RabbitMQPublish publish;
    private final RabbitMQConfigurationProperties properties;

    public <T extends Serializable> void fireEvent(final T eventMessage, final String queueName) {
        RabbitMQConfigurationProperties.Binding binding = properties.getQueueByName(queueName)
                .orElseThrow(() -> new QueueNotFoundException(String.format("Unable to find the given queue: '%s'", queueName)));

        log.debug("enqueueing message: {} at: {} queue, at: {}", eventMessage, queueName, LocalDateTime.now());
        publish.enqueue(new MessageContext(eventMessage, binding));
    }
}
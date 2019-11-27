package br.com.justoeu.config.amqp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.core.MessagePropertiesBuilder;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import br.com.justoeu.config.amqp.mapping.RabbitMQConfigurationProperties.Binding;

@Slf4j
@Component
@RequiredArgsConstructor
public class RabbitMQPublish {

    private final RabbitTemplate template;

    private final ObjectMapper objectMapper;

    public void enqueue(final MessageContext context) {
        final Binding binding = context.getRabbitMQBiding();
        final Object data = context.getRabbitMessage();
        final String exchange = binding.getTopic();
        log.debug("Queuing {} to {}", data, exchange);

        final Message message = template.getMessageConverter().toMessage(stringifyRabbitMessage(data), getProperties(context));
        template.send(exchange, binding.getRoutingKey(), message);
    }

    private String stringifyRabbitMessage(final Object data) {
        try {
            return objectMapper.writeValueAsString(data);
        } catch (JsonProcessingException e) {
            log.error("object couldnt be serialized as a json string", e);
        }
        return data.toString();
    }

    private MessageProperties getProperties(final MessageContext context) {
        return MessagePropertiesBuilder.fromProperties(context.getProperties()).build();
    }
}
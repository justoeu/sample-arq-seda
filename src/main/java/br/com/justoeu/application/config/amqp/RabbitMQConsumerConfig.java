package br.com.justoeu.application.config.amqp;

import br.com.justoeu.application.config.amqp.mapping.Queues;
import br.com.justoeu.application.config.amqp.mapping.RabbitMQConfigurationProperties;
import br.com.justoeu.application.exception.RetryQueueException;
import br.com.justoeu.application.gateway.message.amqp.sub.AuthorizationInvoiceSub;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.amqp.rabbit.retry.RejectAndDontRequeueRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class RabbitMQConsumerConfig {

    private final ConnectionFactory connectionFactory;

    @Bean
    public RabbitAdmin rabbitAdmin(final ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(final RabbitMQConfigurationProperties configurationProperties) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(configurationProperties.getConfig().getConsumers());
        factory.setMaxConcurrentConsumers(configurationProperties.getConfig().getMaxConsumers());
        factory.setDefaultRequeueRejected(false);
        factory.setAdviceChain(
                org.springframework.amqp.rabbit.config.RetryInterceptorBuilder
                        .stateless()
                        .retryPolicy(new SimpleRetryPolicy(configurationProperties.getConfig().getApplyMaxAttempts(), retryableExceptions(), true))
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .backOffOptions(
                                configurationProperties.getConfig().getInitialInterval(),
                                configurationProperties.getConfig().getMultiplier(),
                                configurationProperties.getConfig().getMaxInterval())
                        .build());

        return factory;
    }

    @Bean
    public MessageListenerAdapter messageListenerAdapter(AuthorizationInvoiceSub messageSubscriber) {
        return new MessageListenerAdapter(messageSubscriber, "dequeue");
    }

    @Bean
    public SimpleMessageListenerContainer sefazAuthQueueConfiguration(final RabbitMQConfigurationProperties configurationProperties,
                                                                      final MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(Queues.SEFAZ_AUTH_PROCESS);
        container.setMessageListener(listenerAdapter);
        container.setConcurrentConsumers(100);
        container.setMaxConcurrentConsumers(300);
        container.setDefaultRequeueRejected(false);
        container.setAdviceChain(
                org.springframework.amqp.rabbit.config.RetryInterceptorBuilder
                        .stateless()
                        .retryPolicy(new SimpleRetryPolicy(3 , retryableExceptions(), true))
                        .recoverer(new RejectAndDontRequeueRecoverer())
                        .backOffOptions(
                                configurationProperties.getConfig().getInitialInterval(),
                                configurationProperties.getConfig().getMultiplier(),
                                configurationProperties.getConfig().getMaxInterval())
                        .build()
        );
        return container;
    }

    public static Map<Class<? extends Throwable>, Boolean> retryableExceptions() {
        return new HashMap<>() {

            private static final long serialVersionUID = -7185600890003090267L;

            {
                put(RetryQueueException.class, true);
            }
        };
    }

}
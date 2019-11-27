package br.com.justoeu.config.amqp.mapping;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Optional;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Configuration
@ConfigurationProperties(
        prefix = "app.prop.rabbit"
)
public class RabbitMQConfigurationProperties {

    private List<Binding> bindings;
    private Config config;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Binding {

        private String topic;
        private String routingKey;
        private String queue;
        private boolean declare;
    }


    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Config {

        private Integer initialInterval = 20;
        private Integer maxInterval     = 10000;
        private Integer multiplier      = 2;
        private Integer retries         = 3;
        private Integer consumers       = 10;
        private int applyMaxAttempts    = 5;
        private Integer maxConsumers    = 50;
    }

    public Optional<Binding> getQueueByName(final String queue) {
        return bindings
                .stream()
                .filter(binding -> binding.getQueue().equals(queue))
                .findFirst();
    }
}
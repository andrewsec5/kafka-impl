package br.com.andrew.kafka_impl.consumer;

import br.com.andrew.kafka_impl.dto.TransactionDTO;
import br.com.andrew.kafka_impl.service.FraudService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;

import java.util.function.Consumer;

@Configuration
public class KafkaConsumer {

    private final FraudService fraudService;

    public KafkaConsumer(FraudService fraudService) {
        this.fraudService = fraudService;
    }

    @Bean
    public Consumer<Message<TransactionDTO>> testConsumer(){
        return message -> {
            System.out.println("Consumindo");
            System.out.println(fraudService.isFraud(message.getPayload()) ? "Fraude!" : "Nao fraude.");
            System.out.println(message.getPayload());
        };
    }

}

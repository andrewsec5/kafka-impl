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
    private int dlqCounter = 0;

    public KafkaConsumer(FraudService fraudService) {
        this.fraudService = fraudService;
    }

    @Bean
    public Consumer<Message<TransactionDTO>> testConsumer(){
        return message -> {

            if(dlqCounter == 10){
                dlqCounter = 0;
                throw new RuntimeException("Mock de erro no processamento, mandando para DLQ");
            }
            dlqCounter++;
           boolean isFraude = fraudService.isFraud(message.getPayload());

           if(isFraude){
               fraudService.produceMsg(message.getPayload());
               System.out.println("Mandou pro topico");
           }

        };
    }

}

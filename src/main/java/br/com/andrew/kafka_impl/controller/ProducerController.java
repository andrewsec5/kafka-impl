package br.com.andrew.kafka_impl.controller;

import br.com.andrew.kafka_impl.dto.TransactionDTO;
import br.com.andrew.kafka_impl.messaging.producer.KafkaProducer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class ProducerController {

    private final KafkaProducer kafkaProducer;

    public ProducerController(KafkaProducer kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    @PostMapping
    public void postTest(@RequestBody TransactionDTO request){
        kafkaProducer.publish(request);
    }

}

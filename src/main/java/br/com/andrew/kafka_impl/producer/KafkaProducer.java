package br.com.andrew.kafka_impl.producer;

import br.com.andrew.kafka_impl.dto.FraudeTransactionDTO;
import br.com.andrew.kafka_impl.dto.TransactionDTO;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducer {

    private final StreamBridge streamBridge;

    public KafkaProducer(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    public void publish(TransactionDTO request){
        streamBridge.send("test-out-0", request);
    }

    public void fraudDetected(FraudeTransactionDTO response){
        streamBridge.send("detected-out-0", response);
    }

}

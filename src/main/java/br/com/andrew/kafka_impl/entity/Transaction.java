package br.com.andrew.kafka_impl.entity;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@Builder
@Getter
public class Transaction {

    @Id
    private String id;

    private String clientId;

    private double value;

    private LocalDateTime processDate;

    private boolean isFraud;

}

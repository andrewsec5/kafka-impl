package br.com.andrew.kafka_impl.dto;

import java.time.LocalDateTime;

public record TransactionDTO(
        String id,
        String clientId,
        double value,
        LocalDateTime processDate
) {
}

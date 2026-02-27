package br.com.andrew.kafka_impl.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
public record FraudeTransactionDTO(
        String id,
        String cartaoId,
        BigDecimal valor,
        String tipoTransacao,
        OffsetDateTime dataHora,
        String message,
        Boolean isFraude
) {

}

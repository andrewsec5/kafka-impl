package br.com.andrew.kafka_impl.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Builder
public record TransactionDTO(
        String id,
        String contaId,
        String cartaoId,
        BigDecimal valor,
        String comerciante,
        String localizacao,
        String tipoTransacao,
        OffsetDateTime dataHora
) {
}

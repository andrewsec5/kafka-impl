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
    public static FraudeTransactionDTO toFraude(TransactionDTO request){

        return  FraudeTransactionDTO.builder().id(request.id())
                .valor(request.valor())
                .dataHora(request.dataHora())
                .cartaoId(request.cartaoId())
                .tipoTransacao(request.tipoTransacao())
                .message("Fraude detectado, compra cancelada!")
                .isFraude(true).build();
    }
}

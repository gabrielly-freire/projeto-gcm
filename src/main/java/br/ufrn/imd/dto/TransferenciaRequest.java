package br.ufrn.imd.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para transferência entre contas")
public record TransferenciaRequest(
        @Schema(description = "Número da conta de origem", example = "12345") String from,
        @Schema(description = "Número da conta de destino", example = "67890") String to,
        @Schema(description = "Valor a transferir (deve ser maior que zero)", example = "200.00") double amount
) {}

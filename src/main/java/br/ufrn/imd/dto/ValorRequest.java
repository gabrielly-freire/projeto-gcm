package br.ufrn.imd.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Valor monetário para operação de crédito ou débito")
public record ValorRequest(
        @Schema(description = "Valor da operação (deve ser maior que zero)", example = "150.00") double valor
) {}

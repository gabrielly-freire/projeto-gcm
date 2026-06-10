package br.ufrn.imd.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para aplicação de rendimento em conta poupança")
public record RendimentoRequest(
        @Schema(description = "Número da conta poupança", example = "12345") String numero,
        @Schema(description = "Taxa de juros a aplicar (ex: 0.05 para 5%)", example = "0.05") double taxa
) {}

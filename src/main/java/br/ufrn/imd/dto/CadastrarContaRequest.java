package br.ufrn.imd.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Dados para cadastro de uma nova conta bancária")
public record CadastrarContaRequest(
        @Schema(description = "Número identificador da conta", example = "12345") String numero,
        @Schema(description = "Saldo inicial (obrigatório apenas para conta poupança)", example = "500.00") double saldo,
        @Schema(description = "Tipo da conta: 1 = Simples, 2 = Poupança, 3 = Bônus", example = "1", allowableValues = {"1", "2", "3"}) int tipo
) {}

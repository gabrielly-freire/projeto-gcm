package br.ufrn.imd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record InfoContaResponse(
    String tipo,
    String numero,
    double saldo,
    Integer bonus
) {}
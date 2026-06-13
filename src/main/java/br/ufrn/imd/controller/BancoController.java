package br.ufrn.imd.controller;

import br.ufrn.imd.dto.InfoContaResponse;
import br.ufrn.imd.dto.CadastrarContaRequest;
import br.ufrn.imd.dto.RendimentoRequest;
import br.ufrn.imd.dto.TransferenciaRequest;
import br.ufrn.imd.dto.ValorRequest;
import br.ufrn.imd.model.ContaBancaria;
import br.ufrn.imd.service.ContaBancariaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Banco", description = "Operações de gerenciamento de contas bancárias")
@RestController
@RequestMapping("/banco/conta")
public class BancoController {

    private final ContaBancariaService contaBancariaService;

    public BancoController(ContaBancariaService contaBancariaService) {
        this.contaBancariaService = contaBancariaService;
    }

    @Operation(summary = "Cadastrar conta", description = "Cria uma nova conta bancária. Tipo: 1 = Simples, 2 = Poupança, 3 = Bônus")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Conta cadastrada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos ou tipo de conta inexistente"),
        @ApiResponse(responseCode = "409", description = "Conta com este número já cadastrada")
    })
    @PostMapping
    public ResponseEntity<Void> cadastrarConta(@RequestBody CadastrarContaRequest request) {
        contaBancariaService.cadastrarConta(request.numero(), request.saldo(), request.tipo());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Consultar Conta", description = "Retorna os dados de uma conta, exibindo Tipo, Número, Saldo e Bônus")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Conta encontrada"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{id}")
    public ResponseEntity<br.ufrn.imd.dto.InfoContaResponse> consultarConta(
            @Parameter(description = "Número da conta bancária") @PathVariable String id) {
        
        ContaBancaria conta = contaBancariaService.getConta(id);
        
        String tipoStr = "Simples";
        Integer bonus = null;

        if (contaBancariaService.isContaPoupanca(id)) {
            tipoStr = "Poupança";
        } else if (contaBancariaService.isContaBonus(id)) {
            tipoStr = "Bônus";
            bonus = contaBancariaService.getPontuacao(id);
        }

        br.ufrn.imd.dto.InfoContaResponse response = new br.ufrn.imd.dto.InfoContaResponse(
            tipoStr, conta.getNumero(), conta.getSaldo(), bonus
        );

        return ResponseEntity.ok(response);
    }
    @Operation(summary = "Consultar saldo", description = "Retorna o saldo atual de uma conta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Saldo retornado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @GetMapping("/{id}/saldo")
    public ResponseEntity<Double> getSaldo(
            @Parameter(description = "Número da conta bancária") @PathVariable String id) {
        Double saldo = contaBancariaService.consultarSaldo(id);
        return ResponseEntity.ok(saldo);
    }

    @Operation(summary = "Creditar valor", description = "Adiciona um valor ao saldo da conta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Crédito realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Valor inválido (deve ser maior que zero)"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @PutMapping("/{id}/credito")
    public ResponseEntity<Void> creditar(
            @Parameter(description = "Número da conta bancária") @PathVariable String id,
            @RequestBody ValorRequest request) {
        contaBancariaService.creditar(id, request.valor());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Debitar valor", description = "Remove um valor do saldo da conta. Contas simples e bônus permitem saldo mínimo de -R$ 1000,00; poupança não permite saldo negativo")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Débito realizado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Valor inválido ou saldo insuficiente"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @PutMapping("/{id}/debito")
    public ResponseEntity<Void> debitar(
            @Parameter(description = "Número da conta bancária") @PathVariable String id,
            @RequestBody ValorRequest request) {
        contaBancariaService.debitar(id, request.valor());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Transferir entre contas", description = "Transfere um valor de uma conta de origem para uma conta de destino. Origem e destino não podem ser a mesma conta")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Transferência realizada com sucesso"),
        @ApiResponse(responseCode = "400", description = "Valor inválido, saldo insuficiente ou contas iguais"),
        @ApiResponse(responseCode = "404", description = "Conta de origem ou destino não encontrada")
    })
    @PutMapping("/transferencia")
    public ResponseEntity<Void> transferir(@RequestBody TransferenciaRequest request) {
        contaBancariaService.transferir(request.from(), request.to(), request.amount());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Render juros", description = "Aplica rendimento de juros sobre o saldo de uma conta poupança")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Juros aplicados com sucesso"),
        @ApiResponse(responseCode = "400", description = "Conta não é do tipo poupança"),
        @ApiResponse(responseCode = "404", description = "Conta não encontrada")
    })
    @PutMapping("/rendimento")
    public ResponseEntity<Void> renderJuros(@RequestBody RendimentoRequest request) {
        contaBancariaService.renderJuros(request.numero(), request.taxa());
        return ResponseEntity.ok().build();
    }
}
package br.ufrn.imd.service;

import br.ufrn.imd.exception.ContaJaCadastradaException;
import br.ufrn.imd.model.ContaBancaria;
import br.ufrn.imd.model.ContaBonus;
import br.ufrn.imd.model.ContaPoupanca;
import br.ufrn.imd.repository.ContaBancariaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CadastrarContaServiceTest {

    @Mock
    ContaBancariaRepository repository;

    @InjectMocks
    ContaBancariaService service;

    @BeforeEach
    void setUp() {
        service = new ContaBancariaService();
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    void deve_CadastrarContaBancariaComSucesso_Quando_TipoUm() {
        ContaBancaria conta = new ContaBancaria("001");
        when(repository.existsByNumero("001")).thenReturn(false);
        when(repository.save(conta)).thenReturn(conta);

        ContaBancaria resultado = service.cadastrarConta("001", 0.0, 1);

        assertThat(resultado).isExactlyInstanceOf(ContaBancaria.class);
        assertThat(resultado.getNumero()).isEqualTo("001");
        assertThat(resultado.getSaldo()).isEqualTo(0.0);
    }

    @Test
    void deve_CadastrarContaPoupancaComSucesso_Quando_TipoDois() {
        ContaPoupanca conta = new ContaPoupanca("002", 500.0);
        when(repository.existsByNumero("002")).thenReturn(false);
        when(repository.save(conta)).thenReturn(conta);

        ContaBancaria resultado = service.cadastrarConta("002", 500.0, 2);

        assertThat(resultado).isInstanceOf(ContaPoupanca.class);
        assertThat(resultado.getNumero()).isEqualTo("002");
        assertThat(resultado.getSaldo()).isEqualTo(500.0);
    }

    @Test
    void deve_CadastrarContaBonusComSucesso_Quando_TipoTres() {
        ContaBonus conta = new ContaBonus("003");
        when(repository.existsByNumero("003")).thenReturn(false);
        when(repository.save(conta)).thenReturn(conta);

        var resultado = service.cadastrarConta("003", 0.0, 3);

        assertThat(resultado).isInstanceOf(ContaBonus.class);
        assertThat(resultado.getNumero()).isEqualTo("003");
        assertThat(resultado.getSaldo()).isEqualTo(0.0);
    }

    @Test
    void deve_CadastrarContaBancaria_Quando_NumeroComEspacosNasBordas() {
        ContaBancaria conta = new ContaBancaria("001");
        when(repository.existsByNumero("001")).thenReturn(false);
        when(repository.save(conta)).thenReturn(conta);

        ContaBancaria resultado = service.cadastrarConta("  001  ", 0.0, 1);

        assertThat(resultado.getNumero()).isEqualTo("001");
    }

    @Test
    void deve_CadastrarContaPoupanca_Quando_SaldoInicialZero() {
        ContaPoupanca conta = new ContaPoupanca("002", 0.0);
        when(repository.existsByNumero("002")).thenReturn(false);
        when(repository.save(conta)).thenReturn(conta);

        ContaBancaria resultado = service.cadastrarConta("002", 0.0, 2);

        assertThat(resultado.getSaldo()).isEqualTo(0.0);
    }

    @Test
    void deve_CadastrarContaPoupanca_Quando_SaldoInicialPositivo() {
        ContaPoupanca conta = new ContaPoupanca("002", 1000.0);
        when(repository.existsByNumero("002")).thenReturn(false);
        when(repository.save(conta)).thenReturn(conta);

        ContaBancaria resultado = service.cadastrarConta("002", 1000.0, 2);

        assertThat(resultado.getSaldo()).isEqualTo(1000.0);
    }

    @Test
    void deve_CadastrarContaBonusEBonusSer10_Quando_DadosValidos() {
        ContaBonus conta = new ContaBonus("003");
        when(repository.existsByNumero("003")).thenReturn(false);
        when(repository.save(conta)).thenReturn(conta);

        ContaBancaria resultado = service.cadastrarConta("003", 0.0, 3);

        ContaBonus bonus = (ContaBonus) resultado;
        assertThat(bonus.getPontuacao()).isEqualTo(10);
    }

    @Test
    void deve_LancarContaJaCadastradaException_Quando_NumeroJaExistirParaContaBancaria() {
        when(repository.existsByNumero("001")).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrarConta("001", 0.0, 1))
                .isInstanceOf(ContaJaCadastradaException.class);
    }

    @Test
    void deve_LancarContaJaCadastradaException_Quando_NumeroJaExistirParaContaPoupanca() {
        when(repository.existsByNumero("002")).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrarConta("002", 500.0, 2))
                .isInstanceOf(ContaJaCadastradaException.class);
    }

    @Test
    void deve_LancarContaJaCadastradaException_Quando_NumeroJaExistirParaContaBonus() {
        when(repository.existsByNumero("003")).thenReturn(true);

        assertThatThrownBy(() -> service.cadastrarConta("003", 0.0, 3))
                .isInstanceOf(ContaJaCadastradaException.class);
    }

    @Test
    void deve_LancarIllegalArgumentException_Quando_TipoDeContaInvalido() {
        when(repository.existsByNumero("001")).thenReturn(false);

        assertThatThrownBy(() -> service.cadastrarConta("001", 0.0, 99))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Tipo de conta inválido.");
    }

    @Test
    void deve_LancarIllegalArgumentException_Quando_TipoZeroInvalido() {
        when(repository.existsByNumero("001")).thenReturn(false);

        assertThatThrownBy(() -> service.cadastrarConta("001", 0.0, 0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Tipo de conta inválido.");
    }

    @Test
    void deve_LancarIllegalArgumentException_Quando_NumeroContaNulo() {
        assertThatThrownBy(() -> service.cadastrarConta(null, 0.0, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Número da conta é obrigatório.");
    }

    @Test
    void deve_LancarIllegalArgumentException_Quando_NumeroContaVazio() {
        assertThatThrownBy(() -> service.cadastrarConta("", 0.0, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Número da conta é obrigatório.");
    }

    @Test
    void deve_LancarIllegalArgumentException_Quando_NumeroContaApenasEspacos() {
        assertThatThrownBy(() -> service.cadastrarConta("   ", 0.0, 1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Número da conta é obrigatório.");
    }
}

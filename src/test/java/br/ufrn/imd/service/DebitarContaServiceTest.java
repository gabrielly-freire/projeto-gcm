package br.ufrn.imd.service;

import br.ufrn.imd.exception.ContaBancariaNaoEncontradaException;
import br.ufrn.imd.exception.SaldoInsuficienteException;
import br.ufrn.imd.model.ContaBancaria;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DebitarContaServiceTest {

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
    void deve_DebitarValorComSucesso_Quando_CasoNormal() {
        ContaBancaria conta = new ContaBancaria("001", 100.0);
        when(repository.findByNumero("001")).thenReturn(conta);

        service.debitar("001", 40.0);

        assertThat(conta.getSaldo()).isEqualTo(60.0);
        verify(repository, times(1)).save(conta);
    }

    @Test
    void deve_LancarIllegalArgumentException_Quando_ValorDoParametroNegativo() {
        ContaBancaria conta = new ContaBancaria("001", 100.0);
        when(repository.findByNumero("001")).thenReturn(conta);

        assertThatThrownBy(() -> service.debitar("001", -20.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Valor deve ser maior que zero");
    }

    @Test
    void deve_LancarSaldoInsuficienteException_Quando_SaldoFicarAbaixoDoLimiteNegativoPermitido() {
        ContaBancaria contaSimples = new ContaBancaria("001", 0.0); // Limite -1000.0
        when(repository.findByNumero("001")).thenReturn(contaSimples);

        assertThatThrownBy(() -> service.debitar("001", 1001.0))
                .isInstanceOf(SaldoInsuficienteException.class);
    }

    @Test
    void deve_LancarSaldoInsuficienteException_Quando_SaldoFicarNegativoNaContaPoupanca() {
        ContaPoupanca contaPoupanca = new ContaPoupanca("002", 50.0); // Limite 0.0
        when(repository.findByNumero("002")).thenReturn(contaPoupanca);

        assertThatThrownBy(() -> service.debitar("002", 51.0))
                .isInstanceOf(SaldoInsuficienteException.class);
    }

    @Test
    void deve_LancarContaBancariaNaoEncontradaException_Quando_ContaNaoExistir() {
        when(repository.findByNumero("999")).thenReturn(null);

        assertThatThrownBy(() -> service.debitar("999", 30.0))
                .isInstanceOf(ContaBancariaNaoEncontradaException.class);
    }
}
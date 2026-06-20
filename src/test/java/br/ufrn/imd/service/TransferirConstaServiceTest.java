package br.ufrn.imd.service;

import br.ufrn.imd.exception.ContaBancariaNaoEncontradaException;
import br.ufrn.imd.exception.SaldoInsuficienteException;
import br.ufrn.imd.model.ContaBancaria;
import br.ufrn.imd.model.ContaBonus;
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
class TransferirContaServiceTest {

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
    void deve_TransferirValoresComSucesso_Quando_CasoNormal() {
        ContaBancaria origem = new ContaBancaria("001", 200.0);
        ContaBancaria destino = new ContaBancaria("002", 50.0);

        when(repository.findByNumero("001")).thenReturn(origem);
        when(repository.findByNumero("002")).thenReturn(destino);

        service.transferir("001", "002", 100.0);

        assertThat(origem.getSaldo()).isEqualTo(100.0);
        assertThat(destino.getSaldo()).isEqualTo(150.0);
        verify(repository, times(1)).save(origem);
        verify(repository, times(1)).save(destino);
    }

    @Test
    void deve_LancarIllegalArgumentException_Quando_ValorDaTransferenciaForNegativo() {
        assertThatThrownBy(() -> service.transferir("001", "002", -50.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Valor deve ser maior que zero");
    }

    @Test
    void deve_LancarSaldoInsuficienteException_Quando_SaldoDaOrigemEstourarLimiteNegativo() {
        ContaBancaria origem = new ContaBancaria("001", 0.0); // Limite -1000.0
        ContaBancaria destino = new ContaBancaria("002", 50.0);

        when(repository.findByNumero("001")).thenReturn(origem);
        when(repository.findByNumero("002")).thenReturn(destino);

        assertThatThrownBy(() -> service.transferir("001", "002", 1001.0))
                .isInstanceOf(SaldoInsuficienteException.class);
    }

    @Test
    void deve_BonificarContaDestino_Quando_DestinoForDoTipoBonus() {
        ContaBancaria origem = new ContaBancaria("001", 300.0);
        ContaBonus destinoBonus = new ContaBonus("003");
        destinoBonus.setSaldo(0.0);
        ReflectionTestUtils.setField(destinoBonus, "pontuacao", 0);

        when(repository.findByNumero("001")).thenReturn(origem);
        when(repository.findByNumero("003")).thenReturn(destinoBonus);

        service.transferir("001", "003", 200.0);
        assertThat(destinoBonus.getPontuacao()).isEqualTo(1);
        assertThat(origem.getSaldo()).isEqualTo(100.0);
        assertThat(destinoBonus.getSaldo()).isEqualTo(200.0);
    }

    @Test
    void deve_LancarContaBancariaNaoEncontradaException_Quando_OrigemOuDestinoNaoExistirem() {
        when(repository.findByNumero("001")).thenReturn(null);

        assertThatThrownBy(() -> service.transferir("001", "002", 50.0))
                .isInstanceOf(ContaBancariaNaoEncontradaException.class);
    }
}
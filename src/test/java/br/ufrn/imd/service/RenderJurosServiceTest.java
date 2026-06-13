package br.ufrn.imd.service;

import br.ufrn.imd.exception.ContaBancariaNaoEncontradaException;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RenderJurosServiceTest {

    @Mock
    ContaBancariaRepository repository;

    @InjectMocks
    ContaBancariaService service;

    @BeforeEach
    void setUp() {
        service = new ContaBancariaService();
        // injectar o repository que é privado!
        ReflectionTestUtils.setField(service, "repository", repository);
    }

    @Test
    void deve_AplicarJurosCorretamente_Quando_ContaPoupancaComTaxaPositiva() {
        ContaPoupanca poupanca = new ContaPoupanca("002", 1000.0);
        when(repository.findByNumero("002")).thenReturn(poupanca);

        service.renderJuros("002", 0.05);

        assertThat(poupanca.getSaldo()).isEqualTo(1050.0);
    }

    @Test
    void deve_SalvarContaAposRendimento_Quando_ContaPoupancaExistir() {
        ContaPoupanca poupanca = new ContaPoupanca("002", 1000.0);
        when(repository.findByNumero("002")).thenReturn(poupanca);

        service.renderJuros("002", 0.1);

        verify(repository, times(1)).save(poupanca);
    }

    @Test
    void deve_LancarIllegalArgumentException_Quando_ContaNaoEhPoupanca() {
        when(repository.findByNumero("001")).thenReturn(new ContaBancaria("001", 100.0));

        assertThatThrownBy(() -> service.renderJuros("001", 0.05))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A conta não é do tipo poupança.");
    }

    @Test
    void deve_LancarIllegalArgumentException_Quando_ContaBonusNaoEhPoupanca() {
        when(repository.findByNumero("003")).thenReturn(new ContaBonus("003"));

        assertThatThrownBy(() -> service.renderJuros("003", 0.05))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A conta não é do tipo poupança.");
    }

    @Test
    void deve_LancarContaBancariaNaoEncontradaException_Quando_ContaNaoExistir() {
        when(repository.findByNumero("999")).thenReturn(null);

        assertThatThrownBy(() -> service.renderJuros("999", 0.05))
                .isInstanceOf(ContaBancariaNaoEncontradaException.class)
                .hasMessage("Nenhuma conta com esse número foi encontrada.");
    }

    @Test
    void deve_LancarIllegalArgumentException_Quando_TaxaZero() {
        ContaPoupanca poupanca = new ContaPoupanca("002", 1000.0);
        when(repository.findByNumero("002")).thenReturn(poupanca);

        assertThatThrownBy(() -> service.renderJuros("002", 0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Valor deve ser maior que zero");
    }

    @Test
    void deve_LancarIllegalArgumentException_Quando_TaxaEhNegativa() {
        ContaPoupanca poupanca = new ContaPoupanca("002", 1000.0);
        when(repository.findByNumero("002")).thenReturn(poupanca);

        assertThatThrownBy(() -> service.renderJuros("002", -0.5))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Valor deve ser maior que zero");
    }
}

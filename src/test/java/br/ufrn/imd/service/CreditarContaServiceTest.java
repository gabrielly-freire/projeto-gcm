package br.ufrn.imd.service;

import br.ufrn.imd.exception.ContaBancariaNaoEncontradaException;
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
class CreditarContaServiceTest {

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
    void deve_CreditarValorComSucesso_Quando_CasoNormal() {
        ContaBancaria conta = new ContaBancaria("001", 100.0);
        when(repository.findByNumero("001")).thenReturn(conta);

        service.creditar("001", 50.0);

        assertThat(conta.getSaldo()).isEqualTo(150.0);
        verify(repository, times(1)).save(conta);
    }

    @Test
    void deve_LancarIllegalArgumentException_Quando_ValorDoParametroNegativo() {
        ContaBancaria conta = new ContaBancaria("001", 100.0);
        when(repository.findByNumero("001")).thenReturn(conta);

        assertThatThrownBy(() -> service.creditar("001", -50.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Valor deve ser maior que zero");
    }

    @Test
    void deve_LancarIllegalArgumentException_Quando_ValorDoParametroZero() {
        ContaBancaria conta = new ContaBancaria("001", 100.0);
        when(repository.findByNumero("001")).thenReturn(conta);

        assertThatThrownBy(() -> service.creditar("001", 0.0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Valor deve ser maior que zero");
    }

    @Test
    void deve_AplicarBonificacaoCorretamente_Quando_ContaDoTipoBonus() {
        ContaBonus contaBonus = new ContaBonus("003");
        contaBonus.setSaldo(0.0);
        ReflectionTestUtils.setField(contaBonus, "pontuacao", 0);
        when(repository.findByNumero("003")).thenReturn(contaBonus);
        service.creditar("003", 100.0);
        assertThat(contaBonus.getPontuacao()).isEqualTo(10);
        verify(repository, times(1)).save(contaBonus);
    }

    @Test
    void deve_LancarContaBancariaNaoEncontradaException_Quando_ContaNaoExistir() {
        when(repository.findByNumero("999")).thenReturn(null);

        assertThatThrownBy(() -> service.creditar("999", 50.0))
                .isInstanceOf(ContaBancariaNaoEncontradaException.class);
    }
}
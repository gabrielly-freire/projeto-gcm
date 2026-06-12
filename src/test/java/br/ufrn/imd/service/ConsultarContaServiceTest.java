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
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarContaServiceTest {

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
    void deve_RetornarContaBancaria_Quando_ContaSimplesExistir() {
        ContaBancaria conta = new ContaBancaria("001", 100.0);
        when(repository.findByNumero("001")).thenReturn(conta);

        ContaBancaria resultado = service.getConta("001");

        assertThat(resultado).isExactlyInstanceOf(ContaBancaria.class);
        assertThat(resultado.getNumero()).isEqualTo("001");
        assertThat(resultado.getSaldo()).isEqualTo(100.0);
    }

    @Test
    void deve_RetornarContaPoupanca_Quando_ContaPoupancaExistir() {
        ContaPoupanca poupanca = new ContaPoupanca("002", 200.0);
        when(repository.findByNumero("002")).thenReturn(poupanca);

        ContaBancaria resultado = service.getConta("002");

        assertThat(resultado).isInstanceOf(ContaPoupanca.class);
        assertThat(resultado.getNumero()).isEqualTo("002");
        assertThat(resultado.getSaldo()).isEqualTo(200.0);
    }

    @Test
    void deve_RetornarContaBonus_Quando_ContaBonusExistir() {
        ContaBonus bonus = new ContaBonus("003");
        when(repository.findByNumero("003")).thenReturn(bonus);

        ContaBancaria resultado = service.getConta("003");

        assertThat(resultado).isInstanceOf(ContaBonus.class);
        assertThat(resultado.getNumero()).isEqualTo("003");
    }

    @Test
    void deve_LancarContaBancariaNaoEncontradaException_Quando_ContaNaoExistir() {
        when(repository.findByNumero("999")).thenReturn(null);

        assertThatThrownBy(() -> service.getConta("999"))
                .isInstanceOf(ContaBancariaNaoEncontradaException.class)
                .hasMessage("Nenhuma conta com esse número foi encontrada.");
    }

}

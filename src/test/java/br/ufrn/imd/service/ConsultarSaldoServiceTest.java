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
class ConsultarSaldoServiceTest {

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
    void deve_RetornarSaldo_Quando_ContaBancariaComSaldo() {
        when(repository.findByNumero("001")).thenReturn(new ContaBancaria("001", 150.0));

        double saldo = service.consultarSaldo("001");

        assertThat(saldo).isEqualTo(150.0);
    }

    @Test
    void deve_RetornarSaldo_Quando_ContaPoupancaComSaldo() {
        when(repository.findByNumero("002")).thenReturn(new ContaPoupanca("002", 300.0));

        double saldo = service.consultarSaldo("002");

        assertThat(saldo).isEqualTo(300.0);
    }

    @Test
    void deve_RetornarSaldo_Quando_ContaBonusComSaldo() {
        ContaBonus bonus = new ContaBonus("003");
        bonus.setSaldo(250.0);
        when(repository.findByNumero("003")).thenReturn(bonus);

        double saldo = service.consultarSaldo("003");

        assertThat(saldo).isEqualTo(250.0);
    }

    @Test
    void deve_LancarContaBancariaNaoEncontradaException_Quando_ContaNaoExistir() {
        when(repository.findByNumero("999")).thenReturn(null);

        assertThatThrownBy(() -> service.consultarSaldo("999"))
                .isInstanceOf(ContaBancariaNaoEncontradaException.class)
                .hasMessage("Nenhuma conta com esse número foi encontrada.");
    }
}

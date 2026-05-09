package br.ufrn.imd.repository;
import java.util.HashSet;
import java.util.Set;
import br.ufrn.imd.model.ContaPoupanca;

public class ContaPoupancaRepository implements ContaRepository<ContaPoupanca> {
    
    private static ContaPoupancaRepository instance;

    private ContaPoupancaRepository() {}

    public static ContaPoupancaRepository getInstance() {
        if (instance == null) {
            instance = new ContaPoupancaRepository();
        }
        return instance;
    }

    private Set<ContaPoupanca> contas = new HashSet<>(Set.of(
            new ContaPoupanca("555555555"),
            new ContaPoupanca("333333333"),
            new ContaPoupanca("222222222")
    ));

    public Set<ContaPoupanca> findAll() {
        return contas;
    }

    public ContaPoupanca findByNumero(String numero) {
        for (ContaPoupanca conta : contas) {
            if (conta.getNumero().equals(numero)) {
                return conta;
            }
        }
        return null;
    }

    public void save(ContaPoupanca conta) {
        if (contas.contains(conta)) {
        contas.remove(conta);
    }
        contas.add(conta);
    }

    public boolean existsByNumero(String numero) {
        return contas.stream().anyMatch(c -> c.getNumero().equals(numero));
    }

    
}

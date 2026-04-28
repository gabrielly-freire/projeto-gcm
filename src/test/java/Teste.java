import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class Teste {

    @Test
    void somaTest() {

        double resultado = soma(1, 2);

        assertEquals(3, resultado);
    }

    double soma(double nume1, double nume2){
        return nume1 + nume2;
    }
}

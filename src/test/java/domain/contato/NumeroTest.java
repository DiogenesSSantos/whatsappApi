package domain.contato;

import com.github.dio.mensageria.domain.contato.CelularInvalidoException;
import com.github.dio.mensageria.domain.contato.Numero;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class NumeroTest {


    @Test
    void deveCriarNumeroQuandoTodosOsCamposForemVálido() {
        Numero numero = new Numero("5581984768748");
        assertNotNull(numero);
        assertEquals("5581984768748", numero.getCelular());

    }

    @Test
    void deveLancarExceptionCelularInvalidoExceptionQuandoNumeroCelularEstiverForaDoPadrão() {
        assertThrows(CelularInvalidoException.class, () -> new Numero("81984768748"));
    }

    @Test
    void deveAlterarStatusisWhatsappQuandoChamaroMetodoNaoPossuiWhatsappESeuValorSeraFalso() {
        Numero numero = new Numero("5581984768748");
        assertTrue(numero.isWhatsapp());

        numero.naoPossuiWhatsapp();
        assertFalse(numero.isWhatsapp());

    }

    @Test
    void deveAtualizarNumeroQuandoNumeroNovoEstiverCorreto() {
        String numeroNovo = "558184768748";
        Numero numero = new Numero("5581984768748");

        numero.atualizar(numeroNovo);

        assertNotNull(numero);
        assertEquals(numeroNovo, numero.getCelular());

    }


    @Test
    void deveLancarExceptionCelularInvalidoExceptionQuandoNumeroNovoEstiverNoFormatoIncorreto() {
        String numeroNovo = "2323558184768748";
        Numero numero = new Numero("5581984768748");


        assertThrows(CelularInvalidoException.class , () -> numero.atualizar(numeroNovo),
                () -> "Esperava-se a exception CelularInvalidoException mas não obteve a exception.");

    }


}

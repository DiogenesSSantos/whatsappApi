package entities.contato;

import com.github.dio.mensageria.domain.entities.contato.CelularInvalidoException;
import com.github.dio.mensageria.domain.entities.contato.Contato;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContatoTest {


    @Test
    void deveCriarContatoQuandoTodosOsCamposEstiveremCorretos() {
        Contato contato = new Contato("5581984768748", null);
        Contato contato2 = new Contato("558184768748", "Lidia queiroz");
        Assertions.assertNotNull(contato);
        Assertions.assertNotNull(contato2);
    }


    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {
            "+5511987654321",
            "55 11 987654321",
            "55(11)987654321",
            "55A11987654321",
            "550987654321",
            "55119876543212",
            "5511"
    })
    void deveLancarParaNumerosInvalidos(String numeroInvalido) {
        assertThrows(CelularInvalidoException.class,
                () -> new Contato(numeroInvalido, "Bairro"));
    }


}

package entities.contato;

import com.github.dio.mensageria.domain.entities.contato.CelularInvalidoException;
import com.github.dio.mensageria.domain.entities.contato.Contato;
import com.github.dio.mensageria.domain.entities.contato.Numero;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContatoTest {


    @Test
    void deveCriarContatoQuandoTodosOsCamposEstiveremCorretos() {
        LinkedList<Numero> numeroLinkedList = new LinkedList<>();
        numeroLinkedList.add(new Numero("81984768748"));


        Contato contato = new Contato(numeroLinkedList, null);
        Contato contato2 = new Contato(numeroLinkedList, "Lidia queiroz");
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
                () -> {
                    LinkedList<Numero> numeroLinkedList = new LinkedList<>();
                    numeroLinkedList.add(new Numero(numeroInvalido));

                    new Contato(numeroLinkedList, "Bairro");
                });
    }


}

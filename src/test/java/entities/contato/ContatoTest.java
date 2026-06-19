package entities.contato;

import com.github.dio.mensageria.domain.entities.contato.CelularInvalidoException;
import com.github.dio.mensageria.domain.entities.contato.Contato;
import com.github.dio.mensageria.domain.entities.contato.Numero;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

public class ContatoTest {


    private LinkedList<Numero> getNumeros() {
        LinkedList<Numero> numeroLinkedList = new LinkedList<>();
        numeroLinkedList.add(new Numero("5581984768748"));
        return numeroLinkedList;
    }


    @Test
    void deveCriarContatoQuandoTodosOsCamposEstiveremCorretos() {
        LinkedList<Numero> numeroLinkedList = getNumeros();

        Contato contato = new Contato(numeroLinkedList, null);
        Contato contato2 = new Contato(null, "Lidia queiroz");
        assertNotNull(contato);
        assertFalse(contato.getNumerosCelular().isEmpty());

        assertNotNull(contato2);
        assertTrue(contato2.getNumerosCelular().isEmpty());
    }


    @Test
    void deveAdicionarNovoNumeroQuandoChamarMetodoAdicionarNumeroCelularValido() {
        Contato contato = new Contato(null, null);
        assertTrue(contato.getNumerosCelular().isEmpty());

        contato.adicionarNumeroCelular("558184768748");
        assertEquals(1, contato.getNumerosCelular().size());
        assertEquals("558184768748", contato.getNumerosCelular().getFirst().getCelular());

    }


    @Test
    void deveAtualizarOBairroQuandoChamarOMetodoAtualizarBairro() {
        Contato contato = new Contato(null, null);
        contato.atualizarBairro("Lidia Queiroz");

        assertEquals("Lidia Queiroz", contato.getBairro());

    }

    @Test
    void deveAtualizarNumeroCelularQuandoChamarOMetodoAtualizarNumero() {
        LinkedList<Numero> numeroLinkedList = getNumeros();

        Contato contato = new Contato(numeroLinkedList, null);
        contato.atualizarNumeroParaContato("5581984768748",
                "558184768748");

        assertTrue(contato.existeNumeroParaContato("558184768748"));
        assertEquals(1, contato.getNumerosCelular().size());
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

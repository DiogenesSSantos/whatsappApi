package domain.paciente;

import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.domain.paciente.contato.Contato;
import com.github.dio.mensageria.domain.paciente.contato.Numero;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.domain.paciente.consulta.DataPassadoException;
import com.github.dio.mensageria.domain.paciente.PacienteBuilderException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PacienteTest {


    @Test
    void deveCriarOPacienteQuandoTiverTodosOsCamposValidos() {
        LinkedList<Numero> numeroLinkedList = new LinkedList<>();
        numeroLinkedList.add(new Numero("5581984768748"));

        Paciente paciente = Paciente.builder()
                .nome("Diogenes")
                .contato(new Contato(numeroLinkedList, "Alto da balança"))
                .consulta(new Consulta("Consulta Cardiológica", LocalDateTime.parse("2026-10-22T11:20")))
                .build();

        assertAll(
                () -> assertEquals("Diogenes", paciente.getNome()),
                () -> assertTrue(paciente.getContato().existeNumeroParaContato("5581984768748")),
                () -> assertEquals("Alto da balança", paciente.getContato().getBairro()),
                () -> assertEquals("Consulta Cardiológica", paciente.getConsulta().getNome()),
                () -> assertEquals(LocalDateTime.parse("2026-10-22T11:20"), paciente.getConsulta().getDataAtendimento()),
                () -> assertEquals(Consulta.Status.MARCADO, paciente.getConsulta().getStatus()),
                () -> assertNotNull(paciente.getCodigo()),
                () -> assertNotNull(paciente.getConsulta().getDataMarcacao()));

    }


    @Test
    void deveCriarUmPacienteComUUIDCasoNecessite() {
        LinkedList<Numero> numeroLinkedList = new LinkedList<>();
        numeroLinkedList.add(new Numero("5581984768748"));

        assertThrows(PacienteBuilderException.class,
                () -> Paciente.builder()
                        .codigo(UUID.randomUUID().toString())
                        .nome("Diogenes")
                        .contato(new Contato(numeroLinkedList, "Alto da balança"))
                        .consulta(null)
                        .build(),
                () -> "Esperava exception PacienteBuilderException mas obteve outro comportamento.");

    }


    @Test
    void deveLancarPacienteBuilderExceptionQuandoTentarCriarUmPacienteVazioOuNull() {
        LinkedList<Numero> numeroLinkedList = new LinkedList<>();
        numeroLinkedList.add(new Numero("5581984768748"));


        assertAll(
                () -> assertThrows(PacienteBuilderException.class,
                        () -> Paciente.builder()
                                .nome("")
                                .contato(new Contato(numeroLinkedList, "Alto da balança"))
                                .consulta(new Consulta("Consulta Cardiológica",
                                        LocalDateTime.now().plusDays(2)))
                                .build(),
                        () -> "Esperava exception PacienteBuilderException mas obteve outro comportamento."),

                () -> assertThrows(PacienteBuilderException.class,
                        () -> Paciente.builder()
                                .nome(null)
                                .contato(new Contato(numeroLinkedList, "Alto da balança"))
                                .consulta(new Consulta("Consulta Cardiológica",
                                        LocalDateTime.now().plusDays(2)))
                                .build(),
                        () -> "Esperava exception PacienteBuilderException mas obteve outro comportamento.")


        );

    }


    @Test
    void deveLancarPacienteBuilderExceptionQuandoTentarCriarUmPacienteSemContato() {
        assertThrows(PacienteBuilderException.class,
                () -> Paciente.builder()
                        .nome("Diogenes")
                        .consulta(new Consulta("Consulta Cardiológica", LocalDateTime.now().plusDays(2)))
                        .build(),
                () -> "Esperava exception PacienteBuilderException mas obteve outro comportamento.");

    }


    @Test
    void deveLancarPacienteBuilderExceptionQuandoTentarCriarUmPacienteSemConsulta() {
        LinkedList<Numero> numeroLinkedList = new LinkedList<>();
        numeroLinkedList.add(new Numero("5581984768748"));

        assertThrows(PacienteBuilderException.class,
                () -> Paciente.builder()
                        .nome("Diogenes")
                        .contato(new Contato(numeroLinkedList, "Alto da balança"))
                        .consulta(null)
                        .build(),
                () -> "Esperava exception PacienteBuilderException mas obteve outro comportamento.");

    }


    @Test
    void deveLancarExceptionDataPassadoExceptionQuandoForAtribuidaUmaDataConsultaNoPassado() {
        LinkedList<Numero> numeroLinkedList = new LinkedList<>();
        numeroLinkedList.add(new Numero("5581984768748"));
        LocalDateTime dataPassado = LocalDateTime.parse("2026-01-31T11:22:00");

        assertThrows(DataPassadoException.class,
                () -> Paciente.builder()
                        .nome("Diogenes")
                        .contato(new Contato(numeroLinkedList, "Alto da balança"))
                        .consulta(new Consulta("Consulta Cardiológica", dataPassado))
                        .build(),
                () -> "Esperava exception DataPassadoException mas obteve outro comportamento.");
    }

}

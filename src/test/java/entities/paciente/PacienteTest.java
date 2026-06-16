package entities.paciente;

import com.github.dio.mensageria.domain.entities.consulta.Consulta;
import com.github.dio.mensageria.domain.entities.contato.Contato;
import com.github.dio.mensageria.domain.entities.paciente.Paciente;
import com.github.dio.mensageria.domain.entities.consulta.DataPassadoException;
import com.github.dio.mensageria.domain.entities.paciente.PacienteBuilderException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class PacienteTest {


    @Test
    void deveCriarOPacienteQuandoTiverTodosOsCamposValidos() {
        Paciente paciente = Paciente.builder()
                .nome("Diogenes")
                .contato(new Contato("81984768748", "Alto da balança"))
                .consulta(new Consulta("Consulta Cardiológica", LocalDateTime.parse("2026-10-22T11:20")))
                .build();

        assertAll(
                () -> assertEquals("Diogenes", paciente.getNome()),
                () -> assertEquals("81984768748", paciente.getContato().getNumeroCelular()),
                () -> assertEquals("Alto da balança", paciente.getContato().getBairro()),
                () -> assertEquals("Consulta Cardiológica", paciente.getConsulta().getNome()),
                () -> assertEquals(LocalDateTime.parse("2026-10-22T11:20"), paciente.getConsulta().getDataAtendimento()),
                () -> assertEquals(Consulta.Status.MARCADO, paciente.getConsulta().getStatus()),
                () -> assertNotNull(paciente.getCodigo()),
                () -> assertNotNull(paciente.getConsulta().getDataMarcacao()));

    }


    @Test
    void deveLancarPacienteBuilderExceptionQuandoTentarCriarUmPacienteVazioOuNull() {
        assertAll(
                () -> assertThrows(PacienteBuilderException.class,
                        () -> Paciente.builder()
                                .nome("")
                                .contato(new Contato("81984768748", "Alto da balança"))
                                .consulta(new Consulta("Consulta Cardiológica",
                                        LocalDateTime.now().plusDays(2)))
                                .build(),
                        () -> "Esperava exception PacienteBuilderException mas obteve outro comportamento."),

                () -> assertThrows(PacienteBuilderException.class,
                        () -> Paciente.builder()
                                .nome(null)
                                .contato(new Contato("81984768748", "Alto da balança"))
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
    void deveLancarExceptionDataPassadoExceptionQuandoForAtribuidaUmaDataConsultaNoPassado() {
        LocalDateTime dataPassado = LocalDateTime.parse("2026-01-31T11:22:00");

        assertThrows(DataPassadoException.class,
                () -> Paciente.builder()
                        .nome("Diogenes")
                        .contato(new Contato("81984768748", "Alto da balança"))
                        .consulta(new Consulta("Consulta Cardiológica", dataPassado))
                        .build(),
                () -> "Esperava exception DataPassadoException mas obteve outro comportamento.");
    }

}

package entities;

import com.github.dio.mensageria.domain.entities.paciente.Paciente;
import com.github.dio.mensageria.domain.exception.DataPassadoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

import static org.junit.jupiter.api.Assertions.*;

public class PacienteTest {


    @Test
    void deveCriarOPacienteQuandoTiverTodosOsCamposValidos() {
        Paciente paciente = new Paciente("Diogenes", "81984768748", "Alto da balança",
                "Ortopedista", "2026-10-22T11:20:00", "Aceito");

        assertAll(
                () -> assertEquals("Diogenes", paciente.getNome()),
                () -> assertEquals("81984768748", paciente.getNumero()),
                () -> assertEquals("Alto da balança", paciente.getBairro()),
                () -> assertEquals("Ortopedista", paciente.getConsulta()),
                () -> assertEquals("2026-10-22T11:20:00", paciente.getDataConsulta()),
                () -> assertEquals("Aceito", paciente.getMotivo()),
                () -> assertNotNull(paciente.getCodigo()),
                () -> assertNotNull(paciente.getDataMarcacao()));

    }


    @Test
    void deveLancarExceptionDateTimeFormatterQuandoDataConsultaErrada() {
        String dataFormatadaErrada = "2026-10-478T11:22:00";

        assertThrows(DateTimeParseException.class,
                () -> new Paciente("Diogenes", "81984768748", "Alto da balança",
                        "Ortopedista", dataFormatadaErrada, "Aceito"),
                () -> "Esperava exception DateTimeParseException mas obteve outro comportamento.");

    }

    @Test
    void deveLancarExceptionDataPassadoExceptionQuandoForAtribuidaUmaDataConsultaNoPassado() {
        LocalDateTime dataPassado = LocalDateTime.parse("2026-01-31T11:22:00");

        assertThrows(DataPassadoException.class,
                () -> new Paciente("Diogenes", "81984768748", "Alto da balança",
                        "Ortopedista", dataPassado.toString(), "Aceito"),
                () -> "Esperava exception DataPassadoException mas obteve outro comportamento.");
    }

}

package persistence;


import com.github.dio.mensageria.domain.consulta.Consulta;
import com.github.dio.mensageria.domain.contato.Contato;
import com.github.dio.mensageria.domain.contato.Numero;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.infra.gateways.PacienteRepositoryJPA;
import com.github.dio.mensageria.start;
import config.TestcontainersConfig;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = start.class)
@ContextConfiguration(initializers = TestcontainersConfig.Initializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PacienteEntityRepositoryTest {

    @Autowired
    private PacienteRepositoryJPA pacienteRepositoryJPA;


    @Test
    void deveBuscarTodosPacientesQuandoChamarOmetodoBuscarTodos() {
        List<Paciente> pacienteList = pacienteRepositoryJPA.buscarTodos();

        assertNotNull(pacienteList);
        assertFalse(pacienteList.isEmpty());
    }


    @Test
    void deveSalvarUmPacienteQuandoTodosOsCamposTiveremValido() {
        Paciente paciente = Paciente.builder()
                .nome("Diogenes dos Santos")
                .contato(new Contato(List.of(new Numero("558184768748")), "Lidia queiroz"))
                .consulta(new Consulta("Ortopedia", LocalDateTime.now().plusDays(2)))
                .build();

        Paciente pacienteBD = pacienteRepositoryJPA.salvar(paciente);

        assertNotNull(pacienteBD);
        assertEquals(pacienteBD.getCodigo(), paciente.getCodigo());
        assertEquals(pacienteBD.getNome(), paciente.getNome());
        assertNotNull(pacienteBD.getConsulta());
        assertNotNull(pacienteBD.getContato());

    }

}

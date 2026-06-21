package persistence;


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

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = start.class)
@ContextConfiguration(initializers = TestcontainersConfig.Initializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class PacienteEntityRepositoryTest {

    @Autowired
    private PacienteRepositoryJPA pacienteRepositoryJPA;


    @Test
    void testando() {
        List<Paciente> pacienteList = pacienteRepositoryJPA.buscarTodos();
        assertNotNull(pacienteList);
        assertFalse(pacienteList.isEmpty());

        System.out.println(pacienteList);
    }
}

package domain.consulta;

import com.github.dio.mensageria.domain.consulta.Consulta;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ConsultaTest {


    @Test
    void deveCriarConsultaQuandoTodosOsCamposEstiveremValido(){
        Consulta consulta = new Consulta("Ortopedia", LocalDateTime.now().plusDays(2));


        assertNotNull(consulta);
        assertEquals(Consulta.Status.MARCADO,consulta.getStatus());
        assertEquals(LocalDateTime.now().getDayOfMonth(), consulta.getDataMarcacao().getDayOfMonth());
    }


}

package infraestrutura;

import com.github.dio.mensageria.domain.mensagem.ResultadoEnvio;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.domain.paciente.contato.Contato;
import com.github.dio.mensageria.domain.paciente.contato.Numero;
import com.github.dio.mensageria.Start;
import com.github.dio.mensageria.infra.gateways.MensageriaN8N;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest(classes = Start.class)
@ActiveProfiles("local")
class MensageriaN8NIntegrationTest {

    @Autowired
    private MensageriaN8N mensageriaN8N;

    @Test
    void deveEnviarMensagemParaNumero() throws Exception {
        LinkedList<Numero> numeros = new LinkedList<>();
        numeros.add(new Numero("558184768748"));

        Paciente paciente = Paciente.builder()
                .nome("Diogenes")
                .contato(new Contato(numeros, "Centro"))
                .consulta(new Consulta("Consulta Cardiológica", LocalDateTime.now().plusDays(2)))
                .build();

        ResultadoEnvio resultado = mensageriaN8N.enviar(paciente);

        assertInstanceOf(ResultadoEnvio.Sucesso.class, resultado);
    }
}

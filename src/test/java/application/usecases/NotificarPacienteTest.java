package application.usecases;

import com.github.dio.mensageria.application.gateways.output.Mensageria;
import com.github.dio.mensageria.application.services.FilaMensagem;
import com.github.dio.mensageria.application.usecases.NotificarPaciente;
import com.github.dio.mensageria.domain.mensagem.ResultadoEnvio;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.domain.paciente.contato.Contato;
import com.github.dio.mensageria.domain.paciente.contato.Numero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificarPacienteTest {

    @Mock
    private Mensageria mensageria;

    @Mock
    private FilaMensagem filaMensagem;

    private NotificarPaciente notificarPaciente;

    @BeforeEach
    void setUp() {
        notificarPaciente = new NotificarPaciente(mensageria, filaMensagem);
    }

    private Paciente criarPaciente(String nome) {
        LinkedList<Numero> numeros = new LinkedList<>();
        numeros.add(new Numero("5581984768748"));
        return Paciente.builder()
                .nome(nome)
                .contato(new Contato(numeros, "Centro"))
                .consulta(new Consulta("Cardiologia", LocalDateTime.now().plusDays(2)))
                .build();
    }

    @Test
    void deveEnviarPacienteDiretamenteQuandoChamarEnviar() throws Exception {
        Paciente paciente = criarPaciente("João Silva");
        when(mensageria.enviar(paciente)).thenReturn(new ResultadoEnvio.Sucesso());

        notificarPaciente.enviar(paciente);

        verify(mensageria, times(1)).enviar(paciente);
        verify(filaMensagem, never()).enfileirar(any(Paciente.class));
    }

    @Test
    void deveLancarExceptionQuandoEnviarFalhar() throws Exception {
        Paciente paciente = criarPaciente("João Silva");
        when(mensageria.enviar(paciente)).thenThrow(new RuntimeException("Erro"));

        assertThrows(RuntimeException.class,
                () -> notificarPaciente.enviar(paciente));
    }

    @Test
    void deveEnfileirarPacienteUnico() throws Exception {
        Paciente paciente = criarPaciente("João Silva");

        notificarPaciente.enfileirar(paciente);

        verify(filaMensagem, times(1)).enfileirar(paciente);
        verify(mensageria, never()).enviar(any(Paciente.class));
    }

    @Test
    void deveEnfileirarListaDePacientes() throws Exception {
        Paciente p1 = criarPaciente("Paciente 1");
        Paciente p2 = criarPaciente("Paciente 2");
        Paciente p3 = criarPaciente("Paciente 3");

        notificarPaciente.enfileirar(List.of(p1, p2, p3));

        verify(filaMensagem, times(1)).enfileirar(p1);
        verify(filaMensagem, times(1)).enfileirar(p2);
        verify(filaMensagem, times(1)).enfileirar(p3);
        verify(mensageria, never()).enviar(any(Paciente.class));
    }

    @Test
    void deveEnfileirarListaVaziaSemErro() {
        notificarPaciente.enfileirar(List.of());

        verify(filaMensagem, never()).enfileirar(any(Paciente.class));
    }

    @Test
    void deveRetornarTamanhoDaFila() {
        when(filaMensagem.tamanho()).thenReturn(5);

        int tamanho = notificarPaciente.filaTamanho();

        assertEquals(5, tamanho);
        verify(filaMensagem, times(1)).tamanho();
    }
}

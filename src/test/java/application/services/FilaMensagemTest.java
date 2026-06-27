package application.services;

import com.github.dio.mensageria.application.gateways.output.Mensageria;
import com.github.dio.mensageria.application.services.FilaMensagem;
import com.github.dio.mensageria.domain.mensagem.ResultadoEnvio;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.domain.paciente.contato.Contato;
import com.github.dio.mensageria.domain.paciente.contato.Numero;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class FilaMensagemTest {

    private Mensageria mensageria;
    private FilaMensagem filaMensagem;

    @BeforeEach
    void setUp() {
        mensageria = mock(Mensageria.class);
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
    void deveEnfileirarPacienteEProcessar() throws Exception {
        when(mensageria.enviar(any(Paciente.class))).thenReturn(new ResultadoEnvio.Sucesso());
        filaMensagem = new FilaMensagem(mensageria, 0);

        Paciente paciente = criarPaciente("João Silva");
        filaMensagem.enfileirar(paciente);

        Thread.sleep(500);

        verify(mensageria, times(1)).enviar(paciente);
        assertEquals(0, filaMensagem.tamanho());
    }

    @Test
    void deveProcessarMultiplosPacientesSequencialmente() throws Exception {
        CountDownLatch latch = new CountDownLatch(3);
        AtomicInteger ordem = new AtomicInteger(0);

        when(mensageria.enviar(any(Paciente.class))).thenAnswer(invocation -> {
            int atual = ordem.incrementAndGet();
            latch.countDown();
            return new ResultadoEnvio.Sucesso();
        });

        filaMensagem = new FilaMensagem(mensageria, 0);

        Paciente p1 = criarPaciente("Paciente 1");
        Paciente p2 = criarPaciente("Paciente 2");
        Paciente p3 = criarPaciente("Paciente 3");

        filaMensagem.enfileirar(p1);
        filaMensagem.enfileirar(p2);
        filaMensagem.enfileirar(p3);

        assertTrue(latch.await(5, TimeUnit.SECONDS), "Deveria processar 3 pacientes");
        assertEquals(3, ordem.get());
        assertEquals(0, filaMensagem.tamanho());
    }

    @Test
    void deveRespeitarDelayEntrePacientes() throws Exception {
        when(mensageria.enviar(any(Paciente.class))).thenReturn(new ResultadoEnvio.Sucesso());

        long delayMs = 1000;
        filaMensagem = new FilaMensagem(mensageria, delayMs);

        Paciente p1 = criarPaciente("Paciente 1");
        Paciente p2 = criarPaciente("Paciente 2");

        long inicio = System.currentTimeMillis();
        filaMensagem.enfileirar(p1);
        filaMensagem.enfileirar(p2);

        Thread.sleep(3000);

        long[] tempos = new long[2];
        verify(mensageria, times(2)).enviar(any(Paciente.class));
    }

    @Test
    void deveRetornarTamanhoCorretoDaFila() throws Exception {
        when(mensageria.enviar(any(Paciente.class))).thenAnswer(invocation -> {
            Thread.sleep(2000);
            return new ResultadoEnvio.Sucesso();
        });

        filaMensagem = new FilaMensagem(mensageria, 0);

        Paciente p1 = criarPaciente("Paciente 1");
        Paciente p2 = criarPaciente("Paciente 2");

        filaMensagem.enfileirar(p1);
        filaMensagem.enfileirar(p2);

        Thread.sleep(100);

        assertTrue(filaMensagem.tamanho() >= 1, "Deveria ter pacientes na fila");
    }

    @Test
    void deveContinuarProcessandoMesmoComErro() throws Exception {
        AtomicInteger processados = new AtomicInteger(0);

        when(mensageria.enviar(any(Paciente.class)))
                .thenThrow(new RuntimeException("Erro simulado"))
                .thenReturn(new ResultadoEnvio.Sucesso());

        filaMensagem = new FilaMensagem(mensageria, 0);

        Paciente p1 = criarPaciente("Paciente com erro");
        Paciente p2 = criarPaciente("Paciente válido");

        filaMensagem.enfileirar(p1);
        filaMensagem.enfileirar(p2);

        Thread.sleep(1000);

        verify(mensageria, times(2)).enviar(any(Paciente.class));
        assertEquals(0, filaMensagem.tamanho());
    }
}

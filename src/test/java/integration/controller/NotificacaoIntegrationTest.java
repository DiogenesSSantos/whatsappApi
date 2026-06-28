package integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dio.mensageria.Start;
import com.github.dio.mensageria.application.gateways.output.Mensageria;
import com.github.dio.mensageria.application.services.FilaMensagem;
import com.github.dio.mensageria.domain.mensagem.ResultadoEnvio;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.*;
import config.TestcontainersConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Start.class, properties = "fila.delay-ms=0")
@AutoConfigureMockMvc
@ContextConfiguration(initializers = TestcontainersConfig.Initializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NotificacaoIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private Mensageria mensageria;

    private static final String API_PACIENTES = "/api/pacientes";

    @Test
    void deveEnviarNotificacaoQuandoFilaProcessaPacienteIndividual() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger chamadas = new AtomicInteger(0);

        when(mensageria.enviar(any(Paciente.class))).thenAnswer(invocation -> {
            chamadas.incrementAndGet();
            latch.countDown();
            return new ResultadoEnvio.Sucesso();
        });

        PacienteDTORequest request = criarPacienteDTORequestComNome("Paciente Notificação");

        mockMvc.perform(
                        post(API_PACIENTES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensagem").value("Paciente enfileirado para notificacao"))
                .andDo(print());

        boolean processado = latch.await(10, TimeUnit.SECONDS);

        assertTrue(processado, "Fila deveria processar o paciente em 10 segundos");
        assertEquals(1, chamadas.get(), "mensageria.enviar() deveria ser chamada 1 vez");
        verify(mensageria, times(1)).enviar(any(Paciente.class));
    }

    @Test
    void deveEnviarNotificacaoParaCadaPacienteDoLote() throws Exception {
        int quantidadePacientes = 3;
        CountDownLatch latch = new CountDownLatch(quantidadePacientes);
        AtomicInteger chamadas = new AtomicInteger(0);

        when(mensageria.enviar(any(Paciente.class))).thenAnswer(invocation -> {
            chamadas.incrementAndGet();
            latch.countDown();
            return new ResultadoEnvio.Sucesso();
        });

        PacienteDTORequest paciente1 = criarPacienteDTORequestComNome("Lote Notificação 1");
        PacienteDTORequest paciente2 = criarPacienteDTORequestComNome("Lote Notificação 2");
        PacienteDTORequest paciente3 = criarPacienteDTORequestComNome("Lote Notificação 3");

        List<PacienteDTORequest> lote = List.of(paciente1, paciente2, paciente3);

        mockMvc.perform(
                        post(API_PACIENTES + "/lote")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(lote))
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.mensagem").value("Pacientes enfileirados para notificacao"))
                .andExpect(jsonPath("$.quantidade").value(quantidadePacientes))
                .andDo(print());

        boolean processados = latch.await(15, TimeUnit.SECONDS);

        assertTrue(processados, "Fila deveria processar todos os 3 pacientes em 15 segundos");
        assertEquals(quantidadePacientes, chamadas.get(), "mensageria.enviar() deveria ser chamada 3 vezes");
        verify(mensageria, times(quantidadePacientes)).enviar(any(Paciente.class));
    }

    @Test
    void deveProcessarPacientesIndividuaisELoteNaMesmaFila() throws Exception {
        int totalEsperado = 4;
        CountDownLatch latch = new CountDownLatch(totalEsperado);
        AtomicInteger chamadas = new AtomicInteger(0);

        when(mensageria.enviar(any(Paciente.class))).thenAnswer(invocation -> {
            chamadas.incrementAndGet();
            latch.countDown();
            return new ResultadoEnvio.Sucesso();
        });

        PacienteDTORequest individual = criarPacienteDTORequestComNome("Individual Mix");

        mockMvc.perform(
                        post(API_PACIENTES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(individual))
                )
                .andExpect(status().isCreated());

        PacienteDTORequest lote1 = criarPacienteDTORequestComNome("Mix Lote 1");
        PacienteDTORequest lote2 = criarPacienteDTORequestComNome("Mix Lote 2");
        PacienteDTORequest lote3 = criarPacienteDTORequestComNome("Mix Lote 3");

        mockMvc.perform(
                        post(API_PACIENTES + "/lote")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(List.of(lote1, lote2, lote3)))
                )
                .andExpect(status().isAccepted());

        boolean processados = latch.await(15, TimeUnit.SECONDS);

        assertTrue(processados, "Fila deveria processar todos os 4 pacientes");
        assertEquals(totalEsperado, chamadas.get());
        verify(mensageria, times(totalEsperado)).enviar(any(Paciente.class));
    }

    private PacienteDTORequest criarPacienteDTORequestComNome(String nome) {
        LinkedList<NumeroDTORequest> numeros = new LinkedList<>();
        numeros.add(new NumeroDTORequest("5581987654321", true));

        ContatoDTORequest contato = new ContatoDTORequest(numeros, "Centro");
        ConsultaDTORequest consulta = new ConsultaDTORequest(
                "Consulta Teste",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now(),
                StatusDTORequest.MARCADO
        );

        return new PacienteDTORequest(nome, contato, consulta);
    }
}

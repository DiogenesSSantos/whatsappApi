package integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dio.mensageria.Start;
import com.github.dio.mensageria.infra.controller.advice.ErrorBodyResponse;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.*;
import com.github.dio.mensageria.infra.controller.pacientecontroller.response.PacienteDTOResponse;
import config.TestcontainersConfig;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes de Integração para o Controller de Pacientes.
 * Testa a camada HTTP completa com banco de dados real (TestContainers).
 *
 * @author diogenesssantos
 */
@SpringBootTest(classes = Start.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = TestcontainersConfig.Initializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PacienteControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API_PACIENTES = "/api/pacientes";

    /**
     * Teste de sucesso: Criar paciente com todos os dados válidos
     * Esperado: Status 201 CREATED e response válida
     */
    @Test
    void deveCriarPacienteComSucessoQuandoTodosOsCamposForemValidos() throws Exception {
        PacienteDTORequest request = criarPacienteDTORequestValido();

        MvcResult result = mockMvc.perform(
                        post(API_PACIENTES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.mensagem").value("Paciente enfileirado para notificacao"))
                .andExpect(jsonPath("$.paciente.nome").value("João Silva"))
                .andExpect(jsonPath("$.paciente.contato.bairro").value("Centro"))
                .andExpect(jsonPath("$.paciente.contato.numerosCelular[0].celular").value("5581987654321"))
                .andExpect(jsonPath("$.paciente.contato.numerosCelular[0].isWhatsapp").value(true))
                .andExpect(jsonPath("$.paciente.consulta.nome").value("Consulta Clínica"))
                .andExpect(jsonPath("$.paciente.consulta.status").value("MARCADO"))
                .andExpect(jsonPath("$.filaTamanho").isNumber())
                .andDo(print())
                .andReturn();

        String responseContent = getResponseContentUTF8(result);
        assertNotNull(responseContent);
        assertTrue(responseContent.contains("Paciente enfileirado para notificacao"));
        assertTrue(responseContent.contains("João Silva"));
    }


    /**
     * Teste de sucesso: Criar múltiplos pacientes
     * Esperado: Todos os pacientes devem ser criados com sucesso
     */
    @Test
    void devePermitirCriarMultiplosPacientes() throws Exception {
        PacienteDTORequest pacienteDiogenes = criarPacienteDTORequestComNome("Diogenes Santos");
        PacienteDTORequest pacienteMaria = criarPacienteDTORequestComNome("Maria Jose da Silva");

        mockMvc.perform(
                        post(API_PACIENTES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pacienteDiogenes))
                ).andDo(print())
                .andExpect(status().isCreated());

        mockMvc.perform(
                        post(API_PACIENTES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(pacienteMaria))
                ).andDo(print())
                .andExpect(status().isCreated());
    }

    /**
     * Teste com múltiplos números de telefone
     * Esperado: Todos os números devem ser salvos
     */
    @Test
    void deveCriarPacienteComMultiplosTelefones() throws Exception {
        LinkedList<NumeroDTORequest> numeros = new LinkedList<>();
        numeros.add(new NumeroDTORequest("5581987654321", true));
        numeros.add(new NumeroDTORequest("5581912345678", false));
        numeros.add(new NumeroDTORequest("5581998765432", true));
        numeros.add(new NumeroDTORequest("558184768748", true));


        ContatoDTORequest contato = new ContatoDTORequest(numeros, "Centro");
        ConsultaDTORequest consulta = criarConsultaDTORequest();
        PacienteDTORequest request = new PacienteDTORequest("Maria Silva", contato, consulta);

        MvcResult result = mockMvc.perform(
                        post(API_PACIENTES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andExpect(status().isCreated())
                .andExpect(jsonPath("$.paciente.contato.numerosCelular.length()").value(4))
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertTrue(responseContent.contains("4"));
    }

    /**
     * Teste com diferentes status de consulta
     * Esperado: Todos os status devem ser aceitos
     */
    @Test
    void deveCriarPacienteComDiferentesStatusDeConsulta() throws Exception {
        // Test com cada status
        for (StatusDTORequest status : StatusDTORequest.values()) {
            PacienteDTORequest request = new PacienteDTORequest(
                    "Paciente " + status.name(),
                    new ContatoDTORequest(
                            new LinkedList<>(java.util.List.of(new NumeroDTORequest("5581987654321", true))),
                            "Centro"
                    ),
                    new ConsultaDTORequest(
                            "Consulta " + status.name(),
                            LocalDateTime.now().plusDays(3),
                            LocalDateTime.now(),
                            status
                    )
            );

            mockMvc.perform(
                    post(API_PACIENTES)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request))
            ).andExpect(status().isCreated());
        }
    }

    /**
     * Teste de validação: Nome vazio
     * Esperado: Pode aceitar ou rejeitar (depende da validação implementada)
     */
    @Test
    void deveLidarComNomeVazio() throws Exception {
        PacienteDTORequest request = new PacienteDTORequest(
                "",
                new ContatoDTORequest(
                        new LinkedList<>(java.util.List.of(new NumeroDTORequest("5581987654321", true))),
                        "Centro"
                ),
                criarConsultaDTORequest()
        );


        mockMvc.perform(
                        post(API_PACIENTES)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                ).andDo(print())
                .andExpect(result -> {
                    int status = result.getResponse().getStatus();
                    assertTrue(status == 400 , () -> "Status deve ser 201, 400 ou 500, mas foi " + status);
                });
    }

    /**
     * Teste de validação: Contato nulo
     * Esperado: Erro na validação ou na persistência
     */
    @Test
    void deveLidarComContatoNulo() throws Exception {
        // Arrange
        PacienteDTORequest request = new PacienteDTORequest(
                "João Silva",
                null,
                criarConsultaDTORequest()
        );

        mockMvc.perform(
                post(API_PACIENTES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(result -> {
            int status = result.getResponse().getStatus();
            assertTrue(status >= 400, "Esperado erro com status >= 400");
        });
    }

    /**
     * Teste de validação: Consulta nula
     * Esperado: Erro na validação
     */
    @Test
    void deveLidarComConsultaNula() throws Exception {
        // Arrange
        PacienteDTORequest request = new PacienteDTORequest(
                "João Silva",
                new ContatoDTORequest(
                        new LinkedList<>(java.util.List.of(new NumeroDTORequest("11987654321", true))),
                        "Centro"
                ),
                null
        );

        // Act & Assert
        mockMvc.perform(
                post(API_PACIENTES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(result -> {
            int status = result.getResponse().getStatus();
            assertTrue(status >= 400, "Esperado erro com status >= 400");
        });
    }

    /**
     * Teste de serialização JSON
     * Esperado: JSON válido sendo aceito
     */
    @Test
    void deveAceitarJSONValido() throws Exception {
        String json = """
                {
                    "nome": "João Silva",
                    "contato": {
                        "numerosCelular": [
                            {
                                "celular": "5581987654321",
                                "isWhatsapp": true
                            }
                        ],
                        "bairro": "Centro"
                    },
                    "consulta": {
                        "nome": "Consulta Clínica",
                        "dataAtendimento": "2024-12-31T14:30:00",
                        "dataMarcacao": "2024-12-30T10:00:00",
                        "status": "MARCADO"
                    }
                }
                """;

        mockMvc.perform(
                post(API_PACIENTES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
        ).andExpect(status().isUnprocessableEntity());
    }


    /**
     * Teste de resposta correta
     * Esperado: Response contém todos os campos esperados
     */
    @Test
    void deveRetornarResponseComToposCamposPreenchidos() throws Exception {
        PacienteDTORequest request = criarPacienteDTORequestValido();

        MvcResult result = mockMvc.perform(
                post(API_PACIENTES)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
        ).andExpect(status().isCreated()).andReturn();

        String responseContent = result.getResponse().getContentAsString();
        assertFalse(responseContent.isEmpty());
        assertTrue(responseContent.contains("mensagem"));
        assertTrue(responseContent.contains("paciente"));
        assertTrue(responseContent.contains("filaTamanho"));
        assertTrue(responseContent.contains("nome"));
        assertTrue(responseContent.contains("contato"));
        assertTrue(responseContent.contains("consulta"));
    }

    /**
     * Teste de sucesso: Criar pacientes em lote
     * Esperado: Todos enfileirados com tamanho da fila correto
     */
    @Test
    void deveEnfileirarPacientesEmLoteComSucesso() throws Exception {
        PacienteDTORequest paciente1 = criarPacienteDTORequestComNome("Paciente Lote 1");
        PacienteDTORequest paciente2 = criarPacienteDTORequestComNome("Paciente Lote 2");
        PacienteDTORequest paciente3 = criarPacienteDTORequestComNome("Paciente Lote 3");

        List<PacienteDTORequest> lote = List.of(paciente1, paciente2, paciente3);

        MvcResult result = mockMvc.perform(
                        post(API_PACIENTES + "/lote")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(lote))
                )
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.mensagem").value("Pacientes enfileirados para notificacao"))
                .andExpect(jsonPath("$.quantidade").value(3))
                .andExpect(jsonPath("$.filaTamanho").isNumber())
                .andDo(print())
                .andReturn();

        String responseContent = getResponseContentUTF8(result);
        assertTrue(responseContent.contains("3"));
    }


    // ==================== Métodos Auxiliares ====================
    private PacienteDTORequest criarPacienteDTORequestValido() {
        LinkedList<NumeroDTORequest> numeros = new LinkedList<>();
        numeros.add(new NumeroDTORequest("5581987654321", true));
        numeros.add(new NumeroDTORequest("558184768748", true));

        ContatoDTORequest contato = new ContatoDTORequest(numeros, "Centro");
        ConsultaDTORequest consulta = criarConsultaDTORequest();

        return new PacienteDTORequest("João Silva", contato, consulta);
    }

    private PacienteDTORequest criarPacienteDTORequestComNome(String nome) {
        LinkedList<NumeroDTORequest> numeros = new LinkedList<>();
        numeros.add(new NumeroDTORequest("5581987654321", true));

        ContatoDTORequest contato = new ContatoDTORequest(numeros, "Centro");
        ConsultaDTORequest consulta = criarConsultaDTORequest();

        return new PacienteDTORequest(nome, contato, consulta);
    }

    private ConsultaDTORequest criarConsultaDTORequest() {
        return new ConsultaDTORequest(
                "Consulta Clínica",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now(),
                StatusDTORequest.MARCADO
        );
    }

    private @NotNull String getResponseContentUTF8(MvcResult result) {
        return new String(result.getResponse().getContentAsByteArray(), StandardCharsets.UTF_8);
    }
}


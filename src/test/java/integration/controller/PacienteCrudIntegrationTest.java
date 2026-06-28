package integration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.dio.mensageria.Start;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.ConsultaDTORequest;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.ContatoDTORequest;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.NumeroDTORequest;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.PacienteDTORequest;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.StatusDTORequest;
import config.TestcontainersConfig;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = Start.class)
@AutoConfigureMockMvc
@ContextConfiguration(initializers = TestcontainersConfig.Initializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class PacienteCrudIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static final String API = "/api/pacientes";
    private String codigoPacienteCriado;

    @Test
    @Order(1)
    void deveCriarPaciente() throws Exception {
        PacienteDTORequest request = criarRequestValido();

        MvcResult result = mockMvc.perform(post(API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.mensagem").isNotEmpty())
                .andExpect(jsonPath("$.paciente.codigo").isNotEmpty())
                .andExpect(jsonPath("$.paciente.nome").value("João Teste"))
                .andReturn();

        String json = result.getResponse().getContentAsString();
        var response = objectMapper.readTree(json);
        codigoPacienteCriado = response.get("paciente").get("codigo").asText();
        Assertions.assertNotNull(codigoPacienteCriado);
    }

    @Test
    @Order(2)
    void deveListarTodos() throws Exception {
        mockMvc.perform(get(API))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(org.hamcrest.Matchers.greaterThanOrEqualTo(1)));
    }

    @Test
    @Order(3)
    void deveBuscarPorCodigo() throws Exception {
        mockMvc.perform(get(API + "/" + codigoPacienteCriado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.codigo").value(codigoPacienteCriado))
                .andExpect(jsonPath("$.nome").value("João Teste"));
    }

    @Test
    @Order(4)
    void deveRetornar404ParaCodigoInexistente() throws Exception {
        mockMvc.perform(get(API + "/codigo-inexistente-123"))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(5)
    void deveBuscarComFiltros() throws Exception {
        mockMvc.perform(get(API + "/buscar")
                        .param("nome", "Jo")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray())
                .andExpect(jsonPath("$.pagina").value(0))
                .andExpect(jsonPath("$.tamanhoPagina").value(10))
                .andExpect(jsonPath("$.totalItens").isNumber())
                .andExpect(jsonPath("$.totalPaginas").isNumber());
    }

    @Test
    @Order(6)
    void deveFiltrarPorStatus() throws Exception {
        mockMvc.perform(get(API + "/buscar")
                        .param("status", "MARCADO")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.conteudo").isArray());
    }

    @Test
    @Order(7)
    void deveRetornar400ParaStatusInvalido() throws Exception {
        mockMvc.perform(get(API + "/buscar")
                        .param("status", "STATUS_INVALIDO")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(8)
    void deveAtualizarPaciente() throws Exception {
        PacienteDTORequest requestAtualizado = new PacienteDTORequest(
                "João Atualizado",
                new ContatoDTORequest(
                        List.of(new NumeroDTORequest("5527999999999", true)),
                        "Bairro Atualizado"
                ),
                new ConsultaDTORequest(
                        "Consulta Atualizada",
                        LocalDateTime.now().plusDays(10),
                        LocalDateTime.now(),
                        StatusDTORequest.AGUARDANDO
                )
        );

        mockMvc.perform(put(API + "/" + codigoPacienteCriado)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAtualizado)))
                .andExpect(status().isNoContent());

        // Verifica se foi atualizado
        mockMvc.perform(get(API + "/" + codigoPacienteCriado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Atualizado"))
                .andExpect(jsonPath("$.contato.bairro").value("Bairro Atualizado"))
                .andExpect(jsonPath("$.consulta.nome").value("Consulta Atualizada"));
    }

    @Test
    @Order(9)
    void deveAtualizarStatus() throws Exception {
        mockMvc.perform(patch(API + "/" + codigoPacienteCriado + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "REJEITADO"))))
                .andExpect(status().isNoContent());

        // Verifica se o status foi atualizado
        mockMvc.perform(get(API + "/" + codigoPacienteCriado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.consulta.status").value("REJEITADO"));
    }

    @Test
    @Order(10)
    void deveRetornar400ParaStatusInvalidoNaAtualizacao() throws Exception {
        mockMvc.perform(patch(API + "/" + codigoPacienteCriado + "/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "INVALIDO"))))
                .andExpect(status().isBadRequest());
    }

    @Test
    @Order(11)
    void deveRetornar404AoAtualizarStatusDePacienteInexistente() throws Exception {
        mockMvc.perform(patch(API + "/codigo-inexistente/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(Map.of("status", "MARCADO"))))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(12)
    void deveExcluirPaciente() throws Exception {
        // Cria um paciente para excluir
        PacienteDTORequest request = criarRequestValido();
        request = new PacienteDTORequest(
                "Paciente Para Excluir",
                request.contato(),
                request.consulta()
        );

        MvcResult result = mockMvc.perform(post(API)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        String json = result.getResponse().getContentAsString();
        var response = objectMapper.readTree(json);
        String codigoParaExcluir = response.get("paciente").get("codigo").asText();

        mockMvc.perform(delete(API + "/" + codigoParaExcluir))
                .andExpect(status().isNoContent());

        // Verifica que nao existe mais
        mockMvc.perform(get(API + "/" + codigoParaExcluir))
                .andExpect(status().isNotFound());
    }

    @Test
    @Order(13)
    void deveRetornar404AoExcluirPacienteInexistente() throws Exception {
        mockMvc.perform(delete(API + "/codigo-inexistente-123"))
                .andExpect(status().isNotFound());
    }

    private PacienteDTORequest criarRequestValido() {
        return new PacienteDTORequest(
                "João Teste",
                new ContatoDTORequest(
                        List.of(new NumeroDTORequest("5527999010101", true)),
                        "Centro"
                ),
                new ConsultaDTORequest(
                        "Consulta Geral",
                        LocalDateTime.now().plusDays(7),
                        LocalDateTime.now(),
                        StatusDTORequest.MARCADO
                )
        );
    }
}

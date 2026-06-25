package integration.mapper;

import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.domain.paciente.contato.Contato;
import com.github.dio.mensageria.domain.paciente.contato.Numero;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.infra.controller.pacientecontroller.PacienteControllerMapper;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.*;
import com.github.dio.mensageria.infra.controller.pacientecontroller.response.PacienteDTOResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de Unitários para o Mapper de Pacientes.
 * Valida a conversão entre DTOs e Domain Models.
 *
 * @author diogenesssantos
 */
class PacienteControllerMapperTest {

    private PacienteControllerMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new PacienteControllerMapper();
    }

    /**
     * Teste: Converter DTO válido para Model
     * Esperado: Model criado corretamente
     */
    @Test
    void deveConverterDTORequestParaModelComSucesso() {
        // Arrange
        LinkedList<NumeroDTORequest> numeros = new LinkedList<>();
        numeros.add(new NumeroDTORequest("5581987654321", true));

        ContatoDTORequest contatoDTO = new ContatoDTORequest(numeros, "Centro");
        ConsultaDTORequest consultaDTO = new ConsultaDTORequest(
                "Consulta Clínica",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now(),
                StatusDTORequest.MARCADO
        );
        PacienteDTORequest pacienteDTO = new PacienteDTORequest("João Silva", contatoDTO, consultaDTO);

        // Act
        Paciente paciente = mapper.dtoToModel(pacienteDTO);

        // Assert
        assertNotNull(paciente);
        assertEquals("João Silva", paciente.getNome());
        assertNotNull(paciente.getContato());
        assertNotNull(paciente.getConsulta());
        assertTrue(paciente.getContato().existeNumeroParaContato("5581987654321"));
    }

    /**
     * Teste: Converter Model para DTO Response
     * Esperado: DTO Response criado corretamente
     */
    @Test
    void deveConverterModelParaDTOResponseComSucesso() {
        // Arrange
        LinkedList<Numero> numeros = new LinkedList<>();
        numeros.add(new Numero("5581987654321"));

        Paciente paciente = Paciente.builder()
                .nome("João Silva")
                .contato(new Contato(numeros, "Centro"))
                .consulta(new Consulta("Consulta Clínica", LocalDateTime.now().plusDays(5)))
                .build();

        // Act
        PacienteDTOResponse response = mapper.modelToDTO(paciente);

        // Assert
        assertNotNull(response);
        assertEquals("João Silva", response.nome());
        assertNotNull(response.contato());
        assertNotNull(response.consulta());
        assertEquals("Centro", response.contato().bairro());
    }

    /**
     * Teste: Mapeamento bidirecional (DTO -> Model -> DTO)
     * Esperado: Manter integridade dos dados
     */
    @Test
    void deveManterIntegridadeAoBidirecional() {
        // Arrange
        LinkedList<NumeroDTORequest> numeros = new LinkedList<>();
        numeros.add(new NumeroDTORequest("5581987654321", true));
        numeros.add(new NumeroDTORequest("5581998765432", false));

        ContatoDTORequest contatoDTO = new ContatoDTORequest(numeros, "Centro");
        ConsultaDTORequest consultaDTO = new ConsultaDTORequest(
                "Consulta Clínica",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now(),
                StatusDTORequest.MARCADO
        );
        PacienteDTORequest pacienteDTOOriginal = new PacienteDTORequest(
                "João Silva",
                contatoDTO,
                consultaDTO
        );

        // Act
        Paciente paciente = mapper.dtoToModel(pacienteDTOOriginal);
        PacienteDTOResponse pacienteDTOResponse = mapper.modelToDTO(paciente);

        // Assert
        assertEquals(pacienteDTOOriginal.nome(), pacienteDTOResponse.nome());
        assertEquals(pacienteDTOOriginal.contato().bairro(), pacienteDTOResponse.contato().bairro());
        assertEquals(pacienteDTOOriginal.consulta().nome(), pacienteDTOResponse.consulta().nome());
    }

    /**
     * Teste: Múltiplos números de telefone são mapeados corretamente
     * Esperado: Todos os números preservados
     */
    @Test
    void deveMapearMultiplosNumerosDeTelefonePorretamente() {
        // Arrange
        LinkedList<NumeroDTORequest> numeros = new LinkedList<>();
        numeros.add(new NumeroDTORequest("5581987654321", true));
        numeros.add(new NumeroDTORequest("5581912345678", false));
        numeros.add(new NumeroDTORequest("5581998765432", true));

        ContatoDTORequest contatoDTO = new ContatoDTORequest(numeros, "Centro");
        ConsultaDTORequest consultaDTO = new ConsultaDTORequest(
                "Consulta Clínica",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now(),
                StatusDTORequest.MARCADO
        );
        PacienteDTORequest pacienteDTO = new PacienteDTORequest("João Silva", contatoDTO, consultaDTO);

        // Act
        Paciente paciente = mapper.dtoToModel(pacienteDTO);
        PacienteDTOResponse response = mapper.modelToDTO(paciente);

        // Assert
        assertEquals(3, response.contato().numerosCelular().size());
        assertTrue(response.contato().numerosCelular().stream()
                .anyMatch(n -> n.celular().equals("5581987654321")));
        assertTrue(response.contato().numerosCelular().stream()
                .anyMatch(n -> n.celular().equals("5581912345678")));
        assertTrue(response.contato().numerosCelular().stream()
                .anyMatch(n -> n.celular().equals("5581998765432")));
    }

    /**
     * Teste: Status da consulta é preservado
     * Esperado: Todos os status sejam mapeados corretamente
     */
    @Test
    void deveMapearStatusDaConsultaCorretamente() {
        for (StatusDTORequest status : StatusDTORequest.values()) {
            // Arrange
            ConsultaDTORequest consultaDTO = new ConsultaDTORequest(
                    "Consulta " + status.name(),
                    LocalDateTime.now().plusDays(5),
                    LocalDateTime.now(),
                    status
            );
            PacienteDTORequest pacienteDTO = new PacienteDTORequest(
                    "João Silva",
                    new ContatoDTORequest(
                            new LinkedList<>(java.util.List.of(new NumeroDTORequest("5581987654321", true))),
                            "Centro"
                    ),
                    consultaDTO
            );

            // Act
            Paciente paciente = mapper.dtoToModel(pacienteDTO);
            PacienteDTOResponse response = mapper.modelToDTO(paciente);

            // Assert
            assertNotNull(response.consulta().status());
        }
    }

    /**
     * Teste: Bairro é preservado no mapeamento
     * Esperado: Bairro correto em Request e Response
     */
    @Test
    void deveMapeiarBairroCorretamente() {
        // Arrange
        String[] bairros = {"Centro", "Periférico", "Zona Leste", "Zona Oeste", "Zona Norte", "Zona Sul"};

        for (String bairro : bairros) {
            ContatoDTORequest contatoDTO = new ContatoDTORequest(
                    new LinkedList<>(java.util.List.of(new NumeroDTORequest("5581987654321", true))),
                    bairro
            );
            ConsultaDTORequest consultaDTO = new ConsultaDTORequest(
                    "Consulta",
                    LocalDateTime.now().plusDays(5),
                    LocalDateTime.now(),
                    StatusDTORequest.MARCADO
            );
            PacienteDTORequest pacienteDTO = new PacienteDTORequest("João Silva", contatoDTO, consultaDTO);

            // Act
            Paciente paciente = mapper.dtoToModel(pacienteDTO);
            PacienteDTOResponse response = mapper.modelToDTO(paciente);

            // Assert
            assertEquals(bairro, response.contato().bairro());
        }
    }

    /**
     * Teste: Dados de consulta são preservados
     * Esperado: Nome, datas e status corretos
     */
    @Test
    void devePreservarDadosDaConsulta() {
        // Arrange
        LocalDateTime dataAtendimento = LocalDateTime.now().plusDays(10);
        LocalDateTime dataMarcacao = LocalDateTime.now();

        ConsultaDTORequest consultaDTO = new ConsultaDTORequest(
                "Cirurgia Geral",
                dataAtendimento,
                dataMarcacao,
                StatusDTORequest.AGUARDANDO
        );
        PacienteDTORequest pacienteDTO = new PacienteDTORequest(
                "João Silva",
                new ContatoDTORequest(
                        new LinkedList<>(java.util.List.of(new NumeroDTORequest("5581987654321", true))),
                        "Centro"
                ),
                consultaDTO
        );

        // Act
        Paciente paciente = mapper.dtoToModel(pacienteDTO);
        PacienteDTOResponse response = mapper.modelToDTO(paciente);

        // Assert
        assertEquals("Cirurgia Geral", response.consulta().nome());
        assertEquals(dataAtendimento, response.consulta().dataAtendimento());
    }

    /**
     * Teste: Null safety no mapeamento
     * Esperado: Lidar com nulls sem exceção
     */
    @Test
    void deveHandleNullComSeguranca() {
        // Arrange
        ContatoDTORequest contatoComNumerosVazios = new ContatoDTORequest(
                new LinkedList<>(),
                "Centro"
        );
        ConsultaDTORequest consultaDTO = new ConsultaDTORequest(
                "Consulta",
                LocalDateTime.now().plusDays(5),
                LocalDateTime.now(),
                StatusDTORequest.MARCADO
        );
        PacienteDTORequest pacienteDTO = new PacienteDTORequest("João Silva", contatoComNumerosVazios, consultaDTO);

        // Act
        Paciente paciente = mapper.dtoToModel(pacienteDTO);
        PacienteDTOResponse response = mapper.modelToDTO(paciente);

        // Assert
        assertNotNull(response);
        assertNotNull(response.contato().numerosCelular());
        assertEquals(0, response.contato().numerosCelular().size());
    }
}


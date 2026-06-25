package integration.usecase;

import com.github.dio.mensageria.Start;
import com.github.dio.mensageria.application.usecases.CriarPaciente;
import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.domain.paciente.contato.Contato;
import com.github.dio.mensageria.domain.paciente.contato.Numero;
import com.github.dio.mensageria.domain.paciente.Paciente;
import config.TestcontainersConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Testes de Integração para o Use Case CriarPaciente.
 * Testa a lógica de negócio com banco de dados real (TestContainers).
 *
 * @author diogenesssantos
 */
@SpringBootTest(classes = Start.class)
@ContextConfiguration(initializers = TestcontainersConfig.Initializer.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CriarPacienteUseCaseTest {

    @Autowired
    private CriarPaciente criarPaciente;

    /**
     * Teste: Criar paciente com sucesso
     * Esperado: Paciente salvo no banco com código gerado
     */
    @Test
    void deveCriarPacienteComSucesso() {
        // Arrange
        Paciente paciente = Paciente.builder()
                .nome("João Silva")
                .contato(new Contato(
                        List.of(new Numero("5581987654321")),
                        "Centro"
                ))
                .consulta(new Consulta("Consulta Clínica", LocalDateTime.now().plusDays(5)))
                .build();

        // Act
        Paciente pacienteCriado = criarPaciente.cadastrarPaciente(paciente);

        // Assert
        assertNotNull(pacienteCriado);
        assertNotNull(pacienteCriado.getCodigo());
        assertEquals("João Silva", pacienteCriado.getNome());
        assertEquals("Centro", pacienteCriado.getContato().getBairro());
    }

    /**
     * Teste: Código UUID é gerado automaticamente
     * Esperado: Cada paciente tem um código único
     */
    @Test
    void deveGerarCodigoUnicoPorPaciente() {
        // Arrange
        Paciente paciente1 = Paciente.builder()
                .nome("Paciente Um")
                .contato(new Contato(
                        List.of(new Numero("5581987654321")),
                        "Centro"
                ))
                .consulta(new Consulta("Consulta 1", LocalDateTime.now().plusDays(5)))
                .build();

        Paciente paciente2 = Paciente.builder()
                .nome("Paciente Dois")
                .contato(new Contato(
                        List.of(new Numero("5581987654321")),
                        "Centro"
                ))
                .consulta(new Consulta("Consulta 2", LocalDateTime.now().plusDays(5)))
                .build();

        // Act
        Paciente paciente1Criado = criarPaciente.cadastrarPaciente(paciente1);
        Paciente paciente2Criado = criarPaciente.cadastrarPaciente(paciente2);

        // Assert
        assertNotEquals(paciente1Criado.getCodigo(), paciente2Criado.getCodigo());
        assertNotNull(paciente1Criado.getCodigo());
        assertNotNull(paciente2Criado.getCodigo());
    }

    /**
     * Teste: Múltiplos pacientes podem ser criados
     * Esperado: Todos são salvos com sucesso
     */
    @Test
    void devePermitirCriarMultiplosPacientes() {
        // Act & Assert
        for (int i = 0; i < 5; i++) {
            Paciente paciente = Paciente.builder()
                    .nome("Paciente " + i)
                    .contato(new Contato(
                            List.of(new Numero("5581" + String.format("%08d", 987654320 + i))),
                            "Bairro " + i
                    ))
                    .consulta(new Consulta("Consulta " + i, LocalDateTime.now().plusDays(i + 1)))
                    .build();

            Paciente pacienteCriado = criarPaciente.cadastrarPaciente(paciente);
            assertNotNull(pacienteCriado);
            assertEquals("Paciente " + i, pacienteCriado.getNome());
        }
    }

    /**
     * Teste: Dados de contato são preservados
     * Esperado: Número e bairro corretos após persistência
     */
    @Test
    void devePreservarDadosDeContato() {
        // Arrange
        LinkedList<Numero> numeros = new LinkedList<>();
        numeros.add(new Numero("5581987654321"));
        numeros.add(new Numero("5581998765432"));

        Paciente paciente = Paciente.builder()
                .nome("João Silva")
                .contato(new Contato(numeros, "Vila Mariana"))
                .consulta(new Consulta("Consulta", LocalDateTime.now().plusDays(5)))
                .build();

        // Act
        Paciente pacienteCriado = criarPaciente.cadastrarPaciente(paciente);

        // Assert
        assertNotNull(pacienteCriado.getContato());
        assertEquals("Vila Mariana", pacienteCriado.getContato().getBairro());
        assertTrue(pacienteCriado.getContato().existeNumeroParaContato("5581987654321"));
        assertTrue(pacienteCriado.getContato().existeNumeroParaContato("5581998765432"));
    }

    /**
     * Teste: Dados de consulta são preservados
     * Esperado: Nome, status e datas corretos após persistência
     */
    @Test
    void devePreservarDadosDaConsulta() {
        // Arrange
        LocalDateTime dataAtendimento = LocalDateTime.now().plusDays(10);
        Paciente paciente = Paciente.builder()
                .nome("João Silva")
                .contato(new Contato(
                        List.of(new Numero("5581987654321")),
                        "Centro"
                ))
                .consulta(new Consulta("Cirurgia", dataAtendimento))
                .build();

        // Act
        Paciente pacienteCriado = criarPaciente.cadastrarPaciente(paciente);

        // Assert
        assertNotNull(pacienteCriado.getConsulta());
        assertEquals("Cirurgia", pacienteCriado.getConsulta().getNome());
        assertEquals(dataAtendimento, pacienteCriado.getConsulta().getDataAtendimento());
        assertEquals(Consulta.Status.MARCADO, pacienteCriado.getConsulta().getStatus());
    }

    /**
     * Teste: Data de marcação é registrada
     * Esperado: Data de marcação não é nula e próxima à data atual
     */
    @Test
    void deveRegistrarDataDeMarcacao() {
        // Arrange
        LocalDateTime antes = LocalDateTime.now();
        Paciente paciente = Paciente.builder()
                .nome("João Silva")
                .contato(new Contato(
                        List.of(new Numero("5581987654321")),
                        "Centro"
                ))
                .consulta(new Consulta("Consulta", LocalDateTime.now().plusDays(5)))
                .build();

        // Act
        Paciente pacienteCriado = criarPaciente.cadastrarPaciente(paciente);
        LocalDateTime depois = LocalDateTime.now();

        // Assert
        assertNotNull(pacienteCriado.getConsulta().getDataMarcacao());
        assertTrue(pacienteCriado.getConsulta().getDataMarcacao().isAfter(antes.minusSeconds(1)));
        assertTrue(pacienteCriado.getConsulta().getDataMarcacao().isBefore(depois.plusSeconds(1)));
    }

    /**
     * Teste: Validação de nome não vazio
     * Esperado: Não cria paciente com nome vazio
     */
    @Test
    void nãoDeveCriarPacienteComNomeVazio() {
        // Arrange - Tentar criar com nome vazio
        assertThrows(Exception.class, () -> {
            Paciente paciente = Paciente.builder()
                    .nome("")
                    .contato(new Contato(
                            List.of(new Numero("5581987654321")),
                            "Centro"
                    ))
                    .consulta(new Consulta("Consulta", LocalDateTime.now().plusDays(5)))
                    .build();
        });
    }

    /**
     * Teste: Validação de contato não nulo
     * Esperado: Não cria paciente sem contato
     */
    @Test
    void nãoDeveCriarPacienteSemContato() {
        // Arrange - Tentar criar sem contato
        assertThrows(Exception.class, () -> {
            Paciente paciente = Paciente.builder()
                    .nome("João Silva")
                    .contato(null)
                    .consulta(new Consulta("Consulta", LocalDateTime.now().plusDays(5)))
                    .build();
        });
    }

    /**
     * Teste: Validação de consulta não nula
     * Esperado: Não cria paciente sem consulta
     */
    @Test
    void nãoDeveCriarPacienteSemConsulta() {
        // Arrange - Tentar criar sem consulta
        assertThrows(Exception.class, () -> {
            Paciente paciente = Paciente.builder()
                    .nome("João Silva")
                    .contato(new Contato(
                            List.of(new Numero("5581987654321")),
                            "Centro"
                    ))
                    .consulta(null)
                    .build();
        });
    }

    /**
     * Teste: Dados complexos
     * Esperado: Suportar estrutura completa sem problemas
     */
    @Test
    void deveHandleEstruruturaComplexa() {
        // Arrange
        LinkedList<Numero> numerosList = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            numerosList.add(new Numero("5581" + String.format("%08d", 987654320 + i)));
        }

        Paciente paciente = Paciente.builder()
                .nome("Paciente Complexo")
                .contato(new Contato(numerosList, "Bairro Centro-Sul"))
                .consulta(new Consulta("Cirurgia Cardiológica", LocalDateTime.now().plusDays(30)))
                .build();

        // Act
        Paciente pacienteCriado = criarPaciente.cadastrarPaciente(paciente);

        // Assert
        assertNotNull(pacienteCriado);
        assertEquals(5, pacienteCriado.getContato().getNumerosCelular().size());
        assertEquals("Paciente Complexo", pacienteCriado.getNome());
        assertEquals("Bairro Centro-Sul", pacienteCriado.getContato().getBairro());
    }

    /**
     * Teste: Isolamento de dados entre pacientes
     * Esperado: Pacientes não interferem uns nos outros
     */
    @Test
    void deveManterIsolamentoDados() {
        // Arrange
        Paciente paciente1 = Paciente.builder()
                .nome("Paciente Maria")
                .contato(new Contato(
                        List.of(new Numero("5581987654321")),
                        "Zona Leste"
                ))
                .consulta(new Consulta("Consulta 1", LocalDateTime.now().plusDays(5)))
                .build();

        Paciente paciente2 = Paciente.builder()
                .nome("Paciente João")
                .contato(new Contato(
                        List.of(new Numero("5581912345678")),
                        "Zona Oeste"
                ))
                .consulta(new Consulta("Consulta 2", LocalDateTime.now().plusDays(10)))
                .build();

        // Act
        Paciente paciente1Criado = criarPaciente.cadastrarPaciente(paciente1);
        Paciente paciente2Criado = criarPaciente.cadastrarPaciente(paciente2);

        // Assert - Verificar isolamento
        assertNotEquals(paciente1Criado.getNome(), paciente2Criado.getNome());
        assertNotEquals(paciente1Criado.getContato().getBairro(), paciente2Criado.getContato().getBairro());
        assertNotEquals(
                paciente1Criado.getConsulta().getDataAtendimento(),
                paciente2Criado.getConsulta().getDataAtendimento()
        );
    }
}


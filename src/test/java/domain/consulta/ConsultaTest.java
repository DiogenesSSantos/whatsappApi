package domain.consulta;

import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.domain.paciente.consulta.DataPassadoException;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class ConsultaTest {

    @Test
    void deveCriarConsultaQuandoTodosOsCamposEstiveremValido() {
        Consulta consulta = new Consulta("Ortopedia", LocalDateTime.now().plusDays(2));

        assertNotNull(consulta);
        assertEquals(Consulta.Status.MARCADO, consulta.getStatus());
        assertEquals(LocalDateTime.now().getDayOfMonth(), consulta.getDataMarcacao().getDayOfMonth());
    }

    @Test
    void deveLancarExcecaoQuandoDataAtendimentoForNoPassado() {
        assertThrows(DataPassadoException.class, () ->
                new Consulta("Ortopedia", LocalDateTime.now().minusDays(1)));
    }

    @Test
    void deveCriarConsultaViaDadosPersistidosSemValidarData() {
        LocalDateTime dataPassado = LocalDateTime.now().minusDays(30);
        Consulta consulta = Consulta.deDadosPersistidos(
                "Consulta Antiga", dataPassado, dataPassado.minusDays(5), Consulta.Status.AGUARDANDO);

        assertNotNull(consulta);
        assertEquals("Consulta Antiga", consulta.getNome());
        assertEquals(dataPassado, consulta.getDataAtendimento());
        assertEquals(Consulta.Status.AGUARDANDO, consulta.getStatus());
    }

    @Test
    void deveAtualizarNomeDaConsulta() {
        Consulta consulta = new Consulta("Ortopedia", LocalDateTime.now().plusDays(2));
        consulta.atualizarNome("Oftalmologia");
        assertEquals("Oftalmologia", consulta.getNome());
    }

    @Test
    void deveLancarExcecaoQuandoNomeForVazio() {
        Consulta consulta = new Consulta("Ortopedia", LocalDateTime.now().plusDays(2));
        assertThrows(IllegalArgumentException.class, () -> consulta.atualizarNome(""));
    }

    @Test
    void deveAtualizarDataAtendimento() {
        Consulta consulta = new Consulta("Ortopedia", LocalDateTime.now().plusDays(2));
        LocalDateTime novaData = LocalDateTime.now().plusDays(10);
        consulta.atualizarDataAtendimento(novaData);
        assertEquals(novaData, consulta.getDataAtendimento());
    }

    @Test
    void deveLancarExcecaoQuandoAtualizarDataAtendimentoParaPassado() {
        Consulta consulta = new Consulta("Ortopedia", LocalDateTime.now().plusDays(2));
        assertThrows(DataPassadoException.class, () ->
                consulta.atualizarDataAtendimento(LocalDateTime.now().minusDays(1)));
    }

    @Test
    void deveAtualizarStatus() {
        Consulta consulta = new Consulta("Ortopedia", LocalDateTime.now().plusDays(2));
        consulta.atualizarStatus(Consulta.Status.REJEITADO);
        assertEquals(Consulta.Status.REJEITADO, consulta.getStatus());
    }

    @Test
    void deveAtualizarDataMarcacao() {
        Consulta consulta = new Consulta("Ortopedia", LocalDateTime.now().plusDays(10));
        LocalDateTime novaDataMarcacao = LocalDateTime.now().plusDays(1);
        consulta.atualizarDataMarcacao(novaDataMarcacao);
        assertEquals(novaDataMarcacao, consulta.getDataMarcacao());
    }

    @Test
    void deveLancarExcecaoQuandoDataMarcacaoForDepoisDaDataAtendimento() {
        Consulta consulta = new Consulta("Ortopedia", LocalDateTime.now().plusDays(5));
        LocalDateTime dataInvalida = LocalDateTime.now().plusDays(10);
        assertThrows(IllegalArgumentException.class, () -> consulta.atualizarDataMarcacao(dataInvalida));
    }
}

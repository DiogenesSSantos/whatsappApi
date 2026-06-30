package infraestrutura;

import com.github.dio.mensageria.Start;
import com.github.dio.mensageria.domain.mensagem.ResultadoEnvio;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.domain.paciente.contato.Contato;
import com.github.dio.mensageria.domain.paciente.contato.Numero;
import com.github.dio.mensageria.application.gateways.output.Mensageria;
import config.TestcontainersConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.DisabledIfEnvironmentVariable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;

@SpringBootTest(classes = Start.class)
@ContextConfiguration(initializers = TestcontainersConfig.Initializer.class)
@ActiveProfiles("local")
@DisabledIfEnvironmentVariable(named = "CI", matches = "true")
class MensageriaImplGatewayIntegrationTest {

    @Autowired
    private Mensageria mensageria;

    @Test
    void deveEnviarMensagemParaNumero() throws Exception {
        List<String> consultas = List.of(
                "Cardiologia",
                "Ressonância",
                "Ortopedia",
                "Oftalmologia",
                "Tomografia",
                "Pediatria",
                "Ginecologia",
                "Urologia",
                "Dermatologia",
                "Endocrinologia",
                "Neurologia",
                "Psiquiatria",
                "Otorrinolaringologia",
                "Gastroenterologia",
                "Pneumologia",
                "Nefrologia",
                "Reumatologia",
                "Hematologia",
                "Oncologia",
                "Infectologia",
                "Cirurgia Geral",
                "Cirurgia Vascular",
                "Cirurgia Plástica",
                "Angiologia",
                "Mastologia",
                "Nutrição",
                "Fisioterapia",
                "Fonoaudiologia",
                "Psicologia",
                "Odontologia",
                "Proctologia",
                "Coloproctologia",
                "Alergologia",
                "Imunologia",
                "Medicina do Trabalho",
                "Medicina Física e Reabilitação",
                "Medicina Interna",
                "Medicina Preventiva",
                "Medicina Esportiva",
                "Medicina Familiar",
                "Geriatria",
                "Obstetrícia",
                "Neonatologia",
                "Endoscopia",
                "Eletrocardiograma",
                "Ecocardiograma",
                "Holter",
                "MAPA",
                "Doppler",
                "Ultrassonografia",
                "Mamografia",
                "Densitometria Óssea",
                "Exame de Sangue",
                "Exame de Urina",
                "Teste de Esforço",
                "Broncoscopia",
                "Colonoscopia",
                "Cistoscopia",
                "Artroscopia",
                "Biópsia",
                "Vacinação",
                "Consulta Pré-natal",
                "Consulta Pós-operatória",
                "Avaliação Pré-anestésica",
                "Consulta de Retorno",
                "Consulta de Triagem",
                "Consulta de Emergência",
                "Consulta Agendada",
                "Consulta de Seguimento",
                "Consulta de Avaliação",
                "Consulta de Orientação",
                "Consulta de Aconselhamento",
                "Consulta de Controle",
                "Consulta de Revisão",
                "Consulta de Alta",
                "Consulta de Encaminhamento",
                "Consulta de Especialidade",
                "Consulta Multidisciplinar",
                "Consulta Domiciliar",
                "Teleconsulta",
                "Consulta Online",
                "Consulta de Urgência",
                "Consulta de Rotina",
                "Consulta de Avaliação Nutricional",
                "Consulta de Psicoterapia",
                "Consulta de Terapia Ocupacional",
                "Consulta de Reabilitação",
                "Consulta de Dor",
                "Consulta de Imunização",
                "Consulta de Acompanhamento",
                "Consulta de Triagem Neonatal",
                "Consulta de Saúde Mental",
                "Consulta de Saúde Bucal",
                "Consulta de Saúde do Trabalhador",
                "Consulta de Saúde da Mulher",
                "Consulta de Saúde do Homem",
                "Consulta de Saúde do Idoso",
                "Consulta de Saúde Infantil",
                "Terapia Respiratória",
                "Atendimento de Enfermagem"
        );

        List<String> nomes = List.of(
                "Diogenes da Silva Santos",
                "Ana Maria Oliveira",
                "João Pedro Souza",
                "Maria Clara Ferreira",
                "José Antônio Lima",
                "Beatriz Rodrigues",
                "Carlos Eduardo Gomes",
                "Fernanda Alves",
                "Marcos Vinícius Pereira",
                "Juliana Costa",
                "Rafael Barbosa",
                "Camila Martins",
                "Lucas Henrique Ribeiro",
                "Patrícia Nascimento",
                "André Luiz Carvalho",
                "Aline Santos",
                "Bruno Henrique Dias",
                "Larissa Rocha",
                "Felipe Moreira",
                "Daniela Mendes",
                "Rodrigo Teixeira",
                "Mariana Araújo",
                "Gustavo Pinto",
                "Renata Figueiredo",
                "Tiago Monteiro",
                "Vanessa Cardoso",
                "Leandro Castro",
                "Priscila Lopes",
                "Eduardo Freitas",
                "Simone Cunha",
                "Marcelo Ramos",
                "Paula Santana",
                "Victor Hugo Almeida",
                "Natália Correia",
                "Henrique Duarte",
                "Sílvia Macedo",
                "Fábio Miranda",
                "Lúcia Braga",
                "Alexandre Coelho",
                "Tânia Pires",
                "Wellington Azevedo",
                "Carolina Guimarães",
                "Murilo Batista",
                "Aline Ribeiro",
                "Igor Teixeira",
                "Sofia Lopes",
                "Samuel Nogueira",
                "Elisa Campos",
                "Daniel Carvalho",
                "Mônica Vasconcelos",
                "Otávio Leite",
                "Gabriela Farias",
                "Nelson Barros",
                "Bianca Moura",
                "César Santana",
                "Liana Rocha",
                "Murilo Henrique",
                "Eloá Ribeiro",
                "Jonas Ferreira",
                "Rafaela Pinto",
                "Caio Oliveira",
                "Ingrid Almeida",
                "Leila Gomes",
                "Yuri Silva",
                "Tereza Campos",
                "Matheus Lopes",
                "Isabela Martins",
                "Nelson Souza",
                "Clarice Duarte",
                "Breno Costa",
                "Heloísa Nascimento",
                "Igor Mendes",
                "Luan Ribeiro",
                "Jéssica Freitas",
                "Vitor Ramos",
                "Lorena Cardoso",
                "Samuel Azevedo",
                "Yasmin Guimarães",
                "Davi Batista",
                "Helena Macedo",
                "Enzo Miranda",
                "Rafaela Braga",
                "Pietro Coelho",
                "Fabiana Pires",
                "Guilherme Leal",
                "Cecília Furtado",
                "Augusto Ribeiro",
                "Mirella Rocha",
                "Nicolas Teixeira",
                "Sabrina Oliveira",
                "Ítalo Campos",
                "Lorena Silva",
                "Otília Santos",
                "Brenna Alves",
                "Murilo Fonseca",
                "Débora Cardoso",
                "Heitor Monteiro",
                "Paloma Viana",
                "Joaquim Prado",
                "Estela Duarte"
        );


        LinkedList<Numero> numeros = new LinkedList<>();
        numeros.add(new Numero("558184768748"));
        numeros.add(new Numero("5581984768748"));
        Random random = new Random();

        Paciente paciente = Paciente.builder()
                .nome(nomes.get(random.nextInt(1,100)))
                .contato(new Contato(numeros, "Centro"))
                .consulta(new Consulta(consultas.get(random.nextInt(1,100)) ,
                        LocalDateTime.now().plusDays(2)))
                .build();

        ResultadoEnvio resultado = mensageria.enviar(paciente);

        assertInstanceOf(ResultadoEnvio.Sucesso.class, resultado);
    }
}

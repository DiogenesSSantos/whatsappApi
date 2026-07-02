package com.github.dio.mensageria.infra.gateways;

import com.github.dio.mensageria.application.gateways.output.OllamaGateway;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.json.JSONObject;

/**
 * Implementação concreta do OllamaGateway usando Java HttpClient.
 */
public final class OllamaHttpGateway implements OllamaGateway {

    private final String ollamaApiUrl;
    private final String modelName;
    private static final HttpClient HTTP_CLIENT = HttpClient.newBuilder()
            .connectTimeout(java.time.Duration.ofSeconds(30))
            .build();

    public OllamaHttpGateway(String ollamaUrl, String modelName) {
        this.ollamaApiUrl = ollamaUrl.endsWith("/")
                ? ollamaUrl + "api/generate"
                : ollamaUrl + "/api/generate";
        this.modelName = modelName;
    }

    @Override
    public String gerarVariacaoMensagem(String nomePaciente, String nomeConsulta, LocalDateTime dataAtendimento)
            throws Exception {

        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String dataAtendStr = dataAtendimento.format(fmt);

        LocalDate deadline = dataAtendimento.toLocalDate();
        int uteis = 0;
        while (uteis < 3) {
            deadline = deadline.minusDays(1);
            if (deadline.getDayOfWeek() != DayOfWeek.SATURDAY && deadline.getDayOfWeek() != DayOfWeek.SUNDAY) {
                uteis++;
            }
        }
        String deadlineStr = deadline.format(fmt);

        String systemPrompt = String.format("""
            Voce e um funcionario de saude. Escreva UMA mensagem de WhatsApp.

            DADOS (jamais altere):
            - Paciente: %s
            - Exame/Consulta: %s
            - Data do atendimento: %s
            - Prazo para retirar encaminhamento: ate %s (3 dias uteis antes)
            - Local: Secretaria de Saude do municipio de Vitoria de Santo Antao, setor de regulacao
            - Horario: 08:00 as 14:00

            VARIACAO RADICAL (OBRIGATORIO):
            Cada mensagem deve ser UNICA. Para isso:
            1. NUNCA comece com "Ola [nome], sua consulta..." - isso e proibido.
            2. NUNCA comece com "Atencao:" - ja e muito usado, evite.
            3. SEMPRE inclua o nome do paciente na mensagem.
            4. Aberturas permitidas (varie entre elas):
               - "Bom dia/Boa tarde [nome]"
               - "Prezado(a) [nome]"
               - "Informamos que [nome]..."
               - "[nome], informamos que..."
               - Comece DIRETO: "Sua consulta de [exame]..."
               - Comece com a DATA: "No dia [data], acontecera..."
               - Comece com o LOCAL: "Na Secretaria de Saude..."
            5. Varie a ORDEM das informacoes:
               - As vezes primeiro o local, depois a data, depois o prazo
               - As vezes primeiro o prazo, depois o local, depois a data
               - As vezes primeiro a data, depois o prazo, depois o local
            6. Varie os CONECTIVOS:
               - "para retirar" vs "onde podera retirar" vs "a fim de buscar"
               - "Caso contrario" vs "Nao havendo" vs "Se nao retirar"
            7. Tom formal e profissional. NUNCA emoji, markdown, perguntas ou despedidas.
            8. OBRIGATORIO: nome do paciente, data, prazo, local+horario, aviso de liberacao.

            Retorne SOMENTE a mensagem, sem aspas, sem explicacao.
            """, nomePaciente, nomeConsulta, dataAtendStr, deadlineStr);

        String exemploBase = String.format(
                "Ola %s, sua consulta/exame (%s) esta agendada para o dia %s. "
                + "Compareca a Secretaria de Saude do municipio de Vitoria de Santo Antao, "
                + "setor de regulacao, para retirar o comprovante e o encaminhamento das 08:00 as 14:00. "
                + "IMPORTANTE: o prazo para retirada e ate %s (3 dias uteis). "
                + "Caso contrario, a vaga sera disponibilizada para outro paciente.",
                nomePaciente, nomeConsulta, dataAtendStr, deadlineStr);

        String userPrompt = String.format("""
            REESCREVA esta mensagem de forma radicalmente diferente:
            "%s"

            DADOS:
            - Data: %s | Prazo: ate %s (3 dias uteis antes)
            - Local: Secretaria de Saude de Vitoria de Santo Antao, setor de regulacao
            - Horario: 08:00 as 14:00

            MODELOS (NUNCA copie a estrutura, invente outra):
            "Bom dia %%s. Informamos que %%s, portador(a) de %%s, devera comparecer
            %%s a Secretaria de Saude de Vitoria de Santo Antao, setor de regulacao,
            no horario das 08h as 14h. Retire comprovante e encaminhamento ate %%s.
            Caso contrario, a vaga sera disponibilizada."

            "%%s, sua consulta de %%s acontecera em %%s. Compareca ao setor de
            regulacao da Secretaria de Saude de Vitoria de Santo Antao das 08:00
            as 14:00 para retirar comprovante e encaminhamento. Prazo: ate %%s.
            Se nao retirar, a vaga sera cedida a outro paciente."

            "Atencao: o prazo para retirar o encaminhamento de %%s e ate %%s.
            A consulta acontecera em %%s. Compareca a Secretaria de Saude de
            Vitoria de Santo Antao, setor de regulacao, das 08:00 as 14:00.
            Nao havendo retirada, a vaga sera liberada para outro(a) paciente."

            "%%s, sua consulta de %%s esta confirmada para %%s. Retire comprovante
            e encaminhamento na Secretaria de Saude (Vitoria de Santo Antao, setor
            de regulacao), das 08:00 as 14:00. IMPORTANTE: o prazo para retirada
            e ate %%s. Caso nao retire, a vaga sera liberada."

            REGRAS:
            - NAO copie a estrutura dos modelos acima.
            - NAO use emoji, markdown ou cabecalhos.
            - Varie ordem, palavras e conectivos.
            - Retorne SOMENTE a mensagem, sem aspas.
            """,
            exemploBase,
            nomePaciente, nomeConsulta, dataAtendStr, deadlineStr,
            nomePaciente, nomeConsulta, dataAtendStr, deadlineStr,
            nomePaciente, nomeConsulta, dataAtendStr, deadlineStr,
            nomePaciente, nomeConsulta, dataAtendStr, deadlineStr
        );

        JSONObject payload = new JSONObject()
                .put("model", modelName)
                .put("system", systemPrompt)
                .put("prompt", userPrompt)
                .put("stream", false)
                .put("keep_alive", -1)
                .put("options", Map.of(
                        "temperature", 0.95,
                        "top_p", 0.95,
                        "repeat_penalty", 1.0,
                        "max_tokens", 200
                ));

        int maxTentativas = 3;
        String resposta = null;

        for (int tentativa = 1; tentativa <= maxTentativas; tentativa++) {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(ollamaApiUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                    .timeout(java.time.Duration.ofSeconds(180))
                    .build();

            HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                if (tentativa == maxTentativas) {
                    throw new IOException("Ollama API retornou status: " + response.statusCode()
                            + ". Resposta: " + response.body());
                }
                continue;
            }

            JSONObject jsonResponse = new JSONObject(response.body());
            if (!jsonResponse.has("response")) {
                if (tentativa == maxTentativas) {
                    throw new IOException("Campo 'response' ausente. Resposta: " + response.body());
                }
                continue;
            }

            resposta = jsonResponse.getString("response").trim();

            // Limpeza
            resposta = resposta
                    .replaceAll("\\*\\*([^*]+)\\*\\*", "$1")
                    .replaceAll("\\*([^*]+)\\*", "$1")
                    .replaceAll("^\\s*Exemplo\\s*\\d+[):.]\\s*", "")
                    .replaceAll("^\\s*\\d+[):.]\\s*", "")
                    .replaceAll("\\[exame\\]", nomeConsulta)
                    .replaceAll("\\[nome\\]", nomePaciente)
                    .replaceAll("\\[data\\]", dataAtendStr)
                    .replaceAll("\\[prazo\\]", deadlineStr)
                    .replaceAll("\\n{2,}", "\n")
                    .trim();

            if (respostaValida(resposta)) {
                break;
            }
        }

        return resposta;
    }

    private boolean respostaValida(String resposta) {
        if (resposta == null || resposta.isBlank()) return false;
        if (resposta.contains("DADOS:")) return false;
        if (resposta.contains("Prazo:")) return false;
        if (resposta.contains("Local:")) return false;
        if (resposta.contains("Horário:")) return false;
        if (resposta.contains("Horario:")) return false;
        if (resposta.contains("Exemplo")) return false;
        if (resposta.startsWith("-")) return false;
        return true;
    }

}

package com.github.dio.mensageria.infra.gateways;

import com.github.dio.mensageria.application.gateways.output.OllamaGateway;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.Map;

import org.json.JSONObject;

/**
 * Implementação concreta do OllamaGateway usando Java HttpClient.
 * Responsável apenas por: serialização JSON, chamada HTTP, tratamento de códigos de status.
 * NÃO contém regras de negócio sobre mensagens de saúde.
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
        String systemPrompt = """
            Você é um assistente especializado em gerar variações de notificação de saúde
            para o município de Vitória de Santo Antão.

            REGRAS RIGOROSAS (NUNCA VIOLAR):
            1. NUNCA altere:
               • o nome do paciente [%s]
               • o tipo exato de consulta/exame [%s]
               • a localização fixa: "Secretaria de Saúde do município de Vitória de Santo Antão, setor de regulação"
               • o horário de atendimento: "das 08:00 às 14:00" (ou equivalente, ex: "entre 08:00 e 14:00")
            2. NUNCA adicione: perguntas, ofertas de agendamento, múltiplas saudações,
               informações extras, despedidas ou qualquer texto que não esteja no modelo base.
            3. SEMPRE mantenha: UMA ÚNICA saudação variada no início (ex: "Bom dia",
               "Prezado Sr.", "Olá", "Prezado(a)"), o objetivo de retirar o comprovante,
               e o tom respeitoso/formal.
            4. SEMPRE retorne APENAS a mensagem reescrita, sem aspas, comentários ou
               explicações adicionais.
            Se houver ANY dúvida sobre o que pode ser alterado, RETORNE A MENSAGEM
            ORIGINAL EXATAMENTE FORNECIDA.
            """;

    /* --------------------------------------------------------------
       2️⃣  MENSAGEM BASE – contém exatamente os dados que devem ser preservados
       -------------------------------------------------------------- */
        String exemploBase = String.format(
                "Olá %s, sua consulta/exame (%s) já está agendado. " +
                        "Compareça à Secretaria de Saúde do município de Vitória de Santo Antão, " +
                        "setor de regulação, para retirar o comprovante das 08:00 às 14:00.",
                nomePaciente, nomeConsulta);

    /* --------------------------------------------------------------
       3️⃣  USER PROMPT – few‑shot com exemplos SÓ VÁLIDOS
       -------------------------------------------------------------- */
        String userPrompt = String.format("""
    REESCREVA a mensagem abaixo **APENAS** variando a saudação e a estrutura da frase.
    NÃO MODIFIQUE NADA ALÉM DO QUE ESTÁ ENTRE COLCHETES [ ... ].

    MENSAGEM BASE (NÃO ALTERE OS DADOS ENTRE COLCHETES):
    "%s"

    EXEMPLOS DE REESCRITA CORRETA (siga exatamente este padrão):
    - "Prezado Sr. %s, informamos que seu exame de %s está agendado. "
      + "Favor comparecer à Secretaria de Saúde do município de Vitória de Santo Antão, "
      + "setor de regulação, entre 08:00 e 14:00 para retirar o comprovante."
    - "%s, sua consulta de %s foi confirmada; "
      + "dirija‑se ao setor de regulação da Secretaria de Saúde de Vitória de Santo Antão "
      + "das 08:00 às 14:00 para obter o comprovante."
    - "Bom dia, %s. O comprovante do seu exame de %s pode ser retirado "
      + "na Secretaria de Saúde do município de Vitória de Santo Antão, "
      + "setor de regulação, no horário das 08:00 às 14:00."
    - "Olá, %s! Lembre‑se de comparecer à Secretaria de Saúde do município "
      + "de Vitória de Santo Antão, setor de regulação, das 08:00 até as 14:00 "
      + "para retirar o comprovante do seu %s."
    - "%s, comunico que a realização do seu %s está agendada. "
      + "Compareça ao setor de regulação da Secretaria de Saúde "
      + "de Vitória de Santo Antão, entre 08:00 e 14:00, e retire o comprovante."

    LEMBRE‑SE:
    • NÃO troque [%s] (nome do paciente) nem [%s] (tipo de consulta/exame).
    • NÃO altere a frase exata
      "Secretaria de Saúde do município de Vitória de Santo Antão, setor de regulação".
    • NÃO altere o horário – ele deve aparecer como "08:00 às 14:00"
      (ou qualquer variação que represente o mesmo intervalo, ex: "8h às 14h").
    • Use APENAS UMA saudação no início da mensagem.
    • Mantenha o objetivo: retirar o comprovante no local e horário especificados.
    • Se não tiver certeza de que a variação está dentro das regras,
      RETORNE A MENSAGEM BASE EXATAMENTE.

    RESPOSTA OBRIGATÓRIA: Apenas a mensagem reescrita, nada mais.
    """,
                // argumentos (agora com 13 itens)
                exemploBase,
                nomePaciente, nomeConsulta,
                nomePaciente, nomeConsulta,
                nomePaciente, nomeConsulta,
                nomePaciente, nomeConsulta,
                nomePaciente, nomeConsulta,
                // argumentos para os [%s] finais
                nomePaciente, nomeConsulta
        );


    /* --------------------------------------------------------------
       4️⃣  CHAMADA AO OLLAMA (mesma estrutura que você já tem)
       -------------------------------------------------------------- */
        JSONObject payload = new JSONObject()
                .put("model", modelName)
                .put("system", systemPrompt)
                .put("prompt", userPrompt)
                .put("stream", false)
                .put("options", Map.of(
                        "temperature", 0.67,
                        "top_p", 0.92,
                        "repeat_penalty", 1.15,
                        "max_tokens", 120
                ));

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(ollamaApiUrl))
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload.toString()))
                .timeout(java.time.Duration.ofSeconds(180))
                .build();

        HttpResponse<String> response = HTTP_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() != 200) {
            throw new IOException("Ollama API retornou status não‑200: " + response.statusCode()
                    + ". Resposta: " + response.body());
        }

        JSONObject jsonResponse = new JSONObject(response.body());
        if (!jsonResponse.has("response")) {
            throw new IOException("Resposta do Ollama inválida: campo 'response' ausente. "
                    + "Resposta completa: " + response.body());
        }

        String resposta = jsonResponse.getString("response").trim();


        return resposta;
    }

}
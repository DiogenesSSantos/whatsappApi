package com.github.dio.mensageria.service;


import com.github.dio.mensageria.model.Paciente;
import com.github.dio.mensageria.repository.PacienteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;
import java.util.Optional;

@Service
public class N8NService {
    private static final String MSN_PADRAO_FINAL_DE_SEMANA =String.format(new StringBuilder()
            .append("*Retirada de Comprovante de Agendamento*%n%n")
            .append("📌 *Atenção:* esta mensagem é apenas para retirada do seu comprovante na Secretaria Municipal de Saúde de Vitória de Santo Antão.%n")
            .append("📝 A data e o horário da sua consulta/exame só estarão disponíveis no comprovante impresso.%n%n")
            .append("📅 *Data de retirada:* próxima segunda-feira%n")
            .append("⏰ *Horário:* 08h00 às 14h00%n")
            .append("📍 *Local:* Setor de Regulação%n")
            .append("🎫 *Não esqueça:* cartão do SUS%n%n")
            .append("✅ *Atenciosamente,  Regulação!*")
            .toString());

    private static final String MSN_PADRAO = String.format(new StringBuilder()
            .append("*Retirada de Comprovante de Agendamento*%n%n")
            .append("📌 *Atenção:* esta mensagem é apenas para retirada do seu comprovante na Secretaria Municipal de Saúde de Vitória de Santo Antão.%n")
            .append("📝 A data e o horário da sua consulta/exame só estarão disponíveis no comprovante impresso.%n%n")
            .append("📅 *Data de retirada:* Amanhã%n")
            .append("⏰ *Horário:* 08h00 às 14h00%n")
            .append("📍 *Local:* Setor de Regulação%n")
            .append("🎫 *Não esqueça:* cartão do SUS%n%n")
            .append("✅ *Atenciosamente, Regulação de Saúde!*")
            .toString());


    private static final String URL_N8N = "https://n8n.devdiogenes.shop/webhook/api-java";
    private static final String URL_N8N_RESPOSTA = "https://n8n.devdiogenes.shop/webhook-test/api-java-resposta";
    private static final Logger log = LoggerFactory.getLogger(N8NService.class);
    private final RestTemplate restTemplate;


    @Autowired
    private PacienteRepository pacienteRepository;

    public N8NService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }




    public ResponseEntity<?> enviarPayload(String idCLiente, String nomePaciente, String numero, String mensagem) {
        log.warn("ENVIANDO MENSAGEM PARA N8N");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);


        Map<String, Object> jsonCorpo = Map.of(

                "id_cliente", idCLiente,
                "nome", nomePaciente,
                "telefones", numero,
                "mensagem", mensagem
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(jsonCorpo, headers);

        return restTemplate.exchange(
                URL_N8N, HttpMethod.POST, request, String.class);
    }


    public ResponseEntity<?> resposta(Long id, String numero, String respostaPacinete) {
        System.out.println("RESPOSTA DO N8N");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        Map<String, Object> resposta = getResposta(numero);
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(resposta, headers);

        return restTemplate.exchange(URL_N8N_RESPOSTA, HttpMethod.POST, entity, String.class);
    }



    private static Map<String, Object> getResposta(String numero) {
        if (isFinalSemana()) {
            Map<String, Object> resposta = Map.of(
                    "resposta", MSN_PADRAO_FINAL_DE_SEMANA,
                    "numero_usuario", numero);
            return resposta;

        } else {
            Map<String, Object> resposta = Map.of(
                    "resposta", MSN_PADRAO,
                    "numero_usuario", numero
            );

            return resposta;
        }
    }


    private static boolean isFinalSemana() {
        LocalDate data = LocalDate.now(ZoneId.of("America/Recife")).plusDays(1L);
        return data.getDayOfWeek().toString().equalsIgnoreCase(DayOfWeek.SATURDAY.toString()) || data.getDayOfWeek().toString().equalsIgnoreCase(DayOfWeek.SUNDAY.toString());
    }

}

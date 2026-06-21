package com.github.dio.mensageria.infra.controller.request;

import java.time.LocalDateTime;

public record ConsultaDTORequest(String nome,
                                 LocalDateTime dataAtendimento,
                                 LocalDateTime dataMarcacao,
                                 StatusDTORequest status
                                 ) {
}
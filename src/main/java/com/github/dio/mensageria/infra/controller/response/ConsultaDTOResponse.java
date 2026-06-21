package com.github.dio.mensageria.infra.controller.response;

import com.github.dio.mensageria.infra.controller.request.StatusDTORequest;

import java.time.LocalDateTime;

public record ConsultaDTOResponse(String nome,
                                  LocalDateTime dataAtendimento,
                                  LocalDateTime dataMarcacao,
                                  StatusDTORequest status
                                 ) {
}
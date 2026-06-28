package com.github.dio.mensageria.infra.documentation;

import com.github.dio.mensageria.infra.controller.pacientecontroller.request.PacienteDTORequest;
import com.github.dio.mensageria.infra.controller.pacientecontroller.response.PacienteDTOResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

public interface PacienteControllerSwaggerOpenAPI {

    @Operation(summary = "Criar novo paciente",
            description = "Cria um novo paciente e enfileira para notificação")
    @ApiResponse(responseCode = "201", description = "Paciente criado e enfileirado com sucesso")
    @ApiResponse(responseCode = "400", description = "Dados inválidos")
    @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    ResponseEntity<Map<String, Object>> criarPaciente(@RequestBody PacienteDTORequest pacienteDTORequest) throws Exception;


}

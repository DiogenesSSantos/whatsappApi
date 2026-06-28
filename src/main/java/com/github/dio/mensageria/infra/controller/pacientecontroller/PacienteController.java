package com.github.dio.mensageria.infra.controller.pacientecontroller;


import com.github.dio.mensageria.application.usecases.CriarPaciente;
import com.github.dio.mensageria.application.usecases.NotificarPaciente;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.PacienteDTORequest;
import com.github.dio.mensageria.infra.controller.pacientecontroller.response.PacienteDTOResponse;
import com.github.dio.mensageria.infra.documentation.PacienteControllerSwaggerOpenAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/pacientes")
@Tag(name = "Pacientes", description = "Endpoints para gerenciar pacientes")
public class PacienteController implements PacienteControllerSwaggerOpenAPI {
    private final CriarPaciente criarPaciente;
    private final PacienteControllerMapper mapper;
    private final NotificarPaciente notificarPaciente;

    public PacienteController(CriarPaciente criarPaciente, PacienteControllerMapper mapper, NotificarPaciente notificarPaciente) {
        this.criarPaciente = criarPaciente;
        this.mapper = mapper;
        this.notificarPaciente = notificarPaciente;
    }

    @GetMapping
    @Operation(summary = "Lista todos os pacientes")
    public ResponseEntity<List<PacienteDTOResponse>> buscarTodos() {
        List<PacienteDTOResponse> pacientes = criarPaciente.buscarTodos().stream()
                .map(mapper::modelToDTO)
                .toList();
        return ResponseEntity.ok(pacientes);
    }

    @PostMapping
    @Operation(summary = "Cadastra um paciente e enfileira notificação")
    public ResponseEntity<Map<String, Object>> criarPaciente(@RequestBody PacienteDTORequest pacienteDTORequest) throws Exception {
        Paciente paciente = mapper.dtoToModel(pacienteDTORequest);
        Paciente pacienteSalvoBD = criarPaciente.cadastrarPaciente(paciente);
        notificarPaciente.enfileirar(pacienteSalvoBD);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensagem", "Paciente enfileirado para notificação",
                "paciente", mapper.modelToDTO(pacienteSalvoBD),
                "filaTamanho", notificarPaciente.filaTamanho()
        ));
    }

    @PostMapping("/lote")
    @Operation(summary = "Enfileira múltiplos pacientes para notificação sequencial")
    public ResponseEntity<Map<String, Object>> criarPacientesEmLote(@RequestBody List<PacienteDTORequest> pacientesDTO) {
        List<Paciente> pacientes = pacientesDTO.stream()
                .map(mapper::dtoToModel)
                .toList();

        List<Paciente> pacientesSalvos = pacientes.stream()
                .map(paciente -> {
                    try {
                        return criarPaciente.cadastrarPaciente(paciente);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();

        notificarPaciente.enfileirar(pacientesSalvos);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(Map.of(
                "mensagem", "Pacientes enfileirados para notificação",
                "quantidade", pacientesSalvos.size(),
                "filaTamanho", notificarPaciente.filaTamanho()
        ));
    }

}

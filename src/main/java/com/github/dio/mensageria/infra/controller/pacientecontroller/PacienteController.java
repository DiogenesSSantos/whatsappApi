package com.github.dio.mensageria.infra.controller.pacientecontroller;


import com.github.dio.mensageria.application.usecases.CriarPaciente;
import com.github.dio.mensageria.application.usecases.NotificarPaciente;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.domain.paciente.consulta.Consulta;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.PacienteDTORequest;
import com.github.dio.mensageria.infra.controller.pacientecontroller.response.PacienteDTOResponse;
import com.github.dio.mensageria.infra.controller.pacientecontroller.response.PaginaResponse;
import com.github.dio.mensageria.infra.documentation.PacienteControllerSwaggerOpenAPI;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/buscar")
    @Operation(summary = "Busca pacientes com filtros e paginacao")
    public ResponseEntity<PaginaResponse<PacienteDTOResponse>> buscarComFiltros(
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String bairro,
            @RequestParam(required = false) String consultaNome,
            @RequestParam(required = false) String status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        var pageable = PageRequest.of(page, size, Sort.by("nome"));
        var resultado = criarPaciente.buscarComFiltros(nome, bairro, consultaNome, status, pageable);

        var conteudo = resultado.getContent().stream()
                .map(mapper::modelToDTO)
                .toList();

        return ResponseEntity.ok(new PaginaResponse<>(
                conteudo,
                resultado.getNumber(),
                resultado.getSize(),
                resultado.getTotalElements(),
                resultado.getTotalPages()));
    }

    @GetMapping("/{codigo}")
    @Operation(summary = "Busca um paciente pelo codigo")
    public ResponseEntity<PacienteDTOResponse> buscarPorCodigo(@PathVariable String codigo) {
        return criarPaciente.buscarPorCodigo(codigo)
                .map(paciente -> ResponseEntity.ok(mapper.modelToDTO(paciente)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Cadastra um paciente e enfileira notificacao")
    public ResponseEntity<Map<String, Object>> criarPaciente(@RequestBody PacienteDTORequest pacienteDTORequest) throws Exception {
        Paciente paciente = mapper.dtoToModel(pacienteDTORequest);
        Paciente pacienteSalvoBD = criarPaciente.cadastrarPaciente(paciente);
        notificarPaciente.enfileirar(pacienteSalvoBD);

        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                "mensagem", "Paciente enfileirado para notificacao",
                "paciente", mapper.modelToDTO(pacienteSalvoBD),
                "filaTamanho", notificarPaciente.filaTamanho()
        ));
    }

    @PutMapping("/{codigo}")
    @Operation(summary = "Atualiza os dados de um paciente")
    public ResponseEntity<Void> atualizar(@PathVariable String codigo, @RequestBody PacienteDTORequest pacienteDTORequest) {
        Paciente atualizado = Paciente.builder()
                .codigo(codigo)
                .nome(pacienteDTORequest.nome())
                .contato(mapper.contatoDTOToContatoModel(pacienteDTORequest.contato()))
                .consulta(mapper.consultaDTOToConsultaForUpdate(pacienteDTORequest.consulta()))
                .build();
        criarPaciente.cadastrarPaciente(atualizado);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{codigo}/status")
    @Operation(summary = "Atualiza apenas o status da consulta de um paciente")
    public ResponseEntity<Void> atualizarStatus(@PathVariable String codigo, @RequestBody Map<String, String> body) {
        String statusStr = body.get("status");
        if (statusStr == null || statusStr.isBlank()) {
            return ResponseEntity.badRequest().build();
        }

        Consulta.Status status;
        try {
            status = Consulta.Status.valueOf(statusStr);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }

        boolean atualizado = criarPaciente.atualizarStatus(codigo, status.name());
        return atualizado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{codigo}")
    @Operation(summary = "Exclui um paciente")
    public ResponseEntity<Void> excluir(@PathVariable String codigo) {
        boolean deletado = criarPaciente.deletar(codigo);
        return deletado ? ResponseEntity.noContent().build() : ResponseEntity.notFound().build();
    }

    @PostMapping("/lote")
    @Operation(summary = "Enfileira multiplos pacientes para notificacao sequencial")
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
                "mensagem", "Pacientes enfileirados para notificacao",
                "quantidade", pacientesSalvos.size(),
                "filaTamanho", notificarPaciente.filaTamanho()
        ));
    }

}

package com.github.dio.mensageria.infra.controller;


import com.github.dio.mensageria.application.usecases.CriarPaciente;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.infra.controller.request.PacienteDTORequest;
import com.github.dio.mensageria.infra.controller.response.PacienteDTOResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/pacientes")
public class PacienteController {
    private final CriarPaciente criarPaciente;
    private final PacienteControllerMapper mapper;

    public PacienteController(CriarPaciente criarPaciente, PacienteControllerMapper mapper) {
        this.criarPaciente = criarPaciente;
        this.mapper = mapper;
    }


    @PostMapping
    public ResponseEntity<PacienteDTOResponse> criarPaciente(PacienteDTORequest pacienteDTORequest) {
        Paciente paciente = mapper.dtoToModel(pacienteDTORequest);
        Paciente pacienteSalvoBD = criarPaciente.cadastrarPaciente(paciente);
        PacienteDTOResponse pacienteDTOResponse = mapper.modelToDTO(pacienteSalvoBD);

        return ResponseEntity.status(HttpStatus.OK).body(pacienteDTOResponse);
    }

}

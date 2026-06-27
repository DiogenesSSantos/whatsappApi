package com.github.dio.mensageria.infra.controller.pacientecontroller;


import com.github.dio.mensageria.application.usecases.CriarPaciente;
import com.github.dio.mensageria.application.usecases.NotificarPaciente;
import com.github.dio.mensageria.domain.paciente.Paciente;
import com.github.dio.mensageria.infra.controller.pacientecontroller.request.PacienteDTORequest;
import com.github.dio.mensageria.infra.controller.pacientecontroller.response.PacienteDTOResponse;
import com.github.dio.mensageria.infra.documentation.PacienteControllerSwaggerOpenAPI;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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



    @PostMapping
    public ResponseEntity<PacienteDTOResponse> criarPaciente(@RequestBody PacienteDTORequest pacienteDTORequest) throws Exception {
        Paciente paciente = mapper.dtoToModel(pacienteDTORequest);
        Paciente pacienteSalvoBD = criarPaciente.cadastrarPaciente(paciente);
        notificarPaciente.enviar(pacienteSalvoBD);
        PacienteDTOResponse pacienteDTOResponse = mapper.modelToDTO(pacienteSalvoBD);

        return ResponseEntity.status(HttpStatus.CREATED).body(pacienteDTOResponse);
    }

}

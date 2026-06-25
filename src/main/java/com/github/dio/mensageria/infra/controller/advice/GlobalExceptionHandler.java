package com.github.dio.mensageria.infra.controller.advice;

import com.github.dio.mensageria.domain.paciente.consulta.DataPassadoException;
import com.github.dio.mensageria.domain.paciente.PacienteBuilderException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.OffsetDateTime;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorBodyResponse> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(FieldError::getField, FieldError::getDefaultMessage, (a, b) -> a));

        ErrorBodyResponse body = new ErrorBodyResponse(OffsetDateTime.now(), HttpStatus.BAD_REQUEST.value(),
                "Validation failed", fieldErrors);

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(PacienteBuilderException.class)
    public ResponseEntity<ErrorBodyResponse> handlePacienteBuilder(PacienteBuilderException ex) {
        ErrorBodyResponse body = new ErrorBodyResponse(OffsetDateTime.now(),
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DataPassadoException.class)
    public ResponseEntity<ErrorBodyResponse> handleDataPassado(DataPassadoException ex) {
        ErrorBodyResponse body = new ErrorBodyResponse(OffsetDateTime.now(), HttpStatus.UNPROCESSABLE_ENTITY.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(body);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorBodyResponse> handleDataIntegrity(DataIntegrityViolationException ex) {
        ErrorBodyResponse body = new ErrorBodyResponse(OffsetDateTime.now(), HttpStatus.CONFLICT.value(),
                "Conflito de dados: " + ex.getMostSpecificCause().getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(body);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorBodyResponse> handleAll(Exception ex) {
        ErrorBodyResponse body = new ErrorBodyResponse(OffsetDateTime.now(), HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Erro interno no servidor");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(body);
    }
}
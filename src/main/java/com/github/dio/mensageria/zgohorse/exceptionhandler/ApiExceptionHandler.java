package com.github.dio.mensageria.zgohorse.exceptionhandler;


import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Classe capturadora Global de exception que pode ser lançada no contexto do spring.
 * @author diogenesssantos
 * @hidden
 *
 */
@ControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

}

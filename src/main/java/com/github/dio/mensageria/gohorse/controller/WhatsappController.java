package com.github.dio.mensageria.gohorse.controller;


import com.github.dio.mensageria.gohorse.model.modeloRepresentacional.PacienteMR;
import com.github.dio.mensageria.gohorse.openapi.model.WhatsappDocumentationOpenAPI;
import com.github.dio.mensageria.gohorse.service.WhatsappService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Classe {@link WhatsappController}
 * @author diogenesssantos
 */
@CrossOrigin(origins = {"*"})
@RestController
@RequestMapping(value = {"/api/zap"}, produces = {"application/json"})
public class WhatsappController extends WhatsappDocumentationOpenAPI {

    @Autowired
    private WhatsappService service;

    @PostMapping({"/enviarList"})
    public void enviarParaLista(@RequestBody List<PacienteMR> pacienteMR) {
        this.service.enviarMensagemLista(pacienteMR);
    }

    @DeleteMapping({"/desconectar"})
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void desconectarWhatsApp() {
        this.service.desconectar();
    }

    @PutMapping({"/reconectar"})
    @ResponseStatus(HttpStatus.OK)
    public void reconectarWhatsApp() {
        this.service.conectar();
    }
}

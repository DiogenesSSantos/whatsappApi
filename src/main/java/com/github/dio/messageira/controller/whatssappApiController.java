package com.github.dio.messageira.controller;


import com.github.dio.messageira.controller.modeloRepresentacional.PacienteMR;
import com.github.dio.messageira.core.openapi.model.WhatsappDocumentationOpenAPI;
import com.github.dio.messageira.service.WhatsappService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


public class whatssappApiController extends WhatsappDocumentationOpenAPI {

    @Autowired
    private  WhatsappService service;


    @Override
    public String home() {
        return "";
    }

    @Override
    public void enviarMensagem(List<PacienteMR> pacienteMR) {
        service.enviarMensagemLista(pacienteMR);
    }

    @Override
    public void desconectarWhatsApp() {
        service.desconectar();
    }

    @Override
    public void reconectarWhatsApp() {
        service.conectar();
    }
}
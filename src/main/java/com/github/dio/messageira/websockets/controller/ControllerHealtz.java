package com.github.dio.messageira.websockets.controller;

import com.github.dio.messageira.websockets.WebSocketService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/healtz")
public class ControllerHealtz {

    private final WebSocketService webSocketService;

    ControllerHealtz (WebSocketService ws) {
        this.webSocketService = ws;
    }

    @GetMapping
    public ResponseEntity<?> healtz500() {
        var validarHealtz = webSocketService.getUserLogado();

        if (validarHealtz) {
            return ResponseEntity.status(HttpStatus.OK).body(String.format("ESTÁ FUNCIONANDO USARIO CONECTADO : %s" , webSocketService.getUserLogado().toString()));
        }else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("DEU MERDA VAMOS REINCIAR");
        }

    }
}

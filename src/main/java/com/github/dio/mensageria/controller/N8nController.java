package com.github.dio.mensageria.controller;


import com.github.dio.mensageria.service.N8NService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping(path = {"api/zap/n8n"})
public class N8nController {


    @Autowired
    private N8NService n8NService;


    @PostMapping(path = {"/resposta"})
    public ResponseEntity<?> resposta(@RequestBody Map<String, String> corpoJson) {
        System.out.println(corpoJson);

        n8NService.resposta(Long.valueOf(corpoJson.get("id_cliente_bd")),
                corpoJson.get("numero_usuario"),
                corpoJson.get("mensagem"));


        return null;
    }


}

package com.github.dio.mensageria.controller;


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


    @PostMapping
    public ResponseEntity<?> resposta(@RequestBody Map<String, String> corpoJson) {

        System.out.println(corpoJson);
        return null;
    }



}

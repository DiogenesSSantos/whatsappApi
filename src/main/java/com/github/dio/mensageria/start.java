package com.github.dio.mensageria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutionException;

/**
 * Classe main para inicialização.
 *
 * @author diogenesssantos
 * @hidden
 */
@EnableScheduling
@SpringBootApplication
public class start {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        SpringApplication.run(start.class, args);
    }

}
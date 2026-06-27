package com.github.dio.mensageria;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.concurrent.ExecutionException;

/**
 * Classe main para inicialização.
 *
 * @author diogenesssantos
 */
@EnableScheduling
@SpringBootApplication
public class Start {

    public static void main(String[] args)  {
        SpringApplication.run(Start.class, args);
    }

}
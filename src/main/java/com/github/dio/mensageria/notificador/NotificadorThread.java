package com.github.dio.mensageria.notificador;


import com.github.dio.mensageria.service.WhatsappServiceN8N;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class NotificadorThread {
    private static final Logger log = LoggerFactory.getLogger(NotificadorThread.class);


    public static void notificar(WhatsappServiceN8N service) {
        if (!WhatsappServiceN8N.isNotificado) {
            log.warn("Limpeza sera feita em 48 horas.");
            service.agendarLimpezaComDelayedExecutor();
            WhatsappServiceN8N.isNotificado = Boolean.TRUE;
        }
    }

}

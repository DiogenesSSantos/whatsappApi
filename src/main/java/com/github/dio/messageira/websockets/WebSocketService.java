package com.github.dio.messageira.websockets;

import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

@Service
public class WebSocketService extends TextWebSocketHandler {

    List<WebSocketSession> sessios = new ArrayList<>();

    private Boolean userLogado = true;
    private Timer disconectadoTempo = new Timer();
    private Long tempoMilisegundosContador = 60_000L;


    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        sessios.add(session);
        this.setUserLogado(true);
        disconectadoTempo.cancel();
        System.out.println("WebSocketService CONECTADO");
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessios.remove(session);
        if (sessios.isEmpty()) {
            //this.setUserLogado(false);
            disconectadoTempo.cancel();
            disconectadoTempo = new Timer();

            disconectadoTempo.schedule(new TimerTask() {
                @Override
                public void run() {
//                    userLogado = true;
                    userLogado = false;

                    System.out.println("TIME FINALIZADO");
                }
            }, tempoMilisegundosContador);

            System.out.println("WebSocketService DERRADEIRO DESCONECTADO");
        }
    }

    public Boolean getUserLogado() {
        System.out.println("PEGANDO o VALOR " + userLogado.toString());
        return userLogado;
    }

    public void setUserLogado(Boolean userLogado) {
        System.out.println("SENTANDO VALOR " + userLogado.toString());
        this.userLogado = userLogado;
    }
}

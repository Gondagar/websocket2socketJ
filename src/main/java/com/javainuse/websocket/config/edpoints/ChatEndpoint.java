package com.javainuse.websocket.config.edpoints;


import com.javainuse.websocket.config.codes.MessageDecoder;
import com.javainuse.websocket.config.codes.MessageEncoder;
import com.javainuse.websocket.config.model.Message;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

@Slf4j
@ServerEndpoint(value = "/chat", decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
//@ServerEndpoint(value = "/chat")
@Component
public class ChatEndpoint {

    private Session session;
    public static Set<Session> listeners = new CopyOnWriteArraySet<>();

    @OnClose
    public void onClose(Session session){
        listeners.remove(session);
        log.info("Відключено користувача!");
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        listeners.add(session);
        log.info("Підключено нового користувача!");
    }

    @OnError
    public void onError(Session session, Throwable throwable){
        log.error("Виникла попилка {}", throwable.getMessage());
    }

    @OnMessage
    public void onMessage(Session session, String message) throws IOException, EncodeException {

        log.info("Оримано повідомлення: {}", message);

        broadcast(message);

    }

    private void broadcast(String message) {
        listeners.forEach(currentSession -> {
            try {
                log.info("Відправка повідомлення до {}", currentSession.getId());
                currentSession.getBasicRemote().sendText(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

}

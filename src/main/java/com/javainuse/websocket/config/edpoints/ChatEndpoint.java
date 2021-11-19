package com.javainuse.websocket.config.edpoints;


import com.javainuse.websocket.config.codes.MessageDecoder;
import com.javainuse.websocket.config.codes.MessageEncoder;
import com.javainuse.websocket.config.model.Message;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@ServerEndpoint(value = "/chat", decoders = {MessageDecoder.class}, encoders = {MessageEncoder.class})
@Component
public class ChatEndpoint {

    private Session session;
    public static Map<Long, Session> users = new ConcurrentHashMap();
    public static Map<Long, Session> supports = new ConcurrentHashMap();


    @OnClose
    public void onClose(Session session) {
        users.remove(session);
        log.info("Відключено користувача!");
    }

    @OnOpen
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        this.session = session;
        log.debug("Відкрито нове підкючення!!!");
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        log.error("Виникла помилка {}", throwable.getMessage());
    }

    @OnMessage
    public void onMessage(Session session, Message message)  {



        Map<String, List<String>> requestParameterMap = session.getRequestParameterMap();
        log.info("Request params: ", requestParameterMap.toString());


        if ("connection".equals(message.getEvent())) {

            if (message.isSupport) {
                if (!supports.values().contains(session)) {
                    supports.put(message.userId, session);
                    log.info("Адмін {} успішно підключений!", message.userName);
                    broadcastToAllAdmins(message);
                }
            } else {
                if (!users.values().contains(session)) {
                    users.put(message.userId, session);
                    log.info("Користувач {} успішно підключений!", message.userName);
                    broadcastToAllAdmins(message);
                }
            }

        } else if ("message".equals(message.event)) {
            if (message.isSupport) {

                sendUserResponse(message);
                broadcastToAllAdmins(message);

                log.info("Адмін {} успішно підключений!", message.userName);
            } else {
                broadcastToAllAdmins(message);
                log.info("Повідпослення від користувача {} було надіслано до {} адмінів", message.userName, supports.values().size());

            }
        }

    }

    private void broadcastToAllAdmins(Message message) {
        if (supports.values().size() == 0){
            log.info("Адміни в чаті відсуті!!!!!!");
        }
        supports.values().forEach(currentSession -> {
            try {
                log.info("Відправка повідомлення до {}", currentSession.getId());
                currentSession.getBasicRemote().sendObject(message);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    private void sendUserResponse(Message message) {

        try {
            Session session = users.get(message.userId);
            log.info("Адмін відповів користувачеві {}", message.userId);
            session.getBasicRemote().sendObject(message);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

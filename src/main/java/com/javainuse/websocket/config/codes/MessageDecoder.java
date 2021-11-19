package com.javainuse.websocket.config.codes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javainuse.websocket.config.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.websocket.*;

@Slf4j
public class MessageDecoder implements Decoder.Text<Message> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Message decode(String json) throws DecodeException {
        try {
            log.info("Отримано нове повідомлення: {}", json);
            Message message = objectMapper.readValue(json, Message.class);
            return message;

        } catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public boolean willDecode(String s) {
        return s!=null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void destroy() {

    }

}

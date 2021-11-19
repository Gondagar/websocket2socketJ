package com.javainuse.websocket.config.codes;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javainuse.websocket.config.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;



@Slf4j
public class MessageEncoder implements Encoder.Text<Message> {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(EndpointConfig endpointConfig) {
    }

    @Override
    public void destroy() {

    }

    @Override
    public String encode(Message message) {

        try {
            log.info("Отримано месседж {}", message);
            String s = objectMapper.writeValueAsString(message);
            return s;
        }catch (Exception e){
            e.printStackTrace();
            return  "{}";
        }

    }
}

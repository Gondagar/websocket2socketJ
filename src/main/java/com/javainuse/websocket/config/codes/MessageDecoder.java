package com.javainuse.websocket.config.codes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javainuse.websocket.config.model.Message;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;


@Component
public class MessageDecoder implements Decoder.Text<Message> {

    @Autowired
    ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public Message decode(String json) throws DecodeException {
        return objectMapper.readValue(json,Message.class);
    }

    @Override
    public boolean willDecode(String s) {
        return s!=null;
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }

}

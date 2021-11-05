package com.javainuse.websocket.config.codes;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javainuse.websocket.config.model.Message;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;


@Component
@Data
public class MessageEncoder implements Encoder.Text<Message> {

    @Autowired
    ObjectMapper objectMapper;

    @SneakyThrows
    @Override
    public String encode(Message message) throws EncodeException {
            return objectMapper.writeValueAsString(message);
    }

    @Override
    public void init(EndpointConfig endpointConfig) {

    }

    @Override
    public void destroy() {

    }
}

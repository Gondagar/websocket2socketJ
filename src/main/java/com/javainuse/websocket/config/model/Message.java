package com.javainuse.websocket.config.model;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@ToString
public class Message implements Serializable {

    public Long userId;
    public String userName;
    public Long roomId;
    public Boolean isSupport = false;
    public String messageId;
    public String message;
    public String event;

}


//{"userId":"admin","roomId":1637317173255,"isSupport":true,"messageId":"message-id 1637317420718","userName":"Admin","message":"КУ","event":"message"}

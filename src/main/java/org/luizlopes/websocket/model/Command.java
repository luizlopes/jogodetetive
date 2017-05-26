package org.luizlopes.websocket.model;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class Command implements Serializable {

    private CommandType type;
    private Object options;
    private Object response;

}

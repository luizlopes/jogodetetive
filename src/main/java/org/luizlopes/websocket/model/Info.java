package org.luizlopes.websocket.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Info {

    private Object body;
    private InfoType type;

}

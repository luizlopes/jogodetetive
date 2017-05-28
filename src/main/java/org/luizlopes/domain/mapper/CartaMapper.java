package org.luizlopes.domain.mapper;

import org.luizlopes.domain.Carta;
import org.luizlopes.websocket.model.Command;

import java.util.LinkedHashMap;
import java.util.Map;

public class CartaMapper {

    public static Carta parse(Command command) {
        Map<String, String> map = (LinkedHashMap) command.getResponse();
        return parse(map);
    }

    public static Carta parse(Map<String, String> map) {
        return new Carta(map.get("nome"), map.get("src"));
    }

}
